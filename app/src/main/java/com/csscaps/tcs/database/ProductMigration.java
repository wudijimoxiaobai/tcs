package com.csscaps.tcs.database;

import com.csscaps.tcs.database.table.Invoice;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * Created by tl on 2018/6/12.
 */
@Migration(database = TcsDatabase.class,version = 1)
public class ProductMigration extends AlterTableMigration<Invoice> {


    public ProductMigration(Class<Invoice> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
//        addColumn(SQLiteType.TEXT,"requestBy");
//        addColumn(SQLiteType.TEXT,"requestDate");
    }
}
