package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by tl on 2018/5/3.
 * 商品表
 */
@Table(database = TcsDatabase.class)
public class Product extends BaseModel implements Serializable {
    @PrimaryKey(autoincrement = true)
    @Column
    int productId;

    @Column
    String productName;

    @Column
    String localName;

    @Column
    String unit;

    @Column
    float price;

    @Column
    float percentage;

    @Column
    float fixedAmount;

    @Column
    String purchase;

    @Column
    String adjustment;

    @Column
    String specification;

    @Column
    String relatedTaxItemString;

    @Column
    float commission;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(float fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(String adjustment) {
        this.adjustment = adjustment;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getRelatedTaxItemString() {
        return relatedTaxItemString;
    }

    public void setRelatedTaxItemString(String relatedTaxItemString) {
        this.relatedTaxItemString = relatedTaxItemString;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }
}
