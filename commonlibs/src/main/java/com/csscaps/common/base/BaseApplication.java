package com.csscaps.common.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;


/**
 * Created by tanglei on 16/6/15.
 */
public class BaseApplication extends Application {
    public static Context mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static Context getAppContext() {
        return mApplication;
    }

    public static Resources getAppResources() {
        return mApplication.getResources();
    }
}
