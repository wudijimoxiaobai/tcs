package com.csscaps.tcs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

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

    /*已选数据*/
    protected List<T> checkedTList;

    public BaseManagementListAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
        checkedTList=new ArrayList<>();
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final T item, final int position) {

    }

    public List<T> getCheckedList() {
        return checkedTList;
    }
}
