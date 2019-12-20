package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.ApproveInvoiceFragment;
import com.csscaps.tcs.fragment.InvoiceQueryFragment;

public class ApproveInvoiceActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_approce_invoice;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ApproveInvoiceFragment approveInvoiceFragment = (ApproveInvoiceFragment) getSupportFragmentManager().findFragmentById(R.id.approve_invoice_fragment);
        approveInvoiceFragment.setProcess(false);
    }
}
