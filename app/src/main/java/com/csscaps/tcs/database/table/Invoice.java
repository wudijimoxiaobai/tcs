package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by tl on 2018/5/22.
 */
@Table(database = TcsDatabase.class)
public class Invoice extends BaseModel implements Serializable {


    /**
     * invoice_no : xxx
     * invoice_short_code : xxx
     * invoice_made_by : xxx
     * invoice_from : xxx
     * invoice_type_code : xxx
     * invoice_type_name : xxx
     * drawer_name : xxx
     * client_invoice_datetime : xxx
     * purchaser_tin : xxx
     * purchaser_name : xxx
     * purchaser_address : xxx
     * purchaser_phone : xxx
     * purchaser_id_type : xxx
     * purchaser_id_number : xxx
     * seller_tin : xxx
     * seller_name : xxx
     * seller_address : xxx
     * seller_phone : xxx
     * seller_branch_addr : xxx
     * is_local_currency : xxx
     * currency_code : xxx
     * exchange_rate : xxx
     * total_final : xxx
     * total_vat : xxx
     * total_stamp : xxx
     * total_bpt : xxx
     * total_tax1 : xxx
     * total_tax2 : xxx
     * total_tax3 : xxx
     * total_tax4 : xxx
     * total_tax5 : xxx
     * total_taxable_amount_org : xxx
     * total_taxable_amount : xxx
     * total_tax_due : xxx
     * total_all : xxx
     * remark : xxx
     * negative_flag : xxx
     * deduction_status : xxx
     * applied_negative_total_amount : xxx
     * org_invoice_uid : xxx
     * original_invoice_type_code : xxx
     * original_invoice_no : xxx
     * invalid_flag : xxx
     * invalid_datetime : xxx
     * invalid_user_name : xxx
     * invalid_remark : xxx
     * sign : xxx
     * print_flag : xxx
     * is_deductable : xxx
     * file_name : xxx
     * file_content : xxx
     * goods :
     */

    @Column
    @PrimaryKey
    private String invoice_no;
    @Column
    private String invoice_short_code;
    @Column
    private String invoice_made_by;
    @Column
    private String invoice_from;
    @Column
    private String invoice_type_code;
    @Column
    private String invoice_type_name;
    @Column
    private String drawer_name;
    @Column
    private String client_invoice_datetime;
    @Column
    private String purchaser_tin;
    @Column
    private String purchaser_name;
    @Column
    private String purchaser_address;
    @Column
    private String purchaser_phone;
    @Column
    private String purchaser_id_type;
    @Column
    private String purchaser_id_number;
    @Column
    private String seller_tin;
    @Column
    private String seller_name;
    @Column
    private String seller_address;
    @Column
    private String seller_phone;
    @Column
    private String seller_branch_addr;
    @Column
    private String is_local_currency;
    @Column
    private String currency_code;
    @Column
    private String exchange_rate;
    @Column
    private String total_final;
    @Column
    private String total_vat;
    @Column
    private String total_stamp;
    @Column
    private String total_bpt;
    @Column
    private String total_tax1;
    @Column
    private String total_tax2;
    @Column
    private String total_tax3;
    @Column
    private String total_tax4;
    @Column
    private String total_tax5;
    @Column
    private String total_taxable_amount_org;
    @Column
    private String total_taxable_amount;
    @Column
    private String total_tax_due;
    @Column
    private String total_all;
    @Column
    private String remark;
    @Column
    private String negative_flag;
    @Column
    private String deduction_status;
    @Column
    private String applied_negative_total_amount;
    @Column
    private String org_invoice_uid;
    @Column
    private String original_invoice_type_code;
    @Column
    private String original_invoice_no;
    @Column
    private String invalid_flag;
    @Column
    private String invalid_datetime;
    @Column
    private String invalid_user_name;
    @Column
    private String invalid_remark;
    @Column
    private String sign;
    @Column
    private String print_flag;
    @Column
    private String is_deductable;
    @Column
    private String file_name;
    @Column
    private String file_content;
    @Column
    private String goods;

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getInvoice_short_code() {
        return invoice_short_code;
    }

    public void setInvoice_short_code(String invoice_short_code) {
        this.invoice_short_code = invoice_short_code;
    }

    public String getInvoice_made_by() {
        return invoice_made_by;
    }

    public void setInvoice_made_by(String invoice_made_by) {
        this.invoice_made_by = invoice_made_by;
    }

    public String getInvoice_from() {
        return invoice_from;
    }

    public void setInvoice_from(String invoice_from) {
        this.invoice_from = invoice_from;
    }

    public String getInvoice_type_code() {
        return invoice_type_code;
    }

    public void setInvoice_type_code(String invoice_type_code) {
        this.invoice_type_code = invoice_type_code;
    }

    public String getInvoice_type_name() {
        return invoice_type_name;
    }

    public void setInvoice_type_name(String invoice_type_name) {
        this.invoice_type_name = invoice_type_name;
    }

    public String getDrawer_name() {
        return drawer_name;
    }

    public void setDrawer_name(String drawer_name) {
        this.drawer_name = drawer_name;
    }

    public String getClient_invoice_datetime() {
        return client_invoice_datetime;
    }

    public void setClient_invoice_datetime(String client_invoice_datetime) {
        this.client_invoice_datetime = client_invoice_datetime;
    }

    public String getPurchaser_tin() {
        return purchaser_tin;
    }

    public void setPurchaser_tin(String purchaser_tin) {
        this.purchaser_tin = purchaser_tin;
    }

    public String getPurchaser_name() {
        return purchaser_name;
    }

    public void setPurchaser_name(String purchaser_name) {
        this.purchaser_name = purchaser_name;
    }

    public String getPurchaser_address() {
        return purchaser_address;
    }

    public void setPurchaser_address(String purchaser_address) {
        this.purchaser_address = purchaser_address;
    }

    public String getPurchaser_phone() {
        return purchaser_phone;
    }

    public void setPurchaser_phone(String purchaser_phone) {
        this.purchaser_phone = purchaser_phone;
    }

    public String getPurchaser_id_type() {
        return purchaser_id_type;
    }

    public void setPurchaser_id_type(String purchaser_id_type) {
        this.purchaser_id_type = purchaser_id_type;
    }

    public String getPurchaser_id_number() {
        return purchaser_id_number;
    }

    public void setPurchaser_id_number(String purchaser_id_number) {
        this.purchaser_id_number = purchaser_id_number;
    }

    public String getSeller_tin() {
        return seller_tin;
    }

    public void setSeller_tin(String seller_tin) {
        this.seller_tin = seller_tin;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_address() {
        return seller_address;
    }

    public void setSeller_address(String seller_address) {
        this.seller_address = seller_address;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    public String getSeller_branch_addr() {
        return seller_branch_addr;
    }

    public void setSeller_branch_addr(String seller_branch_addr) {
        this.seller_branch_addr = seller_branch_addr;
    }

    public String getIs_local_currency() {
        return is_local_currency;
    }

    public void setIs_local_currency(String is_local_currency) {
        this.is_local_currency = is_local_currency;
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

    public String getTotal_final() {
        return total_final;
    }

    public void setTotal_final(String total_final) {
        this.total_final = total_final;
    }

    public String getTotal_vat() {
        return total_vat;
    }

    public void setTotal_vat(String total_vat) {
        this.total_vat = total_vat;
    }

    public String getTotal_stamp() {
        return total_stamp;
    }

    public void setTotal_stamp(String total_stamp) {
        this.total_stamp = total_stamp;
    }

    public String getTotal_bpt() {
        return total_bpt;
    }

    public void setTotal_bpt(String total_bpt) {
        this.total_bpt = total_bpt;
    }

    public String getTotal_tax1() {
        return total_tax1;
    }

    public void setTotal_tax1(String total_tax1) {
        this.total_tax1 = total_tax1;
    }

    public String getTotal_tax2() {
        return total_tax2;
    }

    public void setTotal_tax2(String total_tax2) {
        this.total_tax2 = total_tax2;
    }

    public String getTotal_tax3() {
        return total_tax3;
    }

    public void setTotal_tax3(String total_tax3) {
        this.total_tax3 = total_tax3;
    }

    public String getTotal_tax4() {
        return total_tax4;
    }

    public void setTotal_tax4(String total_tax4) {
        this.total_tax4 = total_tax4;
    }

    public String getTotal_tax5() {
        return total_tax5;
    }

    public void setTotal_tax5(String total_tax5) {
        this.total_tax5 = total_tax5;
    }

    public String getTotal_taxable_amount_org() {
        return total_taxable_amount_org;
    }

    public void setTotal_taxable_amount_org(String total_taxable_amount_org) {
        this.total_taxable_amount_org = total_taxable_amount_org;
    }

    public String getTotal_taxable_amount() {
        return total_taxable_amount;
    }

    public void setTotal_taxable_amount(String total_taxable_amount) {
        this.total_taxable_amount = total_taxable_amount;
    }

    public String getTotal_tax_due() {
        return total_tax_due;
    }

    public void setTotal_tax_due(String total_tax_due) {
        this.total_tax_due = total_tax_due;
    }

    public String getTotal_all() {
        return total_all;
    }

    public void setTotal_all(String total_all) {
        this.total_all = total_all;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNegative_flag() {
        return negative_flag;
    }

    public void setNegative_flag(String negative_flag) {
        this.negative_flag = negative_flag;
    }

    public String getDeduction_status() {
        return deduction_status;
    }

    public void setDeduction_status(String deduction_status) {
        this.deduction_status = deduction_status;
    }

    public String getApplied_negative_total_amount() {
        return applied_negative_total_amount;
    }

    public void setApplied_negative_total_amount(String applied_negative_total_amount) {
        this.applied_negative_total_amount = applied_negative_total_amount;
    }

    public String getOrg_invoice_uid() {
        return org_invoice_uid;
    }

    public void setOrg_invoice_uid(String org_invoice_uid) {
        this.org_invoice_uid = org_invoice_uid;
    }

    public String getOriginal_invoice_type_code() {
        return original_invoice_type_code;
    }

    public void setOriginal_invoice_type_code(String original_invoice_type_code) {
        this.original_invoice_type_code = original_invoice_type_code;
    }

    public String getOriginal_invoice_no() {
        return original_invoice_no;
    }

    public void setOriginal_invoice_no(String original_invoice_no) {
        this.original_invoice_no = original_invoice_no;
    }

    public String getInvalid_flag() {
        return invalid_flag;
    }

    public void setInvalid_flag(String invalid_flag) {
        this.invalid_flag = invalid_flag;
    }

    public String getInvalid_datetime() {
        return invalid_datetime;
    }

    public void setInvalid_datetime(String invalid_datetime) {
        this.invalid_datetime = invalid_datetime;
    }

    public String getInvalid_user_name() {
        return invalid_user_name;
    }

    public void setInvalid_user_name(String invalid_user_name) {
        this.invalid_user_name = invalid_user_name;
    }

    public String getInvalid_remark() {
        return invalid_remark;
    }

    public void setInvalid_remark(String invalid_remark) {
        this.invalid_remark = invalid_remark;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrint_flag() {
        return print_flag;
    }

    public void setPrint_flag(String print_flag) {
        this.print_flag = print_flag;
    }

    public String getIs_deductable() {
        return is_deductable;
    }

    public void setIs_deductable(String is_deductable) {
        this.is_deductable = is_deductable;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_content() {
        return file_content;
    }

    public void setFile_content(String file_content) {
        this.file_content = file_content;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }
}
