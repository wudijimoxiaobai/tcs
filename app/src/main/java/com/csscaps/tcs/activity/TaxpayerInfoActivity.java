package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.InvoiceQueryFragment;
import com.csscaps.tcs.fragment.TaxpayerInfoFragment;

public class TaxpayerInfoActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tax_payer;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        TaxpayerInfoFragment approveInvoiceFragment = (TaxpayerInfoFragment) getSupportFragmentManager().findFragmentById(R.id.tax_payer_fragment);
    }
}
