package com.csscaps.tcs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.TaxType;

import java.util.List;

public class TaxTypeAdapter extends QuickAdapter<TaxType> {

    private int selectedPosition=-1;

    public TaxTypeAdapter(Context context, int layoutResId, List<TaxType> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TaxType item, int position) {
        helper.setText(R.id.text, item.getTaxtype_name());
        if(position==selectedPosition){
            helper.setTextColor(R.id.text, ContextCompat.getColor(context,R.color.new_text2));
        }else{
            helper.setTextColor(R.id.text, ContextCompat.getColor(context,R.color.text1));
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
