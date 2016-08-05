package com.rey.sqlfluent;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Rey on 6/30/2015.
 */
public class Conflict {

    @IntDef({NONE, ROLLBACK, ABORT, FAIL, IGNORE, REPLACE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {}

    public static final int NONE = -1;
    public static final int ROLLBACK = 0;
    public static final int ABORT = 1;
    public static final int FAIL = 2;
    public static final int IGNORE = 3;
    public static final int REPLACE = 4;
}
