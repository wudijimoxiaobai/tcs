package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/6/21.
 */

public class RequestResultModel {

    /**
     * invoice_no : 1700212257
     * invoice_type_code : 26060
     * code : 0
     * invalid : N
     * work_status : PEN
     * status : AVL
     * reason :
     * cancellation_date :
     */

    private String invoice_no;
    private String invoice_type_code;
    private String code;
    private String invalid;
    private String work_status;
    private String status;
    private String reason;
    private String cancellation_date;

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getInvoice_type_code() {
        return invoice_type_code;
    }

    public void setInvoice_type_code(String invoice_type_code) {
        this.invoice_type_code = invoice_type_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInvalid() {
        return invalid;
    }

    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }

    public String getWork_status() {
        return work_status;
    }

    public void setWork_status(String work_status) {
        this.work_status = work_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCancellation_date() {
        return cancellation_date;
    }

    public void setCancellation_date(String cancellation_date) {
        this.cancellation_date = cancellation_date;
    }
}
