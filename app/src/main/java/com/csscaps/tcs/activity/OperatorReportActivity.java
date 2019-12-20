package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.OperatorReportFragment;
import com.csscaps.tcs.fragment.PeriodReportFragment;

public class OperatorReportActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_operator_report;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        OperatorReportFragment periodreport = (OperatorReportFragment) getSupportFragmentManager().findFragmentById(R.id.operator_report_fragment);
    }
}

