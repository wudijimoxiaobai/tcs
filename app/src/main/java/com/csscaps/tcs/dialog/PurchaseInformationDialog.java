package com.csscaps.tcs.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.MainActivity;
import com.csscaps.tcs.R;
import com.csscaps.tcs.RTCUtil;
import com.csscaps.tcs.SdcardDBUtil;
import com.csscaps.tcs.Util;
import com.csscaps.tcs.action.IInvoiceIssuingAction;
import com.csscaps.tcs.adapter.InvoiceProductListAdapter;
import com.csscaps.tcs.database.SDInvoiceDatabase;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.ProductModel;
import com.csscaps.tcs.presenter.InvoiceIssuingPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.csscaps.tcs.activity.NewInvoiceActivity.mInvoice;

public class PurchaseInformationDialog extends DialogFragment implements IInvoiceIssuingAction {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.e_tax)
    TextView mETax;
    @BindView(R.id.i_tax)
    TextView mITax;
    @BindView(R.id.total_vat)
    TextView mTotalVat;
    @BindView(R.id.total_other)
    TextView mTotalOther;
    @BindView(R.id.list_view)
    ListView mListView;

    protected List<Product> data;
    private PrintInvoiceDialog printInvoiceDialog;
    private InvoiceIssuingPresenter presenter;

    public PurchaseInformationDialog(List<Product> data) {
        this.data = data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.purchase_information_dialog, null);
        ButterKnife.bind(this, view);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 1f);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 1f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    @OnClick(R.id.print)
    public void onViewClicked(View mView) {
        switch (mView.getId()) {
            case R.id.print:
                PrintInvoiceDialog printInvoiceDialog = new PrintInvoiceDialog(mInvoice, this);
                printInvoiceDialog.show(getFragmentManager(), "PrintInvoiceDialog");
                break;
        }
    }

    private void initView() {
        mInvoice.setProducts(data);
        InvoiceProductListAdapter adapter = new InvoiceProductListAdapter(getContext(), R.layout.invoice_product_list_item_layout, data);
        mListView.setAdapter(adapter);
        double vat = 0, bptf = 0, bptp = 0, fees = 0, sdl = 0, sdf = 0, etax = 0, itax = 0;
        for (Product product : data) {
            ProductModel productModel = product.getProductModel();
            vat += productModel.getVat();
            bptf += productModel.getBpt_final();
            bptp += productModel.getBpt_prepayment();
            fees += productModel.getFees();
            sdl += productModel.getStamp_duty_local();
            sdf += productModel.getStamp_duty_federal();
            etax += productModel.getE_tax();
            itax += productModel.getI_tax();
        }
        Invoice invoice = mInvoice;
        invoice.setTotal_vat(String.format("%.2f", vat));
        invoice.setTotal_bpt(String.format("%.2f", bptf));
        invoice.setTotal_final(String.format("%.2f", sdf));
        invoice.setTotal_stamp(String.format("%.2f", sdl));
        invoice.setTotal_bpt_preypayment(String.format("%.2f", bptp));
        invoice.setTotal_fee(String.format("%.2f", fees));
        invoice.setTotal_taxable_amount(String.format("%.2f", etax));
        invoice.setTotal_all(String.format("%.2f", itax));
        invoice.setTotal_tax_due(String.format("%.2f", itax - etax));
        invoice.setClient_invoice_datetime(DateUtils.dateToStr(RTCUtil.getRTC(), DateUtils.format_yyyyMMddHHmmss_24_EN));

        mTotalVat.setText(invoice.getTotal_vat());
        // mTotalBptF.setText(invoice.getTotal_bpt());
        // mTotalBptP.setText(invoice.getTotal_bpt_preypayment());
        //  mTotalSdl.setText(invoice.getTotal_stamp());
        // mTotalSdf.setText(invoice.getTotal_final());
        // mTotalFees.setText(invoice.getTotal_fee());
        mTotalOther.setText(String.format("%.2f", bptf+sdf+sdl+bptp+fees));
        Log.e("other",String.format("%.2f", bptf+sdf+sdl+bptp+fees));
        mETax.setText(String.format("%.2f", etax));
       // mITax.setText(String.format("%.2f", itax - fees));
        mITax.setText(String.format("%.2f", itax));
        Util.signInvoice(mInvoice);

        List<ProductModel> productModels = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Product p = data.get(i);
            ProductModel productModel = p.getProductModel();
            productModel.setLine_no(String.valueOf(i + 1));
            productModels.add(productModel);
        }
        mInvoice.setGoods(productModels);
    }


    @Override
    public void complete(boolean success) {
        if (success) {
            mInvoice.setUploadStatus(Invoice.SUCCESS);
        } else {
            mInvoice.setUploadStatus(Invoice.FAILURE);
        }
        mInvoice.save();
        SdcardDBUtil.saveSDDB(mInvoice, SDInvoiceDatabase.class);
        dismiss();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        ToastUtil.showShort(getString(R.string.hit5));
        Util.updateReportDataDaily(mInvoice);
    }
}
