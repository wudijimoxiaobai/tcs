package com.tax.fcr.library.network;

import com.tax.fcr.library.utils.EncryptUtils;
import com.tax.fcr.library.utils.SecurityUtil;

/**
 * Created by tl on 2017/12/25.
 */

public class RequestModel {


    private String funcid;
    private String devicesn="80249001000048";
    private String device_type="TCS";
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

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
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

    public String createCheckcode(){
         StringBuffer sb=new StringBuffer();
         sb.append(funcid).append(devicesn).append(device_type).append(SecurityUtil.getBase64(data));
         return EncryptUtils.encryptSHA256ToString(sb.toString());
    }
}
