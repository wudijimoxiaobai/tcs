package com.csscaps.tcs;

import com.csscaps.tcs.database.table.Invoice;
import com.suwell.ofd.formsdk.NativeFormer;
import com.suwell.to.ofd.ofdviewer.OFDView;

import java.io.File;

/**
 * Created by tl on 2018/7/13.
 */

public class ShowOfdUtils {
    //生成ofd文件路径
    public static final String OFD_FILE_PATH = "/sdcard/data.ofd";

    public static void showOfd(final Invoice showInvoice, final OFDView mOfdView) {
        new Thread() {
            @Override
            public void run() {
                String dataXmlPath = TCSApplication.getAppContext().getFilesDir().getAbsolutePath() + "/English/data.xml";
                GeneratingXMLFileUtils.generatingXmlFile(showInvoice, dataXmlPath);
                try {
                    NativeFormer nativeFormer = new NativeFormer();
                    nativeFormer.parseXML(TCSApplication.getAppContext().getFilesDir().getAbsolutePath() + "/English/Main.xml", OFD_FILE_PATH);
                    File file = new File(OFD_FILE_PATH);
                    mOfdView.fromFile(file)
                            .defaultPage(0)
                            .swipeHorizontal(false)
                            .load();
                    mOfdView.useBestQuality(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
