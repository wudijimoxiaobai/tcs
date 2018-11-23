package com.csscaps.tcs.psam;

import android.text.TextUtils;

import com.aclas.aclasdemoforcr20.PCSCException;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.ConvertUtils;
import com.csscaps.common.utils.NumberBytesUtil;
import com.csscaps.tcs.model.RequestData;
import com.tax.fcr.library.utils.EncryptUtils;
import com.tax.fcr.library.utils.Logger;

import static com.aclas.aclasdemoforcr20.PCSC.SCardConnect;
import static com.aclas.aclasdemoforcr20.PCSC.SCardDisconnect;
import static com.aclas.aclasdemoforcr20.PCSC.SCardEstablishContext;
import static com.aclas.aclasdemoforcr20.PCSC.SCardGetStatusChange;
import static com.aclas.aclasdemoforcr20.PCSC.SCardIsValidContext;
import static com.aclas.aclasdemoforcr20.PCSC.SCardListReaders;
import static com.aclas.aclasdemoforcr20.PCSC.SCardReleaseContext;
import static com.aclas.aclasdemoforcr20.PCSC.SCardStatus;
import static com.aclas.aclasdemoforcr20.PCSC.SCardTransmit;
import static com.aclas.aclasdemoforcr20.PCSC.TIMEOUT_INFINITE;
import static com.aclas.aclasdemoforcr20.PCSCDefines.SCARD_PROTOCOL_T0;
import static com.aclas.aclasdemoforcr20.PCSCDefines.SCARD_PROTOCOL_T1;
import static com.aclas.aclasdemoforcr20.PCSCDefines.SCARD_SCOPE_SYSTEM;
import static com.aclas.aclasdemoforcr20.PCSCDefines.SCARD_SHARE_SHARED;
import static com.aclas.aclasdemoforcr20.PCSCDefines.SCARD_STATE_EMPTY;
import static com.aclas.aclasdemoforcr20.PCSCDefines.SCARD_UNPOWER_CARD;
import static com.csscaps.common.utils.AppTools.subByte;
import static com.csscaps.common.utils.ConvertUtils.bytes2HexString;
import static com.csscaps.common.utils.ConvertUtils.hexString2Bytes;
import static com.tax.fcr.library.network.RequestModel.devicesn;

/**
 * Created by tl on 2018/10/31.
 */

public class PSAMUtil {

    protected static long contextId;
    private static long cardId;
    private static int protocol;
    private static String SUCCESSFUL_CODE = "9000";
    private static boolean isOpen3F01 = false;
    public static boolean isAuthenticate = false;

    /**
     * 连接psam
     */
    public static byte[] connect() {
        try {
            contextId = SCardEstablishContext(SCARD_SCOPE_SYSTEM);
            SCardIsValidContext(contextId);
            String[] readerNames = SCardListReaders(contextId);
            int n = readerNames.length;
            int[] status = new int[n];
            for (int i = 0; i < readerNames.length; i++) {
                status[i] = SCARD_STATE_EMPTY;
            }

            SCardGetStatusChange(contextId, TIMEOUT_INFINITE, status, readerNames);
            cardId = SCardConnect(contextId, readerNames[0], SCARD_SHARE_SHARED, SCARD_PROTOCOL_T0 | SCARD_PROTOCOL_T1);
            byte[] Cardstatus = new byte[2];
            byte[] pBAtr = SCardStatus(cardId, Cardstatus);
            protocol = Cardstatus[1];
            isOpen3F01=open3F01();
            Logger.i("connect ");
            return pBAtr;
        } catch (PCSCException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 断开psam卡连接
     */
    public static void disconnect() {
        try {
            Logger.i("disconnect ");
            SCardDisconnect(cardId, SCARD_UNPOWER_CARD);
            SCardReleaseContext(contextId);
            isOpen3F01=false;
        } catch (PCSCException e) {
            e.printStackTrace();
        }
    }


    public static void getDeviceSn() {
        byte[] pBAtr = connect();
        if (pBAtr == null) return;
        byte[] bytes = subByte(pBAtr, 17, 5);
        byte[] b = new byte[5];
        for (int i = 0; i < 5; i++) {
            b[i] = bytes[4 - i];
        }
        devicesn = String.valueOf(NumberBytesUtil.bytesToLong(b));
        RequestData.sellerid = AppSP.getString("sellerid");
        disconnect();
    }

    /**
     * 执行命令
     *
     * @param command 命令
     * @return
     */
    private static PSAMResponse command(byte[] command) {
        Logger.i("command " + bytes2HexString(command));
        PSAMResponse mPSAMResponse  = new PSAMResponse();
        try {
            byte[] result = SCardTransmit(cardId, protocol, command, 0, command.length);
            Logger.i("PSAMResponse" + bytes2HexString(result));
            if (result != null && result.length >= 2) {
                String resultCode = bytes2HexString(subByte(result, result.length - 2, 2));
                Logger.i("resultCode" + resultCode);
                if (resultCode.equals(SUCCESSFUL_CODE)) {
                    mPSAMResponse.setSuccessful(true);
                    mPSAMResponse.setResult(subByte(result, 0, result.length - 2));
                }
            }
        } catch (PCSCException e) {
            e.printStackTrace();
        }
        return mPSAMResponse;
    }

    /**
     * 打开3F01目录
     *
     * @return
     */
    private static boolean open3F01() {
        PSAMResponse mPSAMResponse = command(hexString2Bytes(PSAMCommand.COMMAND_3F01));
        return mPSAMResponse.isSuccessful();
    }

    /**
     * 外部认证
     */
    public static void externalAuthenticate() {
        String AESkey = AppSP.getString("3f_key");
        if (TextUtils.isEmpty(AESkey)) return;
        byte[] AESkeys = ConvertUtils.base64Decode(AESkey);//AES秘钥长度是32个字节
        byte[] DESKeys1 = new byte[8];//DES秘钥长度是8个字节
        byte[] DESKeys2 = new byte[8];
        System.arraycopy(AESkeys, 0, DESKeys1, 0, 8);
        System.arraycopy(AESkeys, 8, DESKeys2, 0, 8);
        //获取8个字节的随机码
        PSAMResponse mPSAMResponse = command(hexString2Bytes(PSAMCommand.COMMAND_GET_RANDOM_8));
        byte[] randomBytes = mPSAMResponse.getResult();
        String transformation = "DES/ECB/NoPadding";
        byte[] data1 = EncryptUtils.encryptDES(randomBytes, DESKeys1, transformation, null);
        byte[] data2 = EncryptUtils.decryptDES(data1, DESKeys2, transformation, null);
        byte[] data3 = EncryptUtils.encryptDES(data2, DESKeys1, transformation, null);
        mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_EXTERNAL_AUTHENTICATE), data3));
        if (mPSAMResponse.isSuccessful()) {
            isAuthenticate = true;
        } else isAuthenticate = false;
    }


    /**
     * AES加密
     *
     * @param sbt
     * @return
     */
    public static byte[] encryptAES(byte[] sbt) {
        while (!isOpen3F01) connect();
        PSAMResponse mPSAMResponse = command(hexString2Bytes(PSAMCommand.COMMAND_DECLARE_AES_ENCRYPT));
        if (mPSAMResponse.isSuccessful()) {
            if (sbt.length > 192) {
                int s = sbt.length / 192;
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < s; i++) {
                    byte[] data;
                    if (i == s - 1) {
                        if (s * 192 == sbt.length) {
                            data = AppTools.subByte(sbt, i * 192, 192);
                            mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                            sb.append(bytes2HexString(mPSAMResponse.getResult()));
                        } else {
                            data = AppTools.subByte(sbt, i * 192, 192);
                            mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_NON_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                            sb.append(bytes2HexString(mPSAMResponse.getResult()));
                            data = AppTools.subByte(sbt, (i + 1) * 192, sbt.length - s * 192);
                            mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                            sb.append(bytes2HexString(mPSAMResponse.getResult()));
                        }
                    } else {
                        data = AppTools.subByte(sbt, i * 192, 192);
                        mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_NON_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                        sb.append(bytes2HexString(mPSAMResponse.getResult()));
                    }
                }
                disconnect();
                return hexString2Bytes(sb.toString());
            } else {
                mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), fill80n080(sbt)));
                disconnect();
                return mPSAMResponse.getResult();

            }
        }
        disconnect();
        return null;
    }

    /**
     * 填充规则为:带加密数据长度按192位分段，然后每段后面补8000…00…0080 （80开头和80结尾，中间n个00；），将数据补成208个字节
     *
     * @param bytes
     * @return
     */
    private static byte[] fill80n080(byte[] bytes) {
        int s = 208 - bytes.length;
        byte[] bytes1 = hexString2Bytes(getPaddingBytesString(s));
        return AppTools.byteMerger(bytes, bytes1);
    }


    /**
     * 获得补码hex字符串  80开头和80结尾，中间n个00
     *
     * @param s >=2
     * @return
     */
    private static String getPaddingBytesString(int s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s; i++) {
            if (i == 0 || i == s - 1) {
                sb.append("80");
            } else {
                sb.append("00");
            }
        }
        return sb.toString();
    }

    /**
     * AES解密
     *
     * @param sbt
     * @return
     */
    public static byte[] decodeAES(byte[] sbt) {
        connect();
        PSAMResponse mPSAMResponse = command(hexString2Bytes(PSAMCommand.COMMAND_DECLARE_AES_DECODE));
        if (mPSAMResponse.isSuccessful()) {
            int s = sbt.length / 208;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < s; i++) {
                byte[] data = AppTools.subByte(sbt, i * 208, 208);
                if (i == s - 1) {
                    mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), data));
                    String resultStr = ConvertUtils.bytes2HexString(mPSAMResponse.getResult());
                    resultStr = wipeOffPaddingBytes(resultStr);
                    byte[] string = ConvertUtils.hexString2Bytes(sb.toString() + resultStr);
                    disconnect();
                    return string;
                } else {
                    mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_NON_FINAL_AES_ENCRYPT_DECODE), data));
                    String resultStr = ConvertUtils.bytes2HexString(mPSAMResponse.getResult());
                    resultStr = wipeOffPaddingBytes(resultStr);
                    sb.append(resultStr);
                }
            }
        }
        disconnect();
        return null;
    }

    /**
     * 去除补码 80...00...80
     *
     * @param finalDecodeString
     * @return
     */
    private static String wipeOffPaddingBytes(String finalDecodeString) {
        for (int i = 0; i < 2; i++) {
            int index = finalDecodeString.lastIndexOf("80");
            finalDecodeString = finalDecodeString.substring(0, index);
        }
        return finalDecodeString;
    }

    /**
     * SHA256
     *
     * @param sbt
     * @return
     */
    public static byte[] SHA256(byte[] sbt) {
        PSAMResponse mPSAMResponse = new PSAMResponse();
        int length = sbt.length;
        if (length >= 256) {
            int s = length / 192;
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < s; i++) {
                byte[] data;
                if (i == 0) {
                    data = AppTools.subByte(sbt, i * 192, 192);
                    mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_S), data));
                    sb.append(bytes2HexString(mPSAMResponse.getResult()));
                } else if (i == s - 1) {
                    if (s * 192 == length) {
                        data = AppTools.subByte(sbt, i * 192, 192);
                        mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_E), new byte[]{(byte) data.length}, data));
                        sb.append(bytes2HexString(mPSAMResponse.getResult()));
                        return hexString2Bytes(sb.toString().replace("null", ""));
                    } else {
                        data = AppTools.subByte(sbt, i * 192, 192);
                        mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_M), data));
                        sb.append(bytes2HexString(mPSAMResponse.getResult()));
                        data = AppTools.subByte(sbt, (i + 1) * 192, length - s * 192);
                        mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_E), new byte[]{(byte) data.length}, data));
                        sb.append(bytes2HexString(mPSAMResponse.getResult()));
                        return hexString2Bytes(sb.toString().replace("null", ""));
                    }
                } else {
                    data = AppTools.subByte(sbt, i * 192, 192);
                    mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_M), data));
                    sb.append(bytes2HexString(mPSAMResponse.getResult()));
                }

                if (s == 1) {
                    data = AppTools.subByte(sbt, (i + 1) * 192, length - s * 192);
                    mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_E), new byte[]{(byte) data.length}, data));
                    sb.append(bytes2HexString(mPSAMResponse.getResult()));
                    return hexString2Bytes(sb.toString().replace("null", ""));
                }
            }
        } else {
            mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_LT), new byte[]{(byte) length}, sbt));
            return mPSAMResponse.getResult();
        }
        return null;
    }


    /**
     * 数据签名
     *
     * @param sbt
     * @return
     */
    public static byte[] RSASign(byte[] sbt) {
        try {
            connect();
            externalAuthenticate();
            if (sbt.length >= 256) {
                sbt = SHA256(sbt);
            }
            PSAMResponse mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_RSA), new byte[]{(byte) sbt.length}, sbt));
            disconnect();
            return mPSAMResponse.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 服务器返回的签名数据进行验签
     *
     * @param sbt
     * @return
     */
    public static boolean verifySign(byte[] sbt) {
        connect();
        sbt = SHA256(sbt);
        PSAMResponse mPSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_VERIFY_SIGN), sbt, new byte[]{(byte) 0}));
        disconnect();
        return mPSAMResponse.isSuccessful();
    }
}
