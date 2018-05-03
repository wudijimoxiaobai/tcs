package com.tax.fcr.library.network;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by thinkpad on 2016/9/2.
 */
public class SecurityConverterFactory extends Converter.Factory {

    public static Converter.Factory create() {
        return new SecurityConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ResponseModel> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new SecurityResponseBodyConverter();
    }

    @Override
    public Converter<RequestModel, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new SecurityRequestBodyConverter();
    }


}


