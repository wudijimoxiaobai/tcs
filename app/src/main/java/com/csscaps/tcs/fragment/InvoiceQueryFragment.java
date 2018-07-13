package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.FastDoubleClickUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.InvoiceQueryListAdapter;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.dialog.BaseAddDialog;
import com.csscaps.tcs.dialog.InvoiceDetailsDialog;
import com.csscaps.tcs.dialog.SearchInvoiceDialog;
import com.csscaps.tcs.model.SearchInvoiceCondition;
import com.csscaps.tcs.service.UploadInvoiceService;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.io.Serializable;
import java.util.List;

import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/22.
 */

public class InvoiceQueryFragment extends BaseManagementListFragment<Invoice> {

    @Override
    protected int getLayoutResId() {
        return R.layout.invoice_query_fragment;
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
    @OnClick({R.id.back, R.id.search, R.id.select})
    public void onClick(View view) {
        int tag = 0;
        try {
            tag = (int) view.getTag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (view.getId()) {
            case R.id.back:
                switch (tag) {
                    case 0://返回
                        getActivity().finish();
                        break;
                    case 1://取消
                        cancel();
                        break;
                }
                break;
            case R.id.select:
                switch (tag) {
                    case 0://选择
                        mBack.setTag(1);
                        mSelect.setTag(1);
//                        mBack.setText(getString(R.string.cancel));
                        mSelect.setText((getString(R.string.upload)));
                        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext, R.drawable.cb_check_selector));
                        mAllSelect.setText(null);
                        mBaseManagementListAdapter.setShowCheckBox(true);
                        notUploaded();
                        break;
                    case 1://上传
                        List<Invoice> list = mBaseManagementListAdapter.getCheckedList();
                        Intent intent = new Intent(mContext, UploadInvoiceService.class);
                        intent.putExtra("list", (Serializable) list);
                        mContext.startService(intent);
                        cancel();
                        break;
                }
                break;
            case R.id.search:
                if (FastDoubleClickUtil.isFastDoubleClick(R.id.search)) break;
                SearchInvoiceDialog searchInvoiceDialog = new SearchInvoiceDialog(mHandler);
                if (((Integer) mSelect.getTag()) == 1) searchInvoiceDialog.setSelect(true);
                searchInvoiceDialog.show(getFragmentManager(), "SearchInvoiceDialog");
                break;
        }
    }

    private void cancel() {
        mBack.setTag(0);
        mSelect.setTag(0);
//      mBack.setText(getString(R.string.back));
        mSelect.setText((getString(R.string.select)));
        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.select);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        mSelect.setCompoundDrawables(drawable, null, null, null);
        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext, android.R.color.transparent));
        mBaseManagementListAdapter.setShowCheckBox(false);
        mAllSelect.setText(getString(R.string.no));
        mAllSelect.setChecked(false);
        onHiddenChanged(false);
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
                where = where.and(Invoice_Table.client_invoice_datetime.between(searchInvoiceCondition.getIssuingDateFrom()).and(DateUtils.getDateToString_YYYY_MM_DD_HH_MM_SS_EN(DateUtils.getDateNow())));
            }

            if (!TextUtils.isEmpty(searchInvoiceCondition.getIssuingDateFrom()) && !TextUtils.isEmpty(searchInvoiceCondition.getIssuingDateTo())) {
                where = where.and(Invoice_Table.client_invoice_datetime.between(searchInvoiceCondition.getIssuingDateFrom()).and(searchInvoiceCondition.getIssuingDateTo()));
            }

            data = where.queryList();
            mBaseManagementListAdapter.setData(data);
        }
    };
}
