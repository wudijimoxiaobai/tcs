package com.csscaps.tcs.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by tl on 2018/10/26.
 */
@Database(name = InvoiceDatabase.NAME, version = InvoiceDatabase.VERSION)
public class InvoiceDatabase {
    public static final String NAME = "InvoiceDatabase";

    public static final int VERSION = 1;
}
