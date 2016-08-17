package com.rey.sqlfluent;

import android.provider.BaseColumns;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

/**
 * Created by Rey on 8/5/2016.
 */
public class SqlFluentTest {

    @Test
    public void testCreateTable() throws Exception {
        String sql = new SqlFluent().createTable("category")
                .columnDef(BaseColumns._ID, DataType.INTEGER).primaryKey(Conflict.REPLACE).autoIncrement()
                .andColumnDef("name", DataType.TEXT)
                .andColumnDef("color", DataType.INTEGER)
                .endCreate()
                .build();

        String result = "CREATE TABLE IF NOT EXISTS category ( _id INTEGER PRIMARY KEY ON CONFLICT REPLACE AUTOINCREMENT, name TEXT, color INTEGER)";

        assertThat(sql).isEqualTo(result);
    }
}