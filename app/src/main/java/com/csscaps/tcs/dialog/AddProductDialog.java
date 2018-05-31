package com.csscaps.tcs.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxType;
import com.csscaps.tcs.fragment.ProductManagementFragment;
import com.csscaps.tcs.model.RelatedTaxItem;

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
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.commission)
    EditText mCommission;
    @BindView(R.id.specification)
    EditText mSpecification;

    @Override
    protected int getLayoutId() {
        return R.layout.add_product_dialog;
    }

    protected void initView() {
        if (t == null) t = new Product();
        ObserverActionUtils.addAction(this);
        if (edit) {
            mTitle.setText(getString(R.string.edit_product));
            tIntoEditText();
            String str = t.getRelatedTaxItemString();
            RelatedTaxItem relatedTaxItem = JSON.parseObject(str, RelatedTaxItem.class);
            call(relatedTaxItem);
        }

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
            ToastUtil.showShort("ProductName不能为空！");
            return;
        } else {
            t.setProductName(productName);
        }

        if (TextUtils.isEmpty(unit)) {
            ToastUtil.showShort("Unit不能为空！");
            return;
        } else {
            t.setUnit(unit);
        }

        if (TextUtils.isEmpty(t.getRelatedTaxItemString())) {
            ToastUtil.showShort("未关联税目！");
            return;
        }
      editTextsIntoT();

        if (edit) {
            if (t.update()) {
                dismiss();
                Subscription subscription =ObserverActionUtils.subscribe(t, ProductManagementFragment.class);
                if(subscription!=null)subscription.unsubscribe();
            } else {
                ToastUtil.showShort("编辑失败！");
            }

        } else {
            if (t.save()) {
                dismiss();
                Subscription subscription =ObserverActionUtils.subscribe(t,SelectProductDialog.class);
                Subscription subscription1 =ObserverActionUtils.subscribe(t, ProductManagementFragment.class);
                if(subscription!=null)subscription.unsubscribe();
                if(subscription1!=null)subscription1.unsubscribe();
            } else {
                ToastUtil.showShort("保存失败！");
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
