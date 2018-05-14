package com.csscaps.tcs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.CustomerManagementFragment;
import com.csscaps.tcs.fragment.TaxItemFragment;
import com.csscaps.tcs.fragment.ProductManagementFragment;

import butterknife.BindView;

/**
 * Created by tl on 2018/5/8.
 */

public class SystemConfigActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tab_layout)
    TabLayout mTableLayout;

    private TaxItemFragment mInvoiceItemFragment;
    private CustomerManagementFragment mCustomerManagementFragment;
    private ProductManagementFragment mProductManagementFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.system_config_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mInvoiceItemFragment = new TaxItemFragment();
        mCustomerManagementFragment = new CustomerManagementFragment();
        mProductManagementFragment = new ProductManagementFragment();
        String tab[] = getResources().getStringArray(R.array.tab1);
        for (int i = 0; i < tab.length; i++) {
            mTableLayout.addTab(mTableLayout.newTab().setText(tab[i]));
        }
        mTableLayout.addOnTabSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content,mProductManagementFragment);
        transaction.add(R.id.content,mCustomerManagementFragment);
        transaction.add(R.id.content,mInvoiceItemFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (tab.getPosition()) {
            case 0:
                transaction.hide(mCustomerManagementFragment);
                transaction.hide(mProductManagementFragment);
                transaction.show(mInvoiceItemFragment);
                break;
            case 1:
                transaction.show(mCustomerManagementFragment);
                transaction.hide(mProductManagementFragment);
                transaction.hide(mInvoiceItemFragment);
                break;
            case 2:
                transaction.hide(mCustomerManagementFragment);
                transaction.show(mProductManagementFragment);
                transaction.hide(mInvoiceItemFragment);
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
