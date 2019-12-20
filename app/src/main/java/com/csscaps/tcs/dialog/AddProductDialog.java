package com.csscaps.tcs.dialog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.DecimalDigitsInputFilter;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxType;
import com.csscaps.tcs.fragment.ProductMgtFragment;
import com.csscaps.tcs.model.RelatedTaxItem;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by tl on 2018/5/14.
 * 添加商品
 */

public class AddProductDialog extends BaseAddDialog<Product> implements Action1<RelatedTaxItem> {

    @BindView(R.id.product_name)
    EditText mProductName;
    @BindView(R.id.local_name)
    EditText mLocalName;
    @BindView(R.id.unit)
    EditText mUnit;
    @BindView(R.id.price)
    EditText mPrice;
    @BindView(R.id.percentage)
    EditText mPercentage;
    @BindView(R.id.fixed_amount)
    EditText mFixedAmount;
    @BindView(R.id.purchase)
    EditText mPurchase;
    @BindView(R.id.adjustment)
    EditText mAdjustment;
    @BindView(R.id.related_tax_items)
    TextView mRelatedTaxItems;
    @BindView(R.id.commission)
    EditText mCommission;
    @BindView(R.id.specification)
    EditText mSpecification;
    @BindView(R.id.unit_discount_percentage)
    EditText mUnitDiscountPercentage;
    @BindView(R.id.unit_discount_amount)
    EditText mUnitDiscountAmount;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.add_product_dialog;
    }

    protected void initView() {
        InputFilter inputFilter[] = new InputFilter[]{new DecimalDigitsInputFilter(2)};
        mPrice.setFilters(inputFilter);
        mFixedAmount.setFilters(inputFilter);
        mPurchase.setFilters(inputFilter);
        mCommission.setFilters(inputFilter);
        mPercentage.setFilters(inputFilter);
        mUnitDiscountAmount.setFilters(inputFilter);
        mUnitDiscountPercentage.setFilters(inputFilter);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (t == null) t = new Product();
        ObserverActionUtils.addAction(this);
        if (edit) {
            tIntoEditText();
            String str = t.getRelatedTaxItemString();
            RelatedTaxItem relatedTaxItem = JSON.parseObject(str, RelatedTaxItem.class);
            call(relatedTaxItem);
        }

        mUnitDiscountAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString().trim())){
                    mUnitDiscountPercentage.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mUnitDiscountPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString().trim())){
                    mUnitDiscountAmount.setText(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObserverActionUtils.removeAction(this);
    }

    @OnClick({R.id.related_tax_items})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.related_tax_items:
                SelectTaxItemDialog selectTaxItemDialog = new SelectTaxItemDialog();
                selectTaxItemDialog.show(getFragmentManager(), "SelectTaxItemDialog");
                break;
        }
    }

    protected void save() {
        String productName = mProductName.getText().toString().trim();
        String unit = mUnit.getText().toString().trim();
        if (TextUtils.isEmpty(productName)) {
            ToastUtil.showShort(getString(R.string.hit6));
            return;
        } else {
            t.setProductName(productName);
        }

        if (TextUtils.isEmpty(unit)) {
            ToastUtil.showShort(getString(R.string.hit7));
            return;
        } else {
            t.setUnit(unit);
        }

        if (TextUtils.isEmpty(t.getRelatedTaxItemString())) {
            ToastUtil.showShort(getString(R.string.hit8));
            return;
        }
        editTextsIntoT();

        if (edit) {
            if (t.update()) {
                dismiss();
                Subscription subscription = ObserverActionUtils.subscribe(t, ProductMgtFragment.class);
                if (subscription != null) subscription.unsubscribe();
            } else {
                ToastUtil.showShort(getString(R.string.hit9));
            }
        } else {
            if (t.save()) {
                dismiss();
                Subscription subscription = ObserverActionUtils.subscribe(t, SelectProductDialog.class);
                Subscription subscription1 = ObserverActionUtils.subscribe(t, ProductMgtFragment.class);
                if (subscription != null) subscription.unsubscribe();
                if (subscription1 != null) subscription1.unsubscribe();
            } else {
                ToastUtil.showShort(getString(R.string.hit10));
            }
        }

    }

    @Override
    public void call(RelatedTaxItem relatedTaxItem) {
        String relatedTaxItemString = JSON.toJSONString(relatedTaxItem);
        t.setRelatedTaxItemString(relatedTaxItemString);
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

}
