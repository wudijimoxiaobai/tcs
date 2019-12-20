package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.csscaps.common.utils.FastDoubleClickUtil;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class CustomerMgtFragment extends BaseManagementListFragment<Customer> {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_customer;
    }

    @Override
    protected int getPopupWindowLayout() {
        return R.layout.customer_popuwindow_layout;
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
    protected void toDetails(Customer customer) {
        Intent intent = new Intent(mContext, CustomerDetailsActivity.class);
        intent.putExtra("customer", customer);
        startActivity(intent);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SearchCustomerCondition searchCustomerCondition = (SearchCustomerCondition) msg.obj;
            Where where = select().from(Customer.class).where();
            if (!TextUtils.isEmpty(searchCustomerCondition.getTin())) {
                where = where.and(Customer_Table.tin.like(String.format(format, searchCustomerCondition.getTin())));
            }

            if (!TextUtils.isEmpty(searchCustomerCondition.getName())) {
                where = where.and(Customer_Table.name.like(String.format(format, searchCustomerCondition.getName())));
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

    @OnClick(R.id.customer_query)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.customer_query:
                if (FastDoubleClickUtil.isFastDoubleClick(R.id.customer_query)) break;
                SearchCustomerDialog searchCustomerDialog = new SearchCustomerDialog(mHandler);
                searchCustomerDialog.show(getChildFragmentManager(), "SearchCustomerDialog");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }
}
