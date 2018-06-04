package com.csscaps.tcs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tl on 2018/6/4.
 */

public class ChangePasswordActivity extends BaseActivity {
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
        if (TCSApplication.currentUser.getRole() == 1) {
            mResetPassword.setVisibility(View.GONE);
        } else {

        }

    }


    @OnClick({R.id.back, R.id.save, R.id.reset_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                String oldPassword = mOldPassword.getText().toString().trim();
                String newPassword = mNewPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();
                if (TextUtils.isEmpty(oldPassword)) {
                    ToastUtil.showShort("Old Password 不能为空！");
                    return;
                } else {
                    if (!oldPwd.equals(oldPassword)) {
                        ToastUtil.showShort("Old Password 输入有误！");
                        return;
                    }
                }

                if (TextUtils.isEmpty(newPassword)) {
                    ToastUtil.showShort("New Password 不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    ToastUtil.showShort("Confirm Password 不能为空！");
                    return;
                } else {
                    if (!confirmPassword.equals(newPassword)) {
                        ToastUtil.showShort("Confirm Password 和 New Password 不一致！");
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