package com.csscaps.tcs.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.ApplicationListActivity;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.fragment.ApproveInvoiceFragment;
import com.csscaps.tcs.fragment.InvoiceDetailsFragment;
import com.csscaps.tcs.fragment.RequestInvoiceFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by tl on 2018/6/19.
 */

public class InvoiceDetailsDialog extends DialogFragment {

    @BindView(R.id.approve)
    TextView mApprove;
    @BindView(R.id.reject)
    TextView mReject;
    @BindView(R.id.issue)
    TextView mIssue;
    @BindView(R.id.confirm)
    TextView mConfirm;
    @BindView(R.id.upload)
    TextView mUpload;

    private int flag;
    private Invoice mInvoice;

    public InvoiceDetailsDialog(Invoice invoice) {
        mInvoice = invoice;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.process_invoice_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 0.9f);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.9f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private void initView() {
        switch (flag) {
            case 0:
                mApprove.setVisibility(View.GONE);
                mReject.setVisibility(View.GONE);
                mIssue.setVisibility(View.GONE);
                mUpload.setVisibility(View.GONE);
                break;
            case 1:
                mApprove.setVisibility(View.GONE);
                mReject.setVisibility(View.GONE);
                mIssue.setVisibility(View.GONE);
                mConfirm.setVisibility(View.GONE);
                break;
            case 2:
                mApprove.setVisibility(View.GONE);
                mReject.setVisibility(View.GONE);
                mUpload.setVisibility(View.GONE);
                mConfirm.setVisibility(View.GONE);
                break;
            case 3:
                mApprove.setVisibility(View.GONE);
                mReject.setVisibility(View.GONE);
                mIssue.setVisibility(View.GONE);
                mUpload.setVisibility(View.GONE);
                mConfirm.setVisibility(View.GONE);
                break;
            case 4:
                mIssue.setVisibility(View.GONE);
                mUpload.setVisibility(View.GONE);
                mConfirm.setVisibility(View.GONE);
                break;
        }
        InvoiceDetailsFragment fragment = new InvoiceDetailsFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    @OnClick({R.id.back, R.id.approve, R.id.reject, R.id.issue, R.id.confirm, R.id.upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                dismiss();
                break;
            case R.id.approve:
                approve();
                break;
            case R.id.reject:
                reject();
                break;
            case R.id.issue:
                issue();
                break;
            case R.id.confirm:
                confirm();
                break;
            case R.id.upload:
                upload();
                break;
        }
    }

    private void approve() {
        dismiss();
        mInvoice.setApproveFlag("4");
        mInvoice.update();
        Subscription subscription = ObserverActionUtils.subscribe(1, ApproveInvoiceFragment.class);
        if (subscription != null) subscription.unsubscribe();
    }

    private void reject() {
        dismiss();
        mInvoice.setApproveFlag("3");
        mInvoice.update();
        Subscription subscription = ObserverActionUtils.subscribe(0, ApproveInvoiceFragment.class);
        if (subscription != null) subscription.unsubscribe();
    }


    private void issue() {
        NegativeDialog negativeDialog = new NegativeDialog(mHandler);
        negativeDialog.show(getFragmentManager(), "NegativeDialog");
    }

    private void confirm() {
        dismiss();
        Subscription subscription = ObserverActionUtils.subscribe(0, RequestInvoiceFragment.class);
        if (subscription != null) subscription.unsubscribe();
        startActivity(new Intent(getContext(), ApplicationListActivity.class));
    }

    private void upload() {


    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


        }
    };
}
