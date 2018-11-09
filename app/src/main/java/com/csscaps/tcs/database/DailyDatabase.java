package com.csscaps.tcs.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by tl on 2018/10/26.
 */
@Database(name = DailyDatabase.NAME, version = DailyDatabase.VERSION)
public class DailyDatabase {
    public static final String NAME = "DailyDatabase";

    public static final int VERSION = 1;
}
