package com.csscaps.tcs.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by tl on 2018/7/18.
 */
@XStreamAlias("QUA")
public class QUA {
    private String QTY;
    private String UNITPRICE;
    private String AMOUNT;

    public QUA(String QTY, String UNITPRICE, String AMOUNT) {
        this.QTY = QTY;
        this.UNITPRICE = UNITPRICE;
        this.AMOUNT = AMOUNT;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getUNITPRICE() {
        return UNITPRICE;
    }

    public void setUNITPRICE(String UNITPRICE) {
        this.UNITPRICE = UNITPRICE;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }
}
