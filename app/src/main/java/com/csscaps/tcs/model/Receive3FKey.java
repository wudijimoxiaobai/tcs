package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/11/13.
 */

public class Receive3FKey extends ReceiveData {
    private String old_3fpwd_key;
    private String new_3fpwd_key;

    public String getOld_3fpwd_key() {
        return old_3fpwd_key;
    }

    public void setOld_3fpwd_key(String old_3fpwd_key) {
        this.old_3fpwd_key = old_3fpwd_key;
    }

    public String getNew_3fpwd_key() {
        return new_3fpwd_key;
    }

    public void setNew_3fpwd_key(String new_3fpwd_key) {
        this.new_3fpwd_key = new_3fpwd_key;
    }
}
