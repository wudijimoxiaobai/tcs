package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.model.OperatorReportSection;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class OPeratorReportAdapter extends QuickAdapter<OperatorReportSection> {
    public OPeratorReportAdapter(Context context, int layoutResId, List<OperatorReportSection> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, OperatorReportSection item, int position) {
       helper.setText(R.id.period,item.getOperator());
       helper.setText(R.id.normal_amount,item.getNormal_Amount());
       helper.setText(R.id.cancellation_amount,item.getCancellation_Amount());
       helper.setText(R.id.negative_amount,item.getNegative_Amount());
    }
}
