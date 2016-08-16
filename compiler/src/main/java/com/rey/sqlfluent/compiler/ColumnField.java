package com.rey.sqlfluent.compiler;

import com.squareup.javapoet.TypeName;

/**
 * Created by Rey on 8/16/2016.
 */
public class ColumnField {

    public final String fieldName;
    public final String columnName;
    public final TypeName fieldType;

    public ColumnField(String fieldName, String columnName, TypeName fieldType){
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.fieldType = fieldType;
    }

    public String getStaticColumnField(){
        return "COL_" + fieldName.toUpperCase();
    }
}
