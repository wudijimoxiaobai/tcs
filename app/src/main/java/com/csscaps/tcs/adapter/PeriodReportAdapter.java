package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.model.PeriodReportSection;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class PeriodReportAdapter extends QuickAdapter<PeriodReportSection> {
    public PeriodReportAdapter(Context context, int layoutResId, List<PeriodReportSection> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, PeriodReportSection item, int position) {
       helper.setText(R.id.period,item.getPeriod());
       helper.setText(R.id.normal_amount,item.getNormal_Amount());
       helper.setText(R.id.cancellation_amount,item.getCancellation_Amount());
       helper.setText(R.id.negative_amount,item.getNegative_Amount());
    }
}
