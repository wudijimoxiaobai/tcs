package com.csscaps.tcs.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.csscaps.common.utils.FastDoubleClickUtil;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.CalculateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.NewInvoiceActivity;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.InvoiceProductListAdapter;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.dialog.BaseAddDialog;
import com.csscaps.tcs.dialog.ProductListItemDialog;
import com.csscaps.tcs.dialog.PurchaseInformationDialog;
import com.csscaps.tcs.dialog.SelectProductDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

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
        data.addAll(NewInvoiceActivity.mInvoice.getProducts());
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
    public void call(Product product) {
        if (product.getProductModel() == null) {
            product.setProductModel(CalculateUtils.calculateProductTax(product));
        }
        super.call(product);
    }

    @Override
    @OnClick({R.id.calculate})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.customer_add:
                ((InvoiceProductListAdapter) mBaseManagementListAdapter).setEditPosition(-1);
                SelectProductDialog selectProductDialog = new SelectProductDialog();
                selectProductDialog.show(getChildFragmentManager(), "SelectProductDialog");
                break;
            case R.id.edit:
                data.addAll(NewInvoiceActivity.mInvoice.getProducts());
                Product product = (Product) view.getTag();
                ProductListItemDialog listItemDialog = new ProductListItemDialog(data, data.indexOf(product));
                listItemDialog.show(getChildFragmentManager(), "ProductListItemDialog");
                break;
            case R.id.calculate:
                if (FastDoubleClickUtil.isFastDoubleClick(R.id.calculate)) break;
                if (data.size() == 0) {
                    ToastUtil.showShort(getString(R.string.hit21));
                    break;
                }
                PurchaseInformationDialog purchaseInformationDialog = new PurchaseInformationDialog(data);
                purchaseInformationDialog.show(getFragmentManager(), "PurchaseInformationDialog");
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            NewInvoiceActivity.mInvoice.getProducts().clear();
            NewInvoiceActivity.mInvoice.getProducts().addAll(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }
}
