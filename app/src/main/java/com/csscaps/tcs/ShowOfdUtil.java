package com.csscaps.tcs;

import com.csscaps.tcs.database.table.Invoice;
import com.suwell.ofd.formsdk.NativeFormer;
import com.suwell.to.ofd.ofdviewer.OFDView;
import com.suwell.to.ofd.ofdviewer.listener.OnLoadCompleteListener;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tl on 2018/7/13.
 */

public class ShowOfdUtil {
    //生成ofd文件路径
    public static final String OFD_FILE_PATH = "/sdcard/data.ofd";
    public static final String LOCK = "lock";
    public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();


    public static void showOfd(final Invoice showInvoice, final OFDView mOfdView, final OnLoadCompleteListener onLoadCompleteListener) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK) {
                    String dataXmlPath = TCSApplication.getAppContext().getFilesDir().getAbsolutePath() + "/English/data.xml";
                    GeneratingXMLFileUtils.generatingXmlFile(showInvoice, dataXmlPath);
                    try {
                        NativeFormer nativeFormer = new NativeFormer();
                        nativeFormer.parseXML(TCSApplication.getAppContext().getFilesDir().getAbsolutePath() + "/English/Main.xml", OFD_FILE_PATH);
                        File file = new File(OFD_FILE_PATH);
                        if (mOfdView.isAttachedToWindow()) {
                            mOfdView.fromFile(file)
                                    .defaultPage(0)
                                    .swipeHorizontal(true)
                                    .onLoad(onLoadCompleteListener)
                                    .load();
                            mOfdView.useBestQuality(true);
                            mOfdView.zoomTo(2.5f);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
