package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.view.View;

import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.CustomerDetailsActivity;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.CustomerListAdapter;
import com.csscaps.tcs.database.table.Customer;
import com.csscaps.tcs.dialog.AddCustomerDialog;
import com.csscaps.tcs.dialog.BaseAddDialog;

import java.util.List;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/8.
 */

public class CustomerManagementFragment extends BaseManagementListFragment<Customer> {

    @Override
    protected int getLayoutResId() {
        return R.layout.customer_mangement_fragment;
    }

    @Override
    protected List<Customer> getData() {
        return select().from(Customer.class).queryList();
    }

    @Override
    protected BaseManagementListAdapter getAdapter(List<Customer> data) {
        return new CustomerListAdapter(mContext,R.layout.customer_list_item,data);
    }

    @Override
    protected BaseAddDialog getDialog() {
        return new AddCustomerDialog();
    }

    @Override
    protected int getPopupWindowLayout() {
        return R.layout.product_popuwindow_layout;
    }

    @Override
    protected void toDetails(Customer customer) {
        Intent intent = new Intent(mContext, CustomerDetailsActivity.class);
        intent.putExtra("customer", customer);
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
