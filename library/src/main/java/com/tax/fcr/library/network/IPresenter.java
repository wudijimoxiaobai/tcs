package com.tax.fcr.library.network;

import android.content.Context;

/**
 * Created by tanglei on 16/6/16.
 */
public interface IPresenter {

    /* 成功*/
    void onSuccess(String requestPath, String objectString);

    /*失败*/
    void onFailure(String requestPath, String errorMes);

    Context getContext();

    boolean isDetached();
}
