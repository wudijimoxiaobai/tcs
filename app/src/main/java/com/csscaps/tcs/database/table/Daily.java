package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.DailyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by tl on 2018/11/9.
 */
@Table(database = DailyDatabase.class)
public class Daily extends BaseModel implements  Cloneable {
    @Override
    public String toString() {
        return "Daily{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", invoice_type_code='" + invoice_type_code + '\'' +
                ", s_invoice_no='" + s_invoice_no + '\'' +
                ", invoice_count=" + invoice_count +
                ", total_taxable_amount=" + total_taxable_amount +
                ", total_all=" + total_all +
                ", total_final=" + total_final +
                ", total_vat=" + total_vat +
                ", total_stamp=" + total_stamp +
                ", total_bpt=" + total_bpt +
                ", total_bpt_preypayment=" + total_bpt_preypayment +
                ", total_fee=" + total_fee +
                ", n_invoice_count=" + n_invoice_count +
                ", n_total_taxable_amount=" + n_total_taxable_amount +
                ", n_total_all=" + n_total_all +
                ", n_total_final=" + n_total_final +
                ", n_total_vat=" + n_total_vat +
                ", n_total_stamp=" + n_total_stamp +
                ", n_total_bpt=" + n_total_bpt +
                ", n_total_bpt_preypayment=" + n_total_bpt_preypayment +
                ", n_total_fee=" + n_total_fee +
                ", c_invoice_count=" + c_invoice_count +
                ", c_total_tax_due=" + c_total_tax_due +
                ", c_total_all=" + c_total_all +
                ", c_total_final=" + c_total_final +
                ", c_total_vat=" + c_total_vat +
                ", c_total_stamp=" + c_total_stamp +
                ", c_total_bpt=" + c_total_bpt +
                ", c_total_bpt_preypayment=" + c_total_bpt_preypayment +
                ", c_total_fee=" + c_total_fee +
                ", addr=" + addr +
                '}';
    }

    @PrimaryKey(autoincrement = true)
    @Column
    int id;
    @Column
    String date;
    @Column
    String invoice_type_code;
    @Column
    String s_invoice_no;

    @Column
    int invoice_count;
    @Column
    double total_taxable_amount;
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
    int n_invoice_count;
    @Column
    double n_total_taxable_amount;
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
    int c_invoice_count;
    @Column
    double c_total_tax_due;
    @Column
    double c_total_all;
    @Column
    double c_total_final;
    @Column
    double c_total_vat;
    @Column
    double c_total_stamp;
    @Column
    double c_total_bpt;
    @Column
    double c_total_bpt_preypayment;
    @Column
    double c_total_fee;

    @Column
    int addr;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoice_type_code() {
        return invoice_type_code;
    }

    public void setInvoice_type_code(String invoice_type_code) {
        this.invoice_type_code = invoice_type_code;
    }

    public String getS_invoice_no() {
        return s_invoice_no;
    }

    public void setS_invoice_no(String s_invoice_no) {
        this.s_invoice_no = s_invoice_no;
    }

    public int getInvoice_count() {
        return invoice_count;
    }

    public void setInvoice_count(int invoice_count) {
        this.invoice_count = invoice_count;
    }

    public double getTotal_taxable_amount() {
        return total_taxable_amount;
    }

    public void setTotal_taxable_amount(double total_taxable_amount) {
        this.total_taxable_amount = total_taxable_amount;
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

    public int getN_invoice_count() {
        return n_invoice_count;
    }

    public void setN_invoice_count(int n_invoice_count) {
        this.n_invoice_count = n_invoice_count;
    }

    public double getN_total_taxable_amount() {
        return n_total_taxable_amount;
    }

    public void setN_total_taxable_amount(double n_total_taxable_amount) {
        this.n_total_taxable_amount = n_total_taxable_amount;
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

    public int getC_invoice_count() {
        return c_invoice_count;
    }

    public void setC_invoice_count(int c_invoice_count) {
        this.c_invoice_count = c_invoice_count;
    }

    public double getC_total_tax_due() {
        return c_total_tax_due;
    }

    public void setC_total_tax_due(double c_total_tax_due) {
        this.c_total_tax_due = c_total_tax_due;
    }

    public double getC_total_all() {
        return c_total_all;
    }

    public void setC_total_all(double c_total_all) {
        this.c_total_all = c_total_all;
    }

    public double getC_total_final() {
        return c_total_final;
    }

    public void setC_total_final(double c_total_final) {
        this.c_total_final = c_total_final;
    }

    public double getC_total_vat() {
        return c_total_vat;
    }

    public void setC_total_vat(double c_total_vat) {
        this.c_total_vat = c_total_vat;
    }

    public double getC_total_stamp() {
        return c_total_stamp;
    }

    public void setC_total_stamp(double c_total_stamp) {
        this.c_total_stamp = c_total_stamp;
    }

    public double getC_total_bpt() {
        return c_total_bpt;
    }

    public void setC_total_bpt(double c_total_bpt) {
        this.c_total_bpt = c_total_bpt;
    }

    public double getC_total_bpt_preypayment() {
        return c_total_bpt_preypayment;
    }

    public void setC_total_bpt_preypayment(double c_total_bpt_preypayment) {
        this.c_total_bpt_preypayment = c_total_bpt_preypayment;
    }

    public double getC_total_fee() {
        return c_total_fee;
    }

    public void setC_total_fee(double c_total_fee) {
        this.c_total_fee = c_total_fee;
    }

    public int getAddr() {
        return addr;
    }

    public void setAddr(int addr) {
        this.addr = addr;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
