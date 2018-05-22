package com.csscaps.tcs.adapter;

import android.content.Context;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.model.InvoiceNoModel;

import java.util.List;

/**
 * Created by tl on 2018/5/22.
 */

public class InvoiceNoAdapter extends QuickAdapter<InvoiceNoModel> {
    public InvoiceNoAdapter(Context context, int layoutResId, List<InvoiceNoModel> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, InvoiceNoModel item, int position) {

    }
}
