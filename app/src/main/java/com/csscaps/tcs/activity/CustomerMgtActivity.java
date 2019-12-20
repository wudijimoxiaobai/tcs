package com.csscaps.tcs.activity;

import android.os.Bundle;
import android.view.View;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.CustomerMgtFragment;

import butterknife.OnClick;

public class CustomerMgtActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_customer;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        CustomerMgtFragment approveInvoiceFragment = (CustomerMgtFragment) getSupportFragmentManager().findFragmentById(R.id.customer_fragment);
    }
}
