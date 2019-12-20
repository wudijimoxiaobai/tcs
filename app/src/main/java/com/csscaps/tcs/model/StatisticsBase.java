package com.csscaps.tcs.model;

public class StatisticsBase {

    String no;
    String period;
    String normal_amount;
    String qty;
    String cancellation_amount;
    String Qty2;
    String negative_amount;
    String qty3;

    @Override
    public String toString() {
        return "StatisticsBase{" +
                "no='" + no + '\'' +
                ", period='" + period + '\'' +
                ", normal_amount='" + normal_amount + '\'' +
                ", qty='" + qty + '\'' +
                ", cancellation_amount='" + cancellation_amount + '\'' +
                ", Qty2='" + Qty2 + '\'' +
                ", negative_amount='" + negative_amount + '\'' +
                ", qty3='" + qty3 + '\'' +
                '}';
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNormal_amount() {
        return normal_amount;
    }

    public void setNormal_amount(String normal_amount) {
        this.normal_amount = normal_amount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCancellation_amount() {
        return cancellation_amount;
    }

    public void setCancellation_amount(String cancellation_amount) {
        this.cancellation_amount = cancellation_amount;
    }

    public String getQty2() {
        return Qty2;
    }

    public void setQty2(String qty2) {
        Qty2 = qty2;
    }

    public String getNegative_amount() {
        return negative_amount;
    }

    public void setNegative_amount(String negative_amount) {
        this.negative_amount = negative_amount;
    }

    public String getQty3() {
        return qty3;
    }

    public void setQty3(String qty3) {
        this.qty3 = qty3;
    }
}
