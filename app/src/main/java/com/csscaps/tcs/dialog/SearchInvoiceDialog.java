package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.model.SearchInvoiceCondition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchInvoiceDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.invoice_code)
    EditText mInvoiceCode;
    @BindView(R.id.invoice_no)
    EditText mInvoiceNo;
    @BindView(R.id.issued_by)
    EditText mIssuedBy;
    @BindView(R.id.upload_status_spinner)
    AppCompatSpinner mUploadStatusSpinner;
    @BindView(R.id.status_spinner)
    AppCompatSpinner mStatusSpinner;
    @BindView(R.id.issuing_date_from)
    TextView mIssuingDateFrom;
    @BindView(R.id.issuing_date_to)
    TextView mIssuingDateTo;
    @BindView(R.id.upload_status_layout)
    ConstraintLayout mUploadStatusLayout;

    private Handler mHandler;
    private SearchInvoiceCondition mSearchInvoiceCondition = new SearchInvoiceCondition();
    private String statusKey[];
    private boolean select;

    public SearchInvoiceDialog(Handler handler) {
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
        View view = inflater.inflate(R.layout.search_invoice_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.6f);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 1f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    private void initView() {
        statusKey = getResources().getStringArray(R.array.invoice_status_key);
        int offset = DeviceUtils.dip2Px(getContext(), 50);
        mUploadStatusSpinner.setDropDownVerticalOffset(offset);
        mStatusSpinner.setDropDownVerticalOffset(offset);
        mUploadStatusSpinner.setOnItemSelectedListener(this);
        mStatusSpinner.setOnItemSelectedListener(this);
        if (select) mUploadStatusLayout.setVisibility(View.GONE);
    }

    @OnClick({R.id.issuing_date_from, R.id.issuing_date_to, R.id.cancel, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.issuing_date_from:
                AppTools.obtainData(getActivity(), mIssuingDateFrom, DateUtils.format_yyyy_MM_dd_EN);
                break;
            case R.id.issuing_date_to:
                AppTools.obtainData(getActivity(), mIssuingDateTo, DateUtils.format_yyyy_MM_dd_EN);
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                if (select) mSearchInvoiceCondition.setUploadStatus(String.valueOf(0));
                if (!TextUtils.isEmpty((String) mIssuingDateFrom.getTag()))
                    mSearchInvoiceCondition.setIssuingDateFrom(DateUtils.dateToStr(DateUtils.getTimeOfDayStart(DateUtils.getStringToDate_YYYY_MM_DD_EN((String) mIssuingDateFrom.getTag())), DateUtils.format_yyyyMMddHHmmss_24_EN));
                if (!TextUtils.isEmpty((String) mIssuingDateTo.getTag()))
                    mSearchInvoiceCondition.setIssuingDateTo(DateUtils.dateToStr(DateUtils.getTimeOfDayEnd(DateUtils.getStringToDate_YYYY_MM_DD_EN((String) mIssuingDateTo.getTag())), DateUtils.format_yyyyMMddHHmmss_24_EN));
                if (!TextUtils.isEmpty(mSearchInvoiceCondition.getIssuingDateFrom()) && !TextUtils.isEmpty(mSearchInvoiceCondition.getIssuingDateTo())) {
                    if (DateUtils.compareDate(mSearchInvoiceCondition.getIssuingDateFrom(), mSearchInvoiceCondition.getIssuingDateTo(), DateUtils.format_yyyy_MM_dd_EN) == 1) {
                        ToastUtil.showShort(getString(R.string.hit48));
                        return;
                    }
                }
                dismiss();
                mSearchInvoiceCondition.setInvoiceCode(mInvoiceCode.getText().toString().trim());
                mSearchInvoiceCondition.setInvoiceNo(mInvoiceNo.getText().toString().trim());
                mSearchInvoiceCondition.setIssuedBy(mIssuedBy.getText().toString().trim());
                mHandler.sendMessage(mHandler.obtainMessage(0, mSearchInvoiceCondition));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.upload_status_spinner:
                if (i > 0) {
                    mSearchInvoiceCondition.setUploadStatus(String.valueOf(i - 1));
                }
                break;
            case R.id.status_spinner:
                if (i > 0) mSearchInvoiceCondition.setStatus(statusKey[i - 1]);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

}

