package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

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

import butterknife.BindView;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/8.
 * 商品管理
 */

public class ProductManagementFragment extends BaseManagementListFragment<Product> {


    @BindView(R.id.add_line)
    View mAddLine;
    @BindView(R.id.select_line)
    View mSelectLine;

    @Override
    protected int getLayoutResId() {
        return R.layout.product_mangement_fragment;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (TCSApplication.currentUser.getRole() == 1) {
            mAdd.setVisibility(View.GONE);
            mSelect.setVisibility(View.GONE);
            mAddLine.setVisibility(View.GONE);
            mSelectLine.setVisibility(View.GONE);
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

    @Override
    @OnClick({R.id.search})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.search:
                SearchProductDialog searchProductDialog=new SearchProductDialog(mHandler);
                searchProductDialog.show(getChildFragmentManager(),"SearchProductDialog");
                break;
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            SearchProductCondition mSearchProductCondition= (SearchProductCondition) msg.obj;
            Where where=select().from(Product.class).where();
            if(!TextUtils.isEmpty(mSearchProductCondition.getProductName())){
                where=where.and(Product_Table.productName.like(String.format(format,mSearchProductCondition.getProductName())));
            }

            if(!TextUtils.isEmpty(mSearchProductCondition.getLocalName())){
                where=where.and(Product_Table.localName.like(String.format(format,mSearchProductCondition.getLocalName())));
            }
            data.clear();
            List list = where.queryList();
            data.addAll(list);
            mBaseManagementListAdapter.notifyDataSetChanged();
        }
    };

}
