package com.csscaps.tcs.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.FastDoubleClickUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.InvoiceQueryListAdapter;
import com.csscaps.tcs.database.table.Customer;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.dialog.AllCustomerDeleteDialog;
import com.csscaps.tcs.dialog.AllDeleteDialog;
import com.csscaps.tcs.dialog.BaseAddDialog;
import com.csscaps.tcs.dialog.InvoiceDetailsDialog;
import com.csscaps.tcs.dialog.InvoiceQuerySelectDialog;
import com.csscaps.tcs.dialog.SearchInvoiceDialog;
import com.csscaps.tcs.model.SearchInvoiceCondition;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.List;

import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class InvoiceQueryFragment extends BaseManagementListFragment<Invoice> {

    protected List<Invoice> list;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_invoice_query;
    }

    @Override
    protected int getPopupWindowLayout() {
        return 0;
    }

    @Override
    protected List<Invoice> getData() {
        return select().from(Invoice.class).orderBy(Invoice_Table.client_invoice_datetime, false).queryList();
    }

    @Override
    protected BaseManagementListAdapter getAdapter(List<Invoice> data) {
        return new InvoiceQueryListAdapter(mContext, R.layout.invoice_query_list_item, data);
    }

    @Override
    protected BaseAddDialog getDialog() {
        return null;
    }

    @Override
    @OnClick({R.id.search, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                if (FastDoubleClickUtil.isFastDoubleClick(R.id.search)) break;
                SearchInvoiceDialog searchInvoiceDialog = new SearchInvoiceDialog(mHandler);
                searchInvoiceDialog.show(getFragmentManager(), "SearchInvoiceDialog");
                break;
            case R.id.select:
                List<Invoice> queryList = select().from(Invoice.class).where(Invoice_Table.uploadStatus.eq("0")).orderBy(Invoice_Table.client_invoice_datetime, false).queryList();
                InvoiceQuerySelectDialog invoiceQuerySelectDialog = new InvoiceQuerySelectDialog(queryList, isDataBase);
                invoiceQuerySelectDialog.setTargetFragment(InvoiceQueryFragment.this, InvoiceQuerySelectDialog.FRAGMNET_query);
                list = invoiceQuerySelectDialog.getList();
                invoiceQuerySelectDialog.show(getFragmentManager(), "InvoiceQuerySelectDialog");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case InvoiceQuerySelectDialog.FRAGMNET_query:
                if (resultCode == Activity.RESULT_OK && data != null) {//获取从DialogFragmentB中传递的mB2A
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        onHiddenChanged(false);
//                        List<Invoice> products = (List<Invoice>) bundle.get(InvoiceQuerySelectDialog.ARGUMENTS_B_2_A_KEY);
//                        mBaseManagementListAdapter.setData(products);
                        // mBaseManagementListAdapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void call(Invoice invoice) {
        onHiddenChanged(false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (FastDoubleClickUtil.isFastDoubleClick()) return;
        Invoice invoice = data.get(i);
        InvoiceDetailsDialog invoiceDetailsDialog = new InvoiceDetailsDialog(invoice);
        if ("0".equals(invoice.getUploadStatus())) {
            invoiceDetailsDialog.setFlag(1);
        } else {
            invoiceDetailsDialog.setFlag(3);
        }
        invoiceDetailsDialog.show(getFragmentManager(), "InvoiceDetailsDialog");
    }

    private void notUploaded() {
        data = select().from(Invoice.class).where(Invoice_Table.uploadStatus.eq("0")).orderBy(Invoice_Table.client_invoice_datetime, false).queryList();
        mBaseManagementListAdapter.setData(data);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            data = select().from(Invoice.class).orderBy(Invoice_Table.client_invoice_datetime, false).queryList();
            mBaseManagementListAdapter.setData(data);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SearchInvoiceCondition searchInvoiceCondition = (SearchInvoiceCondition) msg.obj;
            Where where = select().from(Invoice.class).orderBy(Invoice_Table.client_invoice_datetime, false);
            if (!TextUtils.isEmpty(searchInvoiceCondition.getInvoiceCode())) {
                where = where.and(Invoice_Table.invoice_type_code.eq(searchInvoiceCondition.getInvoiceCode()));
            }

            if (!TextUtils.isEmpty(searchInvoiceCondition.getInvoiceNo())) {
                where = where.and(Invoice_Table.invoice_no.eq(searchInvoiceCondition.getInvoiceNo()));
            }

            if (!TextUtils.isEmpty(searchInvoiceCondition.getIssuedBy())) {
                where = where.and(Invoice_Table.drawer_name.eq(searchInvoiceCondition.getIssuedBy()));
            }

            if (!TextUtils.isEmpty(searchInvoiceCondition.getUploadStatus())) {
                where = where.and(Invoice_Table.uploadStatus.eq(searchInvoiceCondition.getUploadStatus()));
            }

            if (!TextUtils.isEmpty(searchInvoiceCondition.getStatus())) {
                where = where.and(Invoice_Table.status.eq(searchInvoiceCondition.getStatus()));
            }

            if (!TextUtils.isEmpty(searchInvoiceCondition.getIssuingDateFrom()) && TextUtils.isEmpty(searchInvoiceCondition.getIssuingDateTo())) {
                where = where.and(Invoice_Table.client_invoice_datetime.between(searchInvoiceCondition.getIssuingDateFrom()).and(DateUtils.dateToStr(DateUtils.getDateNow(), DateUtils.format_yyyyMMddHHmmss_24_EN)));
            }

            if (!TextUtils.isEmpty(searchInvoiceCondition.getIssuingDateFrom()) && !TextUtils.isEmpty(searchInvoiceCondition.getIssuingDateTo())) {
                where = where.and(Invoice_Table.client_invoice_datetime.between(searchInvoiceCondition.getIssuingDateFrom()).and(searchInvoiceCondition.getIssuingDateTo()));
            }

            data = where.queryList();
            mBaseManagementListAdapter.setData(data);
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }
}
