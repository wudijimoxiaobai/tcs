package com.csscaps.tcs.database;

import com.csscaps.tcs.database.table.Daily;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * Created by tl on 2018/6/12.
 */
@Migration(database = DailyDatabase.class, version = 1)
public class DailyMigration extends AlterTableMigration<Daily> {


    public DailyMigration(Class<Daily> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
//        addColumn(SQLiteType.INTEGER, "addr");
    }
}
