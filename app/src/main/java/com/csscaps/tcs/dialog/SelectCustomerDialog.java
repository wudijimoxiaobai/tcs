package com.csscaps.tcs.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.InvoiceIssuingActivity;
import com.csscaps.tcs.adapter.SelectCustomerListAdapter;
import com.csscaps.tcs.database.table.Customer;
import com.csscaps.tcs.database.table.Customer_Table;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/24.
 */

@SuppressLint("ValidFragment")
public class SelectCustomerDialog extends DialogFragment implements AdapterView.OnItemClickListener, Action1<Customer> {

    @BindView(R.id.list_view)
    ListView mListView;

    SelectCustomerListAdapter adapter;
    List<Customer> data;
    Customer customer;
    int invoiceObject = -1;

    public SelectCustomerDialog(int invoiceObject) {
        this.invoiceObject = invoiceObject;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_customer_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 0.7f);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.8f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    @OnClick({R.id.cancel, R.id.select, R.id.add_customer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.add_customer:
                AddCustomerDialog addCustomerDialog = new AddCustomerDialog();
                addCustomerDialog.setInvoiceObject(invoiceObject);
                addCustomerDialog.show(getFragmentManager(), "AddCustomerDialog");
                break;
            case R.id.select:
                if (customer != null) {
                    Subscription subscription = ObserverActionUtils.subscribe(customer, InvoiceIssuingActivity.class);
                    if(subscription!=null)subscription.unsubscribe();
                    dismiss();
                } else {
                    ToastUtil.showShort("请选择一个！");
                }
                break;
        }
    }

    private void initView() {
        ObserverActionUtils.addAction(this);
        switch (invoiceObject) {
            case 0:
                data = select().from(Customer.class).where(Customer_Table.tin.isNotNull()).queryList();
                break;
            case 1:
                data = select().from(Customer.class).where(Customer_Table.tin.isNull()).queryList();
                break;
            case 2:
                data = select().from(Customer.class).queryList();
                break;
        }

        adapter = new SelectCustomerListAdapter(getContext(), R.layout.select_customer_list_item, data);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.setSelectedPosition(i);
        adapter.notifyDataSetChanged();
        customer = data.get(i);
    }


    @Override
    public void call(Customer customer) {
        data.add(0, customer);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObserverActionUtils.removeAction(this);
    }
}
