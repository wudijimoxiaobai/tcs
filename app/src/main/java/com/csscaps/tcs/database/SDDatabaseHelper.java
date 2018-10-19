package com.csscaps.tcs.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tl on 2018/10/18.
 */

public class SDDatabaseHelper extends SQLiteOpenHelper {
    /**
     * 数据库版本，需要升级数据库时只要加一即可
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * 数据库名
     */
    private static final String DATABASE_NAME = "SDInvoiceDatabase.db";

    public SDDatabaseHelper(Context context) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建user表，属性：id（用户id，主键）、name（姓名）、age（年龄）
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(10),age INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
