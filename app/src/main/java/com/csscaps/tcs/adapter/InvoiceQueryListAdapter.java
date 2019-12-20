package com.csscaps.tcs.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Invoice;

import java.util.List;

public class InvoiceQueryListAdapter extends BaseManagementListAdapter<Invoice> {

    public InvoiceQueryListAdapter(Context context, int layoutResId, List<Invoice> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Invoice item, int position) {
        super.convert(helper, item, position);
        helper.setText(R.id.invoice_code, item.getInvoice_type_code());
        helper.setText(R.id.invoice_no, item.getInvoice_no());
        helper.setText(R.id.issuing_date_time, DateUtils.dateToStr(DateUtils.getStringToDate(item.getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_dd_HH_mm_ss_24_EN));
        helper.setText(R.id.tax_Amount, String.format("%.2f", (Double.valueOf(item.getTotal_tax_due()) - Double.valueOf(item.getTotal_fee()))));
        ImageView iv = helper.getView(R.id.upload_status);
        if (item.getUploadStatus().equals("1")) {
            iv.setImageResource(R.drawable.yy);
        } else {
            iv.setImageResource(R.drawable.nn);
        }
        //AVL:normal, DISA:cancelled, NEG:negative, WRO:wrote off, IVLD:invalid, IRR:illegal
    }
}