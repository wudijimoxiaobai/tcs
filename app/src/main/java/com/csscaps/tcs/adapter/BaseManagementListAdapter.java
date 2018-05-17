package com.csscaps.tcs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.common.utils.AppTools;
import com.csscaps.tcs.R;

import java.util.ArrayList;
import java.util.List;

import static com.tax.fcr.library.utils.NetworkUtils.mContext;

/**
 * Created by tl on 2018/5/17.
 *
 */

public abstract class BaseManagementListAdapter<T> extends QuickAdapter<T> {

    /*是否显示行 选择框*/
    private boolean isShowCheckBox;
    /*是否全选*/
    private boolean mAllSelect;
    /*已选数据*/
    private List<T> checkedTList;

    public BaseManagementListAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
        checkedTList=new ArrayList<>();
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final T item, final int position) {
        CheckBox no = helper.getView(R.id.no);
        AppTools.expandViewTouchDelegate(no, 100, 100, 50, 50);
        no.setText(String.valueOf(position + 1));
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!checkedTList.contains(item)) {
                        checkedTList.add(item);
                    }
                } else{
                    checkedTList.remove(item);
                }

            }
        });


        if (isShowCheckBox) {
            no.setText(null);
            no.setButtonDrawable(ContextCompat.getDrawable(mContext, R.drawable.cb_check_selector));

            if (checkedTList.contains(item)||mAllSelect) {
                no.setChecked(true);
            }

            if (!checkedTList.contains(item)) {
                no.setChecked(false);
            }


        } else {
            no.setButtonDrawable(ContextCompat.getDrawable(mContext, android.R.color.transparent));
        }
    }


    public void setShowCheckBox(boolean showCheckBox) {
        isShowCheckBox = showCheckBox;
        notifyDataSetChanged();
    }

    public void setAllSelect(boolean allSelect) {
        mAllSelect = allSelect;
        if (allSelect) {
            checkedTList.addAll(data);
        }else{
            checkedTList.clear();
        }
        notifyDataSetChanged();
    }

    public List<T> getCheckedList() {
        return checkedTList;
    }
}
