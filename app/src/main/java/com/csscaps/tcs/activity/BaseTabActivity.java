package com.csscaps.tcs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;

import java.util.List;

import butterknife.BindView;

/**
 * Created by tl on 2018/6/8.
 */

public abstract class  BaseTabActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R.id.tab_layout)
    TabLayout mTableLayout;

    private Fragment mFragments[];
    private Fragment showFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.tab_activity;
    }

    @Override
    protected void onInitPresenters() {}

    @Override
    public void initView(Bundle savedInstanceState) {
        mFragments=getFragments();
        String tabTitle[]=getResources().getStringArray(getTabTitleResourcesId());
        for (int i = 0; i < tabTitle.length; i++) {
            mTableLayout.addTab(mTableLayout.newTab().setText(tabTitle[i]));
        }
        mTableLayout.addOnTabSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content,mFragments[0]);
        transaction.commit();
        showFragment=mFragments[0];
    }


    protected abstract Fragment[] getFragments();

    protected abstract int getTabTitleResourcesId();


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> listFragment= fragmentManager.getFragments();
        Fragment fragment=mFragments[tab.getPosition()];
        transaction.hide(showFragment);
        if(listFragment.contains(fragment)){
            transaction.show(fragment);
        }else{
            transaction.add(R.id.content,fragment);
        }
        showFragment=fragment;
        transaction.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
