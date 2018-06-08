package com.csscaps.tcs.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.model.SearchUserCondition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tl on 2018/6/6.
 */

@SuppressLint("ValidFragment")
public class SearchUserDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.user_role_spinner)
    AppCompatSpinner mUserRoleSpinner;
    @BindView(R.id.user_status_spinner)
    AppCompatSpinner mUserStatusSpinner;

    private Handler mHandler;
    private SearchUserCondition mSearchUserCondition = new SearchUserCondition();

    public SearchUserDialog(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_user_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = DeviceUtils.dip2Px(getContext(), 570);
        dialogWindow.setLayout(width, -2);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    private void initView() {
        int offset = DeviceUtils.dip2Px(getContext(), 50);
        mUserRoleSpinner.setDropDownVerticalOffset(offset);
        mUserRoleSpinner.setOnItemSelectedListener(this);
        mUserStatusSpinner.setDropDownVerticalOffset(offset);
        mUserStatusSpinner.setOnItemSelectedListener(this);
    }

    @OnClick({R.id.cancel, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                dismiss();
                mSearchUserCondition.setUserName(mName.getText().toString().trim());
                mHandler.sendMessage(mHandler.obtainMessage(0, mSearchUserCondition));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.user_role_spinner:
                mSearchUserCondition.setRole(i - 1);
                break;
            case R.id.user_status_spinner:
                mSearchUserCondition.setStatus(i - 1);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
