package com.csscaps.tcs;

import android.text.TextUtils;

import com.csscaps.common.base.BaseApplication;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.database.table.User;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.suwell.to.ofd.ofdviewer.OFDView;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.utils.Logger;
import com.tax.fcr.library.utils.NetworkUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;

/**
 * Created by tl on 2018/5/2.
 */

public class TCSApplication extends BaseApplication {

    public static User currentUser;
    private Timer timer = new Timer();
    /*纳税人数据库文件*/
    private final String DB = "TaxpayerDatabase.db";

    @Override
    public void onCreate() {
        super.onCreate();
        setNetworkConfig();
        //dbflow 初始化
        FlowManager.init(new FlowConfig.Builder(this).build());
        initData();
        addOfdTemplate();
    }

    /**
     * 设置网络配置
     */
    private void setNetworkConfig() {
        NetworkUtils.mContext = this;
        String serverAddress = AppSP.getString("serverAddress");
        String serverPort = AppSP.getString("serverPort");
        if (TextUtils.isEmpty(serverAddress) || TextUtils.isEmpty(serverPort)) return;
        String url = String.format(getString(R.string.url_format), serverAddress, serverPort);
        Api.setBaseUrl(url);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        OFDView.setLicense("测试", "802ADEB5CB87C6722AC93CEC0293BD5306445654");
        timer.schedule(new MyTimerTask(), 500, (long) (DateUtils.HOUR_OF_MILLISECOND * 0.5));
        boolean initData = AppSP.getBoolean("initData", false);
        if (!initData) {
            new Thread() {
                @Override
                public void run() {
                    copyDB();
                }
            }.start();
        }
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

    private void copyDB() {
        InputStream in = null;
        OutputStream out = null;
        try {
            File file = getDatabasePath(DB);
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            in = getAssets().open(DB);
            out = new FileOutputStream(file);
            Logger.i("test", "initData11111");
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
            in.close();
            out.close();
            AppSP.putBoolean("initData", true);
            Logger.i("test", "initData2222");
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


}
