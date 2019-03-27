package com.spencer.localaccount.db.accountdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/11/8.
 */

public class AccountDBManager {
    private SQLiteHelperOfAccount helper;
    private SQLiteDatabase db;


    public AccountDBManager(Context context) {
        helper = new SQLiteHelperOfAccount(context);
        db = helper.getWritableDatabase();
    }

    //增
    public void insert(AccountBean accountBean) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO accountTable VALUES(null," +
                            "?,?,?,?,?,?,?)",
                    new Object[]{
                            accountBean.getUserName(),
                            accountBean.getAccount(),
                            accountBean.getPassword(),
                            accountBean.getDescription(),
                            accountBean.getRemark(),
                            accountBean.getEmail(),
                            accountBean.getPhone(),
                    });

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }

    }


    //删除某一条记录
    public void deleteByDes(String description) {
        db.beginTransaction();
        try {
            db.execSQL("delete from accountTable where description = '" + description + "'");

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }


    //删除所有记录
    public void deleteAllByUserName(String userName) {
        db.beginTransaction();
        try {
            db.execSQL("delete from accountTable where userName = '" + userName + "'");

            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }


    public void update(AccountBean accountBean) {
        db.beginTransaction();
        try {
            db.execSQL(

                    "UPDATE accountTable SET " +
                            " userName = '" + accountBean.getUserName() + "'" + "," +
                            " account = '" + accountBean.getAccount() + "'" + "," +
                            " password = '" + accountBean.getPassword() + "'" + "," +
                            " remark = '" + accountBean.getRemark() + "'" + "," +
                            " email = '" + accountBean.getEmail() + "'" + "," +
                            " phone = '" + accountBean.getPhone() + "'" + "," +
                            " WHERE description = '" + accountBean.getDescription() + "'");
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }

    }

    public AccountBean query(String description) {
        AccountBean accountBean = new AccountBean();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT  *  FROM accountTable WHERE description =  '" + description + "'", null);


            while (cursor.moveToNext()) {
                accountBean.setUserName(cursor.getString(1));
                accountBean.setAccount(cursor.getString(2));
                accountBean.setPassword(cursor.getString(3));
                accountBean.setDescription(cursor.getString(4));
                accountBean.setRemark(cursor.getString(5));
                accountBean.setEmail(cursor.getString(6));
                accountBean.setPhone(cursor.getString(7));
            }
            cursor.close();
        } finally {
            db.endTransaction();    //结束事务
        }
        return accountBean;
    }

    public ArrayList<AccountBean> queryAll(String userName) {
        ArrayList<AccountBean> arrayList = new ArrayList<>();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT  *  FROM accountTable WHERE userName =  '" + userName + "'", null);


            while (cursor.moveToNext()) {
                AccountBean accountBean = new AccountBean();
                accountBean.setUserName(cursor.getString(1));
                accountBean.setAccount(cursor.getString(2));
                accountBean.setPassword(cursor.getString(3));
                accountBean.setDescription(cursor.getString(4));
                accountBean.setRemark(cursor.getString(5));
                accountBean.setEmail(cursor.getString(6));
                accountBean.setPhone(cursor.getString(7));
                arrayList.add(accountBean);
            }
            cursor.close();
        } finally {
            db.endTransaction();    //结束事务
        }
        return arrayList;
    }


    public boolean isExist(String description) {
        boolean isExist = false;

        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT  *  FROM accountTable WHERE sn = '" + description + "'", null);

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
