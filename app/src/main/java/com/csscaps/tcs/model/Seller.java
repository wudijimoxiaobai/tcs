package com.csscaps.tcs.model;

public class Seller {

    private String TIN;
    private String NAME;
    private String ADDRESS;
    private String PHONENO;
    private String BRANCH;

    public Seller() {
    }



    public Seller(String tIN, String nAME, String aDDRESS, String pHONENO, String bRANCH) {
        super();
        TIN = tIN;
        NAME = nAME;
        ADDRESS = aDDRESS;
        PHONENO = pHONENO;
        BRANCH = bRANCH;
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

    public String getBRANCH() {
        return BRANCH;
    }

    public void setBRANCH(String bRANCH) {
        BRANCH = bRANCH;
    }

}
