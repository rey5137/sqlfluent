package com.rey.sqlfluent.compiler;

import com.squareup.javapoet.FieldSpec;

/**
 * Created by Rey on 8/16/2016.
 */
public class ColumnField {

    public final FieldSpec fieldSpec;
    public final FieldSpec staticColumnSpec;

    public ColumnField(FieldSpec fieldSpec, FieldSpec staticColumnSpec){
        this.fieldSpec = fieldSpec;
        this.staticColumnSpec = staticColumnSpec;
    }
}
