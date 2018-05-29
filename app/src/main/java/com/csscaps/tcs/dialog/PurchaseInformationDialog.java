package com.csscaps.tcs.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.InvoiceProductListAdapter;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.ProductModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tl on 2018/5/29.
 */

@SuppressLint("ValidFragment")
public class PurchaseInformationDialog extends DialogFragment {

    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.total_vat)
    TextView mTotalVat;
    @BindView(R.id.total_bpt_f)
    TextView mTotalBptF;
    @BindView(R.id.total_bpt_p)
    TextView mTotalBptP;
    @BindView(R.id.total_sdl)
    TextView mTotalSdl;
    @BindView(R.id.total_sdf)
    TextView mTotalSdf;
    @BindView(R.id.total_fees)
    TextView mTotalFees;
    @BindView(R.id.e_tax)
    TextView mETax;
    @BindView(R.id.i_tax)
    TextView mITax;

    private List<Product> data;

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
        initView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 0.9f);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.8f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    @OnClick({R.id.back, R.id.preview, R.id.print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                dismiss();
                break;
            case R.id.preview:
                break;
            case R.id.print:
                break;
        }
    }

    private void initView() {
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
        mTotalVat.setText(String.format("%.2f", vat));
        mTotalBptF.setText(String.format("%.2f",bptf));
        mTotalBptP.setText(String.format("%.2f",bptp));
        mTotalSdl.setText(String.format("%.2f",sdl));
        mTotalSdf.setText(String.format("%.2f",sdf));
        mTotalFees.setText(String.format("%.2f",fees));
        mETax.setText(String.format("%.2f",etax));
        mITax.setText(String.format("%.2f",itax));
    }


}
