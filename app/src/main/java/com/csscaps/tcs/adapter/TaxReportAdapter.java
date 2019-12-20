package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.model.TaxReportSection;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class TaxReportAdapter extends QuickAdapter<TaxReportSection> {
    public TaxReportAdapter(Context context, int layoutResId, List<TaxReportSection> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TaxReportSection item, int position) {
        helper.setText(R.id.tax_period, item.getPeriod());
        helper.setText(R.id.tax_ta, item.getTA());
        helper.setText(R.id.tax_vat, item.getVAT());
        helper.setText(R.id.tax_other, (other(item.getBPT_F()) + other(item.getBPT_P()) + other(item.getSD_F()) + other(item.getSD_L()) + other(item.getFee())) + "");
    }

    private int other(String str) {
        int a = 0;
        try {
            a = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return a;
    }
}
