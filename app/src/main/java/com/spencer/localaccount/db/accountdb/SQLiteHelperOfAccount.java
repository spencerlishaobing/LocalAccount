package com.spencer.localaccount.db.accountdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/11/8.
 */

public class SQLiteHelperOfAccount extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "account.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelperOfAccount(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLiteHelperOfAccount(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS accountTable " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " userName VARCHAR, " +
                " account VARCHAR, " +
                " password VARCHAR, " +
                " description VARCHAR, " +
                " remark VARCHAR, " +
                " email VARCHAR, " +
                " phone VARCHAR" +
                ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
    }
}
