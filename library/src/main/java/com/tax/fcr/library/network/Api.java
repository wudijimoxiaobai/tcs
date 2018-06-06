package com.tax.fcr.library.network;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tax.fcr.library.utils.NetworkUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * AUTHOR: TL
 * DATE: 21/10/2015 19:09
 */
public abstract class Api {
    private static String BASE_URL = "";
    public final static String ERR_NETWORK = "1";
    public final static String FAIL_CONNECT = "2";

    public static void setBaseUrl(@NonNull String url) {
        BASE_URL = url;
        ServiceFactory.setINSTANCE(null);
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }


    public static void post(final IPresenter presenter, RequestModel requestParam) {
        if (TextUtils.isEmpty(BASE_URL)) return;
        if (NetworkUtils.netWorkJudge()) {
            Observable<ResponseModel> observable = ServiceFactory.getInstance()
                    .getDefaultService().postService(requestParam);
            Log.i("Retrofit", "请求：POST:" + requestParam.getFuncid() + "\n Request:" + JSON.toJSONString(requestParam, true));
            subscribe(requestParam.getFuncid(), observable, presenter);
        } else {
            presenter.onFailure(requestParam.getFuncid(), ERR_NETWORK);
        }
    }

    public static void get(final IPresenter presenter, final String requestPath) {
        if (TextUtils.isEmpty(BASE_URL)) return;
        if (NetworkUtils.netWorkJudge()) {
            Observable<ResponseModel> observable = ServiceFactory.getInstance()
                    .getDefaultService().getService(requestPath);
            subscribe(requestPath, observable, presenter);
        } else {
            presenter.onFailure(requestPath, ERR_NETWORK);
        }
    }

    public static void upload(final IPresenter presenter, String requestPath, File file) {
        if (TextUtils.isEmpty(BASE_URL)) return;
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        if (NetworkUtils.netWorkJudge()) {
            Observable<ResponseModel> observable = ServiceFactory.getInstance()
                    .getDefaultService().uploadService(requestPath, body);
            subscribe(requestPath, observable, presenter);
        } else {
            presenter.onFailure(requestPath, ERR_NETWORK);
        }
    }


    private static void subscribe(final String requestPath, Observable<ResponseModel> observable, final IPresenter presenter) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onNext(ResponseModel s) {
                        Log.i("Retrofit", "返回：" + requestPath + " \nResponse:" + JSON.toJSONString(s, true));
                        if ("0".equals(s.getCode())) {
                            String data = s.getData();
                            presenter.onSuccess(requestPath, data);
                        } else {
                            presenter.onFailure(requestPath, s.getMessage());
                        }
                    }

                    @Override
                    public void onCompleted() {
                        if (!isUnsubscribed()) unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (!isUnsubscribed()) unsubscribe();

                        String errorCode;
                       /* if (e instanceof SocketTimeoutException)
                            errorCode = FAIL_CONNECT;
                        else*/
                        errorCode = ERR_NETWORK;

                        presenter.onFailure(requestPath, errorCode);
                    }
                });
    }


    public static void download(@NonNull String url, final @NonNull File file, @NonNull final OnDownloadCompletedListener listener) {
        if (TextUtils.isEmpty(BASE_URL)) return;
        ServiceFactory.getInstance()
                .getDownloadService().download(url)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                        try {
                            if (!file.exists()) {
                                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                                file.createNewFile();
                            }
                            new AsyncTask<Void, Integer, Void>() {
                                Exception exception;

                                @Override
                                protected void onPreExecute() {
                                    if (listener != null)
                                        listener.contentLength(response.body().contentLength());
                                }

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    ProgressUpdate progressUpdate = new ProgressUpdate() {
                                        @Override
                                        public void updateProgress(int progress) {
                                            publishProgress(progress);
                                        }
                                    };
                                    exception = writeToFile(response.body().byteStream(), file, progressUpdate);
                                    return null;
                                }

                                @Override
                                protected void onProgressUpdate(Integer... progress) {
                                    if (listener != null) listener.onProgressUpdate(progress[0]);
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    if (listener != null) {
                                        if (exception == null) {
                                            listener.complete(file);
                                        } else {
                                            listener.error(exception);
                                        }
                                    }
                                }
                            }.execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (listener != null) listener.error((Exception) t);
                    }
                });

    }


    private static Exception writeToFile(final InputStream inputStream, final File file, ProgressUpdate progressUpdate) {
        if (file.canWrite()) {
            FileOutputStream fos = null;
            try {
                byte[] buffer = new byte[1024];
                fos = new FileOutputStream(file);
                int readLen;
                while ((readLen = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, readLen);
                    progressUpdate.updateProgress(readLen);
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return e;
            } finally {
                try {
                    inputStream.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public interface OnDownloadCompletedListener {
        void complete(File file);

        void error(Exception e);

        void contentLength(long length);

        void onProgressUpdate(int progress);
    }

    private interface ProgressUpdate {
        void updateProgress(int progress);
    }
}