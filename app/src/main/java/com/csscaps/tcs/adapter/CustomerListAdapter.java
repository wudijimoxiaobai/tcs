package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Customer;

import java.util.List;

/**
 * Created by tl on 2018/5/17.
 */

public class CustomerListAdapter extends BaseManagementListAdapter<Customer>{

    public CustomerListAdapter(Context context, int layoutResId, List<Customer> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Customer item, int position) {
        super.convert(helper, item, position);
        helper.setText(R.id.tin,item.getTin());
        helper.setText(R.id.name,item.getName());
        helper.setText(R.id.tel,item.getTel());
        helper.setText(R.id.email,item.getEmail());
        helper.setText(R.id.address,item.getAddress());
        helper.setText(R.id.city,item.getCity());
        helper.setText(R.id.region,item.getState());
        helper.setText(R.id.remarks,item.getRemarks());
    }
}
