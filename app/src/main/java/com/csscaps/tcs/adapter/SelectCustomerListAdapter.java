package com.csscaps.tcs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Customer;

import java.util.List;

/**
 * Created by tl on 2018/5/24.
 */

public class SelectCustomerListAdapter extends QuickAdapter<Customer> {
    private int selectedPosition = -1;

    public SelectCustomerListAdapter(Context context, int layoutResId, List<Customer> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Customer item, int position) {
        helper.setText(R.id.no, String.valueOf(position + 1));
        helper.setText(R.id.tin, item.getTin());
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.tel, item.getTel());
        helper.setText(R.id.address, item.getAddress());
        helper.setText(R.id.national_id, item.getNationalId());
        helper.setText(R.id.passport, item.getPassport());
        if (position == selectedPosition) {
            helper.setBackgroundColor(R.id.layout, ContextCompat.getColor(context,R.color.bg));
        } else {
            helper.setBackgroundColor(R.id.layout, ContextCompat.getColor(context,R.color.top_bg));
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
