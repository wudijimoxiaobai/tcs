package com.csscaps.tcs.fragment;

import android.view.View;

import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.InvoiceIssuingActivity;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.InvoiceProductListAdapter;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.dialog.BaseAddDialog;
import com.csscaps.tcs.dialog.PurchaseInformationDialog;
import com.csscaps.tcs.dialog.SelectProductDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by tl on 2018/5/24.
 */

public class InvoiceProductListFragment extends BaseManagementListFragment<Product> {

    private List<Product> data = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        setDataBase(false);
        return R.layout.invoice_product_list_fragment;
    }

    @Override
    protected int getPopupWindowLayout() {
        return R.layout.product_list_popuwindow_layout;
    }

    @Override
    protected List getData() {
        data.addAll(InvoiceIssuingActivity.mInvoice.getProducts());
        return data;
    }


    @Override
    protected BaseManagementListAdapter getAdapter(List data) {
        return new InvoiceProductListAdapter(mContext, R.layout.invoice_product_list_item_layout, data);
    }

    @Override
    protected BaseAddDialog getDialog() {
        return null;
    }


    @Override
    @OnClick({R.id.calculate})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.add:
                ((InvoiceProductListAdapter) mBaseManagementListAdapter).setEditPosition(-1);
                SelectProductDialog selectProductDialog = new SelectProductDialog();
                selectProductDialog.show(getChildFragmentManager(), "SelectProductDialog");
                break;
            case R.id.edit:
                Product product = (Product) view.getTag();
                int i = data.indexOf(product);
                ((InvoiceProductListAdapter) mBaseManagementListAdapter).setEditPosition(i);
                mBaseManagementListAdapter.notifyDataSetChanged();
                break;
            case R.id.calculate:
                if(data.size()==0){
                    ToastUtil.showShort("请添加商品！");
                    break;
                }
                PurchaseInformationDialog purchaseInformationDialog=new PurchaseInformationDialog(data);
                purchaseInformationDialog.show(getFragmentManager(),"PurchaseInformationDialog");
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        InvoiceIssuingActivity.mInvoice.getProducts().clear();
        InvoiceIssuingActivity.mInvoice.getProducts().addAll(data);
    }
}
