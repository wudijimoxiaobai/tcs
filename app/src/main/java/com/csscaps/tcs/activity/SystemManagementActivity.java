package com.csscaps.tcs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.NetworkConfigurationFragment;
import com.csscaps.tcs.fragment.UserManagementFragment;

import butterknife.BindView;

/**
 * Created by tl on 2018/6/1.
 */

public class SystemManagementActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tab_layout)
    TabLayout mTableLayout;

    private NetworkConfigurationFragment mNetworkConfigurationFragment;
    private UserManagementFragment mUserManagementFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.system_management_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mNetworkConfigurationFragment = new NetworkConfigurationFragment();
        mUserManagementFragment = new UserManagementFragment();
        String tab[] = getResources().getStringArray(R.array.tab3);
        for (int i = 0; i < tab.length; i++) {
            mTableLayout.addTab(mTableLayout.newTab().setText(tab[i]));
        }
        mTableLayout.addOnTabSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, mUserManagementFragment);
//        transaction.add(R.id.content, mNetworkConfigurationFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (tab.getPosition()) {
            case 0:
                transaction.hide(mUserManagementFragment);
                transaction.show(mNetworkConfigurationFragment);
                break;
            case 1:
                transaction.hide(mNetworkConfigurationFragment);
                transaction.show(mUserManagementFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
