package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by tl on 2018/5/9.
 */
@Table(database = TcsDatabase.class)
public class InvoiceType extends BaseModel {

    /**
     * invoice_type_uid : xxx
     * invoice_type_name : xxx
     * invoice_type_code : xxx
     * invioce_template_uid : xxx
     * specification : xxx
     * suite_qty : xxx
     * copies : xxx
     * price : xxx
     * description : xxx
     * status : xxx
     * all_industries : xxx
     * all_scales : xxx
     * all_categories : xxx
     * release_date : xxx
     * release_user_id : xxx
     * crt_datetime : xxx
     * insert_user_id : xxx
     * last_mod_datetime : xxx
     * last_mod_user_id : xxx
     * calculate_method : xxx
     */

    @PrimaryKey
    @Column
    String invoice_type_uid;
    @Column
    String invoice_type_name;
    @Column
    String invoice_type_code;
    @Column
    String invioce_template_uid;
    @Column
    String specification;
    @Column
    String suite_qty;
    @Column
    String copies;
    @Column
    String price;
    @Column
    String description;
    @Column
    String status;
    @Column
    String all_industries;
    @Column
    String all_scales;
    @Column
    String all_categories;
    @Column
    String release_date;
    @Column
    String release_user_id;
    @Column
    String crt_datetime;
    @Column
    String insert_user_id;
    @Column
    String last_mod_datetime;
    @Column
    String last_mod_user_id;
    @Column
    String calculate_method;
    @Column
    int invoiceObject;//0:非注册纳税人 1:注册纳税人 2:两者都可以

    public String getInvoice_type_uid() {
        return invoice_type_uid;
    }

    public void setInvoice_type_uid(String invoice_type_uid) {
        this.invoice_type_uid = invoice_type_uid;
    }

    public String getInvoice_type_name() {
        return invoice_type_name;
    }

    public void setInvoice_type_name(String invoice_type_name) {
        this.invoice_type_name = invoice_type_name;
    }

    public String getInvoice_type_code() {
        return invoice_type_code;
    }

    public void setInvoice_type_code(String invoice_type_code) {
        this.invoice_type_code = invoice_type_code;
    }

    public String getInvioce_template_uid() {
        return invioce_template_uid;
    }

    public void setInvioce_template_uid(String invioce_template_uid) {
        this.invioce_template_uid = invioce_template_uid;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSuite_qty() {
        return suite_qty;
    }

    public void setSuite_qty(String suite_qty) {
        this.suite_qty = suite_qty;
    }

    public String getCopies() {
        return copies;
    }

    public void setCopies(String copies) {
        this.copies = copies;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAll_industries() {
        return all_industries;
    }

    public void setAll_industries(String all_industries) {
        this.all_industries = all_industries;
    }

    public String getAll_scales() {
        return all_scales;
    }

    public void setAll_scales(String all_scales) {
        this.all_scales = all_scales;
    }

    public String getAll_categories() {
        return all_categories;
    }

    public void setAll_categories(String all_categories) {
        this.all_categories = all_categories;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRelease_user_id() {
        return release_user_id;
    }

    public void setRelease_user_id(String release_user_id) {
        this.release_user_id = release_user_id;
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

    public String getCalculate_method() {
        return calculate_method;
    }

    public void setCalculate_method(String calculate_method) {
        this.calculate_method = calculate_method;
    }

    public int getInvoiceObject() {
        return invoiceObject;
    }

    public void setInvoiceObject(int invoiceObject) {
        this.invoiceObject = invoiceObject;
    }
}
