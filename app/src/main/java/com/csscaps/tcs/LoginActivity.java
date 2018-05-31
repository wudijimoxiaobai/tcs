package com.csscaps.tcs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.database.table.User;
import com.csscaps.tcs.database.table.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tl on 2018/5/4.
 */

public class LoginActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.user)
    EditText mUser;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.spinner)
    AppCompatSpinner mSpinner;
    @BindView(R.id.login)
    TextView mLogin;


    @Override
    protected int getLayoutResId() {
        return R.layout.login_activity;
    }

    @Override
    protected void onInitPresenters() {}

    @Override
    public void initView(Bundle savedInstanceState) {
        mSpinner.setDropDownVerticalOffset(DeviceUtils.dip2Px(this, 50));
        mSpinner.setSelection(AppSP.getInt("language", 0));
        mSpinner.setOnItemSelectedListener(this);
    }


    @OnClick(R.id.login)
    public void onClick() {
        mUser.setText("admin");
        mPassword.setText("123456");
        String name=mUser.getText().toString().trim();
        String password=mPassword.getText().toString().trim();
        User user = SQLite.select().from(User.class).where(User_Table.name.eq(name)).and(User_Table.password.eq(password)).querySingle();
        if (user != null) {
            AppSP.putString("user", JSON.toJSONString(user));
            Intent intent=new Intent(this, MainActivity.class);
            intent.putExtra("name",name);
            startActivity(intent);
            finish();
        } else {
            ToastUtil.showShort("用户名或密码错误！");
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int p = AppSP.getInt("language", 0);
        if (i != p) {
            AppSP.putInt("language", i);
            String str[] = getResources().getStringArray(R.array.language_no);
            AppTools.changeAppLanguage(this, str[i]);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
