package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/6/25.
 */

public class SearchInvoiceCondition {

    private String invoiceCode;
    private String invoiceNo;
    private String issuedBy;
    private String uploadStatus;
    private String status;
    private String issuingDateFrom;
    private String issuingDateTo;

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssuingDateFrom() {
        return issuingDateFrom;
    }

    public void setIssuingDateFrom(String issuingDateFrom) {
        this.issuingDateFrom = issuingDateFrom;
    }

    public String getIssuingDateTo() {
        return issuingDateTo;
    }

    public void setIssuingDateTo(String issuingDateTo) {
        this.issuingDateTo = issuingDateTo;
    }
}
