package com.rey.sqlfluent.compiler;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * Created by Rey on 8/16/2016.
 */
public class ModelClass {

    private static final TypeName TYPE_STRING = TypeName.get(String.class);

    private final ClassName modelClassName;
    private final ClassName indexClassName;
    private final ClassName queryClassName;
    private List<ColumnField> fields = new ArrayList<>();

    public ModelClass(ClassName modelClassName, ClassName indexClassName, ClassName queryClassName){
        this.modelClassName = modelClassName;
        this.indexClassName = indexClassName;
        this.queryClassName = queryClassName;
    }

    public void addColumnField(ColumnField... fields){
        Collections.addAll(this.fields, fields);
    }

    private void buildIndexFields(TypeSpec.Builder builder){
        for(ColumnField field : fields){
            FieldSpec fieldSpec = FieldSpec.builder(TypeName.INT, field.fieldName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build();
            builder.addField(fieldSpec);

            FieldSpec staticColumnSpec = FieldSpec.builder(String.class, field.getStaticColumnField())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$S", field.columnName)
                    .build();
            builder.addField(staticColumnSpec);
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

        for(ColumnField field : fields)
            constructorBuilder.addStatement("$N = $N.getColumnIndex($N)",
                    field.fieldName, cursorParam, field.getStaticColumnField());

        MethodSpec staticMethod = MethodSpec.methodBuilder("instance")
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

        SqlFluentProcessor.instance.debug("build: " + field.fieldType + " " + field.fieldName);

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

        MethodSpec.Builder builder = MethodSpec.methodBuilder("fromCursor")
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

        for(ColumnField field : fields)
            buildLoadFieldCode(builder, cursorParam, modelParam, indexParam, field);

        builder.addStatement("return $N", modelParam);

        return builder.build();
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
        queryClassBuilder.addMethod(buildLoadMethod());

        JavaFile queryClassFile = JavaFile.builder(queryClassName.packageName(), queryClassBuilder.build())
                .addFileComment("Generated code from Sql Fluent. Do not modify!")
                .build();

        List<JavaFile> files = new ArrayList<>();
        files.add(indexClassFile);
        files.add(queryClassFile);

        return files;
    }
}
