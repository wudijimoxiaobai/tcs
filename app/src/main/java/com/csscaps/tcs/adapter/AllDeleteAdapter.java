package com.csscaps.tcs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.common.utils.AppTools;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Product;

import java.util.ArrayList;
import java.util.List;

import static com.tax.fcr.library.utils.NetworkUtils.mContext;

public class AllDeleteAdapter extends QuickAdapter<Product> {

    private List<Product> checkedList = new ArrayList<>();
    private CheckBox mCheckBox;
    /*是否显示行 选择框*/
    protected boolean isShowCheckBox;
    /*是否全选*/
    protected boolean mAllSelect = false;

    public AllDeleteAdapter(Context context, int layoutResId, List<Product> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final Product item, int position) {
        helper.setText(R.id.product_name, item.getProductName());
        CheckBox checkBox = helper.getView(R.id.checkbox);
        AppTools.expandViewTouchDelegate(checkBox, 100, 100, 100, 100);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!checkedList.contains(item)) {
                        checkedList.add(item);
                    }
//                    if (!checkedList.contains(item)) {
//                        checkedList.remove(item);
//                    }
                } else {
                    if (checkedList.contains(item)) {
                        checkedList.remove(item);
                    }
                    //   checkedList.add(item);

                }
            }
        });

        if (checkedList.contains(item) || mAllSelect) {
            checkBox.setChecked(true);
        }

        if (!checkedList.contains(item)) {
            checkBox.setChecked(false);
        }
    }

    public void setAllSelect(boolean allSelect) {
        mAllSelect = allSelect;
        if (allSelect) {
            checkedList.addAll(data);
        } else {
            checkedList.clear();
        }
        notifyDataSetChanged();
    }


    public List<Product> getCheckedList() {
        return checkedList;
    }

}
