package com.tax.fcr.library.network;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.tax.fcr.library.utils.SecurityUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by thinkpad on 2016/9/2.
 */
public class SecurityResponseBodyConverter implements Converter<ResponseBody, ResponseModel> {

    public static Class mClass;

    @Override
    public ResponseModel convert(ResponseBody value) throws IOException {
        String originalData = value.string();
        ResponseModel responseModel = JSON.parseObject(originalData, ResponseModel.class);
        String data = responseModel.getData();
        if (!TextUtils.isEmpty(data)) {
           /*    //AES 解密
         try {
                byte b[] = SecurityUtil.base64Decode(data);
                Method method = mClass.getMethod("decodeAES", byte[].class);
                b = (byte[]) method.invoke(mClass, b);
                data = new String(b, StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            data = SecurityUtil.getFromBase64(data);
        }
        responseModel.setData(data);
        return responseModel;
    }
}