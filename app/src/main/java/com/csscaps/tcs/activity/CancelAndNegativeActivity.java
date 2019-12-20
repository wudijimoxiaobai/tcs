package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.CancellationAndNegativeInvoiceReportFragment;
import com.csscaps.tcs.fragment.StatisticsFragment;

public class CancelAndNegativeActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cancel_negative;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        CancellationAndNegativeInvoiceReportFragment statistics = (CancellationAndNegativeInvoiceReportFragment) getSupportFragmentManager().findFragmentById(R.id.cancel_negative_fragment);
    }
}
