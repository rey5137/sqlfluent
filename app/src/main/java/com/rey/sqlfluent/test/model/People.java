package com.rey.sqlfluent.test.model;

import com.rey.sqlfluent.annotation.Column;
import com.rey.sqlfluent.annotation.Model;

/**
 * Created by Rey on 8/16/2016.
 */
public class People {

    @Column("name")
    String name;

    @Column("age")
    int age;

}
