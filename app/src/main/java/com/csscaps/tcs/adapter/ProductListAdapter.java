package com.csscaps.tcs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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

/**
 * Created by tl on 2018/5/16.
 */

public class ProductListAdapter extends QuickAdapter<Product> {

    private boolean isShowCheckBox;
    private boolean mAllSelect;
    private List<Product> checkedProductList;


    public ProductListAdapter(Context context, int layoutResId, List<Product> data) {
        super(context, layoutResId, data);
        checkedProductList = new ArrayList<>();
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final Product item, final int position) {
        CheckBox no = helper.getView(R.id.no);
        AppTools.expandViewTouchDelegate(no, 100, 100, 50, 50);
        no.setText(String.valueOf(position + 1));
        helper.setText(R.id.product_name, item.getProductName());
        helper.setText(R.id.local_name, item.getLocalName());
        helper.setText(R.id.specification, item.getSpecification());
        helper.setText(R.id.unit, item.getUnit());
        helper.setText(R.id.price, String.valueOf(item.getPrice()));
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!checkedProductList.contains(item)) {
                        checkedProductList.add(item);
                    }
                } else{
                    checkedProductList.remove(item);
                }

            }
        });


        if (isShowCheckBox) {
            no.setText(null);
            no.setButtonDrawable(ContextCompat.getDrawable(mContext, R.drawable.cb_check_selector));

            if (checkedProductList.contains(item)||mAllSelect) {
                no.setChecked(true);
            }

            if (!checkedProductList.contains(item)) {
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
            checkedProductList.addAll(data);
        }else{
            checkedProductList.clear();
        }
        notifyDataSetChanged();
    }

    public List<Product> getCheckedProductList() {
        return checkedProductList;
    }
}
