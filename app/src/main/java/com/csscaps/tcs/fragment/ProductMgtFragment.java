package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.csscaps.common.utils.FastDoubleClickUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.activity.ProductDetailsActivity;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.ProductListAdapter;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.database.table.Product_Table;
import com.csscaps.tcs.dialog.AddProductDialog;
import com.csscaps.tcs.dialog.BaseAddDialog;
import com.csscaps.tcs.dialog.SearchProductDialog;
import com.csscaps.tcs.model.SearchProductCondition;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class ProductMgtFragment extends BaseManagementListFragment<Product> {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_product_mgt;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (TCSApplication.currentUser.getRole() == 1) {
            mAdd.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getPopupWindowLayout() {
        if (TCSApplication.currentUser.getRole() == 1) return R.layout.product_popuwindow1_layout;
        return R.layout.product_popuwindow_layout;
    }

    @Override
    protected List<Product> getData() {
        return select().from(Product.class).queryList();
    }

    @Override
    protected BaseManagementListAdapter getAdapter(List<Product> data) {
        return new ProductListAdapter(mContext, R.layout.product_list_item, data);
    }

    @Override
    protected BaseAddDialog getDialog() {
        return new AddProductDialog();
    }

    @Override
    protected void toDetails(Product product) {
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @OnClick(R.id.query)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.query:
                if (FastDoubleClickUtil.isFastDoubleClick(R.id.query)) break;
                SearchProductDialog searchProductDialog = new SearchProductDialog(mHandler);
                searchProductDialog.show(getChildFragmentManager(), "SearchProductDialog");
                break;

        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            SearchProductCondition mSearchProductCondition = (SearchProductCondition) msg.obj;
            Where where = select().from(Product.class).where();
            if (!TextUtils.isEmpty(mSearchProductCondition.getProductName())) {
                where = where.and(Product_Table.productName.like(String.format(format, mSearchProductCondition.getProductName())));
            }

            if (!TextUtils.isEmpty(mSearchProductCondition.getLocalName())) {
                where = where.and(Product_Table.localName.like(String.format(format, mSearchProductCondition.getLocalName())));
            }
            data.clear();
            List list = where.queryList();
            data.addAll(list);
            mBaseManagementListAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }
}
