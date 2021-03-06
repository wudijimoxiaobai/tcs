package com.csscaps.tcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.User;

import butterknife.BindView;

public class UserDetailsActivity extends BaseDetailsActivity<User> {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.real_name)
    TextView mRealName;
    @BindView(R.id.code)
    TextView mCode;
    @BindView(R.id.role)
    TextView mRole;
    @BindView(R.id.tel)
    TextView mTel;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.status)
    TextView mStatus;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.remarks)
    TextView mRemarks;
    @BindView(R.id.gender)
    TextView mGender;

    private User mUser;


    @Override
    protected int getLayoutResId() {
        return R.layout.user_details_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void parseArgumentsFromIntent(Intent argIntent) {
        mUser = (User) argIntent.getSerializableExtra("user");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tIntoTextView(mUser);
        mGender.setText(mUser.getGender() == 0 ? getString(R.string.male) : getString(R.string.female));
        mRole.setText(mUser.getRole() == 0 ? getString(R.string.admin) : getString(R.string.operator));
        mStatus.setText(mUser.getStatus() == 0 ? getString(R.string.active) : getString(R.string.inactive));
    }
}
