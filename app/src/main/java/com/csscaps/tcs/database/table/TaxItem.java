package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by tl on 2018/5/3.
 * 税目表
 */
@Table(database = TcsDatabase.class)
public class TaxItem extends BaseModel {
    @Override
    public String toString() {
        return "TaxItem{" +
                "taxable_item_uid='" + taxable_item_uid + '\'' +
                ", taxtype_uid='" + taxtype_uid + '\'' +
                ", item_codechar='" + item_codechar + '\'' +
                ", item_name_in_english='" + item_name_in_english + '\'' +
                ", descriptionchar='" + descriptionchar + '\'' +
                ", adjusted_value='" + adjusted_value + '\'' +
                ", tax_amount_ratio='" + tax_amount_ratio + '\'' +
                ", tax_rate='" + tax_rate + '\'' +
                ", formula_desc='" + formula_desc + '\'' +
                ", unit='" + unit + '\'' +
                ", parent_item_uidinteger='" + parent_item_uidinteger + '\'' +
                ", is_leaf='" + is_leaf + '\'' +
                ", node_level='" + node_level + '\'' +
                ", statuschar='" + statuschar + '\'' +
                ", crt_datetime='" + crt_datetime + '\'' +
                ", i18n_codechar='" + i18n_codechar + '\'' +
                '}';
    }

    /**
     * taxable_item_uid : xxx
     * taxtype_uid : xxx
     * item_codechar : xxx
     * item_name_in_english : xxx
     * descriptionchar : xxx
     * adjusted_value : xxx
     * tax_amount_ratio : xxx
     * tax_rate : xxx
     * formula_desc : xxx
     * parent_item_uidinteger : xxx
     * is_leaf : xxx
     * node_level : xxx
     * statuschar : xxx
     * crt_datetime : xxx
     * i18n_codechar : xxx
     */
    @Column
    @PrimaryKey
    String taxable_item_uid;

    @Column
    String taxtype_uid;

    @Column
    String item_codechar;

    @Column
    String item_name_in_english;

    @Column
    String descriptionchar;

    @Column
    String adjusted_value;

    @Column
    String tax_amount_ratio;

    @Column
    String tax_rate;

    @Column
    String formula_desc;

    @Column
    String unit;

    @Column
    String parent_item_uidinteger;

    @Column
    String is_leaf;

    @Column
    String node_level;

    @Column
    String statuschar;

    @Column
    String crt_datetime;

    @Column
    String i18n_codechar;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTaxable_item_uid() {
        return taxable_item_uid;
    }

    public void setTaxable_item_uid(String taxable_item_uid) {
        this.taxable_item_uid = taxable_item_uid;
    }

    public String getTaxtype_uid() {
        return taxtype_uid;
    }

    public void setTaxtype_uid(String taxtype_uid) {
        this.taxtype_uid = taxtype_uid;
    }

    public String getItem_codechar() {
        return item_codechar;
    }

    public void setItem_codechar(String item_codechar) {
        this.item_codechar = item_codechar;
    }

    public String getItem_name_in_english() {
        return item_name_in_english;
    }

    public void setItem_name_in_english(String item_name_in_english) {
        this.item_name_in_english = item_name_in_english;
    }

    public String getDescriptionchar() {
        return descriptionchar;
    }

    public void setDescriptionchar(String descriptionchar) {
        this.descriptionchar = descriptionchar;
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

    public String getFormula_desc() {
        return formula_desc;
    }

    public void setFormula_desc(String formula_desc) {
        this.formula_desc = formula_desc;
    }

    public String getParent_item_uidinteger() {
        return parent_item_uidinteger;
    }

    public void setParent_item_uidinteger(String parent_item_uidinteger) {
        this.parent_item_uidinteger = parent_item_uidinteger;
    }

    public String getIs_leaf() {
        return is_leaf;
    }

    public void setIs_leaf(String is_leaf) {
        this.is_leaf = is_leaf;
    }

    public String getNode_level() {
        return node_level;
    }

    public void setNode_level(String node_level) {
        this.node_level = node_level;
    }

    public String getStatuschar() {
        return statuschar;
    }

    public void setStatuschar(String statuschar) {
        this.statuschar = statuschar;
    }

    public String getCrt_datetime() {
        return crt_datetime;
    }

    public void setCrt_datetime(String crt_datetime) {
        this.crt_datetime = crt_datetime;
    }

    public String getI18n_codechar() {
        return i18n_codechar;
    }

    public void setI18n_codechar(String i18n_codechar) {
        this.i18n_codechar = i18n_codechar;
    }


}
