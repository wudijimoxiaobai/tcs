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


    @Override
    public ResponseModel convert(ResponseBody value) throws IOException {
        String originalData = value.string();
        ResponseModel responseModel = JSON.parseObject(originalData, ResponseModel.class);
        String data = responseModel.getData();
        if (!TextUtils.isEmpty(data)) data = SecurityUtil.getFromBase64(responseModel.getData());
        responseModel.setData(data);
        return responseModel;
    }
}