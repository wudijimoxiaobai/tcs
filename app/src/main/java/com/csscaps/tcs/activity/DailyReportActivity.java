package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.DailyReportFragment;
import com.csscaps.tcs.fragment.GoodsServicesReportFragment;

public class DailyReportActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_daily_report;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        DailyReportFragment statistics = (DailyReportFragment) getSupportFragmentManager().findFragmentById(R.id.daily_report_fragment);
    }
}

