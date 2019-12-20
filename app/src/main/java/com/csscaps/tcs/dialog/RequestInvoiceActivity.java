package com.csscaps.tcs.dialog;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.InvoiceApplicationFragment;
import com.csscaps.tcs.fragment.RequestInvoiceFragment;

public class RequestInvoiceActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_request_invoice;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        RequestInvoiceFragment approveInvoiceFragment = (RequestInvoiceFragment) getSupportFragmentManager().findFragmentById(R.id.request_invoice_fragment);
    }
}
