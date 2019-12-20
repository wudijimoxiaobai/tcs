package com.csscaps.tcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.AppSP;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.database.table.ControlData;
import com.csscaps.tcs.dialog.OnlineDeclarationAdapter;
import com.csscaps.tcs.service.ReportDataService;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class OnlineDeclarationActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.male)
    RadioButton mMale;
    @BindView(R.id.female)
    RadioButton mFemale;
    @BindView(R.id.gender_group)
    RadioGroup mGenderGroup;

    @Override
    protected int getLayoutResId() {
        return R.layout.online_declaration_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mGenderGroup.setOnCheckedChangeListener(this);
        List<ControlData> listControlData = select().from(ControlData.class).queryList();
        OnlineDeclarationAdapter onlineDeclarationAdapter = new OnlineDeclarationAdapter(this, R.layout.online_declartion_list_item, listControlData);
        mListView.setAdapter(onlineDeclarationAdapter);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.declare})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.declare:
                startService(new Intent(TCSApplication.getAppContext(), ReportDataService.class).putExtra("fromOnlineDeclarationActivity", true));
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (R.id.male == i) {
            AppSP.putBoolean("automatic", true);
        } else AppSP.putBoolean("automatic", false);
    }
}

