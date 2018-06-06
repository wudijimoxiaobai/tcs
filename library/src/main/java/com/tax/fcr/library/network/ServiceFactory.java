package com.tax.fcr.library.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by thinkpad on 2016/9/6.
 */
public class ServiceFactory {
    private final Retrofit retrofit;
    private DefaultService defaultService;
    private DownloadService downloadService;
    private static  ServiceFactory INSTANCE;

    private ServiceFactory() {
        retrofit = new Retrofit.Builder()
                .client(createOkHttpClient())
                .baseUrl(Api.getBaseUrl())
                .addConverterFactory(SecurityConverterFactory.create()) // this line is for encrypt request/response
//                .addConverterFactory(GsonConverterFactory.create()) // This line is for non-encrypt request/response
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    public static void setINSTANCE(ServiceFactory INSTANCE) {
        ServiceFactory.INSTANCE = INSTANCE;
    }

    public static ServiceFactory getInstance() {
        if(INSTANCE!=null) return INSTANCE;
        return INSTANCE=new ServiceFactory();
    }

    public DefaultService getDefaultService() {
        if (defaultService == null) defaultService = retrofit.create(DefaultService.class);
        return defaultService;
    }

    public DownloadService getDownloadService() {
        if (downloadService == null) downloadService = retrofit.create(DownloadService.class);
        return downloadService;
    }

    private final static long DEFAULT_TIMEOUT = 10;

    private OkHttpClient createOkHttpClient() {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        httpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        httpClientBuilder.hostnameVerifier(HttpsUtils.getHostnameVerifier());
        //设置缓存
        // File httpCacheDirectory = new File(FileUtils.getCacheDir(SolidApplication.getInstance()), "OkHttpCache");
        // httpClientBuilder.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));

        return httpClientBuilder.build();
    }
}

