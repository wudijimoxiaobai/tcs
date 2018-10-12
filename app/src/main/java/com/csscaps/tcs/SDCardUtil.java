package com.csscaps.tcs;

import aclasdriver.SDCard;

/**
 * Created by tl on 2018/10/10.
 */

public class SDCardUtil {

    private static final String Password = "123456";
    private static SDCard mSdcard = new SDCard();

    public static boolean checkLockSdcardStatus(){
        int ret;
        ret = mSdcard.CheckCardStatus();
        if(ret == 1){
            return true;
        }
        return false;
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
