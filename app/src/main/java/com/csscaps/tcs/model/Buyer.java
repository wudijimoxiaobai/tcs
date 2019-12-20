package com.csscaps.tcs.model;

public class Buyer {

    private String BUYERTIN;
    private String NAME;
    private String ADDRESS;
    private String PHONENO;
    private String NATIONALID;
    private String Money;

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }
    public Buyer() {
    }


    public Buyer(String tIN, String nAME, String aDDRESS, String pHONENO, String nATIONALID) {
        super();
        BUYERTIN = tIN;
        NAME = nAME;
        ADDRESS = aDDRESS;
        PHONENO = pHONENO;
        NATIONALID = nATIONALID;
    }

    public String getTIN() {
        return BUYERTIN;
    }

    public void setTIN(String tIN) {
        BUYERTIN = tIN;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String nAME) {
        NAME = nAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String aDDRESS) {
        ADDRESS = aDDRESS;
    }

    public String getPHONENO() {
        return PHONENO;
    }

    public void setPHONENO(String pHONENO) {
        PHONENO = pHONENO;
    }

    public String getNATIONALID() {
        return NATIONALID;
    }

   // public void setNATIONALID(String nATIONALID) {
   //     NATIONALID = nATIONALID;
    //}

}
