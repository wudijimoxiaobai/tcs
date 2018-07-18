package com.csscaps.tcs.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("Root")
public class InvoiceData {

    @XStreamAlias("Head")
    private Head head;

    @XStreamAlias("SELLER")
    private Seller seller;

    @XStreamAlias("BUYER")
    private Buyer buyer;

    @XStreamAlias("TotalofStampDuty-Local")
    private String TotalofStampDuty_Local;

    @XStreamAlias("TotalofStamDuty-Federal")
    private String TotalofStamDuty_Federal;

    private String Unit="SDG";
    private String TotalofVAT;
    private String TotalofBPTFinal;
    private String TotalofBPTPrepayment;
    private String TotalofFees;
    private String TotalAmountExcl;
    private String TotalAmountExclIncl;
    private String Remark;
    private String Issuedby;

    @XStreamImplicit
    private List<QD> QDs;
    private String RWM;
    private String title;

    //*************************
    @XStreamAlias("QD")
    private NQD QD;
    @XStreamAlias("QD1")
    private QD1 QD1;

    public InvoiceData() {
    }


    public InvoiceData(String unit, String totalofVAT, String totalAmountExcl, String totalAmountExclIncl, String remark, String issuedby, String rWM) {
        super();
        Unit = unit;
        TotalofVAT = totalofVAT;
        TotalAmountExcl = totalAmountExcl;
        TotalAmountExclIncl = totalAmountExclIncl;
        Remark = remark;
        Issuedby = issuedby;
        RWM = rWM;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getTotalofVAT() {
        return TotalofVAT;
    }

    public void setTotalofVAT(String totalofVAT) {
        TotalofVAT = totalofVAT;
    }

    public String getTotalAmountExcl() {
        return TotalAmountExcl;
    }

    public void setTotalAmountExcl(String totalAmountExcl) {
        TotalAmountExcl = totalAmountExcl;
    }

    public String getTotalAmountExclIncl() {
        return TotalAmountExclIncl;
    }

    public void setTotalAmountExclIncl(String totalAmountExclIncl) {
        TotalAmountExclIncl = totalAmountExclIncl;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getIssuedby() {
        return Issuedby;
    }

    public void setIssuedby(String issuedby) {
        Issuedby = issuedby;
    }

    public List<QD> getQDs() {
        return QDs;
    }

    public void setQDs(List<QD> qDs) {
        QDs = qDs;
    }

    public String getRWM() {
        return RWM;
    }

    public void setRWM(String rWM) {
        RWM = rWM;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalofBPTFinal() {
        return TotalofBPTFinal;
    }

    public void setTotalofBPTFinal(String totalofBPTFinal) {
        TotalofBPTFinal = totalofBPTFinal;
    }

    public String getTotalofBPTPrepayment() {
        return TotalofBPTPrepayment;
    }

    public void setTotalofBPTPrepayment(String totalofBPTPrepayment) {
        TotalofBPTPrepayment = totalofBPTPrepayment;
    }

    public String getTotalofStampDuty_Local() {
        return TotalofStampDuty_Local;
    }

    public void setTotalofStampDuty_Local(String totalofStampDuty_Local) {
        TotalofStampDuty_Local = totalofStampDuty_Local;
    }

    public String getTotalofStamDuty_Federal() {
        return TotalofStamDuty_Federal;
    }

    public void setTotalofStamDuty_Federal(String totalofStamDuty_Federal) {
        TotalofStamDuty_Federal = totalofStamDuty_Federal;
    }

    public String getTotalofFees() {
        return TotalofFees;
    }

    public void setTotalofFees(String totalofFees) {
        TotalofFees = totalofFees;
    }

    public NQD getQD() {
        return QD;
    }

    public void setQD(NQD QD) {
        this.QD = QD;
    }

    public com.csscaps.tcs.model.QD1 getQD1() {
        return QD1;
    }

    public void setQD1(com.csscaps.tcs.model.QD1 QD1) {
        this.QD1 = QD1;
    }
}
