package com.csscaps.tcs.psam;

/**
 * Created by tl on 2018/11/1.
 * psam 指令
 */

public class PSAMCommand {
    /*打开3Fo1目录*/
    final static String COMMAND_3F01 = "00A40000023F01";

    /*取随4个字节机数*/
    final static String COMMAND_GET_RANDOM_4 = "0084000004";

    /*取随8个字节机数*/
    final static String COMMAND_GET_RANDOM_8 = "0084000008";

    /*声明AES加密*/
    final static String COMMAND_DECLARE_AES_ENCRYPT = "802A080000";

    /*非最终数据块AES加解密*/
    final static String COMMAND_NON_FINAL_AES_ENCRYPT_DECODE = "80FA0201D0";

    /*最终数据块AES加解密*/
    final static String COMMAND_FINAL_AES_ENCRYPT_DECODE = "80FA0001D0";

    /*声明AES解密*/
    final static String COMMAND_DECLARE_AES_DECODE = "802A090000";

    /*sha256  小于256*/
    final static String COMMAND_SHA256_LT = "80CC0000";

    /*sha256  大于256 第一个报文*/
    final static String COMMAND_SHA256_GT_S = "80CC8000C0";

    /*sha256  大于256 中间报文*/
    final static String COMMAND_SHA256_GT_M = "80CCC000C0";

    /*sha256  大于256 最后报文*/
    final static String COMMAND_SHA256_GT_E = "80CCE000";

    /*RSA 签名*/
    final static String COMMAND_RSA = "80C20001";

    /*服务器返回数据验签*/
    final static String COMMAND_VERIFY_SIGN = "80C4000300";

    /*外部认证*/
    final static String COMMAND_EXTERNAL_AUTHENTICATE = "0082000008";
}
