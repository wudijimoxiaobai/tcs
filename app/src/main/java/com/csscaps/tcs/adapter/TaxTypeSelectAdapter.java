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
import com.csscaps.tcs.database.table.TaxType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tl on 2018/5/10.
 */

public class TaxTypeSelectAdapter extends QuickAdapter<TaxType> {

    private int mSelectedPosition = -1;
    private CheckBox mCheckBox;
    private TextView mTextView;
    private Handler mHandler;
    private List<String> list = new ArrayList<>();

    public TaxTypeSelectAdapter(Context context, int layoutResId, List<TaxType> data, Handler handler) {
        super(context, layoutResId, data);
        mHandler = handler;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final TaxType item, final int position) {
        final TextView textView = helper.getView(R.id.text);
        final CheckBox checkBox = helper.getView(R.id.checkbox);
        textView.setText(item.getTaxtype_name());
        if ("N".equals(item.getIs_taxable_item_related())) {
            helper.setVisible(R.id.checkbox, true);
            checkBox.setEnabled(true);
        } else {
            helper.setVisible(R.id.checkbox, false);
            checkBox.setEnabled(false);
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String positionString = String.valueOf(position);
                if (b && !list.contains(positionString)) {
//                    setSelectedPosition(position);
//                    textView.setTextColor(ContextCompat.getColor(context, R.color.blue));
//                    if (mCheckBox != null) mCheckBox.setChecked(false);
//                    if (mTextView != null)
//                        mTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
//                    mCheckBox = checkBox;
//                    mTextView = textView;
                    list.add(positionString);
                    mHandler.sendMessage(mHandler.obtainMessage(1, item.getTaxtype_uid()));
                }

                if (!b && list.contains(positionString)) {
//                    mCheckBox = null;
//                    mTextView = null;
                    list.remove(positionString);
                    mHandler.sendMessage(mHandler.obtainMessage(0, item.getTaxtype_uid()));
//                    textView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                }
            }
        });

        if (position == mSelectedPosition) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.blue1));
//            mCheckBox = checkBox;
//            mTextView = textView;
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.text1));
        }

        if (list.contains(String.valueOf(position))) {
            checkBox.setChecked(true);
        } else checkBox.setChecked(false);
    }

    public void setSelectedPosition(int selectedPosition) {
//        if (selectedPosition != -1) {
//            TaxType taxType = getData().get(selectedPosition);
//            if ("N".equals(taxType.getIs_taxable_item_related())) {
//                mHandler.sendMessage(mHandler.obtainMessage(1, taxType.getTaxtype_uid()));
//            }
//        } else {
//            TaxType taxType = getData().get(mSelectedPosition);
//            mHandler.sendMessage(mHandler.obtainMessage(0, taxType.getTaxtype_uid()));
//        }
        this.mSelectedPosition = selectedPosition;

    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }
}
