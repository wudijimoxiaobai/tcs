package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/11/13.
 */

public class Request3FKey extends RequestData{

    private String funcid;
    private String devicesn;

    @Override
    public String getFuncid() {
        return funcid;
    }

    @Override
    public void setFuncid(String funcid) {
        this.funcid = funcid;
    }

    @Override
    public String getDevicesn() {
        return devicesn;
    }

    @Override
    public void setDevicesn(String devicesn) {
        this.devicesn = devicesn;
    }
}
