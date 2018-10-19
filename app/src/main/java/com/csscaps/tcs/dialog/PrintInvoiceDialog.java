package com.csscaps.tcs.dialog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.PrintUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.ShowOfdUtils;
import com.csscaps.tcs.activity.ProductListActivity;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.presenter.InvoiceIssuingPresenter;
import com.suwell.to.ofd.ofdviewer.OFDView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tl on 2018/7/12.
 */

public class PrintInvoiceDialog extends DialogFragment {

    @BindView(R.id.ofd_view)
    OFDView mOfdView;

    private Invoice invoice;
    private InvoiceIssuingPresenter presenter;
    private PurchaseInformationDialog mPurchaseInformationDialog;
    private PrintUtil mPrintUtil;

    public PrintInvoiceDialog(Invoice invoice, PurchaseInformationDialog mPurchaseInformationDialog) {
        this.invoice = invoice;
        this.mPurchaseInformationDialog = mPurchaseInformationDialog;
        presenter = new InvoiceIssuingPresenter(mPurchaseInformationDialog, mPurchaseInformationDialog.getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
        mPrintUtil = ((ProductListActivity) getActivity()).getPrintUtil();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.print_invoice_dialog, null);
        ButterKnife.bind(this, view);
        ShowOfdUtils.showOfd(invoice, mOfdView);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.END);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 2f / 5f);
        dialogWindow.setLayout(width, -1);
        dialogWindow.setWindowAnimations(R.style.dialog_right_anim);
    }

    @OnClick({R.id.back, R.id.cancel, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                if (mPrintUtil.checkPaper()) {
                    ((ProductListActivity) getActivity()).getPrintUtil().setHandler(mHandler);
                    float dpi = (PrintUtil.DotLineWidth * 25.4f / mOfdView.getMapPagesWH().get(0)[0]);
                    Bitmap bitmap = mOfdView.mOFDParseCore.renderPageBitmap(0, dpi);
                    mPrintUtil.print(bitmap);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (presenter != null) presenter.onDetach();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                presenter.issuingInvoice(mPurchaseInformationDialog.data);
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
