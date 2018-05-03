package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by tl on 2018/5/3.
 * 商品表
 */
@Table(database = TcsDatabase.class)
public class Product extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    private int productId;

    @Column
    private String productName;

    @Column
    private String localName;

    @Column
    private String unit;

    @Column
    private float price;

    @Column
    private String percentage;

    @Column
    private String fixedAmount;

    @Column
    private String purchase;

    @Column
    private String adjustment;

    @Column
    private String specification;

    @Column
    @NotNull
    @ForeignKey(saveForeignKeyModel = false)
    private TaxItem mTaxItem;

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

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(String fixedAmount) {
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

    public TaxItem getTaxItem() {
        return mTaxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        mTaxItem = taxItem;
    }
}
