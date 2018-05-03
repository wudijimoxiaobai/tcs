package com.tax.fcr.library.utils;

import android.util.Base64;

/**
 * Created by thinkpad on 2016/9/2.
 */
public class SecurityUtil {

    //加密
    public static String getBase64(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }

    // 解密
    public static String getFromBase64(String s) {
        return new String(Base64.decode(s.getBytes(), Base64.NO_WRAP));
    }

    /**
     * Base64 解码
     *
     * @param s 要解码的字符串
     * @return Base64 解码后的字符串
     */
    public static byte[] base64Decode(final String s) {
        return Base64.decode(s, Base64.NO_WRAP);
    }

    /**
     * Base64 编码
     *
     * @param input 要编码的字节数组
     * @return Base64 编码后的字符串
     */
    public static String base64Encode2String(final byte[] input) {
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }
}
