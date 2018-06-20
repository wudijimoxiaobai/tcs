package com.tax.fcr.library.network;

/**
 * Created by tl on 2018/6/20.
 */

public interface IPresenter1 {
    /* 成功*/
    void onSuccess(String requestPath, String objectString, Object o);

    /*失败*/
    void onFailure(String requestPath, String errorMes, Object o);
}
