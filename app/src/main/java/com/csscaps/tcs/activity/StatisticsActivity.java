package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.StatisticsFragment;

public class StatisticsActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_statistics;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        StatisticsFragment statistics = (StatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.statistics_fragment);
    }
}
