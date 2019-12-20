package com.csscaps.tcs.activity;

import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.UserManagementFragment;

public class UserManagerActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_manager;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        UserManagementFragment approveInvoiceFragment = (UserManagementFragment) getSupportFragmentManager().findFragmentById(R.id.user_manager_fragment);
    }
}
