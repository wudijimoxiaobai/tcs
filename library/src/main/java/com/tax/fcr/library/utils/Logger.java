package com.tax.fcr.library.utils;

import android.util.Log;

import com.tax.fcr.library.BuildConfig;


/**
 * 日志
 */
public class Logger {


    public static boolean isDebug = BuildConfig.DEBUG;
    public static final String TAG = "CSSCAPS";

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }
    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void w(String msg) {
        if (isDebug)
            Log.w(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }
}
