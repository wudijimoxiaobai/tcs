package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by tl on 2018/5/25.
 */
@Table(database = TcsDatabase.class)
public class TaxMethod extends BaseModel {

    /**
     * tax_calc_rule_uid : xxx
     * invoice_type_taxtype_uid : xxx
     * taxed_method : xxx
     * amt_qty_mode : xxx
     * taxable_item_uid : xxx
     * is_tax_included : xxx
     * adjusted_value : xxx
     * tax_amount_ratio : xxx
     * tax_rate : xxx
     * remark : xxx
     * crt_datetime : xxx
     * last_update_time : xxx
     */
    @Column
    @PrimaryKey
    private String tax_calc_rule_uid;
    @Column
    private String invoice_type_taxtype_uid;
    @Column
    private String taxed_method;
    @Column
    private String amt_qty_mode;
    @Column
    private String taxable_item_uid;
    @Column
    private String is_tax_included;
    @Column
    private String adjusted_value;
    @Column
    private String tax_amount_ratio;
    @Column
    private String tax_rate;
    @Column
    private String remark;
    @Column
    private String crt_datetime;
    @Column
    private String last_update_time;

    public String getTax_calc_rule_uid() {
        return tax_calc_rule_uid;
    }

    public void setTax_calc_rule_uid(String tax_calc_rule_uid) {
        this.tax_calc_rule_uid = tax_calc_rule_uid;
    }

    public String getInvoice_type_taxtype_uid() {
        return invoice_type_taxtype_uid;
    }

    public void setInvoice_type_taxtype_uid(String invoice_type_taxtype_uid) {
        this.invoice_type_taxtype_uid = invoice_type_taxtype_uid;
    }

    public String getTaxed_method() {
        return taxed_method;
    }

    public void setTaxed_method(String taxed_method) {
        this.taxed_method = taxed_method;
    }

    public String getAmt_qty_mode() {
        return amt_qty_mode;
    }

    public void setAmt_qty_mode(String amt_qty_mode) {
        this.amt_qty_mode = amt_qty_mode;
    }

    public String getTaxable_item_uid() {
        return taxable_item_uid;
    }

    public void setTaxable_item_uid(String taxable_item_uid) {
        this.taxable_item_uid = taxable_item_uid;
    }

    public String getIs_tax_included() {
        return is_tax_included;
    }

    public void setIs_tax_included(String is_tax_included) {
        this.is_tax_included = is_tax_included;
    }

    public String getAdjusted_value() {
        return adjusted_value;
    }

    public void setAdjusted_value(String adjusted_value) {
        this.adjusted_value = adjusted_value;
    }

    public String getTax_amount_ratio() {
        return tax_amount_ratio;
    }

    public void setTax_amount_ratio(String tax_amount_ratio) {
        this.tax_amount_ratio = tax_amount_ratio;
    }

    public String getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(String tax_rate) {
        this.tax_rate = tax_rate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCrt_datetime() {
        return crt_datetime;
    }

    public void setCrt_datetime(String crt_datetime) {
        this.crt_datetime = crt_datetime;
    }

    public String getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(String last_update_time) {
        this.last_update_time = last_update_time;
    }
}
