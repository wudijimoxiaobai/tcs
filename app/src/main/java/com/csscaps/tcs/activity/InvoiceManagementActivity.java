package com.csscaps.tcs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.ApproveInvoiceFragment;
import com.csscaps.tcs.fragment.InvoiceApplicationFragment;
import com.csscaps.tcs.fragment.InvoiceQueryFragment;
import com.csscaps.tcs.fragment.RequestInvoiceFragment;

import butterknife.BindView;

/**
 * Created by tl on 2018/5/22.
 */

public class InvoiceManagementActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tab_layout)
    TabLayout mTableLayout;

    private InvoiceApplicationFragment mInvoiceApplicationFragment;
    private RequestInvoiceFragment mRequestInvoiceFragment;
    private ApproveInvoiceFragment mApproveInvoiceFragment;
    private InvoiceQueryFragment mInvoiceQueryFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.invoice_management_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mInvoiceApplicationFragment=new InvoiceApplicationFragment();
        mRequestInvoiceFragment=new RequestInvoiceFragment();
        mApproveInvoiceFragment=new ApproveInvoiceFragment();
        mInvoiceQueryFragment=new InvoiceQueryFragment();
        String tab[] = getResources().getStringArray(R.array.tab2);
        for (int i = 0; i < tab.length; i++) {
            mTableLayout.addTab(mTableLayout.newTab().setText(tab[i]));
        }
        mTableLayout.addOnTabSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content,mRequestInvoiceFragment);
        transaction.add(R.id.content,mApproveInvoiceFragment);
        transaction.add(R.id.content,mInvoiceQueryFragment);
        transaction.add(R.id.content,mInvoiceApplicationFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (tab.getPosition()) {
            case 0:
                transaction.hide(mRequestInvoiceFragment);
                transaction.hide(mApproveInvoiceFragment);
                transaction.hide(mInvoiceQueryFragment);
                transaction.show(mInvoiceApplicationFragment);
                break;
            case 1:
                transaction.hide(mApproveInvoiceFragment);
                transaction.hide(mInvoiceQueryFragment);
                transaction.hide(mInvoiceApplicationFragment);
                transaction.show(mRequestInvoiceFragment);
                break;
            case 2:
                transaction.hide(mInvoiceQueryFragment);
                transaction.hide(mInvoiceApplicationFragment);
                transaction.hide(mRequestInvoiceFragment);
                transaction.show(mApproveInvoiceFragment);
                break;
            case 3:
                transaction.hide(mInvoiceApplicationFragment);
                transaction.hide(mRequestInvoiceFragment);
                transaction.hide(mApproveInvoiceFragment);
                transaction.show(mInvoiceQueryFragment);
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
