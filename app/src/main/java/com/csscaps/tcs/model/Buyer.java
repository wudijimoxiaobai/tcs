package com.csscaps.tcs.model;

public class Buyer {

    private String TIN;
    private String NAME;
    private String ADDRESS;
    private String PHONENO;
    private String NATIONALID;

    public Buyer() {
    }


    public Buyer(String tIN, String nAME, String aDDRESS, String pHONENO, String nATIONALID) {
        super();
        TIN = tIN;
        NAME = nAME;
        ADDRESS = aDDRESS;
        PHONENO = pHONENO;
        NATIONALID = nATIONALID;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String tIN) {
        TIN = tIN;
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

    public void setNATIONALID(String nATIONALID) {
        NATIONALID = nATIONALID;
    }

}
