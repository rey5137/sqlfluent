package com.rey.sqlfluent.compiler;

import android.support.annotation.NonNull;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * Created by Rey on 8/16/2016.
 */
public class ModelClass {

    public static final TypeName TYPE_STRING = TypeName.get(String.class);

    private static final String METHOD_INSTANCE = "instance";
    private static final String METHOD_LOAD = "fromCursor";

    private final ClassName modelClassName;
    private final ClassName indexClassName;
    private final ClassName queryClassName;

    private List<ColumnField> columnFields = new ArrayList<>();
    private List<QueryMethod> queryMethods = new ArrayList<>();

    public ModelClass(ClassName modelClassName, ClassName indexClassName, ClassName queryClassName){
        this.modelClassName = modelClassName;
        this.indexClassName = indexClassName;
        this.queryClassName = queryClassName;
    }

    public void addColumnField(ColumnField... fields){
        Collections.addAll(this.columnFields, fields);
    }

    public void addQueryMethod(QueryMethod... methods){
        Collections.addAll(this.queryMethods, methods);
    }

    private void buildIndexFields(TypeSpec.Builder builder){
        for(ColumnField field : columnFields){
            FieldSpec fieldSpec = FieldSpec.builder(TypeName.INT, field.fieldName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build();
            builder.addField(fieldSpec);
        }
    }

    private void buildIndexConstructor(TypeSpec.Builder builder){
        String cursorParam = "cursor";
        ClassName cursorClass = ClassName.get("android.database", "Cursor");
        ParameterSpec parameterSpec = ParameterSpec.builder(cursorClass, cursorParam)
                .addAnnotation(NonNull.class)
                .build();

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(parameterSpec);

        for(ColumnField field : columnFields)
            constructorBuilder.addStatement("$N = $N.getColumnIndex($S)",
                    field.fieldName, cursorParam, field.columnName);

        MethodSpec staticMethod = MethodSpec.methodBuilder(METHOD_INSTANCE)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(parameterSpec)
                .returns(indexClassName)
                .addStatement("return new $T($N)", indexClassName, cursorParam)
                .build();

        builder.addMethod(constructorBuilder.build())
                .addMethod(staticMethod);
    }

    private void buildLoadFieldCode(MethodSpec.Builder builder, String cursorParam, String modelParam, String indexParam, ColumnField field){
        builder.beginControlFlow("if($N.$N >= 0)", indexParam, field.fieldName);

        if(field.fieldType.equals(TypeName.INT))
            builder.addStatement("$N.$N = $N.getInt($N.$N)", modelParam, field.fieldName, cursorParam, indexParam, field.fieldName);
        else if(field.fieldType.equals(TypeName.LONG))
            builder.addStatement("$N.$N = $N.getLong($N.$N)", modelParam, field.fieldName, cursorParam, indexParam, field.fieldName);
        else if(field.fieldType.equals(TypeName.FLOAT))
            builder.addStatement("$N.$N = $N.getFloat($N.$N)", modelParam, field.fieldName, cursorParam, indexParam, field.fieldName);
        else if(field.fieldType.equals(TypeName.DOUBLE))
            builder.addStatement("$N.$N = $N.getDouble($N.$N)", modelParam, field.fieldName, cursorParam, indexParam, field.fieldName);
        else if(field.fieldType.equals(TypeName.SHORT))
            builder.addStatement("$N.$N = $N.getShort($N.$N)", modelParam, field.fieldName, cursorParam, indexParam, field.fieldName);
        else if(field.fieldType.equals(TypeName.BOOLEAN))
            builder.addStatement("$N.$N = $N.getInt($N.$N) == 1", modelParam, field.fieldName, cursorParam, indexParam, field.fieldName);
        else if(field.fieldType.equals(TYPE_STRING))
            builder.addStatement("$N.$N = $N.isNull($N.$N) ? null : $N.getString($N.$N)", modelParam, field.fieldName, cursorParam, indexParam, field.fieldName, cursorParam, indexParam, field.fieldName);

        builder.endControlFlow();
    }

    private MethodSpec buildLoadMethod(){
        String cursorParam = "cursor";
        ClassName cursorClass = ClassName.get("android.database", "Cursor");
        ParameterSpec cursorParamSpec = ParameterSpec.builder(cursorClass, cursorParam)
                .addAnnotation(NonNull.class)
                .build();

        String modelParam = "model";
        ParameterSpec modelParamSpec = ParameterSpec.builder(modelClassName, modelParam)
                .build();

        String indexParam = "index";
        ParameterSpec indexParamSpec = ParameterSpec.builder(indexClassName, indexParam)
                .build();

        MethodSpec.Builder builder = MethodSpec.methodBuilder(METHOD_LOAD)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(cursorParamSpec)
                .addParameter(modelParamSpec)
                .addParameter(indexParamSpec)
                .returns(modelClassName);

        builder.beginControlFlow("if($N == null)", modelParam)
                .addStatement("$N = new $T()", modelParam, modelClassName)
                .endControlFlow()
                .beginControlFlow("if($N == null)", indexParam)
                .addStatement("$N = $T.instance($N)", indexParam, indexClassName, cursorParam)
                .endControlFlow();

        for(ColumnField field : columnFields)
            buildLoadFieldCode(builder, cursorParam, modelParam, indexParam, field);

        builder.addStatement("return $N", modelParam);

        return builder.build();
    }

    private <T> Class<? extends T[]> getArrayClass(Class<T> clazz) {
        return (Class<? extends T[]>) Array.newInstance(clazz, 0).getClass();
    }

    private MethodSpec buildQueryMethod(QueryMethod queryMethod){
        String dbParam = "db";
        ClassName dbClass = ClassName.get("android.database.sqlite", "SQLiteDatabase");
        ParameterSpec dbParamSpec = ParameterSpec.builder(dbClass, dbParam)
                .addAnnotation(NonNull.class)
                .build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(queryMethod.methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(dbParamSpec)
                .returns(queryMethod.singleResult ? modelClassName : ArrayTypeName.of(modelClassName));

        if(queryMethod.paramTypes != null)
            for(int i = 0; i < queryMethod.paramTypes.length; i++)
                methodBuilder.addParameter(queryMethod.paramTypes[i], queryMethod.paramNames[i]);

        if(queryMethod.originalIsMethod) {
            StringBuilder sb = new StringBuilder();
            sb.append("String sql = ")
                    .append(modelClassName.simpleName()).append('.').append(queryMethod.originalMethodName)
                    .append('(');
            if (queryMethod.paramNames != null)
                for (int i = 0; i < queryMethod.paramNames.length; i++) {
                    if (i > 0)
                        sb.append(", ");
                    sb.append(queryMethod.paramNames[i]);
                }
            sb.append(")");
            methodBuilder.addStatement(sb.toString());
        }
        else
            methodBuilder.addStatement("String sql = $T.$N", modelClassName, queryMethod.originalMethodName);

        if(queryMethod.singleResult){
            methodBuilder.addStatement("Cursor cursor = $N.rawQuery(sql, null)", dbParam)
                    .addStatement("$T model = null", modelClassName)
                    .beginControlFlow("if(cursor.moveToFirst())")
                    .addStatement("model = $N(cursor, null, null)", METHOD_LOAD)
                    .endControlFlow()
                    .addStatement("cursor.close()")
                    .addStatement("return model");
        }
        else {
            methodBuilder.addStatement("Cursor cursor = $N.rawQuery(sql, null)", dbParam)
                    .addStatement("$T[] models = null", modelClassName)
                    .beginControlFlow("if(cursor.moveToFirst())")
                    .addStatement("$T index = $T.$N(cursor)", indexClassName, indexClassName, METHOD_INSTANCE)
                    .addStatement("models = new $T[cursor.getCount()]", modelClassName)
                    .beginControlFlow("for(int i = 0; i < models.length; i++)")
                    .addStatement("models[i] = $N(cursor, null, index)", METHOD_LOAD)
                    .addStatement("cursor.moveToNext()")
                    .endControlFlow()
                    .endControlFlow()
                    .addStatement("cursor.close()")
                    .addStatement("return models");
        }

        return methodBuilder.build();
    }

    private void buildQueryMethods(TypeSpec.Builder builder){
        builder.addMethod(buildLoadMethod());
        for(QueryMethod queryMethod : queryMethods)
            builder.addMethod(buildQueryMethod(queryMethod));
    }

    Collection<JavaFile> brewJava() {
        TypeSpec.Builder indexClassBuilder = TypeSpec.classBuilder(indexClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        buildIndexFields(indexClassBuilder);
        buildIndexConstructor(indexClassBuilder);

        JavaFile indexClassFile = JavaFile.builder(indexClassName.packageName(), indexClassBuilder.build())
                .addFileComment("Generated code from Sql Fluent. Do not modify!")
                .build();

        TypeSpec.Builder queryClassBuilder = TypeSpec.classBuilder(queryClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        buildQueryMethods(queryClassBuilder);

        JavaFile queryClassFile = JavaFile.builder(queryClassName.packageName(), queryClassBuilder.build())
                .addFileComment("Generated code from Sql Fluent. Do not modify!")
                .build();

        List<JavaFile> files = new ArrayList<>();
        files.add(indexClassFile);
        files.add(queryClassFile);

        return files;
    }
}
