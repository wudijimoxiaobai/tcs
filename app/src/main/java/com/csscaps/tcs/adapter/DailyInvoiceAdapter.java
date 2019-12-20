package com.csscaps.tcs.adapter;

import android.content.Context;
import android.util.Log;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Invoice;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class DailyInvoiceAdapter extends QuickAdapter<Invoice> {
    public DailyInvoiceAdapter(Context context, int layoutResId, List<Invoice> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Invoice item, int position) {
        helper.setText(R.id.no, position + 1+"");
        helper.setText(R.id.invoice_code,item.getInvoice_type_code());
        helper.setText(R.id.buyer,item.getPurchaser_name());
        helper.setText(R.id.timer, DateUtils.dateToStr(DateUtils.getStringToDate(item.getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_HH_mm_ss_24_EN));
        helper.setText(R.id.total,item.getTotal_all());
        helper.setText(R.id.tax,item.getTotal_taxable_amount());
    }
}
