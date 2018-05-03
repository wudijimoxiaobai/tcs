package com.csscaps.tcs;

import com.csscaps.common.base.BaseApplication;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tax.fcr.library.TCSInvoice;

/**
 * Created by tl on 2018/5/2.
 */

public class TCSApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        TCSInvoice.init(this, BuildConfig.server);
        //dbflow 初始化
        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
