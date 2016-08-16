package com.rey.sqlfluent.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
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

    private final ClassName indexClassName;
    private List<ColumnField> fields = new ArrayList<>();

    public ModelClass(ClassName indexClassName){
        this.indexClassName = indexClassName;
    }

    public void addColumnField(ColumnField... fields){
        Collections.addAll(this.fields, fields);
    }

    Collection<JavaFile> brewJava() {
        TypeSpec.Builder result = TypeSpec.classBuilder(indexClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        List<JavaFile> files = new ArrayList<>();

        files.add(JavaFile.builder(indexClassName.packageName(), result.build())
                .addFileComment("Generated code from Sql Fluent. Do not modify!")
                .build());

        return files;
    }
}
