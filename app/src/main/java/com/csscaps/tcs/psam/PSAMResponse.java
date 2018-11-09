package com.csscaps.tcs.psam;

/**
 * Created by tl on 2018/11/1.
 * psam卡指令返回结果
 */

public class PSAMResponse {
    private byte[] result;
    private boolean isSuccessful;

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}

