package com.csscaps.tcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxType;
import com.csscaps.tcs.model.RelatedTaxItem;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tl on 2018/5/17.
 */

public class ProductDetailsActivity extends BaseDetailsActivity<Product> {

    @BindView(R.id.back)
    TextView mBack;
    @BindView(R.id.product_name)
    TextView mProductName;
    @BindView(R.id.local_name)
    TextView mLocalName;
    @BindView(R.id.unit)
    TextView mUnit;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.percentage)
    TextView mPercentage;
    @BindView(R.id.fixed_amount)
    TextView mFixedAmount;
    @BindView(R.id.purchase)
    TextView mPurchase;
    @BindView(R.id.commission)
    TextView mCommission;
    @BindView(R.id.adjustment)
    TextView mAdjustment;
    @BindView(R.id.related_tax_items)
    TextView mRelatedTaxItems;
    @BindView(R.id.specification)
    TextView mSpecification;
    @BindView(R.id.unit_discount_percentage)
    TextView mUnitDiscountPercentage;
    @BindView(R.id.unit_discount_amount)
    TextView mUnitDiscountAmount;

    private Product mProduct;

    @Override
    protected int getLayoutResId() {
        return R.layout.product_details_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void parseArgumentsFromIntent(Intent argIntent) {
        mProduct = (Product) argIntent.getSerializableExtra("product");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tIntoTextView(mProduct);
        String str = mProduct.getRelatedTaxItemString();
        RelatedTaxItem relatedTaxItem = JSON.parseObject(str, RelatedTaxItem.class);
        StringBuffer stringBuffer = new StringBuffer();
        List<TaxItem> mTaxItemList = relatedTaxItem.getTaxItemList();
        List<TaxType> mTaxTypeList = relatedTaxItem.getTaxTypeList();
        for (TaxItem taxItem : mTaxItemList) {
            String name = taxItem.getItem_name_in_english();
            stringBuffer.append("," + name);
        }

        for (TaxType taxType : mTaxTypeList) {
            String name = taxType.getTaxtype_name();
            stringBuffer.append("," + name);
        }
        mRelatedTaxItems.setText(stringBuffer.delete(0, 1).toString());
    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
