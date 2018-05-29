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
    String price;

    @Column
    String percentage;

    @Column
    String fixedAmount;

    @Column
    String purchase;

    @Column
    String adjustment;

    @Column
    String specification;

    @Column
    String relatedTaxItemString;

    @Column
    String commission;

    String quantity;

    String totalTax="0";

    String eTax="0";

    String iTax="0";

    ProductModel mProductModel;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public String geteTax() {
        return eTax;
    }

    public void seteTax(String eTax) {
        this.eTax = eTax;
    }

    public String getiTax() {
        return iTax;
    }

    public void setiTax(String iTax) {
        this.iTax = iTax;
    }

    public ProductModel getProductModel() {
        return mProductModel;
    }

    public void setProductModel(ProductModel productModel) {
        mProductModel = productModel;
    }
}
