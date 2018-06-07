package com.csscaps.tcs.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.csscaps.common.DecimalDigitsInputFilter;
import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.tcs.CalculateUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Product;

import java.util.List;

/**
 * Created by tl on 2018/5/24.
 */

public class InvoiceProductListAdapter extends BaseManagementListAdapter<Product> {

    private InputFilter inputFilter[]=new InputFilter[]{new DecimalDigitsInputFilter(2)};
    private int editPosition = -1;
    private int count;

    public InvoiceProductListAdapter(Context context, int layoutResId, List<Product> data) {
        super(context, layoutResId, data);
        count=this.data.size();
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final Product item, int position) {
        super.convert(helper, item, position);
        if (item.getProductModel() == null) {
            item.setProductModel(CalculateUtils.calculateProductTax(item));
        }
        helper.setText(R.id.product_name, item.getProductName());
        helper.setText(R.id.specification, item.getSpecification());
        helper.setText(R.id.unit, item.getUnit());
        final TextView eTax = helper.getView(R.id.e_tax);
        final TextView taxAmount = helper.getView(R.id.tax_Amount);
        final TextView iTax = helper.getView(R.id.i_tax);
        final TextView priceTextView = helper.getView(R.id.price_tv);
        final TextView quantityTextView = helper.getView(R.id.quantity_tv);
        final EditText quantityEditText = helper.getView(R.id.quantity);
        final EditText priceEditText = helper.getView(R.id.price);
        quantityEditText.setFilters(inputFilter);
        priceEditText.setFilters(inputFilter);

        quantityEditText.setText(item.getQuantity());
        priceEditText.setText(item.getPrice());
        quantityTextView.setText(TextUtils.isEmpty(item.getQuantity()) ? "0" : item.getQuantity());
        priceTextView.setText(TextUtils.isEmpty(item.getPrice()) ? "0" : item.getPrice());
        eTax.setText(item.geteTax());
        iTax.setText(item.getiTax());
        taxAmount.setText(item.getTotalTax());

        if (position == editPosition) {
            quantityEditText.setVisibility(View.VISIBLE);
            priceEditText.setVisibility(View.VISIBLE);
            priceTextView.setVisibility(View.GONE);
            quantityTextView.setVisibility(View.GONE);
        } else {
            quantityEditText.setVisibility(View.GONE);
            priceEditText.setVisibility(View.GONE);
            priceTextView.setVisibility(View.VISIBLE);
            quantityTextView.setVisibility(View.VISIBLE);
        }

        if (quantityEditText.getVisibility() == View.VISIBLE) {
            quantityEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    item.setQuantity(editable.toString().trim());
                    quantityTextView.setText(item.getQuantity());
                    item.setProductModel(CalculateUtils.calculateProductTax(item));
                    eTax.setText(item.geteTax());
                    iTax.setText(item.getiTax());
                    taxAmount.setText(item.getTotalTax());
                }
            });


            priceEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    item.setPrice(editable.toString().trim());
                    priceTextView.setText(item.getPrice());
                    item.setProductModel(CalculateUtils.calculateProductTax(item));
                    eTax.setText(item.geteTax());
                    iTax.setText(item.getiTax());
                    taxAmount.setText(item.getTotalTax());

                }
            });
        }

    }

    public void setEditPosition(int editPosition) {
        this.editPosition = editPosition;
    }

    @Override
    public void notifyDataSetChanged() {
        if(data.size()!=count) setEditPosition(-1);
        super.notifyDataSetChanged();
        count=this.data.size();
    }
}
