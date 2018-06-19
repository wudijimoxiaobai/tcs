package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.ApproveInvoiceFragment;

/**
 * Created by tl on 2018/6/19.
 */

public class ApplicationListActivity extends BaseActivity{
    @Override
    protected int getLayoutResId() {
        return R.layout.application_list_activity;
    }

    @Override
    protected void onInitPresenters() {}

    @Override
    public void initView(Bundle savedInstanceState) {
        ApproveInvoiceFragment approveInvoiceFragment= (ApproveInvoiceFragment) getSupportFragmentManager().findFragmentById(R.id.application_fragment);
        approveInvoiceFragment.setProcess(false);
    }
}
