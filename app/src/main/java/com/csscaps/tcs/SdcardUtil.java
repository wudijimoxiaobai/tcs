package com.csscaps.tcs;

import android.util.Log;

import aclasdriver.SDCard;

/**
 * Created by tl on 2018/10/10.
 * 外置sdcard 加密
 */

public class SdcardUtil {

    private static final String Password = "aclas";
    private static SDCard mSdcard = new SDCard();

    /**
     * 检查sd卡状态
     *
     * @return
     */
    public static int checkLockSdcardStatus() {
        int ret;
        ret = mSdcard.CheckCardStatus();
        Log.i("TEST", "Status " + ret);
        return ret;
    }

    /**
     * 设置sd卡密码
     */
    public static void sdcardSetPassword() {

        byte[] b = Password.getBytes();
        mSdcard.SdcardSetPasswd(b, 1);
    }

    /**
     * 锁定SD卡
     */
    public static void lockSdcard() {
        if (checkLockSdcardStatus() == 0) {
            int ret = -1;
            while (ret != 0) {
                byte[] b = Password.getBytes();
                mSdcard.SdcardSetPasswd(b, 0);
                ret = mSdcard.SdcardLock();
                Log.i("TEST", "SdcardLock Status " + ret);
            }
        }
    }

    /**
     * 解锁sd卡
     */
    public static void unlockSdcard() {
        if (checkLockSdcardStatus() == 1) {
            int ret = -1;
            while (ret != 0) {
                byte[] b = Password.getBytes();
                mSdcard.SdcardSetPasswd(b, 0);
                ret = mSdcard.SdcardUnLock();
            }
        }
    }

    /**
     * 清除sd卡密码
     */
    public static void sdcardClearPassword() {
        byte[] b = Password.getBytes();
        mSdcard.SdcardSetPasswd(b, 0);
        mSdcard.SdcardClearPasswd();
    }


}
