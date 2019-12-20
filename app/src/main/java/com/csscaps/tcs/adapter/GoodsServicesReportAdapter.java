package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.model.GoodsServicesReportSection;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class GoodsServicesReportAdapter extends QuickAdapter<GoodsServicesReportSection> {
    public GoodsServicesReportAdapter(Context context, int layoutResId, List<GoodsServicesReportSection> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, GoodsServicesReportSection item, int position) {
        helper.setText(R.id.no, position + 1 + "");
        helper.setText(R.id.item, item.getGoods_services());
        helper.setText(R.id.ta, item.getTA());
        helper.setText(R.id.qty, item.getQty());
        helper.setText(R.id.vat, item.getVAT());
        helper.setText(R.id.other_tax, (other(item.getBPT_F()) + other(item.getBPT_P()) + other(item.getSD_F()) + other(item.getSD_L()) + other(item.getFee())) + "");
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
