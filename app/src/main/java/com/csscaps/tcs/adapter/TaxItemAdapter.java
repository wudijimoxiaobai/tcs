package com.csscaps.tcs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.TaxItem;

import java.util.List;

/**
 * Created by tl on 2018/5/10.
 */

public class TaxItemAdapter extends QuickAdapter<TaxItem> {

    private int selectedPosition=-1;

    public TaxItemAdapter(Context context, int layoutResId, List<TaxItem> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TaxItem item, final int position) {
        helper.setText(R.id.text,item.getItem_name_in_english());

        if(position==selectedPosition){
            helper.setTextColor(R.id.text, ContextCompat.getColor(context,R.color.blue));
        }else{
            helper.setTextColor(R.id.text, ContextCompat.getColor(context,android.R.color.black));
        }

    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
