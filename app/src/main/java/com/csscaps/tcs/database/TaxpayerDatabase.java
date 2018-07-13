package com.csscaps.tcs.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by tl on 2018/7/12.
 */
@Database(name = TaxpayerDatabase.NAME, version = TaxpayerDatabase.VERSION)
public class TaxpayerDatabase {
    public static final String NAME = "TaxpayerDatabase";

    public static final int VERSION = 1;
}
