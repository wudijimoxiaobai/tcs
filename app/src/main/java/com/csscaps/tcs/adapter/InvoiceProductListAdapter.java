package com.csscaps.tcs.adapter;

import android.content.Context;
import android.text.InputFilter;

import com.csscaps.common.DecimalDigitsInputFilter;
import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.tcs.CalculateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Product;

import java.util.List;

public class InvoiceProductListAdapter extends BaseManagementListAdapter<Product> {

    private InputFilter inputFilter[] = new InputFilter[]{new DecimalDigitsInputFilter(2)};
    private int editPosition = -1;
    private int count;

    public InvoiceProductListAdapter(Context context, int layoutResId, List<Product> data) {
        super(context, layoutResId, data);
        count = this.data.size();
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final Product item, int position) {
        super.convert(helper, item, position);
        if (item.getProductModel() == null) {
            item.setProductModel(CalculateUtils.calculateProductTax(item));
        }
        helper.setText(R.id.no, item.getProductName());
        helper.setText(R.id.price, item.getPrice());
        helper.setText(R.id.qty, item.getQuantity())
                .setText(R.id.all_price, item.getiTax())
                .setText(R.id.amount, item.getTotalTax());
    }

    public void setEditPosition(int editPosition) {
        this.editPosition = editPosition;
    }

    @Override
    public void notifyDataSetChanged() {
        if (data.size() != count) setEditPosition(-1);
        super.notifyDataSetChanged();
        count = this.data.size();
    }
}
