package com.csscaps.tcs.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceQueryCelectAdapter extends QuickAdapter<Invoice> {

    private List<Invoice> checkedList = new ArrayList<>();
    private CheckBox mCheckBox;
    /*是否显示行 选择框*/
    protected boolean isShowCheckBox;
    /*是否全选*/
    protected boolean mAllSelect = false;

    public InvoiceQueryCelectAdapter(Context context, int layoutResId, List<Invoice> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final Invoice item, int position) {
        helper.setText(R.id.invoice_code, item.getInvoice_type_code());
        helper.setText(R.id.invoice_no, item.getInvoice_no());
        helper.setText(R.id.issuing_date_time, DateUtils.dateToStr(DateUtils.getStringToDate(item.getClient_invoice_datetime(), DateUtils.format_yyyyMMddHHmmss_24_EN), DateUtils.format_yyyy_MM_dd_HH_mm_ss_24_EN));
        helper.setText(R.id.tax_Amount, String.format("%.2f", (Double.valueOf(item.getTotal_tax_due()) - Double.valueOf(item.getTotal_fee()))));
        ImageView iv = helper.getView(R.id.upload_status);
        if (item.getUploadStatus().equals("1")) {
            iv.setImageResource(R.drawable.yy);
        } else {
            iv.setImageResource(R.drawable.nn);
        }
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


    public List<Invoice> getCheckedList() {
        return checkedList;
    }

}
