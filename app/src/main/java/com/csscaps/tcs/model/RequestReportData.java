package com.csscaps.tcs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tl on 2018/6/27.
 */

public class RequestReportData extends RequestData {

    private String date;
    private String type;
    private String code;
    private List<ReportDataModel> invoice_data=new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ReportDataModel> getInvoice_data() {
        return invoice_data;
    }

    public void setInvoice_data(List<ReportDataModel> invoice_data) {
        this.invoice_data = invoice_data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
