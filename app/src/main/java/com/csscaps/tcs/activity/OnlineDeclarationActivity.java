package com.csscaps.tcs.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.AppSP;
import com.csscaps.tcs.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tl on 2018/6/25.
 */

public class OnlineDeclarationActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.automatic)
    Switch mAutomatic;
    @BindView(R.id.compulsory)
    Switch mCompulsory;

    @Override
    protected int getLayoutResId() {
        return R.layout.online_declaration_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mAutomatic.setOnCheckedChangeListener(this);
        mCompulsory.setOnCheckedChangeListener(this);
    }


    @OnClick({R.id.back, R.id.synchronize,R.id.declare, R.id.upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.synchronize:
                break;
            case R.id.declare:
                break;
            case R.id.upload:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.automatic:
                AppSP.putBoolean("automatic",b);
                break;
            case R.id.compulsory:
                break;
        }

    }

}
