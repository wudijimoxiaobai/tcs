package com.csscaps.tcs;

import com.csscaps.common.utils.NumberBytesUtil;
import com.tax.fcr.library.utils.Logger;

import aclasdriver.AclasBaseFunction;

/**
 * Created by tl on 2018/11/15.
 */

public class FMUtil {
    private static AclasBaseFunction base = new AclasBaseFunction();

    public static void init() {
        base.initFicalMemory();
    }

    public static boolean writeFM(int addr, byte[] bOut) {
        int iRet = base.writeFicalMemory(addr, bOut.length, bOut);
        Logger.i("iRet "+iRet);
        if (iRet == 0) return true;
        return false;
    }


    public static byte[] readFM(int addr, byte[] bIn) {
        base.readFicalMemory(addr, bIn.length, bIn);
        return bIn;
    }

    public static void eraserFM() {
        base.eraserFicalMemory();
    }

    public static int getFMCapability() {
        byte[] bSize = new byte[4];
        base.getFicalMemoryCapability(bSize);
        return NumberBytesUtil.bytesToInt(bSize);
    }
}
