package com.csscaps.tcs;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.model.ReceiveServerTime;
import com.csscaps.tcs.model.RequestData;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;

import java.util.Date;

import aclasdriver.AclasBaseFunction;

/**
 * Created by tl on 2018/11/9.
 */

public class RTCUtil {

    private static AclasBaseFunction base = new AclasBaseFunction();

    public static Date getRTC() {
        Date date = new Date();
        int iRet=base.getRTCTime(date);
        if(iRet!=0) getRTC();
        return date;
    }

    public static void setRTCFormServer() {
        RequestData requestData = new RequestData();
        requestData.setFuncid(ServerConstants.A014);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(requestData.getFuncid());
        requestData.setDevicesn(requestModel.getDevicesn());
        requestModel.setData(JSON.toJSONString(requestData));
        Api.post(new IPresenter() {
            @Override
            public void onSuccess(String requestPath, String objectString) {
                ReceiveServerTime receiveServerTime = JSON.parseObject(objectString, ReceiveServerTime.class);
                String time = receiveServerTime.getTime();
                if (!TextUtils.isEmpty(time)) {
                    Date date = DateUtils.getStringToDate_YYYY_MM_DD_HH_MM_SS_EN_24(time);
                    base.setRTCTime(date);
                }
            }

            @Override
            public void onFailure(String requestPath, String errorMes) {

            }
        }, requestModel);

    }
}
