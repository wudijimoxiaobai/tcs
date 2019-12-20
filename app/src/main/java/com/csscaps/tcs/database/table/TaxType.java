package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by tl on 2018/5/9.
 * 税种
 */
@Table(database = TcsDatabase.class)
public class  TaxType extends BaseModel {

    /**
     * taxtype_uid : xxx
     * taxtype_name : xxx
     * taxtype_code : xxx
     * taxtype_cycle : xxx
     * is_taxable_item_related : xxx
     * taxed_method : xxx
     * description : xxx
     * period_start : xxx
     * effective_date : xxx
     * expire_date : xxx
     * crt_datetime : xxx
     * status : xxx
     * i18n_code : xxx
     */
    @PrimaryKey
    @Column
    String taxtype_uid;
    @Column
    String taxtype_name;
    @Column
    String taxtype_code;
    @Column
    String taxtype_cycle;
    @Column
    String is_taxable_item_related;
    @Column
    String taxed_method;
    @Column
    String description;
    @Column
    String period_start;
    @Column
    String effective_date;
    @Column
    String expire_date;
    @Column
    String crt_datetime;
    @Column
    String status;
    @Column
    String i18n_code;


    public String getTaxtype_uid() {
        return taxtype_uid;
    }

    public void setTaxtype_uid(String taxtype_uid) {
        this.taxtype_uid = taxtype_uid;
    }

    public String getTaxtype_name() {
        return taxtype_name;
    }

    public void setTaxtype_name(String taxtype_name) {
        this.taxtype_name = taxtype_name;
    }

    public String getTaxtype_code() {
        return taxtype_code;
    }

    public void setTaxtype_code(String taxtype_code) {
        this.taxtype_code = taxtype_code;
    }

    public String getTaxtype_cycle() {
        return taxtype_cycle;
    }

    public void setTaxtype_cycle(String taxtype_cycle) {
        this.taxtype_cycle = taxtype_cycle;
    }

    public String getIs_taxable_item_related() {
        return is_taxable_item_related;
    }

    public void setIs_taxable_item_related(String is_taxable_item_related) {
        this.is_taxable_item_related = is_taxable_item_related;
    }

    public String getTaxed_method() {
        return taxed_method;
    }

    public void setTaxed_method(String taxed_method) {
        this.taxed_method = taxed_method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeriod_start() {
        return period_start;
    }

    public void setPeriod_start(String period_start) {
        this.period_start = period_start;
    }

    public String getEffective_date() {
        return effective_date;
    }

    public void setEffective_date(String effective_date) {
        this.effective_date = effective_date;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getCrt_datetime() {
        return crt_datetime;
    }

    public void setCrt_datetime(String crt_datetime) {
        this.crt_datetime = crt_datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getI18n_code() {
        return i18n_code;
    }

    public void setI18n_code(String i18n_code) {
        this.i18n_code = i18n_code;
    }

    @Override
    public String toString() {
        return "TaxType{" +
                "taxtype_uid='" + taxtype_uid + '\'' +
                ", taxtype_name='" + taxtype_name + '\'' +
                ", taxtype_code='" + taxtype_code + '\'' +
                ", taxtype_cycle='" + taxtype_cycle + '\'' +
                ", is_taxable_item_related='" + is_taxable_item_related + '\'' +
                ", taxed_method='" + taxed_method + '\'' +
                ", description='" + description + '\'' +
                ", period_start='" + period_start + '\'' +
                ", effective_date='" + effective_date + '\'' +
                ", expire_date='" + expire_date + '\'' +
                ", crt_datetime='" + crt_datetime + '\'' +
                ", status='" + status + '\'' +
                ", i18n_code='" + i18n_code + '\'' +
                '}';
    }
}
