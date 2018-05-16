package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.utils.DeviceUtils;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by tl on 2018/5/14.
 * 添加商品
 */

public class AddProductDialog extends AppCompatDialogFragment implements Action1<RelatedTaxItem> {


    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.save)
    TextView mSave;
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
    @BindView(R.id.remark)
    EditText mRemark;

    private Product mProduct;
    private boolean edit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_product_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.8f);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 0.6f);
        dialogWindow.setLayout(width, -2);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    private void initView() {
        if (mProduct == null) mProduct = new Product();
        ObserverActionUtils.addAction(this);
        if (edit) {
            mProductName.setText(mProduct.getProductName());
            mLocalName.setText(mProduct.getLocalName());
            mUnit.setText(mProduct.getUnit());
            mPrice.setText(mProduct.getPrice() + "");
            mPercentage.setText(mProduct.getPercentage() + "");
            mFixedAmount.setText(mProduct.getFixedAmount() + "");
            mPurchase.setText(mProduct.getPurchase());
            mAdjustment.setText(mProduct.getAdjustment());
            mRemark.setText(mProduct.getRemark());
            String str=  mProduct.getRelatedTaxItemString();
            RelatedTaxItem relatedTaxItem=JSON.parseObject(str,RelatedTaxItem.class);
            call(relatedTaxItem);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObserverActionUtils.removeAction(this);
    }

    @OnClick({R.id.cancel, R.id.save, R.id.related_tax_items})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.save:
                save();
                break;
            case R.id.related_tax_items:
                SelectTaxItemDialog selectTaxItemDialog = new SelectTaxItemDialog();
                selectTaxItemDialog.show(getFragmentManager(), "SelectTaxItemDialog");
                break;
        }
    }

    private void save() {
        String productName = mProductName.getText().toString().trim();
        String unit = mUnit.getText().toString().trim();
        String price = mPrice.getText().toString().trim();
        String fixedAmount = mFixedAmount.getText().toString().trim();
        String percentage = mPercentage.getText().toString().trim();
        String purchase = mPurchase.getText().toString().trim();
        String adjustment = mAdjustment.getText().toString().trim();
        String remark = mRemark.getText().toString().trim();
        if (TextUtils.isEmpty(productName)) {
            ToastUtil.showShort("ProductName不能为空！");
            return;
        } else {
            mProduct.setProductName(productName);
        }

        if (TextUtils.isEmpty(unit)) {
            ToastUtil.showShort("Unit不能为空！");
            return;
        } else {
            mProduct.setUnit(unit);
        }

        if (TextUtils.isEmpty(mProduct.getRelatedTaxItemString())) {
            ToastUtil.showShort("未关联税目！");
            return;
        }

        mProduct.setLocalName(mLocalName.getText().toString().trim());
        mProduct.setPrice(TextUtils.isEmpty(price) ? 0 : Float.valueOf(price));
        mProduct.setPercentage(TextUtils.isEmpty(percentage) ? 0 : Float.valueOf(percentage));
        mProduct.setFixedAmount(TextUtils.isEmpty(fixedAmount) ? 0 : Float.valueOf(price));
        mProduct.setPurchase(purchase);
        mProduct.setAdjustment(adjustment);
        mProduct.setRemark(remark);
        if (edit) {
            if (mProduct.update()) {
                dismiss();
                ObserverActionUtils.subscribe(mProduct, ProductManagementFragment.class);
            } else {
                ToastUtil.showShort("保存失败！");
            }

        } else {
            if (mProduct.save()) {
                dismiss();
                ObserverActionUtils.subscribe(mProduct, ProductManagementFragment.class);
            } else {
                ToastUtil.showShort("保存失败！");
            }
        }

    }


    @Override
    public void call(RelatedTaxItem relatedTaxItem) {
        String relatedTaxItemString = JSON.toJSONString(relatedTaxItem);
        mProduct.setRelatedTaxItemString(relatedTaxItemString);
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


    public void edit(Product product) {
        edit = true;
        this.mProduct = product;
    }
}
