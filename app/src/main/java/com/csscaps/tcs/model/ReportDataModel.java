package com.csscaps.tcs.model;

/**
 * Created by tl on 2018/6/27.
 */

public class ReportDataModel {

    private String invoice_type_code;
    private String summary_data;
    private String code;
    private String managerData;

    public String getInvoice_type_code() {
        return invoice_type_code;
    }

    public void setInvoice_type_code(String invoice_type_code) {
        this.invoice_type_code = invoice_type_code;
    }

    public String getSummary_data() {
        return summary_data;
    }

    public void setSummary_data(String summary_data) {
        this.summary_data = summary_data;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getManagerData() {
        return managerData;
    }

    public void setManagerData(String managerData) {
        this.managerData = managerData;
    }
}
