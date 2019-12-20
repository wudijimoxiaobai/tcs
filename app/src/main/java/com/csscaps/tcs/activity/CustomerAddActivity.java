package com.csscaps.tcs.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Customer;
import com.csscaps.tcs.database.table.Taxpayer;
import com.csscaps.tcs.database.table.Taxpayer_Table;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class CustomerAddActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.registered)
    RadioButton mRegistered;
    @BindView(R.id.unregistered)
    RadioButton mUnregistered;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.tin)
    EditText mTin;
    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.national_id)
    EditText mNationalId;
    @BindView(R.id.passport)
    EditText mPassport;
    @BindView(R.id.tel)
    EditText mTel;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.address)
    EditText mAddress;
    @BindView(R.id.city)
    EditText mCity;
    @BindView(R.id.state)
    EditText mState;
    @BindView(R.id.remarks)
    EditText mRemarks;
    @BindView(R.id.tv1)
    TextView mTv1;

    private Customer t;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_customer_add;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initdata();
    }

    private void initdata() {

        if (t == null) {
            t = new Customer();
        }
        mTin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
                String tin = mTin.getText().toString().trim();
                verify(tin);
//                }
                return false;
            }
        });
    }

    private boolean verify(String tin) {
        try {
            boolean initData = AppSP.getBoolean("initData", false);
            if (initData) {
                Taxpayer taxpayer = select().from(Taxpayer.class).where(Taxpayer_Table.tin.eq(tin)).querySingle();
                if (taxpayer != null) {
                    mName.setText(taxpayer.getEname());
                    mTel.setText(taxpayer.getTel());
                    mAddress.setText(taxpayer.getAddress());
                    mCity.setText(taxpayer.getCity());
                    mState.setText(taxpayer.getState());
                    t.setRegistered(true);
                    return true;
                } else {
                    ToastUtil.showShort(getString(R.string.hit13));
                }
            } else {
                ToastUtil.showShort(getString(R.string.hit14));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.registered:
                mTv1.setVisibility(View.VISIBLE);
                mTin.setVisibility(View.VISIBLE);
                t.setRegistered(true);
                setEditTextEnable(false);
                break;
            case R.id.unregistered:
                mTv1.setVisibility(View.GONE);
                mTin.setVisibility(View.GONE);
                t.setRegistered(false);
                setEditTextEnable(true);
                break;
        }
    }

    private void setEditTextEnable(boolean enabled) {
        mName.setEnabled(enabled);
        mNationalId.setEnabled(enabled);
        mPassport.setEnabled(enabled);
        mEmail.setEnabled(enabled);
        mAddress.setEnabled(enabled);
        mCity.setEnabled(enabled);
        mState.setEnabled(enabled);
        mRemarks.setEnabled(enabled);
    }

    @OnClick(R.id.save)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save:
                save();

                break;
        }
    }

    private void save() {
        if (t.isRegistered() && TextUtils.isEmpty(mTin.getText().toString().trim())) {
            ToastUtil.showShort(getString(R.string.hit15));
            return;
        }
        String tin = mTin.getText().toString().trim();
        Taxpayer taxpayer = select().from(Taxpayer.class).where(Taxpayer_Table.tin.eq(tin)).querySingle();
        if (t.isRegistered() && taxpayer == null) {
            ToastUtil.showShort(getString(R.string.hit16));
            return;
        }

        if (t.isRegistered() && TextUtils.isEmpty(t.getName())) {
            if (!verify(tin)) return;
        }

        if (!t.isRegistered() && TextUtils.isEmpty(t.getName())) {
            ToastUtil.showShort(getString(R.string.hit17));
            return;
        }
        if (!t.isRegistered() && TextUtils.isEmpty(t.getNationalId()) && TextUtils.isEmpty(t.getPassport())) {
            ToastUtil.showShort(getString(R.string.hit18));
            return;
        }

        if (TextUtils.isEmpty(tin) || !t.isRegistered()) {
            t.setTin(null);
        }
    }
}
