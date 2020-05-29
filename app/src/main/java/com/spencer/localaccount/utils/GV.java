package com.spencer.localaccount.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hasee on 2019/3/26.
 */

public class GV {
    private static final String DATA = "data";

    public static void setUserName(Context context, String value) {
        GV.setValue(context, "account", value);
    }

    public static String getUserName(Context context) {
        return getValue(context, "account");
    }

    public static void setValue(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(DATA, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATA, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
