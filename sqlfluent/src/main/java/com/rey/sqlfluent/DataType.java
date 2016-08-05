package com.rey.sqlfluent;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Rey on 6/30/2015.
 */
public class DataType {

    @IntDef({INT, INTEGER, TINYINT, SMALLINT, MEDIUMINT, BIGINT, UNSIGNED_BIG_INT, INT2, INT8,
            CHARACTER, VARCHAR, VARYING_CHARACTER, NCHAR, NATIVE_CHARACTER, NVARCHAR, TEXT, CLOB,
            REAL, DOUBLE, DOUBLE_PRECISION, FLOAT, NUMERIC, DECIMAL, BLOB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {}

    public static final int INT                     = 1;
    public static final int INTEGER                 = 2;
    public static final int TINYINT                 = 3;
    public static final int SMALLINT                = 4;
    public static final int MEDIUMINT               = 5;
    public static final int BIGINT                  = 6;
    public static final int UNSIGNED_BIG_INT        = 7;
    public static final int INT2                    = 8;
    public static final int INT8                    = 9;

    public static final int CHARACTER               = 10;
    public static final int VARCHAR                 = 11;
    public static final int VARYING_CHARACTER       = 12;
    public static final int NCHAR                   = 13;
    public static final int NATIVE_CHARACTER        = 14;
    public static final int NVARCHAR                = 15;
    public static final int TEXT                    = 16;
    public static final int CLOB                    = 17;

    public static final int REAL                    = 20;
    public static final int DOUBLE                  = 21;
    public static final int DOUBLE_PRECISION        = 22;
    public static final int FLOAT                   = 23;
    public static final int NUMERIC                 = 24;
    public static final int DECIMAL                 = 25;

    public static final int BLOB                    = 30;

}
