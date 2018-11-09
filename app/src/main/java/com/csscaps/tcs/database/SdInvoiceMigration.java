package com.csscaps.tcs.database;

import com.csscaps.tcs.database.table.SdInvoice;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * Created by tl on 2018/6/12.
 */
@Migration(database = SDInvoiceDatabase.class, version = 1)
public class SdInvoiceMigration extends AlterTableMigration<SdInvoice> {


    public SdInvoiceMigration(Class<SdInvoice> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
//        addColumn(SQLiteType.TEXT, "fiscal_long_code");
    }
}
