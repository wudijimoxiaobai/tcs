package com.tax.fcr.library.network;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.tax.fcr.library.utils.SecurityUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by thinkpad on 2016/9/2.
 */
public class SecurityRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");


    @Override
    public RequestBody convert(T value) throws IOException {
        if (value instanceof File) {
            return RequestBody.create(MediaType.parse("image.jpeg"), (File) value);
        } else {
            RequestModel requestModel = (RequestModel) value;
            String data = requestModel.getData();
            if (!TextUtils.isEmpty(data)) requestModel.setData(SecurityUtil.getBase64(data));
            String postBody = JSON.toJSONString(requestModel);
            return RequestBody.create(MEDIA_TYPE, postBody);
        }
    }
}