package com.csscaps.tcs;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.csscaps.tcs.database.SDInvoiceDatabase;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.SdInvoice;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by tl on 2018/10/17.
 * SD卡数据库操作
 */

public class SdcardDBUtil {

    // sd卡数据库是否打是否打开
    public static boolean isOpen = true;

    /**
     * 关闭sd卡数据库
     */
    public static void closeDB() {
        isOpen = false;
        FlowManager.getDatabase(SDInvoiceDatabase.class).getHelper().closeDB();
    }

    /**
     * 打开sd卡数据库
     * @param context
     */
    public static void openDB(Context context) {
        boolean isOk=true;
        while (isOk){
            try {
                Thread.sleep(10);
                Map<String, String> map = System.getenv();
                String SDPath = map.get("SECONDARY_STORAGE");
                String status = Environment.getExternalStorageState(new File(SDPath));
                Log.i("test","status "+status);
                if(!status.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) continue;
                Thread.sleep(100);
                if(!isOpen){
                    File file = new File(SDPath + "/FCR");
                    FileDatabaseContext mSdDatabaseContext = new FileDatabaseContext(context, file, false);
                    FlowManager.getDatabase(SDInvoiceDatabase.class).reset(mSdDatabaseContext);
                    FlowManager.getDatabase(SDInvoiceDatabase.class).getWritableDatabase();
                }
                isOk=false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isOpen = true;
    }


    public static void insertInvoiceSDDB(Context context,Invoice invoice){
        SdcardUtil.unlockSdcard();
        Log.i("TEST", " " + SdcardUtil.checkLockSdcardStatus());
        if (!SdcardUtil.checkLockSdcardStatus()) {
            SdcardDBUtil.openDB(context);
            SdInvoice sdInvoice = new SdInvoice();
            sdInvoice.setInvoice_type_code(invoice.getInvoice_short_code());
            sdInvoice.setInvoice_made_by(invoice.getInvoice_made_by());
            sdInvoice.save();
            SdcardDBUtil.closeDB();
        }
        SdcardUtil.lockSdcard();
        Log.i("TEST", " " + SdcardUtil.checkLockSdcardStatus());


    }

    public static void insertProductModelSDDB(Context context,List<ProductModel> goods){


    }
}



