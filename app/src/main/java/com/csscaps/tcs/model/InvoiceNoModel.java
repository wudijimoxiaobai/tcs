package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/5/22.
 */

public class InvoiceNoModel {
    /**
     * invoice_type_code : xxxxx
     * control_method : xxxxx
     * current_or_next_period : xxxxx
     * invoice_header : xxxxx
     * invoice_num_start : xxxxx
     * invoice_num_end : xxxxx
     * segment_cipher : xxx
     */

    private String invoice_type_code;
    private String control_method;
    private String current_or_next_period;
    private String invoice_header;
    private String invoice_num_start;
    private String invoice_num_end;
    private String segment_cipher;

    public String getInvoice_type_code() {
        return invoice_type_code;
    }

    public void setInvoice_type_code(String invoice_type_code) {
        this.invoice_type_code = invoice_type_code;
    }

    public String getControl_method() {
        return control_method;
    }

    public void setControl_method(String control_method) {
        this.control_method = control_method;
    }

    public String getCurrent_or_next_period() {
        return current_or_next_period;
    }

    public void setCurrent_or_next_period(String current_or_next_period) {
        this.current_or_next_period = current_or_next_period;
    }

    public String getInvoice_header() {
        return invoice_header;
    }

    public void setInvoice_header(String invoice_header) {
        this.invoice_header = invoice_header;
    }

    public String getInvoice_num_start() {
        return invoice_num_start;
    }

    public void setInvoice_num_start(String invoice_num_start) {
        this.invoice_num_start = invoice_num_start;
    }

    public String getInvoice_num_end() {
        return invoice_num_end;
    }

    public void setInvoice_num_end(String invoice_num_end) {
        this.invoice_num_end = invoice_num_end;
    }

    public String getSegment_cipher() {
        return segment_cipher;
    }

    public void setSegment_cipher(String segment_cipher) {
        this.segment_cipher = segment_cipher;
    }



}
