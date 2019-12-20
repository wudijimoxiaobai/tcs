package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.InvoiceQueryFragment;

public class InvoiceQueryActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invoice_query;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        InvoiceQueryFragment approveInvoiceFragment = (InvoiceQueryFragment) getSupportFragmentManager().findFragmentById(R.id.invoice_query_fragment);
    }
}
