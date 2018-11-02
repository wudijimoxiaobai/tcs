package com.csscaps.tcs.psam;

/**
 * Created by tl on 2018/11/1.
 * psam 指令
 */

public class PSAMCommand {
    /*打开3Fo1目录*/
    final static String COMMAND_3F01 = "00A40000023F01";

    /*取随机数*/
    final static String COMMAND_GET_RANDOM = "0084000004";

    /*声明AES加密*/
    final static String COMMAND_DECLARE_AES_ENCRYPT = "802A080000";

    /*非最终数据块AES加解密*/
    final static String COMMAND_NON_FINAL_AES_ENCRYPT_DECODE = "80FA0201D0";

    /*最终数据块AES加解密*/
    final static String COMMAND_FINAL_AES_ENCRYPT_DECODE = "80FA0001D0";

    /*声明AES解密*/
    final static String COMMAND_DECLARE_AES_DECODE = "802A090000";


}
