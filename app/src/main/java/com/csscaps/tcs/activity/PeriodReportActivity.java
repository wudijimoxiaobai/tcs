package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.PeriodReportFragment;
import com.csscaps.tcs.fragment.StatisticsFragment;

public class PeriodReportActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_period_report;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        PeriodReportFragment periodreport = (PeriodReportFragment) getSupportFragmentManager().findFragmentById(R.id.period_report_fragment);
    }
}
