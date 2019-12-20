package com.csscaps.tcs.dialog;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.ControlData;

import java.util.List;

public class OnlineDeclarationAdapter extends QuickAdapter<ControlData> {

    public OnlineDeclarationAdapter(Context context, int layoutResId, List<ControlData> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, ControlData item, int position) {
        helper.setText(R.id.invoice_deadline, DateUtils.getDateToString_YYYY_MM_DD_EN(DateUtils.getStringToDate(item.getIssuing_last_date(), DateUtils.format_yyyyMMdd)));
        helper.setText(R.id.invoice_code, item.getInvoice_type_code());
        helper.setText(R.id.deadline_date_to, DateUtils.getDateToString_YYYY_MM_DD_EN(DateUtils.getStringToDate(item.getE_report_date(), DateUtils.format_yyyyMMdd)));
        helper.setText(R.id.deadline_date, DateUtils.getDateToString_YYYY_MM_DD_EN(DateUtils.getStringToDate(item.getNew_date(), DateUtils.format_yyyyMMdd)));
    }
}
