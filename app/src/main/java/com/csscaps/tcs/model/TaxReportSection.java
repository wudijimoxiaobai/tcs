package com.csscaps.tcs.model;

public class TaxReportSection {

    private String No;
    private String Period;
    private String TA;
    private String VAT;
    private String BPT_F;
    private String BPT_P;
    private String SD_F;
    private String SD_L;
    private String Fee;

    public TaxReportSection() {
    }

    @Override
    public String toString() {
        return "TaxReportSection{" +
                "No='" + No + '\'' +
                ", Period='" + Period + '\'' +
                ", TA='" + TA + '\'' +
                ", VAT='" + VAT + '\'' +
                ", BPT_F='" + BPT_F + '\'' +
                ", BPT_P='" + BPT_P + '\'' +
                ", SD_F='" + SD_F + '\'' +
                ", SD_L='" + SD_L + '\'' +
                ", Fee='" + Fee + '\'' +
                '}';
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getTA() {
        return TA;
    }

    public void setTA(String TA) {
        this.TA = TA;
    }

    public String getVAT() {
        return VAT;
    }

    public void setVAT(String VAT) {
        this.VAT = VAT;
    }

    public String getBPT_F() {
        return BPT_F;
    }

    public void setBPT_F(String BPT_F) {
        this.BPT_F = BPT_F;
    }

    public String getBPT_P() {
        return BPT_P;
    }

    public void setBPT_P(String BPT_P) {
        this.BPT_P = BPT_P;
    }

    public String getSD_F() {
        return SD_F;
    }

    public void setSD_F(String SD_F) {
        this.SD_F = SD_F;
    }

    public String getSD_L() {
        return SD_L;
    }

    public void setSD_L(String SD_L) {
        this.SD_L = SD_L;
    }

    public String getFee() {
        return Fee;
    }

    public void setFee(String fee) {
        Fee = fee;
    }
}
