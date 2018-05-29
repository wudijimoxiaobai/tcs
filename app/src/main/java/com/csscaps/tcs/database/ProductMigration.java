package com.csscaps.tcs.database;

import com.csscaps.tcs.database.table.Product;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * Created by tl on 2017/10/23.
 */
@Migration(database = TcsDatabase.class,version = 1)
public class ProductMigration extends AlterTableMigration<Product> {


    public ProductMigration(Class<Product> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
//        addColumn(SQLiteType.TEXT,"duration");
    }
}
