package com.csscaps.tcs.psam;

import com.aclas.aclasdemoforcr20.PCSCException;
import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.ConvertUtils;
import com.csscaps.common.utils.NumberBytesUtil;
import com.csscaps.tcs.ServerConstants;
import com.csscaps.tcs.model.ReceiveExternalAuthenticate;
import com.csscaps.tcs.model.RequestExternalAuthenticate;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;
import com.tax.fcr.library.utils.Logger;

import static com.aclas.aclasdemoforcr20.PCSC.SCardConnect;
import static com.aclas.aclasdemoforcr20.PCSC.SCardDisconnect;
import static com.aclas.aclasdemoforcr20.PCSC.SCardEstablishContext;
import static com.aclas.aclasdemoforcr20.PCSC.SCardGetStatusChange;
import static com.aclas.aclasdemoforcr20.PCSC.SCardIsValidContext;
import static com.aclas.aclasdemoforcr20.PCSC.SCardListReaders;
import static com.aclas.aclasdemoforcr20.PCSC.SCardReconnect;
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
            Logger.i("devicesn " + devicesn);
            isOpen3F01 = open3F01();
            externalAuthenticate();
        } catch (PCSCException e) {
            e.printStackTrace();
        }
    }


    /**
     * 断开psam卡连接
     */
    public static void disconnect() {
        try {
            SCardReconnect(cardId, SCARD_SHARE_SHARED, SCARD_PROTOCOL_T0 | SCARD_PROTOCOL_T1, SCARD_UNPOWER_CARD);
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
    private static PSAMResponse command(byte[] command) {
        Logger.i("command " + bytes2HexString(command));
        PSAMResponse PSAMResponse = PSAMResponse = new PSAMResponse();
        try {
            byte[] result = SCardTransmit(cardId, protocol, command, 0, command.length);
            Logger.i("PSAMResponse" + bytes2HexString(result));
            if (result != null && result.length >= 2) {
                String resultCode = bytes2HexString(subByte(result, result.length - 2, 2));
                Logger.i("resultCode" + resultCode);
                if (resultCode.equals(SUCCESSFUL_CODE)) {
                    PSAMResponse.setSuccessful(true);
                    PSAMResponse.setResult(subByte(result, 0, result.length - 2));
                }
            }
        } catch (PCSCException e) {
            e.printStackTrace();
        }
        return PSAMResponse;
    }

    /**
     * 打开3F01目录
     *
     * @return
     */
    private static boolean open3F01() {
        PSAMResponse PSAMResponse = command(hexString2Bytes(PSAMCommand.COMMAND_3F01));
        return PSAMResponse.isSuccessful();
    }

    /**
     * 外部认证
     */
    public static void externalAuthenticate() {
        //获取8个字节的随机码
        PSAMResponse PSAMResponse = command(hexString2Bytes(PSAMCommand.COMMAND_GET_RANDOM_8));
        RequestExternalAuthenticate requestExternalAuthenticateModel = new RequestExternalAuthenticate();
        requestExternalAuthenticateModel.setFuncid(ServerConstants.ASKJ012);
        requestExternalAuthenticateModel.setPsam_sn(devicesn);
        requestExternalAuthenticateModel.setDevicesn(devicesn);
        requestExternalAuthenticateModel.setRandom_data(bytes2HexString(PSAMResponse.getResult()));
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(requestExternalAuthenticateModel.getFuncid());
        requestExternalAuthenticateModel.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(requestExternalAuthenticateModel));
        Api.post(new IPresenter() {
            @Override
            public void onSuccess(String requestPath, String objectString) {
                objectString = objectString.replace("\\", "");
                objectString = objectString.replace("\"{", "{");
                objectString = objectString.replace("}\"", "}");
                ReceiveExternalAuthenticate receiveExternalAuthenticate = JSON.parseObject(objectString, ReceiveExternalAuthenticate.class);
                String data = receiveExternalAuthenticate.getMessage().getData();
                PSAMResponse PSAMResponse = command(hexString2Bytes(data));
                if (PSAMResponse.isSuccessful()) {
                    isAuthenticate = true;
                } else isAuthenticate = false;
            }

            @Override
            public void onFailure(String requestPath, String errorMes) {

            }
        }, requestModel);
    }

    /**
     * AES加密
     *
     * @param sbt
     * @return
     */
    public static byte[] encryptAES(byte[] sbt) {
        PSAMResponse PSAMResponse = command(hexString2Bytes(PSAMCommand.COMMAND_DECLARE_AES_ENCRYPT));
        if (PSAMResponse.isSuccessful()) {
            if (sbt.length > 192) {
                int s = sbt.length / 192;
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < s; i++) {
                    byte[] data;
                    if (i == s - 1) {
                        if (s * 192 == sbt.length) {
                            data = AppTools.subByte(sbt, i * 192, 192);
                            PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                            sb.append(bytes2HexString(PSAMResponse.getResult()));
                        } else {
                            data = AppTools.subByte(sbt, i * 192, 192);
                            PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_NON_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                            sb.append(bytes2HexString(PSAMResponse.getResult()));
                            data = AppTools.subByte(sbt, (i + 1) * 192, sbt.length - s * 192);
                            PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                            sb.append(bytes2HexString(PSAMResponse.getResult()));
                        }
                    } else {
                        data = AppTools.subByte(sbt, i * 192, 192);
                        PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_NON_FINAL_AES_ENCRYPT_DECODE), fill80n080(data)));
                        sb.append(bytes2HexString(PSAMResponse.getResult()));
                    }
                }
                return hexString2Bytes(sb.toString());
            } else {
                PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), fill80n080(sbt)));
                return PSAMResponse.getResult();

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
     * @param sbt
     * @return
     */
    public static byte[] decodeAES(byte[] sbt) {
        PSAMResponse PSAMResponse = command(hexString2Bytes(PSAMCommand.COMMAND_DECLARE_AES_DECODE));
        if (PSAMResponse.isSuccessful()) {
            int s = sbt.length / 208;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < s; i++) {
                byte[] data = AppTools.subByte(sbt, i * 208, 208);
                if (i == s - 1) {
                    PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_FINAL_AES_ENCRYPT_DECODE), data));
                    String resultStr = ConvertUtils.bytes2HexString(PSAMResponse.getResult());
                    resultStr = wipeOffPaddingBytes(resultStr);
                    byte[] string = ConvertUtils.hexString2Bytes(sb.toString() + resultStr);
                    return string;
                } else {
                    PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_NON_FINAL_AES_ENCRYPT_DECODE), data));
                    String resultStr = ConvertUtils.bytes2HexString(PSAMResponse.getResult());
                    resultStr = wipeOffPaddingBytes(resultStr);
                    sb.append(resultStr);
                }
            }
        }
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
        PSAMResponse PSAMResponse = new PSAMResponse();
        int length = sbt.length;
        if (length >= 256) {
            int s = length / 192;
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < s; i++) {
                byte[] data;
                if (i == 0) {
                    data = AppTools.subByte(sbt, i * 192, 192);
                    PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_S), data));
                    sb.append(bytes2HexString(PSAMResponse.getResult()));
                } else if (i == s - 1) {
                    if (s * 192 == length) {
                        data = AppTools.subByte(sbt, i * 192, 192);
                        PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_E), new byte[]{(byte) data.length}, data));
                        sb.append(bytes2HexString(PSAMResponse.getResult()));
                        return hexString2Bytes(sb.toString().replace("null", ""));
                    } else {
                        data = AppTools.subByte(sbt, i * 192, 192);
                        PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_M), data));
                        sb.append(bytes2HexString(PSAMResponse.getResult()));
                        data = AppTools.subByte(sbt, (i + 1) * 192, length - s * 192);
                        PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_E), new byte[]{(byte) data.length}, data));
                        sb.append(bytes2HexString(PSAMResponse.getResult()));
                        return hexString2Bytes(sb.toString().replace("null", ""));
                    }
                } else {
                    data = AppTools.subByte(sbt, i * 192, 192);
                    PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_M), data));
                    sb.append(bytes2HexString(PSAMResponse.getResult()));
                }

                if (s == 1) {
                    data = AppTools.subByte(sbt, (i + 1) * 192, length - s * 192);
                    PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_GT_E), new byte[]{(byte) data.length}, data));
                    sb.append(bytes2HexString(PSAMResponse.getResult()));
                    return hexString2Bytes(sb.toString().replace("null", ""));
                }
            }
        } else {
            PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_SHA256_LT), new byte[]{(byte) length}, sbt));
            return PSAMResponse.getResult();
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
        if (sbt.length >= 256) {
            sbt = SHA256(sbt);
        }
        PSAMResponse PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_RSA), new byte[]{(byte) sbt.length}, sbt));
        return PSAMResponse.getResult();
    }


    /**
     * 服务器返回的签名数据进行验签
     *
     * @param sbt
     * @return
     */
    public static boolean verifySign(byte[] sbt) {
        sbt = SHA256(sbt);
        PSAMResponse PSAMResponse = command(AppTools.byteMerger(hexString2Bytes(PSAMCommand.COMMAND_VERIFY_SIGN), sbt, new byte[]{(byte) 0}));
        return PSAMResponse.isSuccessful();
    }
}
