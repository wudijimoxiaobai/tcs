package com.csscaps.tcs;

import android.util.Log;

import aclasdriver.SDCard;

/**
 * Created by tl on 2018/10/10.
 * 外置sdcard 加密
 */

public class SdcardUtil {

    private static final String Password = "aclas";
    private static SDCard mSdcard =new SDCard();

    public static boolean checkLockSdcardStatus(){
        int ret;
        ret = mSdcard.CheckCardStatus();
        Log.i("TEST","Status "+ret);
        if(ret == 0){
            return false;
        }
        return true;
    }

    public static void sdcardSetPassword(){
        byte[] b = Password.getBytes();
        mSdcard.SdcardSetPasswd(b, 1);
    }

    public static void lockSdcard(){
        byte[] b = Password.getBytes();
        mSdcard.SdcardSetPasswd(b, 0);
        mSdcard.SdcardLock();
    }

    public static void unlockSdcard(){
        byte[] b = Password.getBytes();
        mSdcard.SdcardSetPasswd(b, 0);
        mSdcard.SdcardUnLock();
    }

    public static void sdcardClearPassword(){
        byte[] b = Password.getBytes();
        mSdcard.SdcardSetPasswd(b, 0);
        mSdcard.SdcardClearPasswd();
    }


}
