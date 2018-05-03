package com.tax.fcr.library.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Retrofit download API interface.
 *
 * Created by Mingliang on 2016/9/17 017.
 */
public interface DownloadService {
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String fileURL);
}
