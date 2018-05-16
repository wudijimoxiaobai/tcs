package com.csscaps.tcs.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.TaxItem;

import java.util.List;

/**
 * Created by tl on 2018/5/10.
 */

public class TaxItemSelectAdapter extends QuickAdapter<TaxItem> {

    private int mSelectedPosition = -1;
    private CheckBox mCheckBox;
    private TextView mTextView;
    private Handler mHandler;
    private String flag;

    public TaxItemSelectAdapter(Context context, int layoutResId, List<TaxItem> data, Handler handler) {
        super(context, layoutResId, data);
        mHandler = handler;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TaxItem item, final int position) {
        final TextView textView = helper.getView(R.id.text);
        final CheckBox checkBox = helper.getView(R.id.checkbox);
        textView.setText(item.getItem_name_in_english());
        if ("N".equals(item.getIs_leaf())) {
            helper.setVisible(R.id.checkbox, false);
        } else {
            helper.setVisible(R.id.checkbox, true);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b && position != getSelectedPosition()) {
                    setSelectedPosition(position);
                    textView.setTextColor(ContextCompat.getColor(context, R.color.blue));
                    if (mCheckBox != null) mCheckBox.setChecked(false);
                    if (mTextView != null)
                        mTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
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

        if (position == mSelectedPosition) {
            checkBox.setChecked(true);
            textView.setTextColor(ContextCompat.getColor(context, R.color.blue));
            mCheckBox = checkBox;
            mTextView = textView;
        } else {
            checkBox.setChecked(false);
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }

    }

    public void initSelectedPosition(int selectedPosition) {
        mSelectedPosition = selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {

        if (selectedPosition != -1) {
            TaxItem taxItem = getData().get(selectedPosition);
            if ("Y".equals(taxItem.getIs_leaf())) {
                if (mSelectedPosition != -1) {
                    TaxItem selectedTaxItem = getData().get(mSelectedPosition);
                    mHandler.sendMessage(mHandler.obtainMessage(0, flag + "," + selectedTaxItem.getTaxable_item_uid() + "," + mSelectedPosition));
                }
                mHandler.sendMessage(mHandler.obtainMessage(1, flag + "," + taxItem.getTaxable_item_uid() + "," + selectedPosition));
            }
        } else {
            TaxItem selectedTaxItem = getData().get(mSelectedPosition);
            mHandler.sendMessage(mHandler.obtainMessage(0, flag + "," + selectedTaxItem.getTaxable_item_uid() + "," + mSelectedPosition));
        }
        mSelectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
