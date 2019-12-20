package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.CancellationAndNegativeInvoiceReportFragment;
import com.csscaps.tcs.fragment.TaxReportFragment;

public class TaxReportActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tax_report;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        TaxReportFragment statistics = (TaxReportFragment) getSupportFragmentManager().findFragmentById(R.id.tax_report_fragment);
    }
}