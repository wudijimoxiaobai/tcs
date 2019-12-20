package com.csscaps.tcs.model;

public class CancelNegativeNodel {

   private String No;
   private String type;
   private String amout;
   private String qty;
   private String percentage;

    public CancelNegativeNodel() {
    }

    @Override
    public String toString() {
        return "CancelNegativeNodel{" +
                "No='" + No + '\'' +
                ", type='" + type + '\'' +
                ", amout='" + amout + '\'' +
                ", qty='" + qty + '\'' +
                ", percentage='" + percentage + '\'' +
                '}';
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmout() {
        return amout;
    }

    public void setAmout(String amout) {
        this.amout = amout;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
