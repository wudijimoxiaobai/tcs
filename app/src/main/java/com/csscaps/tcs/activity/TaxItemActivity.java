package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.TaxItemFragment;

public class TaxItemActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tax_item;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        TaxItemFragment approveInvoiceFragment = (TaxItemFragment) getSupportFragmentManager().findFragmentById(R.id.tax_mgt_item);
    }
}
