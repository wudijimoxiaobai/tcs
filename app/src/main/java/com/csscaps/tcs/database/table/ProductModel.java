package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tl on 2018/5/22.
 */
@Table(database = TcsDatabase.class)
public class ProductModel {

    @Column
    @PrimaryKey
    String invoice_no;
    @Column
    String category;
    @Column
    String line_no;
    @Column
    String taxable_item_uid;
    @Column
    String product_code;
    @Column
    String currency_code;
    @Column
    String exchange_rate;
    @Column
    String item_name;
    @Column
    String item_desc;
    @Column
    String unit;
    @Column
    String unit_price;
    @Column
    String unit_price_tax_rate;
    @Column
    String taxtype;
    @Column
    String qty;
    @Column
    String taxable_amount;
    @Column
    String taxable_amount_org;
    @Column
    String tax_due;
    @Column
    String amount_inc;
    @Column
    String unit_price_after_tax;
    @Column
    String vat_amount;
    @Column
    double vat;
    @Column
    double bpt_final;
    @Column
    double bpt_prepayment;
    @Column
    double stamp_duty_federal;
    @Column
    double stamp_duty_local;
    @Column
    double fees;
    @Column
    double e_tax;
    @Column
    double i_tax;


    Map<String, Double> mapTax = new HashMap();


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLine_no() {
        return line_no;
    }

    public void setLine_no(String line_no) {
        this.line_no = line_no;
    }

    public String getTaxable_item_uid() {
        return taxable_item_uid;
    }

    public void setTaxable_item_uid(String taxable_item_uid) {
        this.taxable_item_uid = taxable_item_uid;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(String exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getUnit_price_tax_rate() {
        return unit_price_tax_rate;
    }

    public void setUnit_price_tax_rate(String unit_price_tax_rate) {
        this.unit_price_tax_rate = unit_price_tax_rate;
    }

    public String getTaxtype() {
        return taxtype;
    }

    public void setTaxtype(String taxtype) {
        this.taxtype = taxtype;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTaxable_amount() {
        return taxable_amount;
    }

    public void setTaxable_amount(String taxable_amount) {
        this.taxable_amount = taxable_amount;
    }

    public String getTaxable_amount_org() {
        return taxable_amount_org;
    }

    public void setTaxable_amount_org(String taxable_amount_org) {
        this.taxable_amount_org = taxable_amount_org;
    }

    public String getTax_due() {
        return tax_due;
    }

    public void setTax_due(String tax_due) {
        this.tax_due = tax_due;
    }

    public String getAmount_inc() {
        return amount_inc;
    }

    public void setAmount_inc(String amount_inc) {
        this.amount_inc = amount_inc;
    }

    public String getUnit_price_after_tax() {
        return unit_price_after_tax;
    }

    public void setUnit_price_after_tax(String unit_price_after_tax) {
        this.unit_price_after_tax = unit_price_after_tax;
    }

    public String getVat_amount() {
        return vat_amount;
    }

    public void setVat_amount(String vat_amount) {
        this.vat_amount = vat_amount;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public Map<String, Double> getMapTax() {
        return mapTax;
    }

    public void setMapTax(Map<String, Double> mapTax) {
        this.mapTax = mapTax;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getBpt_final() {
        return bpt_final;
    }

    public void setBpt_final(double bpt_final) {
        this.bpt_final = bpt_final;
    }

    public double getBpt_prepayment() {
        return bpt_prepayment;
    }

    public void setBpt_prepayment(double bpt_prepayment) {
        this.bpt_prepayment = bpt_prepayment;
    }

    public double getStamp_duty_federal() {
        return stamp_duty_federal;
    }

    public void setStamp_duty_federal(double stamp_duty_federal) {
        this.stamp_duty_federal = stamp_duty_federal;
    }

    public double getStamp_duty_local() {
        return stamp_duty_local;
    }

    public void setStamp_duty_local(double stamp_duty_local) {
        this.stamp_duty_local = stamp_duty_local;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public double getE_tax() {
        return e_tax;
    }

    public void setE_tax(double e_tax) {
        this.e_tax = e_tax;
    }

    public double getI_tax() {
        return i_tax;
    }

    public void setI_tax(double i_tax) {
        this.i_tax = i_tax;
    }
}
