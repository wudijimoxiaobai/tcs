package com.csscaps.tcs.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.AllCustomerDeleteAdapter;
import com.csscaps.tcs.adapter.AllDeleteAdapter;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.Customer;
import com.csscaps.tcs.database.table.Product;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllCustomerDeleteDialog extends DialogFragment {

    public static final String ARGUMENTS_B_2_A_KEY = "data";
    public static final int FRAGMNET_customer = 1;
    @BindView(R.id.details)
    ImageView mDetails;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_all_details)
    ListView mListView;

    int i = 0;
    boolean PMF = false;

    private List<Customer> customer = new ArrayList<>();
    private AllCustomerDeleteAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    public AllCustomerDeleteDialog(List<Customer> customer, boolean pmf) {
        this.customer = customer;
        PMF = pmf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_all_customer_delete, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        adapter = new AllCustomerDeleteAdapter(getContext(), R.layout.dialog_all_list, customer);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setCancelable(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTargetFragment() != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(ARGUMENTS_B_2_A_KEY, (Serializable) customer);
                    getTargetFragment().onActivityResult(AllDeleteDialog.FRAGMNET_A_2_Fragment_B_REQUEST_CODE,
                            Activity.RESULT_OK,
                            resultIntent);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 1f);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 1f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public List<Customer> getList() {
        return customer;
    }

    @OnClick({R.id.details, R.id.delate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.details:
                if (i == 0) {
                    i++;
                    adapter.setAllSelect(true);
                } else {
                    i--;
                    adapter.setAllSelect(false);
                }
                break;
            case R.id.delate:
                List<Customer> checkedList = adapter.getCheckedList();
                if (PMF) {
                    FlowManager.getDatabase(TcsDatabase.class)
                            .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                    new ProcessModelTransaction.ProcessModel<Customer>() {
                                        @Override
                                        public void processModel(Customer model, DatabaseWrapper wrapper) {
                                            model.delete();
                                        }
                                    }).addAll(checkedList).build())
                            .build()
                            .execute();
                }
                customer.removeAll(checkedList);
                checkedList.clear();
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
