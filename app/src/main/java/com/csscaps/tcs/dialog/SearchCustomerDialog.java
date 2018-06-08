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
import com.csscaps.tcs.model.SearchCustomerCondition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tl on 2018/6/6.
 */

@SuppressLint("ValidFragment")
public class SearchCustomerDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {


    @BindView(R.id.tin)
    EditText mTin;
    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.customer_type_spinner)
    AppCompatSpinner mCustomerTypeSpinner;

    private Handler mHandler;
    private SearchCustomerCondition searchCustomerCondition = new SearchCustomerCondition();

    public SearchCustomerDialog(Handler handler) {
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
        View view = inflater.inflate(R.layout.search_customer_dialog, null);
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
        mCustomerTypeSpinner.setDropDownVerticalOffset(offset);
        mCustomerTypeSpinner.setOnItemSelectedListener(this);
    }

    @OnClick({R.id.cancel, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                dismiss();
                searchCustomerCondition.setTin(mTin.getText().toString().trim());
                searchCustomerCondition.setName(mName.getText().toString().trim());
                mHandler.sendMessage(mHandler.obtainMessage(0, searchCustomerCondition));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        searchCustomerCondition.setType(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
