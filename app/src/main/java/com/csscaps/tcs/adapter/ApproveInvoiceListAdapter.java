package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Invoice;

import java.util.List;

public class ApproveInvoiceListAdapter extends QuickAdapter<Invoice> {
    public ApproveInvoiceListAdapter(Context context, int layoutResId, List<Invoice> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Invoice item, int position) {
        helper.setText(R.id.invoice_no, item.getInvoice_no());
        helper.setText(R.id.request_date, item.getRequestDate());
        helper.setText(R.id.request_by, item.getRequestBy());
        switch (item.getApproveFlag()) {
            case "0":
                helper.setText(R.id.status, context.getResources().getString(R.string.pass));
                break;
            case "1":
                helper.setText(R.id.status, context.getResources().getString(R.string.reject));
                break;
            case "2":
                helper.setText(R.id.status, context.getResources().getString(R.string.to_confirm));
                break;
            case "3":
                helper.setText(R.id.status, context.getResources().getString(R.string.internal_reject));
                break;
            case "4":
                helper.setText(R.id.status, context.getResources().getString(R.string.pending_approval));
                break;
        }

        switch (item.getRequestType()) {
            case "DISA":
                helper.setText(R.id.request_type, context.getResources().getString(R.string.DISA));
                break;
            case "NEG":
                helper.setText(R.id.request_type, context.getResources().getString(R.string.NEG));
                break;
        }
    }
}

