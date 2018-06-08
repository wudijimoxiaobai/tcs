package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.CustomerDetailsActivity;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.CustomerListAdapter;
import com.csscaps.tcs.database.table.Customer;
import com.csscaps.tcs.database.table.Customer_Table;
import com.csscaps.tcs.dialog.AddCustomerDialog;
import com.csscaps.tcs.dialog.BaseAddDialog;
import com.csscaps.tcs.dialog.SearchCustomerDialog;
import com.csscaps.tcs.model.SearchCustomerCondition;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.List;

import butterknife.OnClick;

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
        return new CustomerListAdapter(mContext, R.layout.customer_list_item, data);
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
    @OnClick({R.id.search})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.search:
                SearchCustomerDialog searchCustomerDialog = new SearchCustomerDialog(mHandler);
                searchCustomerDialog.show(getChildFragmentManager(), "SearchCustomerDialog");
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SearchCustomerCondition searchCustomerCondition = (SearchCustomerCondition) msg.obj;
            Where where = select().from(Customer.class).where();
            if (!TextUtils.isEmpty(searchCustomerCondition.getTin())) {
                where = where.and(Customer_Table.tin.like(String.format(format,searchCustomerCondition.getTin())));
            }

            if (!TextUtils.isEmpty(searchCustomerCondition.getName())) {
                where = where.and(Customer_Table.name.like(String.format(format,searchCustomerCondition.getName())));
            }

            switch (searchCustomerCondition.getType()) {
                case 1:
                    where = where.and(Customer_Table.registered.is(true));
                    break;
                case 2:
                    where = where.and(Customer_Table.registered.is(false));
                    break;
            }
            data.clear();
            List list = where.queryList();
            data.addAll(list);
            mBaseManagementListAdapter.notifyDataSetChanged();
        }
    };

}
