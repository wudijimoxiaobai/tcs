package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by tl on 2018/6/26.
 */
@Table(database = TcsDatabase.class)
public class ReportData extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    int reportId;
    @Column
    String invoice_type_code;
    @Column
    String  date;
    @Column
    String report_date;
    @Column
    double total_all;
    @Column
    double total_final;
    @Column
    double total_vat;
    @Column
    double total_stamp;
    @Column
    double total_bpt;
    @Column
    double total_bpt_preypayment;
    @Column
    double total_fee;
    @Column
    double total_taxable_amount;
    @Column
    double n_total_all;
    @Column
    double n_total_final;
    @Column
    double n_total_vat;
    @Column
    double n_total_stamp;
    @Column
    double n_total_bpt;
    @Column
    double n_total_bpt_preypayment;
    @Column
    double n_total_fee;
    @Column
    double n_total_taxable_amount;
    @Column
    int invoice_number;
    @Column
    int c_invoice_number;
    @Column
    int n_invoice_number;
    @Column
    String report_status="0";

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getInvoice_type_code() {
        return invoice_type_code;
    }

    public void setInvoice_type_code(String invoice_type_code) {
        this.invoice_type_code = invoice_type_code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public double getTotal_all() {
        return total_all;
    }

    public void setTotal_all(double total_all) {
        this.total_all = total_all;
    }

    public double getTotal_final() {
        return total_final;
    }

    public void setTotal_final(double total_final) {
        this.total_final = total_final;
    }

    public double getTotal_vat() {
        return total_vat;
    }

    public void setTotal_vat(double total_vat) {
        this.total_vat = total_vat;
    }

    public double getTotal_stamp() {
        return total_stamp;
    }

    public void setTotal_stamp(double total_stamp) {
        this.total_stamp = total_stamp;
    }

    public double getTotal_bpt() {
        return total_bpt;
    }

    public void setTotal_bpt(double total_bpt) {
        this.total_bpt = total_bpt;
    }

    public double getTotal_bpt_preypayment() {
        return total_bpt_preypayment;
    }

    public void setTotal_bpt_preypayment(double total_bpt_preypayment) {
        this.total_bpt_preypayment = total_bpt_preypayment;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public double getTotal_taxable_amount() {
        return total_taxable_amount;
    }

    public void setTotal_taxable_amount(double total_taxable_amount) {
        this.total_taxable_amount = total_taxable_amount;
    }

    public double getN_total_all() {
        return n_total_all;
    }

    public void setN_total_all(double n_total_all) {
        this.n_total_all = n_total_all;
    }

    public double getN_total_final() {
        return n_total_final;
    }

    public void setN_total_final(double n_total_final) {
        this.n_total_final = n_total_final;
    }

    public double getN_total_vat() {
        return n_total_vat;
    }

    public void setN_total_vat(double n_total_vat) {
        this.n_total_vat = n_total_vat;
    }

    public double getN_total_stamp() {
        return n_total_stamp;
    }

    public void setN_total_stamp(double n_total_stamp) {
        this.n_total_stamp = n_total_stamp;
    }

    public double getN_total_bpt() {
        return n_total_bpt;
    }

    public void setN_total_bpt(double n_total_bpt) {
        this.n_total_bpt = n_total_bpt;
    }

    public double getN_total_bpt_preypayment() {
        return n_total_bpt_preypayment;
    }

    public void setN_total_bpt_preypayment(double n_total_bpt_preypayment) {
        this.n_total_bpt_preypayment = n_total_bpt_preypayment;
    }

    public double getN_total_fee() {
        return n_total_fee;
    }

    public void setN_total_fee(double n_total_fee) {
        this.n_total_fee = n_total_fee;
    }

    public double getN_total_taxable_amount() {
        return n_total_taxable_amount;
    }

    public void setN_total_taxable_amount(double n_total_taxable_amount) {
        this.n_total_taxable_amount = n_total_taxable_amount;
    }

    public int getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(int invoice_number) {
        this.invoice_number = invoice_number;
    }

    public int getC_invoice_number() {
        return c_invoice_number;
    }

    public void setC_invoice_number(int c_invoice_number) {
        this.c_invoice_number = c_invoice_number;
    }

    public int getN_invoice_number() {
        return n_invoice_number;
    }

    public void setN_invoice_number(int n_invoice_number) {
        this.n_invoice_number = n_invoice_number;
    }

    public String getReport_status() {
        return report_status;
    }

    public void setReport_status(String report_status) {
        this.report_status = report_status;
    }
}
