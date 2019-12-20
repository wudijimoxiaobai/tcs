package com.csscaps.tcs.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.LoginActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.activity.NotworkConfigActivity;
import com.csscaps.tcs.activity.TaxpayerInfoActivity;
import com.csscaps.tcs.activity.UserDetailsActivity;
import com.csscaps.tcs.activity.UserManagerActivity;
import com.csscaps.tcs.database.table.User;
import com.csscaps.tcs.database.table.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class MeFragment extends BaseFragment {

    @BindView(R.id.user_name)
    TextView mUserName;
    String UserName;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_me;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_name", Context.MODE_PRIVATE);
        UserName = sharedPreferences.getString("name", "");
        mUserName.setText(UserName);
    }

    @OnClick({R.id.me_network_config, R.id.me_about, R.id.me_user, R.id.me_help, R.id.me_taxpayer_info, R.id.me_login, R.id.me})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.me_network_config:
                startActivity(new Intent(getActivity(), NotworkConfigActivity.class));
                break;
            case R.id.me_about:
                break;
            case R.id.me_user:
                startActivity(new Intent(getActivity(), UserManagerActivity.class));
                break;
            case R.id.me_help:
                break;
            case R.id.me_taxpayer_info:
                startActivity(new Intent(getActivity(), TaxpayerInfoActivity.class));
                break;
            case R.id.me_login:
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                break;
            case R.id.me:
                User user = SQLite.select().from(User.class).where(User_Table.userName.eq(UserName)).querySingle();
                Intent intent = new Intent(mContext, UserDetailsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {

        return true;
    }
}
