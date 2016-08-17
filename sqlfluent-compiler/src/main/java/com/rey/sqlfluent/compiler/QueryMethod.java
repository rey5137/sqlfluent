package com.rey.sqlfluent.compiler;

import com.squareup.javapoet.TypeName;

/**
 * Created by Rey on 8/17/2016.
 */
public class QueryMethod {

    public final String originalMethodName;
    public final String methodName;
    public final TypeName[] paramTypes;
    public final String[] paramNames;
    public final boolean singleResult;
    public final boolean originalIsMethod;

    public QueryMethod(String originalMethodName, String methodName, TypeName[] paramTypes, String[] paramNames, boolean singleResult, boolean originalIsMethod) {
        this.originalMethodName = originalMethodName;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.paramNames = paramNames;
        this.singleResult = singleResult;
        this.originalIsMethod = originalIsMethod;
    }
}
