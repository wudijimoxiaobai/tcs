package com.csscaps.tcs;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.csscaps.common.base.BaseApplication;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.Taxpayer;
import com.csscaps.tcs.database.table.User;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.utils.Logger;
import com.tax.fcr.library.utils.NetworkUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by tl on 2018/5/2.
 */

public class TCSApplication extends BaseApplication {

    public static User currentUser;
    private Timer timer = new Timer();


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
        timer.schedule(new MyTimerTask(), 500, (long) (DateUtils.HOUR_OF_MILLISECOND * 0.5));
        boolean initData = AppSP.getBoolean("initData", false);
        if (!initData) {
            new Thread() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                    InputStream is = null;
                    InputStreamReader isr = null;
                    BufferedReader br = null;
                    try {
                        is = getAssets().open("tcs.csv");
                        isr = new InputStreamReader(is);
                        br = new BufferedReader(isr);
                        String line = "";
                        List<Taxpayer> taxpayerList = new ArrayList<>();
                        long s = System.currentTimeMillis();
                        int i = 0;
                        while ((line = br.readLine()) != null) {
                            i++;
                            line = line.replaceAll("\"", "");
                            String[] str = line.split(",");
                            try {
                                Taxpayer taxpayer = new Taxpayer();
                                taxpayer.setTin(str[1]);
                                taxpayer.setEname(str[2]);
                                taxpayer.setAname(str[3]);
                                taxpayer.setState(str[4]);
                                taxpayer.setCity(str[5]);
                                taxpayer.setAddress(str[6]);
                                taxpayer.setTel(str[7]);
                                taxpayerList.add(taxpayer);
                                if (i % 500 == 0) {
                                    insert(taxpayerList);
                                    taxpayerList.clear();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        insert(taxpayerList);
                        taxpayerList.clear();
                        long e = System.currentTimeMillis();
                        Logger.i(" time1  " + (e - s));
                        AppSP.putBoolean("initData", true);
                        mHandler.sendEmptyMessage(1);
                        br.close();
                        isr.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            br.close();
                            isr.close();
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

    }

    private void insert(List<Taxpayer> taxpayerList) {
//        long s = System.currentTimeMillis();
        // 批量插入
        FlowManager.getDatabase(TcsDatabase.class)
                .executeTransaction(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<Taxpayer>() {
                            @Override
                            public void processModel(Taxpayer model, DatabaseWrapper wrapper) {
                                model.save();
                            }
                        }).addAll(taxpayerList).build());
//        long e = System.currentTimeMillis();
//        Logger.i(" time3  " + (e - s));
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showShort(getString(R.string.hit19));
                    break;
                case 1:
                    ToastUtil.showShort(getString(R.string.hit20));
                    break;
            }
        }
    };

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
                        if(!f.exists()){
                            f.createNewFile();
                        }else{
                            continue;
                        }
                        Logger.i(str[i]);
                        is = getAssets().open("English/" + str[i]);
                        fos = new FileOutputStream(f);
                        byte[] b = new byte[1024];
                        while (is.read(b) != -1) {
                            fos.write(b);
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
}
