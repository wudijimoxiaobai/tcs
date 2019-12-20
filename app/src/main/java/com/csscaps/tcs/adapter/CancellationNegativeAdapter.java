package com.csscaps.tcs.adapter;

import android.content.Context;
import android.util.Log;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.model.CancelNegativeNodel;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class CancellationNegativeAdapter extends QuickAdapter<CancelNegativeNodel> {
    public CancellationNegativeAdapter(Context context, int layoutResId, List<CancelNegativeNodel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, CancelNegativeNodel item, int position) {
       helper.setText(R.id.type,item.getType());
       helper.setText(R.id.amount,item.getAmout());
       helper.setText(R.id.qty,item.getQty());
       helper.setText(R.id.percentage,item.getPercentage());
    }
}
