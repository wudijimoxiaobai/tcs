package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/5/9.
 */

public class RequestData {

    /**
     * funcid : ATCS002
     * sellerid : xxx
     * devicesn : xxxxx
     * last_update_time : xxx
     * systime : xxxxx
     * checkcode : xxxxx
     */

    private String funcid;
    private String sellerid="300000000540";
    private String devicesn;
    private String last_update_time;
    private String systime;
    private String checkcode;

    public String getFuncid() {
        return funcid;
    }

    public void setFuncid(String funcid) {
        this.funcid = funcid;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getDevicesn() {
        return devicesn;
    }

    public void setDevicesn(String devicesn) {
        this.devicesn = devicesn;
    }

    public String getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(String last_update_time) {
        this.last_update_time = last_update_time;
    }

    public String getSystime() {
        return systime;
    }

    public void setSystime(String systime) {
        this.systime = systime;
    }

    public String getCheckcode() {
        return checkcode;
    }

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }
}

