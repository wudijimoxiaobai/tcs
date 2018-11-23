package com.csscaps.tcs;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.csscaps.common.base.BaseApplication;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.database.table.User;
import com.csscaps.tcs.psam.PSAMUtil;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.suwell.to.ofd.ofdviewer.OFDView;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.SecurityRequestBodyConverter;
import com.tax.fcr.library.network.SecurityResponseBodyConverter;
import com.tax.fcr.library.utils.Logger;
import com.tax.fcr.library.utils.NetworkUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Timer;

import static com.csscaps.tcs.SdcardDBUtil.isWriteSD;
import static com.csscaps.tcs.SdcardDBUtil.lock;

/**
 * Created by tl on 2018/5/2.
 */

public class TCSApplication extends BaseApplication {

    public static User currentUser;
    private Timer timer = new Timer();
    /*纳税人数据库文件*/
    private final String TAXPAYER_DB = "TaxpayerDatabase.db";
    /*发票数据库文件 */
    private final String INVOICE_DB = "InvoiceDatabase.db";

    @Override
    public void onCreate() {
        super.onCreate();
        setNetworkConfig();
        //dbflow 初始化
        FlowManager.init(new FlowConfig.Builder(this).build());
        PSAMUtil.getDeviceSn();
        initData();
        addOfdTemplate();
        SecurityRequestBodyConverter.mClass = SecurityResponseBodyConverter.mClass = PSAMUtil.class;
        FMUtil.init();
        SdcardUtil.sdcardSetPassword();
//        registerActivityLifecycleCallbacks();
        unlockSdcard();
    }

    /**
     * 设置网络配置
     */
    private void setNetworkConfig() {
        NetworkUtils.mContext = this;
        String serverAddress = AppSP.getString("serverAddress");
        String serverPort = AppSP.getString("serverPort");
        if (TextUtils.isEmpty(serverAddress) || TextUtils.isEmpty(serverPort)) return;
        String url;
        if (BuildConfig.https) {
            url = String.format(getString(R.string.url_https_format), serverAddress, serverPort);
        } else url = String.format(getString(R.string.url_http_format), serverAddress, serverPort);
        Api.setBaseUrl(url);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        OFDView.setLicense("测试", "802ADEB5CB87C6722AC93CEC0293BD5306445654");
        boolean initData = AppSP.getBoolean("initData", false);
        if (!initData) {
            new Thread() {
                @Override
                public void run() {
                    //copy纳税人数据库
                    copyDB(TAXPAYER_DB);
                    //copy sd卡发票数据库文件
                    copyDB(INVOICE_DB);
                    AppSP.putBoolean("initData", true);
                    lockTimer();
                }
            }.start();
        } else {
            lockTimer();
        }
    }

    /**
     * 加锁执行定时任务
     */
    private void lockTimer() {
//        //设置sd卡密码，锁定sd卡；
//        SdcardUtil.sdcardSetPassword();
//        SdcardUtil.lockSdcard();
        timer.schedule(new MyTimerTask(), 500, (long) (DateUtils.HOUR_OF_MILLISECOND * 0.5));
    }

    /**
     * 添加ofd模板
     */
    private void addOfdTemplate() {
        new Thread() {
            @Override
            public void run() {
                String parentPath = getFilesDir().getAbsolutePath();
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    String str[] = getAssets().list("English");
                    File parentFile = new File(parentPath + "/English/");
                    if (!parentFile.exists()) {
                        parentFile.mkdir();
                    }

                    for (int i = 0; i < str.length; i++) {
                        File f = new File(parentPath + "/English/" + str[i]);
                        if (!f.exists()) {
                            f.createNewFile();
                        } else {
                            continue;
                        }
                        Logger.i(str[i]);
                        is = getAssets().open("English/" + str[i]);
                        fos = new FileOutputStream(f);
                        byte[] b = new byte[1024];
                        int len;
                        while ((len = is.read(b)) != -1) {
                            fos.write(b, 0, len);
                        }
                        fos.flush();
                        is.close();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) is.close();
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * copy数据库文件
     *
     * @param dbName 数据库名称
     */
    private void copyDB(String dbName) {
        OutputStream out = null;
        InputStream in = null;
        try {
            switch (dbName) {
                case TAXPAYER_DB:
                    in = getAssets().open(TAXPAYER_DB);
                    break;
                case INVOICE_DB:
                    //copy sd卡发票数据库文件
                    Map<String, String> map = System.getenv();
                    String SDPath = map.get("SECONDARY_STORAGE");
                    SdcardUtil.unlockSdcard();
                    File sdFile = new File(SDPath);
                    while (!sdFile.canRead()) {
                    }
                    File file = new File(SDPath + "/FCR/SDInvoiceDatabase.db");
                    if (file.exists()) {
                        in = new FileInputStream(file);
                    }
                    break;
            }

            File file = getDatabasePath(dbName);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            out = new FileOutputStream(file);
            Logger.i("test", "initData" + dbName);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
            in.close();
            out.close();
            Logger.i("test", "initData" + dbName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private int mFinalCount;

    private void registerActivityLifecycleCallbacks() {

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mFinalCount++;
                //如果mFinalCount ==1，说明是从后台到前台
                if (mFinalCount == 1) {

                }

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                //如果mFinalCount ==0，说明是前台到后台
                if (mFinalCount == 0) {
                    //说明从前台回到了后台
//                    SdcardDBUtil.closeDB(SDInvoiceDatabase.class);
//                    SdcardDBUtil.closeDB(DailyDatabase.class);
//                    SdcardUtil.lockSdcard();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    public static void unlockSdcard(){

        //说明从后台回到了前台
        SdcardDBUtil.cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    SdcardUtil.unlockSdcard();
                    //数据库文件是否可以打开
                    int status = SdcardUtil.checkLockSdcardStatus();
                    if (status == 0) {//已解锁
                        boolean isOk = true;
                        while (isOk) {
                            //SD卡是否可读写
                            if (!isWriteSD()) {
                                continue;
                            }
                            isOk = false;
                        }
//                        SdcardDBUtil.openDB(SDInvoiceDatabase.class);
//                        SdcardDBUtil.openDB(DailyDatabase.class);
                        Logger.i("**********已解锁*********");
                    } else if (status == -1) {//解锁失败
                        run();
                    }
                }
            }
        });
    }
}
