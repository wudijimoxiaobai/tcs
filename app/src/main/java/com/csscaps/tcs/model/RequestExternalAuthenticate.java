package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/11/5.
 */

public class RequestExternalAuthenticate extends RequestData{
    private String psam_sn;
    private String random_data;

    public String getPsam_sn() {
        return psam_sn;
    }

    public void setPsam_sn(String psam_sn) {
        this.psam_sn = psam_sn;
    }

    public String getRandom_data() {
        return random_data;
    }

    public void setRandom_data(String random_data) {
        this.random_data = random_data;
    }
}
