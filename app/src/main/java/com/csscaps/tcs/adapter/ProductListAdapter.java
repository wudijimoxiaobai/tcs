package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Product;

import java.util.List;

/**
 * Created by tl on 2018/5/16.
 */

public class ProductListAdapter extends BaseManagementListAdapter<Product> {

    public ProductListAdapter(Context context, int layoutResId, List<Product> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Product item, int position) {
        super.convert(helper, item, position);
        helper.setText(R.id.product_name, item.getProductName());
        helper.setText(R.id.local_name, item.getLocalName());
        helper.setText(R.id.specification, item.getSpecification());
        helper.setText(R.id.unit, item.getUnit());
        helper.setText(R.id.price, String.valueOf(item.getPrice()));
    }

}
