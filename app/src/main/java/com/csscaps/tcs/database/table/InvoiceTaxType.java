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
public class InvoiceTaxType extends BaseModel {


    /**
     * invoice_type_taxtype_uid : xxx
     * invoice_type_uid : xxx
     * taxtype_uid : xxx
     * taxed_method : xxx
     * is_tax_included : xxx
     * crt_datetime : xxx
     * insert_user_id : xxx
     * last_mod_datetime : xxx
     * last_mod_user_id : xxx
     * status : xxx
     */
    @Column
    @PrimaryKey
    private String invoice_type_taxtype_uid;
    @Column
    private String invoice_type_uid;
    @Column
    private String taxtype_uid;
    @Column
    private String taxed_method;
    @Column
    private String is_tax_included;
    @Column
    private String crt_datetime;
    @Column
    private String insert_user_id;
    @Column
    private String last_mod_datetime;
    @Column
    private String last_mod_user_id;
    @Column
    private String status;

    public String getInvoice_type_taxtype_uid() {
        return invoice_type_taxtype_uid;
    }

    public void setInvoice_type_taxtype_uid(String invoice_type_taxtype_uid) {
        this.invoice_type_taxtype_uid = invoice_type_taxtype_uid;
    }

    public String getInvoice_type_uid() {
        return invoice_type_uid;
    }

    public void setInvoice_type_uid(String invoice_type_uid) {
        this.invoice_type_uid = invoice_type_uid;
    }

    public String getTaxtype_uid() {
        return taxtype_uid;
    }

    public void setTaxtype_uid(String taxtype_uid) {
        this.taxtype_uid = taxtype_uid;
    }

    public String getTaxed_method() {
        return taxed_method;
    }

    public void setTaxed_method(String taxed_method) {
        this.taxed_method = taxed_method;
    }

    public String getIs_tax_included() {
        return is_tax_included;
    }

    public void setIs_tax_included(String is_tax_included) {
        this.is_tax_included = is_tax_included;
    }

    public String getCrt_datetime() {
        return crt_datetime;
    }

    public void setCrt_datetime(String crt_datetime) {
        this.crt_datetime = crt_datetime;
    }

    public String getInsert_user_id() {
        return insert_user_id;
    }

    public void setInsert_user_id(String insert_user_id) {
        this.insert_user_id = insert_user_id;
    }

    public String getLast_mod_datetime() {
        return last_mod_datetime;
    }

    public void setLast_mod_datetime(String last_mod_datetime) {
        this.last_mod_datetime = last_mod_datetime;
    }

    public String getLast_mod_user_id() {
        return last_mod_user_id;
    }

    public void setLast_mod_user_id(String last_mod_user_id) {
        this.last_mod_user_id = last_mod_user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
