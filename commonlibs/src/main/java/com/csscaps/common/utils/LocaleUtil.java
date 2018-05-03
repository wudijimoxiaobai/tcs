package com.csscaps.common.utils;



import com.csscaps.common.base.BaseApplication;

import java.util.Locale;

/**
 * Created by archerding on 2016/9/5.
 */
public class LocaleUtil {
    public static String getLocale() {
        Locale locale = BaseApplication.getAppResources().getConfiguration().locale;
        return locale.toString().replace('_','-');
    }
}
