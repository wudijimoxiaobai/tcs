package com.csscaps.tcs.dialog;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Customer;
import com.csscaps.tcs.database.table.Taxpayer;
import com.csscaps.tcs.database.table.Taxpayer_Table;
import com.csscaps.tcs.fragment.CustomerManagementFragment;

import butterknife.BindView;
import rx.Subscription;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/17.
 */

public class AddCustomerDialog extends BaseAddDialog<Customer> implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.title)
    TextView mTitle;
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
    @BindView(R.id.tin_layout)
    LinearLayout mTinLayout;
    @BindView(R.id.registered)
    RadioButton mRegistered;
    @BindView(R.id.unregistered)
    RadioButton mUnregistered;

    int invoiceObject = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.add_customer_dialog;
    }

    @Override
    protected void initView() {
        if (t == null) t = new Customer();
        mRadioGroup.setOnCheckedChangeListener(this);
        setEditTextEnable(false);
        if (edit) {
            mTitle.setText(getString(R.string.edit_customer));
            if (t.isRegistered()) {
                mRadioGroup.check(R.id.registered);
            } else {
                mRadioGroup.check(R.id.unregistered);
                mTin.setText(null);
            }
            tIntoEditText();
        }
        mTin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String tin = mTin.getText().toString().trim();
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
                        } else {
                            ToastUtil.showShort(getString(R.string.hit13));
                        }
                    } else {
                        ToastUtil.showShort(getString(R.string.hit14));
                    }

                }
                return false;
            }
        });

        switch (invoiceObject) {
            case 0:
                mRadioGroup.check(R.id.registered);
                mUnregistered.setVisibility(View.GONE);
                break;
            case 1:
                mRadioGroup.check(R.id.unregistered);
                mRegistered.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.registered:
                mTinLayout.setVisibility(View.VISIBLE);
                t.setRegistered(true);
                setEditTextEnable(false);
                break;
            case R.id.unregistered:
                mTinLayout.setVisibility(View.GONE);
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


    @Override
    protected void save() {
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




        editTextsIntoT();

        if(!t.isRegistered()&&TextUtils.isEmpty(t.getName())){
            ToastUtil.showShort(getString(R.string.hit17));
            return;
        }
        if(!t.isRegistered()&&TextUtils.isEmpty(t.getNationalId())&&TextUtils.isEmpty(t.getPassport())){
            ToastUtil.showShort(getString(R.string.hit18));
            return;
        }

        if (TextUtils.isEmpty(tin)||!t.isRegistered()) {
            t.setTin(null);
        }

        if (edit) {
            if (t.update()) {
                dismiss();
                Subscription subscription =ObserverActionUtils.subscribe(t, CustomerManagementFragment.class);
                if(subscription!=null)subscription.unsubscribe();
            } else {
                ToastUtil.showShort(getString(R.string.hit9));
            }

        } else {
            if (t.save()) {
                dismiss();
                Subscription subscription =ObserverActionUtils.subscribe(t, CustomerManagementFragment.class);
                Subscription subscription1 =ObserverActionUtils.subscribe(t, SelectCustomerDialog.class);
                if(subscription!=null)subscription.unsubscribe();
                if(subscription1!=null)subscription1.unsubscribe();
            } else {
                ToastUtil.showShort(getString(R.string.hit10));
            }
        }
    }

    public void setInvoiceObject(int invoiceObject) {
        this.invoiceObject = invoiceObject;
    }

}
