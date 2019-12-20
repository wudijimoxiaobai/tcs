package com.csscaps.tcs.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("QD")
public class QD {

    private String ITEMNAME;
    private String DESCRIPTION;
    private String UNIT;
    private String QTY;
    private String UNITPRICE;
    private String AMOUNT;
    private String VAT;

    @XStreamAlias("ITEMNAME")

    private ItemName mItemName;


    public QD() {
    }



    public QD(String iTEMNAME, String dESCRIPTION, String uNIT, String qTY, String uNITPRICE, String aMOUNT, String vAT) {
        super();
        ITEMNAME = iTEMNAME;
        DESCRIPTION = dESCRIPTION;
        UNIT = uNIT;
        QTY = qTY;
        UNITPRICE = uNITPRICE;
        AMOUNT = aMOUNT;
        VAT = vAT;
    }

    public String getITEMNAME() {
        return ITEMNAME;
    }

    public void setITEMNAME(String iTEMNAME) {
        ITEMNAME = iTEMNAME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String dESCRIPTION) {
        DESCRIPTION = dESCRIPTION;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String uNIT) {
        UNIT = uNIT;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String qTY) {
        QTY = qTY;
    }

    public String getUNITPRICE() {
        return UNITPRICE;
    }

    public void setUNITPRICE(String uNITPRICE) {
        UNITPRICE = uNITPRICE;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String aMOUNT) {
        AMOUNT = aMOUNT;
    }

    public String getVAT() {
        return VAT;
    }

    public void setVAT(String vAT) {
        VAT = vAT;
    }

    public ItemName getItemName() {
        return mItemName;
    }

    public void setItemName(ItemName itemName) {
        mItemName = itemName;
    }

}
