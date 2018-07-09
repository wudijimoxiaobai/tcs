package com.csscaps.tcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.AppSP;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.adapter.OnlineDeclarationAdapter;
import com.csscaps.tcs.database.table.ControlData;
import com.csscaps.tcs.service.ReportDataService;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

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
        List<ControlData> listControlData = select().from(ControlData.class).queryList();
        OnlineDeclarationAdapter onlineDeclarationAdapter=new OnlineDeclarationAdapter(this,R.layout.online_declartion_list_item,listControlData);
        mListView.setAdapter(onlineDeclarationAdapter);
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
                startService(new Intent(TCSApplication.getAppContext(), ReportDataService.class).putExtra("fromOnlineDeclarationActivity",true));
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
