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
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.action.IInvoiceIssuingAction;
import com.csscaps.tcs.activity.ApplicationListActivity;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.InvoiceNo;
import com.csscaps.tcs.database.table.InvoiceNo_Table;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.database.table.ProductModel_Table;
import com.csscaps.tcs.fragment.ApproveInvoiceFragment;
import com.csscaps.tcs.fragment.InvoiceDetailsFragment;
import com.csscaps.tcs.fragment.RequestInvoiceFragment;
import com.csscaps.tcs.presenter.InvoiceIssuingPresenter;
import com.csscaps.tcs.service.UploadInvoiceService;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/6/19.
 */

public class InvoiceDetailsDialog extends DialogFragment implements IInvoiceIssuingAction {

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
    private Invoice mInvoice, negativeInvoice;
    private InvoiceIssuingPresenter mIssuingPresenter;

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
                negativeInvoice();
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
        mIssuingPresenter = new InvoiceIssuingPresenter(this, getContext());
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
        dismiss();
        List<Invoice> list = new ArrayList<>();
        list.add(mInvoice);
        Intent intent=new Intent(getContext(),UploadInvoiceService.class);
        intent.putExtra("list",(Serializable) list);
        getActivity().startService(intent);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            mIssuingPresenter.issuingInvoice(negativeInvoice);
        }
    };

    private void negativeInvoice() {
        try {
            negativeInvoice = (Invoice) mInvoice.clone();
            negativeInvoice.setOriginal_invoice_type_code(mInvoice.getInvoice_type_code());
            negativeInvoice.setOriginal_invoice_no(mInvoice.getInvoice_no());
            FlowCursor flowCursor = select().from(InvoiceNo.class).where(InvoiceNo_Table.invoice_type_code.eq(mInvoice.getInvoice_type_code())).orderBy(InvoiceNo_Table.id, true).query();
            flowCursor.moveToFirst();
            String invoice_num = flowCursor.getStringOrDefault("invoice_num");
            flowCursor.close();
            negativeInvoice.setInvoice_no(invoice_num);
            negativeInvoice.setTotal_vat("-" + mInvoice.getTotal_vat());
            negativeInvoice.setTotal_bpt("-" + mInvoice.getTotal_bpt());
            negativeInvoice.setTotal_fee("-" + mInvoice.getTotal_fee());
            negativeInvoice.setTotal_final("-" + mInvoice.getTotal_final());
            negativeInvoice.setTotal_bpt_preypayment("-" + mInvoice.getTotal_bpt_preypayment());
            negativeInvoice.setTotal_stamp("-" + mInvoice.getTotal_stamp());
            negativeInvoice.setTotal_tax_due("-" + mInvoice.getTotal_tax_due());
            negativeInvoice.setTotal_taxable_amount("-" + mInvoice.getTotal_taxable_amount());
            negativeInvoice.setTotal_all("-" + mInvoice.getTotal_all());

            String invoiceNo = mInvoice.getInvoice_no();
            List<ProductModel> productModels = select().from(ProductModel.class).where(ProductModel_Table.invoice_no.eq(invoiceNo)).queryList();

            List<ProductModel> negProductModels = new ArrayList<>();
            for (int i = 0; i < productModels.size(); i++) {
                ProductModel productMode = productModels.get(i);
                ProductModel negProductMode = (ProductModel) productMode.clone();
                negProductMode.setInvoice_no(invoice_num);
                negProductMode.setId(0);
                negProductMode.setVat(-productMode.getVat());
                negProductMode.setBpt_prepayment(-productMode.getBpt_prepayment());
                negProductMode.setBpt_final(-productMode.getBpt_final());
                negProductMode.setFees(-productMode.getFees());
                negProductMode.setStamp_duty_federal(-productMode.getStamp_duty_federal());
                negProductMode.setStamp_duty_local(-productMode.getStamp_duty_local());
                negProductMode.setE_tax(-productMode.getE_tax());
                negProductMode.setI_tax(-productMode.getI_tax());
                negProductMode.setTax_due("-" + productMode.getTax_due());
                negProductMode.setTaxable_amount("-" + productMode.getTaxable_amount());
                negProductMode.setAmount_inc("-" + productMode.getAmount_inc());
                negProductModels.add(negProductMode);
            }
            negativeInvoice.setGoods(negProductModels);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void complete(boolean success) {
        if (success) {
            negativeInvoice.setUploadStatus(Invoice.SUCCESS);
        } else {
            negativeInvoice.setUploadStatus(Invoice.FAILURE);
        }
        negativeInvoice.save();
        dismiss();
        ToastUtil.showShort(getString(R.string.hit5));
    }
}
