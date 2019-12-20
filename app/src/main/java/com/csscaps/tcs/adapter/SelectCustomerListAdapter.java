package com.csscaps.tcs.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;

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
        helper.setText(R.id.customer_add_name, item.getName())
                .setText(R.id.customer_add_region, item.getState());
        if (position == selectedPosition) {
            helper.setBackgroundColor(R.id.layout, ContextCompat.getColor(context, R.color.new_text2));
        } else {
            helper.setBackgroundColor(R.id.layout, ContextCompat.getColor(context, R.color.new_text));
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
