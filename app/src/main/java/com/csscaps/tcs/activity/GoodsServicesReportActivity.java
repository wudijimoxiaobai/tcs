package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.CancellationAndNegativeInvoiceReportFragment;
import com.csscaps.tcs.fragment.GoodsServicesReportFragment;

public class GoodsServicesReportActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_services;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        GoodsServicesReportFragment statistics = (GoodsServicesReportFragment) getSupportFragmentManager().findFragmentById(R.id.goods_services_fragment);
    }
}
