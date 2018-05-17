package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.view.View;

import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.ProductDetailsActivity;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.ProductListAdapter;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.dialog.AddProductDialog;
import com.csscaps.tcs.dialog.BaseAddDialog;

import java.util.List;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/8.
 * 商品管理
 */

public class ProductManagementFragment extends BaseManagementListFragment<Product> {

    @Override
    protected int getLayoutResId() {
        return R.layout.product_mangement_fragment;
    }

    @Override
    protected int getPopupWindowLayout() {
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
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.search:

                break;

        }
    }

}
