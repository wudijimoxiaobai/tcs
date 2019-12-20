package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.NewInvoiceActivity;
import com.csscaps.tcs.database.table.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListItemDialog extends DialogFragment {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.product_name)
    TextView mProductName;
    @BindView(R.id.specification)
    TextView mSpecification;
    @BindView(R.id.unit)
    TextView mUnit;
    @BindView(R.id.quantity)
    TextView mQuantity;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.e_tax)
    TextView mETax;
    @BindView(R.id.tax_Amount)
    TextView mTaxAmount;
    @BindView(R.id.i_tax)
    TextView mITax;

    private int mPosition;
    private List<Product> products = new ArrayList<>();

    public ProductListItemDialog(List<Product> products,int position) {
        mPosition = position;
        this.products = products;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_p_l_i, null);
        ButterKnife.bind(this, view);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initView();
        return view;
    }

    private void initView() {
        mProductName.setText(products.get(mPosition).getProductName());
        mSpecification.setText(products.get(mPosition).getSpecification());
        mUnit.setText(products.get(mPosition).getUnit());
        mQuantity.setText(products.get(mPosition).getQuantity());
        mPrice.setText(products.get(mPosition).getPrice());
        mETax.setText(products.get(mPosition).geteTax());
        mTaxAmount.setText(products.get(mPosition).getTotalTax());
        mITax.setText(products.get(mPosition).getiTax());
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 1f);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 1f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }


}
