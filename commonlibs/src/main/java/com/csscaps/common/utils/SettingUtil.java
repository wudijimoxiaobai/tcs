package com.csscaps.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.csscaps.common.base.BaseApplication;


/**
 * Created by archerding on 16-8-21.
 */

public class SettingUtil {

    private static SettingUtil mInstance;

    private SharedPreferences mPrefs;

    public static SettingUtil getInstance() {
        if (mInstance == null) {
            mInstance = new SettingUtil(BaseApplication.getAppContext());
        }
        return mInstance;
    }

    private SettingUtil(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SettingUtil putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public SettingUtil putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

    public SettingUtil putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefs.getBoolean(key, defValue);
    }

}
