package com.tax.fcr.library.network;


import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Total API
 * AUTHOR: TL
 * DATE: 21/10/2015 18:44
 */
public interface DefaultService {
    /**
     * 通用接口调用 post
     *
     * @return
     */
//    @POST("tcs.web/bizWndUrl")
    @POST("tcs.front/bizWndUrl")
    Observable<ResponseModel> postService(@Body() RequestModel requestParam);


    @POST("{path}")
    Observable<ResponseModel> postService(@Path("path") String path, @Body() RequestModel requestParam);

    /**
     * 通用接口调用 get
     *
     * @return
     */
    @GET
    Observable<ResponseModel> getService(@Url String requestPath);


    /**
     * 通用接口调用 upload
     *
     * @return
     */
    @Multipart
    @PUT
    Observable<ResponseModel> uploadService(@Url String requestPath, @Part MultipartBody.Part file);


}


