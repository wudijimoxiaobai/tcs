package com.csscaps.tcs.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.csscaps.tcs.psam.PSAMUtil;
import com.tax.fcr.library.utils.Logger;

/**
 * Created by tl on 2018/11/13.
 */

public class ScreenTurningOffReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_OFF:
                Logger.i("ACTION_SCREEN_OFF");
                PSAMUtil.disconnect();
                break;
            case Intent.ACTION_SCREEN_ON:
                Logger.i("ACTION_SCREEN_ON");
                PSAMUtil.connect();
                break;
        }
    }
}
