package com.csscaps.tcs.dialog;

import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.database.table.User;
import com.csscaps.tcs.database.table.User_Table;
import com.csscaps.tcs.fragment.UserManagementFragment;

import butterknife.BindView;
import rx.Subscription;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class AddUserDialog extends BaseAddDialog<User> implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.real_name)
    EditText mRealName;
    @BindView(R.id.code)
    EditText mCode;
    @BindView(R.id.male)
    RadioButton mMale;
    @BindView(R.id.female)
    RadioButton mFemale;
    @BindView(R.id.gender_group)
    RadioGroup mGenderGroup;
    @BindView(R.id.role_spinner)
    AppCompatSpinner mRoleSpinner;
    @BindView(R.id.tel)
    EditText mTel;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.yes)
    RadioButton mYes;
    @BindView(R.id.no)
    RadioButton mNo;
    @BindView(R.id.status_group)
    RadioGroup mStatusGroup;
    @BindView(R.id.address)
    EditText mAddress;
    @BindView(R.id.remarks)
    EditText mRemarks;

    @Override
    protected int getLayoutId() {
        return R.layout.add_user_dialog;
    }

    @Override
    protected void initView() {
        int offset = DeviceUtils.dip2Px(getContext(), 50);
        mRoleSpinner.setDropDownVerticalOffset(offset);
        if (t == null) t = new User();
        if (edit) {
            if (t.getId() == 1) {
                mUserName.setEnabled(false);
                mRoleSpinner.setEnabled(false);
                mNo.setVisibility(View.GONE);
            }
            if (t.getId() == TCSApplication.currentUser.getId()) {
                mRoleSpinner.setEnabled(false);
                mYes.setEnabled(false);
                mNo.setEnabled(false);
            }

            //  mTitle.setText(getString(R.string.edit_user));
            tIntoEditText();
            mRoleSpinner.setSelection(t.getRole());
            switch (t.getGender()) {
                case 0:
                    mMale.setChecked(true);
                    break;
                case 1:
                    mFemale.setChecked(true);
                    break;
            }

            switch (t.getStatus()) {
                case 0:
                    mYes.setChecked(true);
                    break;
                case 1:
                    mNo.setChecked(true);
                    break;
            }
        }
        mRoleSpinner.setOnItemSelectedListener(this);
        mGenderGroup.setOnCheckedChangeListener(this);
        mStatusGroup.setOnCheckedChangeListener(this);

    }

    @Override
    protected void save() {
        if (TextUtils.isEmpty(mUserName.getText().toString().trim())) {
            ToastUtil.showShort(getString(R.string.hit30));
            return;
        }
        if (TextUtils.isEmpty(mTel.getText().toString().trim())) {
            ToastUtil.showShort(getString(R.string.hit31));
            return;
        }
        editTextsIntoT();
        User user = select().from(User.class).where(User_Table.userName.eq(t.getUserName())).querySingle();
        if (edit) {
            if (user != null && t.getId() != user.getId()) {
                ToastUtil.showShort(getString(R.string.hit37));
                return;
            }
            if (t.update()) {
                if (TCSApplication.currentUser.getId() == t.getId()) {
                    TCSApplication.currentUser = t;
                }
                dismiss();
                Subscription subscription = ObserverActionUtils.subscribe(t, UserManagementFragment.class);
                if (subscription != null) subscription.unsubscribe();
            } else {
                ToastUtil.showShort(getString(R.string.hit9));
            }

        } else {
            if (user != null) {
                ToastUtil.showShort(getString(R.string.hit37));
                return;
            }
            if (t.save()) {
                dismiss();
                Subscription subscription = ObserverActionUtils.subscribe(t, UserManagementFragment.class);
                subscription.unsubscribe();
            } else {
                ToastUtil.showShort(getString(R.string.hit10));
            }

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        t.setRole(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        if (radioGroup == mGenderGroup) {
            if (R.id.male == i) {
                t.setGender(0);
            } else t.setGender(1);

        } else if (radioGroup == mStatusGroup) {
            if (R.id.yes == i) {
                t.setStatus(0);
            } else t.setStatus(1);
        }
    }
}
