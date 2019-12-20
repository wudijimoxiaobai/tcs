package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.InvoiceApplicationFragment;

public class InvoiceApplicationActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invoice_application;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        InvoiceApplicationFragment approveInvoiceFragment = (InvoiceApplicationFragment) getSupportFragmentManager().findFragmentById(R.id.invoice_app_fragment);
    }
}
