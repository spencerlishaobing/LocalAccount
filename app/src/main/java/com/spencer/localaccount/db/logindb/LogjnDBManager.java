package com.spencer.localaccount.db.logindb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Administrator on 2018/11/8.
 */

public class LogjnDBManager {
    private SQLiteHelperOfLogjn helper;
    private SQLiteDatabase db;

    public LogjnDBManager(Context context) {
        helper = new SQLiteHelperOfLogjn(context);
        db = helper.getWritableDatabase();
    }

    //增
    public void insert(LogjnBean logjnBean) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO loginTable VALUES(null," +
                            "?,?,?" + ")",
                    new Object[]{
                            logjnBean.getAccount(),
                            logjnBean.getPassword(),
                            logjnBean.getEmail(),
                           });

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }

    }



    public void deleteByAccount(String account) {
        db.beginTransaction();
        try {
            db.execSQL("delete from loginTable where account = '" + account + "'");

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }


    public void update(LogjnBean logjnBean) {
        db.beginTransaction();
        try {
            db.execSQL(

                    "UPDATE loginTable SET " +
                            " password = '" + logjnBean.getPassword() + "'" + "," +
                            " email = '" + logjnBean.getEmail() + "'" + "," +
                            " WHERE account = '" + logjnBean.getAccount() + "'");
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }

    }

    public LogjnBean query(String account) {
        LogjnBean logjnBean = new LogjnBean();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT  *  FROM loginTable WHERE account =  '" + account + "'", null);


            while (cursor.moveToNext()) {

                logjnBean.setAccount(cursor.getString(1));
                logjnBean.setPassword(cursor.getString(2));
                logjnBean.setEmail(cursor.getString(2));

            }
            cursor.close();
        } finally {
            db.endTransaction();    //结束事务
        }
        return logjnBean;
    }


    public boolean isExist(String account) {
        boolean isExist = false;

        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT  *  FROM loginTable WHERE account = '" + account + "'", null);

            while (cursor.moveToNext()) {
                isExist = true;
            }
            cursor.close();
        } finally {
            db.endTransaction();    //结束事务
        }
        return isExist;
    }


}
