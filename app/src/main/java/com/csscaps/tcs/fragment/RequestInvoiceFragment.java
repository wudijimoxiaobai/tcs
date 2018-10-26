package com.csscaps.tcs.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.SdcardDBUtil;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.dialog.InvoiceDetailsDialog;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/22.
 */

public class RequestInvoiceFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, Action1 {

    @BindView(R.id.application_type)
    AppCompatSpinner mApplicationType;
    @BindView(R.id.invoice_code)
    EditText mInvoiceCode;
    @BindView(R.id.invoice_no)
    EditText mInvoiceNo;
    @BindView(R.id.reason)
    EditText mReason;

    private int status;
    private final String AVL = "AVL";
    private final String DISA = "DISA";
    private final String NEG = "NEG";
    private Invoice invoice;

    @Override
    protected int getLayoutResId() {
        return R.layout.request_invoice_fragment;
    }

    @Override
    protected void onInitPresenters() {
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        int offset = DeviceUtils.dip2Px(mContext, 50);
        mApplicationType.setDropDownVerticalOffset(offset);
        mApplicationType.setOnItemSelectedListener(this);
        ObserverActionUtils.addAction(this);
    }


    @OnClick({R.id.back, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.search:
                search();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        status = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void search() {
        String invoiceCode = mInvoiceCode.getText().toString().trim();
        String invoiceNo = mInvoiceNo.getText().toString().trim();
        String reason = mReason.getText().toString().trim();
        if (TextUtils.isEmpty(invoiceCode)) {
            ToastUtil.showShort(getString(R.string.hit44));
            return;
        }

        if (TextUtils.isEmpty(invoiceNo)) {
            ToastUtil.showShort(getString(R.string.hit45));
            return;
        }

        if (TextUtils.isEmpty(reason)) {
            ToastUtil.showShort(getString(R.string.hit51));
            return;
        }

        Invoice invoice = select().from(Invoice.class).where(Invoice_Table.invoice_no.eq(invoiceNo))
                .and(Invoice_Table.invoice_type_code.eq(invoiceCode))
                .and(Invoice_Table.status.eq(AVL)).and(Invoice_Table.approveFlag.isNull()).querySingle();
        if (invoice == null) {
            ToastUtil.showShort(getString(R.string.hit46));
            return;
        } else {
            String dateTime = invoice.getClient_invoice_datetime();
            String dateNow = DateUtils.dateToStr(DateUtils.getDateNow(), DateUtils.format_yyyyMM);
            switch (status) {
                case 0://作废
                    if (!dateTime.startsWith(dateNow)) {
                        ToastUtil.showShort(getString(R.string.hit52));
                        return;
                    }
                    invoice.setRequestType(DISA);
                    invoice.setInvalid_remark(reason);
                    break;
                case 1://负数
                    dateTime = TextUtils.substring(dateTime, 0, 6);
                    if (DateUtils.compareDate(dateNow, dateTime, DateUtils.format_yyyyMM) != 1) {
                        ToastUtil.showShort(getString(R.string.hit53));
                        return;
                    }
                    invoice.setRequestType(NEG);
                    invoice.setNegative_approval_remark(reason);
                    break;
            }
            invoice.setApproveFlag("2");
        }
        this.invoice = invoice;
        InvoiceDetailsDialog invoiceDetailsDialog = new InvoiceDetailsDialog(invoice);
        invoiceDetailsDialog.setFlag(0);
        invoiceDetailsDialog.show(getFragmentManager(), "InvoiceDetailsDialog");
    }

    @Override
    public void call(Object o) {
        if (invoice != null) {
            invoice.setRequestBy(TCSApplication.currentUser.getUserName());
            invoice.setRequestDate(DateUtils.getDateToString_YYYY_MM_DD_EN(DateUtils.getDateNow()));
            invoice.update();
            SdcardDBUtil.saveSDDB(invoice);
        }
    }
}
