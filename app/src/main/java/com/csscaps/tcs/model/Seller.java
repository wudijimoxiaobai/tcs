package com.csscaps.tcs.model;

public class Seller {

    private String TIN;
    private String NAME;
    private String Cashier;
    private String Change;
    private String ADDRESSONE;
    private String ADDRESSTWO;
    private String PHONENO;
    private String BRANCH;

    public String getADDRESSONE() {
        return ADDRESSONE;
    }

    public void setADDRESSONE(String ADDRESSONE) {
        this.ADDRESSONE = ADDRESSONE;
    }

    public String getADDRESSTWO() {
        return ADDRESSTWO;
    }

    public void setADDRESSTWO(String ADDRESSTWO) {
        this.ADDRESSTWO = ADDRESSTWO;
    }

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    public Seller() {
    }



    public Seller(String tIN, String nAME, String aDDRESS, String pHONENO, String bRANCH) {
        super();
        TIN = tIN;
        NAME = nAME;
        ADDRESSONE = aDDRESS;
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
        return ADDRESSONE;
    }

    public void setADDRESS(String aDDRESS) {
        ADDRESSONE = aDDRESS;
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

//    public void setBRANCH(String bRANCH) {
//        BRANCH = bRANCH;
//    }

    public String getCashier() {
        return Cashier;
    }

    public void setCashier(String cashier) {
        Cashier = cashier;
    }

}
