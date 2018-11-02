package com.csscaps.tcs.psam;

import com.aclas.aclasdemoforcr20.PCSCException;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.ConvertUtils;
import com.csscaps.common.utils.NumberBytesUtil;
import com.tax.fcr.library.utils.Logger;

import java.nio.charset.StandardCharsets;

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

    /**
     * 连接psam
     */
    public static void connect() {
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
            byte[] bytes = subByte(pBAtr, 17, 5);
            byte[] b = new byte[5];
            for (int i = 0; i < 5; i++) {
                b[i] = bytes[4 - i];
            }
            devicesn = String.valueOf(NumberBytesUtil.bytesToLong(b));
            Logger.i("devicesn "+devicesn);
            isOpen3F01 = open3F01();
        } catch (PCSCException e) {
            e.printStackTrace();
        }
    }


    /**
     * 断开psam卡连接
     */
    public static void disconnect() {
        try {
            SCardDisconnect(cardId, SCARD_UNPOWER_CARD);
            SCardReleaseContext(contextId);
        } catch (PCSCException e) {
            e.printStackTrace();
        }
    }


    /**
     * 执行命令
     *
     * @param command 命令
     * @return
     */
    private static Response command(byte[] command) {
        Response response = response = new Response();
        try {
            byte[] result = SCardTransmit(cardId, protocol, command, 0, command.length);
            Logger.i("response" + bytes2HexString(result));
            if (result != null && result.length >= 2) {
                String resultCode = bytes2HexString(subByte(result, result.length - 2, 2));
                Logger.i("resultCode" + resultCode);
                if (resultCode.equals(SUCCESSFUL_CODE)) {
                    response.setSuccessful(true);
                    response.setResult(subByte(result, 0, result.length - 2));
                }
            }
        } catch (PCSCException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 打开3F01目录
     *
     * @return
     */
    private static boolean open3F01() {
        Response response = command(hexString2Bytes(PSAMCommand.COMMAND_3F01));
        return response.isSuccessful();
    }

    /**
     * AES加密
     *
     * @param str
     * @return
     */
    public static String encryptAES(String str) {
        Response response = command(hexString2Bytes(PSAMCommand.COMMAND_DECLARE_AES_ENCRYPT));
        if (response.isSuccessful()) {
            byte[] sbt = str.getBytes(StandardCharsets.UTF_8);
            if (sbt.length > 192) {
                int s = sbt.length / 192;
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < s; i++) {
                    byte[] data;
                    if (i == s - 1) {
                        if (s * 192 == sbt.length) {
                            data = AppTools.subByte(sbt, i * 192, 192);
                            response = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                            sb.append(bytes2HexString(response.getResult()));
                        } else {
                            data = AppTools.subByte(sbt, i * 192, 192);
                            response = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_NON_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                            sb.append(bytes2HexString(response.getResult()));
                            data = AppTools.subByte(sbt, i * 192, sbt.length - s * 192);
                            response = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                            sb.append(bytes2HexString(response.getResult()));
                        }
                    } else {
                        data = AppTools.subByte(sbt, i * 192, 192);
                        response = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_NON_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                        sb.append(bytes2HexString(response.getResult()));
                    }
                }
                return sb.toString();
            } else {
                response = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), fill80n080(sbt)));
                return bytes2HexString(response.getResult());

            }
        }
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
     * @param str
     * @return
     */
    public static String decodeAES(String str) {
        Response response = command(hexString2Bytes(PSAMCommand.COMMAND_DECLARE_AES_DECODE));
        if (response.isSuccessful()) {
            byte[] sbt = str.getBytes();
            if (sbt.length > 208) {
                int s = sbt.length / 208;
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < s; i++) {
                    byte[] data;
                    if (i == s - 1) {
                        data = AppTools.subByte(sbt, i * 208, 208);
                        response = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), data));
                        String resultStr = ConvertUtils.bytes2HexString(response.getResult());
                        resultStr = wipeOffPaddingBytes(resultStr);
                        byte []string= ConvertUtils.hexString2Bytes(sb.toString() + resultStr);
                        return new String(string,StandardCharsets.UTF_8);
                    } else {
                        data = AppTools.subByte(sbt, i * 208, 208);
                        response = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_NON_FINAL_AES_ENCRYPT_DECODE), data));
                        String resultStr = ConvertUtils.bytes2HexString(response.getResult());
                        resultStr = wipeOffPaddingBytes(resultStr);
                        sb.append(resultStr);
                    }
                }

            } else {
                response = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), sbt));
                String resultStr = ConvertUtils.bytes2HexString(response.getResult());
                return wipeOffPaddingBytes(resultStr);
            }
        }
        return null;
    }

    /**
     * 去除补码 80...00...80
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
}
