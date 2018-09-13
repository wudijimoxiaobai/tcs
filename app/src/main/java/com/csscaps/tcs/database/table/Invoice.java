package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */
@Table(database = TcsDatabase.class)
public class Invoice extends BaseModel implements Serializable, Cloneable {

    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    @Column
    @PrimaryKey
    String invoice_no;
    @Column
    String invoice_short_code;
    @Column
    String invoice_made_by = "S";
    @Column
    String invoice_from;
    @Column
    String invoice_type_code;
    @Column
    String invoice_type_name;
    @Column
    String drawer_name;
    @Column
    String client_invoice_datetime;
    @Column
    String purchaser_tin;
    @Column
    String purchaser_name;
    @Column
    String purchaser_address;
    @Column
    String purchaser_phone;
    @Column
    String purchaser_id_type;
    @Column
    String purchaser_id_number;
    @Column
    String seller_tin;
    @Column
    String seller_name;
    @Column
    String seller_address;
    @Column
    String seller_phone;
    @Column
    String seller_branch_addr;
    @Column
    String is_local_currency;
    @Column
    String currency_code;
    @Column
    String exchange_rate;
    @Column
    String total_final;
    @Column
    String total_vat;
    @Column
    String total_stamp;
    @Column
    String total_bpt;
    @Column
    String total_bpt_preypayment;
    @Column
    String total_fee;
    @Column
    String total_tax1;
    @Column
    String total_tax2;
    @Column
    String total_tax3;
    @Column
    String total_tax4;
    @Column
    String total_tax5;
    @Column
    String total_taxable_amount_org;
    @Column
    String total_taxable_amount;
    @Column
    String total_tax_due;
    @Column
    String total_all;
    @Column
    String remark;
    @Column
    String negative_flag;
    @Column
    String deduction_status;
    @Column
    String applied_negative_total_amount;
    @Column
    String org_invoice_uid;
    @Column
    String original_invoice_type_code;
    @Column
    String original_invoice_no;
    @Column
    String invalid_flag;
    @Column
    String invalid_datetime;
    @Column
    String invalid_user_name;
    @Column
    String invalid_remark;
    @Column
    String sign;
    @Column
    String print_flag = "N";
    @Column
    String is_deductable = "N";
    @Column
    String file_name;
    @Column
    String file_content;
    @Column
    String uploadStatus;
    @Column
    String status = "AVL";//AVL:normal, DISA:cancelled, NEG:negative, WRO:wrote off, IVLD:invalid, IRR:illegal
    @Column
    String approveFlag;//0:通过 1：拒绝 2：待确认 3：内部拒绝 4：待审批
    @Column
    String reason;
    @Column
    String requestType;//DISA:cancelled, NEG:negative
    @Column
    String requestBy;
    @Column
    String requestDate;
    @Column
    String requestStatus; //0:申请失败 1:申请成功
    @Column
    String negative_approval_remark;//负数原因

    String invalid = "N";

    List<ProductModel> goods;

    String invoice_type_uid;

    List<Product> mProducts = new ArrayList<>();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

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

    public String getInvoice_type_uid() {
        return invoice_type_uid;
    }

    public void setInvoice_type_uid(String invoice_type_uid) {
        this.invoice_type_uid = invoice_type_uid;
    }

    public List<ProductModel> getGoods() {
        return goods;
    }

    public void setGoods(List<ProductModel> goods) {
        this.goods = goods;
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_bpt_preypayment() {
        return total_bpt_preypayment;
    }

    public void setTotal_bpt_preypayment(String total_bpt_preypayment) {
        this.total_bpt_preypayment = total_bpt_preypayment;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getApproveFlag() {
        return approveFlag;
    }

    public void setApproveFlag(String approveFlag) {
        this.approveFlag = approveFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getInvalid() {
        return invalid;
    }

    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }

    public String getNegative_approval_remark() {
        return negative_approval_remark;
    }

    public void setNegative_approval_remark(String negative_approval_remark) {
        this.negative_approval_remark = negative_approval_remark;
    }
}
