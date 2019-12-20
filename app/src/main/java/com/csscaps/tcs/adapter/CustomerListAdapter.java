package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Customer;

import java.util.List;

public class CustomerListAdapter extends BaseManagementListAdapter<Customer> {

    public CustomerListAdapter(Context context, int layoutResId, List<Customer> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Customer item, int position) {
        super.convert(helper, item, position);
        helper.setText(R.id.customer_liat_item_name, item.getName())
                .setText(R.id.customer_liat_item_region, item.getState());
    }
}
