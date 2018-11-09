package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/11/6.
 */

public class ReceiveExternalAuthenticate extends ReceiveData {
    private Message message;
    private String psam_sn;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getPsam_sn() {
        return psam_sn;
    }

    public void setPsam_sn(String psam_sn) {
        this.psam_sn = psam_sn;
    }


}

