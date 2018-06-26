package com.csscaps.tcs.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by tl on 2018/6/26.
 */

public class ReportDataService extends Service {
    boolean fromOnlineDeclarationActivity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fromOnlineDeclarationActivity = intent.getBooleanExtra("fromOnlineDeclarationActivity", false);
        report();
        return super.onStartCommand(intent, flags, startId);
    }

    private void report(){


    }
}
