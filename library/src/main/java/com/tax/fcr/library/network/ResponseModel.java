package com.tax.fcr.library.network;

/**
 * Created by tanglei on 16/6/29.
 * 网络数据返回实体类
 */
public class ResponseModel {

    private String funcid;
    private String devicesn;
    private String code;
    private String message;
    private String data;
    private String checkcode;

    public String getFuncid() {
        return funcid;
    }

    public void setFuncid(String funcid) {
        this.funcid = funcid;
    }

    public String getDevicesn() {
        return devicesn;
    }

    public void setDevicesn(String devicesn) {
        this.devicesn = devicesn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCheckcode() {
        return checkcode;
    }

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }
}
