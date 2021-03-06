package com.csscaps.tcs.model;

public class PeriodReportSection {

    private String No;
    private String Period;
    private String Normal_Amount;
    private String Qty;
    private String Cancellation_Amount;
    private String Qty2;
    private String Negative_Amount;
    private String Qty3;

    @Override
    public String toString() {
        return "PeriodReportSection{" +
                "No='" + No + '\'' +
                ", Period='" + Period + '\'' +
                ", Normal_Amount='" + Normal_Amount + '\'' +
                ", Qty='" + Qty + '\'' +
                ", Cancellation_Amount='" + Cancellation_Amount + '\'' +
                ", Qty2='" + Qty2 + '\'' +
                ", Negative_Amount='" + Negative_Amount + '\'' +
                ", Qty3='" + Qty3 + '\'' +
                '}';
    }

    public PeriodReportSection() {
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

    public String getNormal_Amount() {
        return Normal_Amount;
    }

    public void setNormal_Amount(String normal_Amount) {
        Normal_Amount = normal_Amount;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getCancellation_Amount() {
        return Cancellation_Amount;
    }

    public void setCancellation_Amount(String cancellation_Amount) {
        Cancellation_Amount = cancellation_Amount;
    }

    public String getQty2() {
        return Qty2;
    }

    public void setQty2(String qty2) {
        Qty2 = qty2;
    }

    public String getNegative_Amount() {
        return Negative_Amount;
    }

    public void setNegative_Amount(String negative_Amount) {
        Negative_Amount = negative_Amount;
    }

    public String getQty3() {
        return Qty3;
    }

    public void setQty3(String qty3) {
        Qty3 = qty3;
    }
}
