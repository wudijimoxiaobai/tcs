package com.csscaps.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.csscaps.common.base.BaseApplication;


/**
 * Created by tanglei on 16/6/30.
 * 查数据缓存
 */
public class AppSP {

    public static SharedPreferences sp;

    static {
        sp = BaseApplication.getAppContext().getSharedPreferences("AppSP", Context.MODE_PRIVATE);
    }

    public static void putString(String k, String v) {
        sp.edit().putString(k, v).commit();
    }

    public static String getString(String k) {
        String v = sp.getString(k, null);
        return v;
    }

    public static void putInt(String k, int v) {
        sp.edit().putInt(k, v).commit();
    }

    public static int getInt(String k) {
        return sp.getInt(k, -1);
    }

    public static int getInt(String k, int defValue) {
        return sp.getInt(k, defValue);
    }

    public static void putBoolean(String k, boolean v) {
        sp.edit().putBoolean(k, v).commit();
    }

    public static boolean getBoolean(String k) {
        return sp.getBoolean(k, false);
    }

    public static boolean getBoolean(String k, boolean defValue) {
        return sp.getBoolean(k, defValue);
    }

    public static void putFloat(String k, float v) {
        sp.edit().putFloat(k, v).commit();
    }

    public static float getFloat(String k, float defValue) {
        return sp.getFloat(k, defValue);
    }

    public static void putLong(String k, long v) {
        sp.edit().putLong(k, v).commit();
    }

    public static long getLong(String k) {
        return sp.getLong(k, -1);
    }
}
