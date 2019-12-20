package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by tl on 2018/6/28.
 */
@Table(database = TcsDatabase.class)
public class ControlData extends BaseModel {
    @Override
    public String toString() {
        return "ControlData{" +
                "invoice_type_code='" + invoice_type_code + '\'' +
                ", total_amount_perinvoice=" + total_amount_perinvoice +
                ", total_all=" + total_all +
                ", n_total_all=" + n_total_all +
                ", new_date='" + new_date + '\'' +
                ", issuing_last_date='" + issuing_last_date + '\'' +
                ", report_flag='" + report_flag + '\'' +
                ", tcs_pwd_flag='" + tcs_pwd_flag + '\'' +
                ", s_report_date='" + s_report_date + '\'' +
                ", e_report_date='" + e_report_date + '\'' +
                ", invoice_type='" + invoice_type + '\'' +
                ", rfu='" + rfu + '\'' +
                ", check_tax_control_panel='" + check_tax_control_panel + '\'' +
                ", check_tax_control_panel_n_invoice='" + check_tax_control_panel_n_invoice + '\'' +
                ", invoice_number_permonth=" + invoice_number_permonth +
                ", tax_type_item_index=" + tax_type_item_index +
                ", issuing_invoice_type='" + issuing_invoice_type + '\'' +
                ", issuing_n_invoice_days=" + issuing_n_invoice_days +
                '}';
    }

    @PrimaryKey
    @Column
    String invoice_type_code;
    @Column
    long total_amount_perinvoice;
    @Column
    long total_all;
    @Column
    long n_total_all;
    @Column
    String new_date;
    @Column
    String issuing_last_date;
    @Column
    String report_flag;//
    @Column
    String tcs_pwd_flag;
    @Column
    String s_report_date;
    @Column
    String e_report_date;
    @Column
    String invoice_type;
    @Column
    String rfu;
    @Column
    String check_tax_control_panel;
    @Column
    String check_tax_control_panel_n_invoice;
    @Column
    int invoice_number_permonth;
    @Column
    long tax_type_item_index;
    @Column
    String issuing_invoice_type;
    @Column
    int issuing_n_invoice_days;


    public String getInvoice_type_code() {
        return invoice_type_code;
    }

    public void setInvoice_type_code(String invoice_type_code) {
        this.invoice_type_code = invoice_type_code;
    }

    public long getTotal_amount_perinvoice() {
        return total_amount_perinvoice;
    }

    public void setTotal_amount_perinvoice(long total_amount_perinvoice) {
        this.total_amount_perinvoice = total_amount_perinvoice;
    }

    public long getTotal_all() {
        return total_all;
    }

    public void setTotal_all(long total_all) {
        this.total_all = total_all;
    }

    public long getN_total_all() {
        return n_total_all;
    }

    public void setN_total_all(long n_total_all) {
        this.n_total_all = n_total_all;
    }

    public String getNew_date() {
        return new_date;
    }

    public void setNew_date(String new_date) {
        this.new_date = new_date;
    }

    public String getIssuing_last_date() {
        return issuing_last_date;
    }

    public void setIssuing_last_date(String issuing_last_date) {
        this.issuing_last_date = issuing_last_date;
    }

    public String getReport_flag() {
        return report_flag;
    }

    public void setReport_flag(String report_flag) {
        this.report_flag = report_flag;
    }

    public String getTcs_pwd_flag() {
        return tcs_pwd_flag;
    }

    public void setTcs_pwd_flag(String tcs_pwd_flag) {
        this.tcs_pwd_flag = tcs_pwd_flag;
    }

    public String getS_report_date() {
        return s_report_date;
    }

    public void setS_report_date(String s_report_date) {
        this.s_report_date = s_report_date;
    }

    public String getE_report_date() {
        return e_report_date;
    }

    public void setE_report_date(String e_report_date) {
        this.e_report_date = e_report_date;
    }

    public String getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(String invoice_type) {
        this.invoice_type = invoice_type;
    }

    public String getCheck_tax_control_panel() {
        return check_tax_control_panel;
    }

    public void setCheck_tax_control_panel(String check_tax_control_panel) {
        this.check_tax_control_panel = check_tax_control_panel;
    }

    public String getCheck_tax_control_panel_n_invoice() {
        return check_tax_control_panel_n_invoice;
    }

    public void setCheck_tax_control_panel_n_invoice(String check_tax_control_panel_n_invoice) {
        this.check_tax_control_panel_n_invoice = check_tax_control_panel_n_invoice;
    }

    public int getInvoice_number_permonth() {
        return invoice_number_permonth;
    }

    public void setInvoice_number_permonth(int invoice_number_permonth) {
        this.invoice_number_permonth = invoice_number_permonth;
    }

    public long getTax_type_item_index() {
        return tax_type_item_index;
    }

    public void setTax_type_item_index(long tax_type_item_index) {
        this.tax_type_item_index = tax_type_item_index;
    }

    public String getIssuing_invoice_type() {
        return issuing_invoice_type;
    }

    public void setIssuing_invoice_type(String issuing_invoice_type) {
        this.issuing_invoice_type = issuing_invoice_type;
    }

    public int getIssuing_n_invoice_days() {
        return issuing_n_invoice_days;
    }

    public void setIssuing_n_invoice_days(int issuing_n_invoice_days) {
        this.issuing_n_invoice_days = issuing_n_invoice_days;
    }

    public String getRfu() {
        return rfu;
    }

    public void setRfu(String rfu) {
        this.rfu = rfu;
    }
}
