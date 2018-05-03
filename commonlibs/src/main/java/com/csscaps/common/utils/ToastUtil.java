package com.csscaps.common.utils;

import android.widget.Toast;

import com.csscaps.common.base.BaseApplication;


/**
 * Toast 工具类
 */
public class ToastUtil {
    private static Toast mToast;

    public static void showShort(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showLong(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
