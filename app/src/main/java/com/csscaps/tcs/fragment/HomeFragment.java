package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.ApproveInvoiceActivity;
import com.csscaps.tcs.activity.InvoiceApplicationActivity;
import com.csscaps.tcs.activity.InvoiceQueryActivity;
import com.csscaps.tcs.activity.NewInvoiceActivity;
import com.csscaps.tcs.activity.StatisticsActivity;
import com.csscaps.tcs.dialog.RequestInvoiceActivity;
import com.csscaps.tcs.dialog.SynDataDialog;
import com.csscaps.tcs.service.SynchronizeService;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class HomeFragment extends BaseFragment {
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.home_request_invoice, R.id.home_statistics, R.id.home_invoice_application, R.id.home_approve_invoice, R.id.home_invoice_query, R.id.home_new_invoice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_request_invoice:
                startActivity(new Intent(getActivity(), RequestInvoiceActivity.class));
                break;
            case R.id.home_statistics:
                startActivity(new Intent(getActivity(), StatisticsActivity.class));
                break;
            case R.id.home_invoice_application:
                startActivity(new Intent(getActivity(), InvoiceApplicationActivity.class));
                break;
            case R.id.home_approve_invoice:
                startActivity(new Intent(getActivity(), ApproveInvoiceActivity.class));
                break;
            case R.id.home_invoice_query:
                startActivity(new Intent(getActivity(), InvoiceQueryActivity.class));
                break;
            case R.id.home_new_invoice:
                startActivity(new Intent(getActivity(), NewInvoiceActivity.class));
                break;
        }
    }
}
