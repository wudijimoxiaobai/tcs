package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.CancelAndNegativeActivity;
import com.csscaps.tcs.activity.DailyReportActivity;
import com.csscaps.tcs.activity.GoodsServicesReportActivity;
import com.csscaps.tcs.activity.OperatorReportActivity;
import com.csscaps.tcs.activity.PeriodReportActivity;
import com.csscaps.tcs.activity.TaxReportActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class StatisticsFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_statistics;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @OnClick({R.id.period_report, R.id.daily_report, R.id.c_n_i, R.id.tax_report, R.id.goods_s_r, R.id.cc_01})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.period_report:
                startActivity(new Intent(getActivity(), PeriodReportActivity.class));
                break;
            case R.id.daily_report:
                startActivity(new Intent(getActivity(), DailyReportActivity.class));
                break;
            case R.id.c_n_i:
                startActivity(new Intent(getActivity(), CancelAndNegativeActivity.class));
                break;
            case R.id.tax_report:
                startActivity(new Intent(getActivity(), TaxReportActivity.class));
                break;
            case R.id.goods_s_r:
                startActivity(new Intent(getActivity(), GoodsServicesReportActivity.class));
                break;
            case R.id.cc_01:
                startActivity(new Intent(getActivity(), OperatorReportActivity.class));
                break;
        }
    }
}
