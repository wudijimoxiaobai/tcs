package com.csscaps.tcs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.activity.InvoiceInformationManagementActivity;
import com.csscaps.tcs.activity.InvoiceIssuingActivity;
import com.csscaps.tcs.activity.InvoiceManagementActivity;
import com.csscaps.tcs.activity.SystemManagementActivity;
import com.csscaps.tcs.dialog.SynDataDialog;
import com.csscaps.tcs.service.SynchronizeService;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements Action1<Object> {

    @BindView(R.id.user)
    TextView mUser;
    @BindView(R.id.time_date)
    TextView mTimeDate;

    private String name;
    private SynDataDialog mSynDataDialog = new SynDataDialog();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitPresenters() {
        startService(new Intent(this, SynchronizeService.class).putExtra("autoSyn", true));
    }

    @Override
    protected void parseArgumentsFromIntent(Intent argIntent) {
        name = argIntent.getStringExtra("name");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mUser.setText(name);
        mHandler.sendEmptyMessage(0);
        ObserverActionUtils.addAction(this);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String time = DateUtils.getDateToString_HH_MM_SS_EN(DateUtils.getDateNow());
            String date = DateUtils.getDateToString_YYYY_MM_DD_EN(DateUtils.getDateNow());
            int mWeekday = DateUtils.getDayOfWeek(DateUtils.getDateNow());
            String week = getResources().getStringArray(R.array.weekday)[mWeekday - 1];
            String format = getResources().getString(R.string.date_time);
//            mTimeDate.setText(String.format(format, time, date, week));
            mTimeDate.setText(date + " " + week);
            sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeMessages(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(0);
    }

    @OnClick({R.id.syn, R.id.exit, R.id.invoice_information_management, R.id.invoice_issuing, R.id.invoice_management, R.id.statistics, R.id.declaration, R.id.system_management})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                finish();
                System.exit(0);
                break;
            case R.id.syn:
                startService(new Intent(this, SynchronizeService.class));
                mSynDataDialog.show(getSupportFragmentManager(), "SynDataDialog");
                break;
            case R.id.invoice_issuing:
                startActivity(new Intent(this, InvoiceIssuingActivity.class));
                break;
            case R.id.invoice_management:
                startActivity(new Intent(this, InvoiceManagementActivity.class));
                break;
            case R.id.declaration:
                break;
            case R.id.statistics:
                break;
            case R.id.system_management:
                startActivity(new Intent(this, SystemManagementActivity.class));
                break;
            case R.id.invoice_information_management:
                startActivity(new Intent(this, InvoiceInformationManagementActivity.class));
                break;
        }
    }

    @Override
    public void call(Object o) {
        mSynDataDialog.dismiss();
    }
}
