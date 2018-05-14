package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tl on 2018/5/14.
 * 添加商品
 */

public class AddProductDialog extends AppCompatDialogFragment {


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
        int height= (int) (DeviceUtils.getScreenHeight(getContext())*0.8f);
//        dialogWindow.setLayout(-1, -2);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    private void initView() {


    }




    @OnClick({R.id.cancel, R.id.save, R.id.related_tax_items})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.save:
                break;
            case R.id.related_tax_items:
                SelectTaxItemDialog selectTaxItemDialog=new SelectTaxItemDialog();
                selectTaxItemDialog.show(getFragmentManager(),"SelectTaxItemDialog");
                break;
        }
    }
}
