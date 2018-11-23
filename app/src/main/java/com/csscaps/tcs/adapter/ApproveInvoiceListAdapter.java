package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Invoice;

import java.util.List;

/**
 * Created by tl on 2018/6/19.
 */

public class ApproveInvoiceListAdapter extends QuickAdapter<Invoice> {
    public ApproveInvoiceListAdapter(Context context, int layoutResId, List<Invoice> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Invoice item, int position) {
        helper.setText(R.id.no, String.valueOf(position + 1));
        helper.setText(R.id.invoice_code, item.getInvoice_type_code());
        helper.setText(R.id.invoice_no, item.getInvoice_no());
        helper.setText(R.id.i_tax, String.format("%.2f",(Double.valueOf(item.getTotal_all())- Double.valueOf(item.getTotal_fee()))));
        helper.setText(R.id.issuing_date_time, DateUtils.dateToStr(DateUtils.getStringToDate(item.getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_dd_HH_mm_ss_24_EN));
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
