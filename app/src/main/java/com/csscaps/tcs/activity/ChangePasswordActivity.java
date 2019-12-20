package com.csscaps.tcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.old_password)
    EditText mOldPassword;
    @BindView(R.id.new_password)
    EditText mNewPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.reset_password)
    TextView mResetPassword;


    private String oldPwd;

    @Override
    protected int getLayoutResId() {
        return R.layout.change_password_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void parseArgumentsFromIntent(Intent argIntent) {
        oldPwd = argIntent.getStringExtra("pwd");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (TCSApplication.currentUser.getRole() == 1) {
            mResetPassword.setVisibility(View.GONE);
        } else {

        }

    }


    @OnClick({R.id.save, R.id.reset_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                String oldPassword = mOldPassword.getText().toString().trim();
                String newPassword = mNewPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();
                if (TextUtils.isEmpty(oldPassword)) {
                    ToastUtil.showShort(getString(R.string.hit32));
                    return;
                } else {
                    if (!oldPwd.equals(oldPassword)) {
                        ToastUtil.showShort(getString(R.string.hit27));
                        return;
                    }
                }

                if (TextUtils.isEmpty(newPassword)) {
                    ToastUtil.showShort(getString(R.string.hit33));
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    ToastUtil.showShort(getString(R.string.hit34));
                    return;
                } else {
                    if (!confirmPassword.equals(newPassword)) {
                        ToastUtil.showShort(getString(R.string.hit28));
                        return;
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("pwd", newPassword);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.reset_password:
                Intent intent1 = new Intent();
                intent1.putExtra("pwd", "12345678");
                setResult(RESULT_OK, intent1);
                finish();
                break;
        }
    }


}