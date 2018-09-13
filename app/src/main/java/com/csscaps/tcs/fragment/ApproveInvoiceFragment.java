package com.csscaps.tcs.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.FastDoubleClickUtil;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.TCSApplication;
import com.csscaps.tcs.adapter.ApproveInvoiceListAdapter;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.dialog.InvoiceDetailsDialog;
import com.csscaps.tcs.dialog.SearchApproveInvoiceDialog;
import com.csscaps.tcs.model.SearchApproveInvoiceCondition;
import com.csscaps.tcs.presenter.InvoiceApprovePresenter;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/22.
 */

public class ApproveInvoiceFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, Action1 {

    @BindView(R.id.list_view)
    ListView mListView;

    private final String DISA = "DISA";
    private final String NEG = "NEG";
    private final String AVL = "AVL";
    private ApproveInvoiceListAdapter mAdapter;
    private List<Invoice> data;
    private boolean process = true;
    private Invoice invoice;
    private InvoiceApprovePresenter mApprovePresenter;
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutResId() {
        return R.layout.approve_invoice_fragment;
    }

    @Override
    protected void onInitPresenters() {
        mApprovePresenter = new InvoiceApprovePresenter();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ObserverActionUtils.addAction(this);
        data = select().from(Invoice.class).where(Invoice_Table.requestType.eq(DISA)).or(Invoice_Table.requestType.eq(NEG)).orderBy(Invoice_Table.requestDate, false).queryList();
        mAdapter = new ApproveInvoiceListAdapter(mContext, R.layout.approve_invoice_list_item, data);
        mListView.setAdapter(mAdapter);
        if (process/*&& TCSApplication.currentUser.getRole()==0*/) {
            mListView.setOnItemClickListener(this);
        }

    }


    @OnClick({R.id.back, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.search:
                if (FastDoubleClickUtil.isFastDoubleClick(R.id.search)) break;
                SearchApproveInvoiceDialog searchApproveInvoiceDialog = new SearchApproveInvoiceDialog(mHandler);
                searchApproveInvoiceDialog.show(getFragmentManager(), "SearchApproveInvoiceDialog");
                break;
            case R.id.details:
                popupWindow.dismiss();
                InvoiceDetailsDialog invoiceDetailsDialog = new InvoiceDetailsDialog(invoice);
                invoiceDetailsDialog.setFlag(3);
                invoiceDetailsDialog.show(getFragmentManager(), "InvoiceDetailsDialog");
                break;
            case R.id.process:
                popupWindow.dismiss();
                InvoiceDetailsDialog invoiceDetailsDialog1 = new InvoiceDetailsDialog(invoice);
                if (invoice.getRequestType().equals(NEG) && invoice.getApproveFlag().equals("0")) {
                    invoiceDetailsDialog1.setFlag(2);
                } else {
                    invoiceDetailsDialog1.setFlag(4);
                }
                invoiceDetailsDialog1.show(getFragmentManager(), "InvoiceDetailsDialog1");
                break;
        }
    }

    public void setProcess(boolean process) {
        this.process = process;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ObserverActionUtils.addAction(this);
            refresh();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        invoice = data.get(i);
        LinearLayout linearLayout = null;
        if (((TCSApplication.currentUser.getRole() == 0 && invoice.getApproveFlag().equals("2") && invoice.getStatus().equals(AVL)) || (invoice.getRequestType().equals(NEG) && invoice.getApproveFlag().equals("0") && invoice.getStatus().equals(AVL)))) {
            linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.approve_list_popuwindow_layout, null);
        } else {
            linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.product_popuwindow1_layout, null);
        }
        AppTools.measureView(linearLayout);
        int w = linearLayout.getMeasuredWidth();
        int h = linearLayout.getMeasuredHeight();
        popupWindow = AppTools.getPopupWindow(linearLayout, w, h);
        popupWindow.showAsDropDown(view, (int) ((view.getWidth() - w) / 2f), (int) -(view.getHeight() + h * 3f / 5));
        linearLayout.findViewById(R.id.details).setOnClickListener(this);
        TextView process = linearLayout.findViewById(R.id.process);
        if (process != null) process.setOnClickListener(this);
    }

    private void refresh() {
        data = select().from(Invoice.class).where(Invoice_Table.requestType.eq(DISA)).or(Invoice_Table.requestType.eq(NEG)).orderBy(Invoice_Table.requestDate, false).queryList();
        mAdapter.setData(data);
    }

    @Override
    public void call(Object o) {
        refresh();
        if (((int) o) == 1) {
            if (invoice.getRequestType().equals(DISA)) {
                mApprovePresenter.cancellation(invoice);
            } else if (invoice.getRequestType().equals(NEG)) {
                mApprovePresenter.negative(invoice);
            }
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SearchApproveInvoiceCondition mSearchApproveInvoiceCondition = (SearchApproveInvoiceCondition) msg.obj;
            Where where = select().from(Invoice.class).where(Invoice_Table.requestType.in(NEG,DISA)).orderBy(Invoice_Table.requestDate, false);
            if (!TextUtils.isEmpty(mSearchApproveInvoiceCondition.getInvoiceCode())) {
                where = where.and(Invoice_Table.invoice_type_code.eq(mSearchApproveInvoiceCondition.getInvoiceCode()));
            }

            if (!TextUtils.isEmpty(mSearchApproveInvoiceCondition.getInvoiceNo())) {
                where = where.and(Invoice_Table.invoice_no.eq(mSearchApproveInvoiceCondition.getInvoiceNo()));
            }

            if (!TextUtils.isEmpty(mSearchApproveInvoiceCondition.getRequestType())) {
                where = where.and(Invoice_Table.requestType.eq(mSearchApproveInvoiceCondition.getRequestType()));
            }

            if (!TextUtils.isEmpty(mSearchApproveInvoiceCondition.getStatus())) {
                where = where.and(Invoice_Table.approveFlag.eq(mSearchApproveInvoiceCondition.getStatus()));
            }

            if (!TextUtils.isEmpty(mSearchApproveInvoiceCondition.getDateFrom()) && TextUtils.isEmpty(mSearchApproveInvoiceCondition.getDateTo())) {
                where = where.and(Invoice_Table.requestDate.between(mSearchApproveInvoiceCondition.getDateFrom()).and(DateUtils.getDateToString_YYYY_MM_DD_EN(DateUtils.getDateNow())));
            }

            if (!TextUtils.isEmpty(mSearchApproveInvoiceCondition.getDateFrom()) && !TextUtils.isEmpty(mSearchApproveInvoiceCondition.getDateTo())) {
                where = where.and(Invoice_Table.requestDate.between(mSearchApproveInvoiceCondition.getDateFrom()).and(mSearchApproveInvoiceCondition.getDateTo()));
            }

            data.clear();
            List list = where.queryList();
            data.addAll(list);
            mAdapter.notifyDataSetChanged();

        }
    };


}
