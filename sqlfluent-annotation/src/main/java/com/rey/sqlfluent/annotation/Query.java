package com.rey.sqlfluent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Rey on 8/16/2016.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface Query {

    /** The name of query method */
    String value() default "";

    /** Return only first result or not.*/
    boolean singleResult() default false;

}
