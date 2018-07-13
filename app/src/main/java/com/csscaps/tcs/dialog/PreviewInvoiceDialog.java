package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.ShowOfdUtils;
import com.csscaps.tcs.database.table.Invoice;
import com.suwell.to.ofd.ofdviewer.OFDView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tl on 2018/7/12.
 */

public class PreviewInvoiceDialog extends DialogFragment {

    @BindView(R.id.ofd_view)
    OFDView mOfdView;

    private Invoice invoice;

    public PreviewInvoiceDialog(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview_invoice_dialog, null);
        ButterKnife.bind(this, view);
        ShowOfdUtils.showOfd(invoice,mOfdView);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.END);
        int width = DeviceUtils.dip2Px(getContext(), 600);
        dialogWindow.setLayout(width, -1);
        dialogWindow.setWindowAnimations(R.style.dialog_right_anim);
    }

    @OnClick({R.id.back, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
            case R.id.cancel:
                dismiss();
                break;
        }
    }
}
