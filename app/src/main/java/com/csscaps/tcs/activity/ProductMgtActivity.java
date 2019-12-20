package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.ProductMgtFragment;

public class ProductMgtActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_mgt;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ProductMgtFragment approveInvoiceFragment = (ProductMgtFragment) getSupportFragmentManager().findFragmentById(R.id.product_fragment);
    }

}
