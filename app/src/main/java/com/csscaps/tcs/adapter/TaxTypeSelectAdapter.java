package com.csscaps.tcs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.TaxType;

import java.util.List;

/**
 * Created by tl on 2018/5/10.
 */

public class TaxTypeSelectAdapter extends QuickAdapter<TaxType> {

    private int selectedPosition=-1;
    private CheckBox mCheckBox;
    private TextView mTextView;

    public TaxTypeSelectAdapter(Context context, int layoutResId, List<TaxType> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TaxType item, final int position) {
        final TextView textView = helper.getView(R.id.text);
        final CheckBox checkBox = helper.getView(R.id.checkbox);
        textView.setText(item.getTaxtype_name());
        if("N".equals(item.getIs_taxable_item_related())){
            helper.setVisible(R.id.checkbox, true);
        }else{
            helper.setVisible(R.id.checkbox, false);

        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b && position != getSelectedPosition()) {
                    setSelectedPosition(position);
                    textView.setTextColor(ContextCompat.getColor(context, R.color.blue));
                    if (mCheckBox != null) mCheckBox.setChecked(false);
                    if (mTextView != null) mTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                    mCheckBox = checkBox;
                    mTextView = textView;
                }

                if (!b && position == getSelectedPosition()) {
                    mCheckBox = null;
                    mTextView = null;
                    setSelectedPosition(-1);
                    textView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                }
            }
        });

        if (position == selectedPosition) {
            checkBox.setChecked(true);
            textView.setTextColor(ContextCompat.getColor(context, R.color.blue));
            mCheckBox = checkBox;
            mTextView = textView;
        } else {
            checkBox.setChecked(false);
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
