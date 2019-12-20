package com.csscaps.tcs.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.InvoiceQueryCelectAdapter;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.Invoice;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvoiceQuerySelectDialog extends DialogFragment {

    public static final String ARGUMENTS_B_2_A_KEY = "data";
    public static final int FRAGMNET_query = 2;
    @BindView(R.id.cancel)
    ImageView mCancel;
    @BindView(R.id.details)
    ImageView mDetails;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_all_details)
    ListView mListView;

    int i = 0;
    boolean PMF = false;

    private List<Invoice> customer = new ArrayList<>();
    private InvoiceQueryCelectAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    public InvoiceQuerySelectDialog(List<Invoice> customer, boolean pmf) {
        this.customer = customer;
        PMF = pmf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_invoice_query_updote, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        adapter = new InvoiceQueryCelectAdapter(getContext(), R.layout.query_dialog_delate, customer);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setCancelable(false);
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

    public List<Invoice> getList() {
        return customer;
    }

    @OnClick({R.id.cancel, R.id.details, R.id.delate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (getTargetFragment() != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(ARGUMENTS_B_2_A_KEY, (Serializable) customer);
                    getTargetFragment().onActivityResult(InvoiceQuerySelectDialog.FRAGMNET_query,
                            Activity.RESULT_OK,
                            resultIntent);
                }
                dismiss();
                break;
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
                List<Invoice> checkedList = adapter.getCheckedList();
                if (PMF) {
                    FlowManager.getDatabase(TcsDatabase.class)
                            .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                    new ProcessModelTransaction.ProcessModel<Invoice>() {
                                        @Override
                                        public void processModel(Invoice model, DatabaseWrapper wrapper) {
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
