package com.rey.sqlfluent.test.model;

import com.rey.sqlfluent.annotation.Column;
import com.rey.sqlfluent.annotation.Query;

/**
 * Created by Rey on 8/16/2016.
 */
public class People {

    @Column("name")
    String mName;

    @Column("age")
    int mAge;

    @Query("queryA")
    static final String QUERY_A = "";

    @Query(singleResult = true)
    public static String getAll(){
        return null;
    }

    @Query("queryAllWithName")
    public static String getAll(int start, int length, String name){
        return null;
    }

}
