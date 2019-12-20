package com.csscaps.tcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Customer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tl on 2018/5/21.
 */

public class CustomerDetailsActivity extends BaseDetailsActivity<Customer> {

    @BindView(R.id.registered)
    TextView mRegistered;
    @BindView(R.id.tin)
    TextView mTin;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.national_id)
    TextView mNationalId;
    @BindView(R.id.passport)
    TextView mPassport;
    @BindView(R.id.tel)
    TextView mTel;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.city)
    TextView mCity;
    @BindView(R.id.state)
    TextView mState;
    @BindView(R.id.remarks)
    TextView mRemarks;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Customer mCustomer;

    @Override
    protected int getLayoutResId() {
        return R.layout.customer_details_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void parseArgumentsFromIntent(Intent argIntent) {
        mCustomer = (Customer) argIntent.getSerializableExtra("customer");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tIntoTextView(mCustomer);
        if (mCustomer.isRegistered()) {
            mRegistered.setText(getString(R.string.registered));
        } else {
            mRegistered.setText(getString(R.string.unregistered));
        }

    }
}
