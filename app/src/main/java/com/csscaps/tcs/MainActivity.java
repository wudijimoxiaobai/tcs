package com.csscaps.tcs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.activity.SystemConfigActivity;
import com.csscaps.tcs.service.SynchronizeService;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.user)
    TextView mUser;
    @BindView(R.id.time_date)
    TextView mTimeDate;

    private String name;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void parseArgumentsFromIntent(Intent argIntent) {
        name = argIntent.getStringExtra("name");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String currUser = getResources().getString(R.string.curr_user);
        mUser.setText(String.format(currUser, name));
        mHandler.sendEmptyMessage(0);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String time = DateUtils.getDateToString_HH_MM_SS_EN(DateUtils.getDateNow());
            String date = DateUtils.getDateToString_YYYY_MM_DD_EN(DateUtils.getDateNow());
            int mWeekday = DateUtils.getDayOfWeek(DateUtils.getDateNow());
            String week = getResources().getStringArray(R.array.weekday)[mWeekday - 1];
            String format = getResources().getString(R.string.date_time);
            mTimeDate.setText(String.format(format, time, date, week));
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

    @OnClick({R.id.syn,R.id.exit, R.id.invoice_issuing, R.id.invoice_management, R.id.invoice_query, R.id.system_configuration})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                finish();
                System.exit(0);
                break;
            case R.id.syn:
                startService(new Intent(this, SynchronizeService.class));
                break;
            case R.id.invoice_issuing:

                break;
            case R.id.invoice_management:
                break;
            case R.id.invoice_query:
                break;
            case R.id.system_configuration:
                startActivity(new Intent(this, SystemConfigActivity.class));
                break;

        }
    }




    @OnClick(R.id.syn)
    public void onClick() {
    }
}
