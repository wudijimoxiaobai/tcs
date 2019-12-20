package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;

import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.model.SearchApproveInvoiceCondition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchApproveInvoiceDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.invoice_code)
    EditText mInvoiceCode;
    @BindView(R.id.request_type)
    AppCompatSpinner mRequestTypeSpinner;
    @BindView(R.id.status)
    AppCompatSpinner mApproveStatusSpinner;
    @BindView(R.id.date_from)
    EditText mDateFrom;
    @BindView(R.id.date_to)
    EditText mDateTo;

    private Handler mHandler;
    private SearchApproveInvoiceCondition mSearchApproveInvoiceCondition = new SearchApproveInvoiceCondition();

    public SearchApproveInvoiceDialog(Handler handler) {
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
        View view = inflater.inflate(R.layout.search_approve_invoice_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.5f);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 1f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    private void initView() {
        int offset = DeviceUtils.dip2Px(getContext(), 50);
        mRequestTypeSpinner.setDropDownVerticalOffset(offset);
        mApproveStatusSpinner.setDropDownVerticalOffset(offset);
        mRequestTypeSpinner.setOnItemSelectedListener(this);
        mApproveStatusSpinner.setOnItemSelectedListener(this);
    }

    @OnClick({R.id.date_from, R.id.date_to, R.id.cancel, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_from:
                AppTools.obtainData(getActivity(), mDateFrom, DateUtils.format_yyyy_MM_dd_EN);
                break;
            case R.id.date_to:
                AppTools.obtainData(getActivity(), mDateTo, DateUtils.format_yyyy_MM_dd_EN);
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                mSearchApproveInvoiceCondition.setDateFrom((String) mDateFrom.getTag());
                mSearchApproveInvoiceCondition.setDateTo((String) mDateTo.getTag());
                if (!TextUtils.isEmpty(mSearchApproveInvoiceCondition.getDateFrom()) && !TextUtils.isEmpty(mSearchApproveInvoiceCondition.getDateTo())) {
                    if (DateUtils.compareDate(mSearchApproveInvoiceCondition.getDateFrom(), mSearchApproveInvoiceCondition.getDateTo(), DateUtils.format_yyyy_MM_dd_EN) == 1) {
                        ToastUtil.showShort(getString(R.string.hit48));
                        return;
                    }
                }
                dismiss();
                mSearchApproveInvoiceCondition.setInvoiceCode(mInvoiceCode.getText().toString().trim());
                mHandler.sendMessage(mHandler.obtainMessage(0, mSearchApproveInvoiceCondition));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.request_type:
                if (i == 1) {
                    mSearchApproveInvoiceCondition.setRequestType("DISA");
                } else if (i == 2) {
                    mSearchApproveInvoiceCondition.setRequestType("NEG");
                }
                break;
            case R.id.status:
                if (i > 0) mSearchApproveInvoiceCondition.setStatus(String.valueOf(i - 1));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}