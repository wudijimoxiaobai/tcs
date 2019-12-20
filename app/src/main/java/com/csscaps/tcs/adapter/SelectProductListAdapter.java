package com.csscaps.tcs.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csscaps.common.DecimalDigitsInputFilter;
import com.csscaps.common.baseadapter.BaseAdapterHelper;
import com.csscaps.common.baseadapter.QuickAdapter;
import com.csscaps.common.utils.AppTools;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.dialog.SelecProductListAdapterEditetxtDialog;

import java.util.ArrayList;
import java.util.List;

public class SelectProductListAdapter extends QuickAdapter<Product> implements TextWatcher {

    private List<Product> checkedList = new ArrayList<>();
    private InputFilter inputFilter[] = new InputFilter[]{new DecimalDigitsInputFilter(2)};
    private CheckBox mCheckBox;
    private Context mContext;

    public SelectProductListAdapter(Context context, int layoutResId, List<Product> data) {
        super(context, layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, final Product item, final int position) {
        final int[] z = {0};

        helper.setText(R.id.product_list_name, item.getProductName());
        helper.setText(R.id.unit, item.getUnit());

        final EditText quantity = helper.getView(R.id.quantity);
        final TextView priceET = helper.getView(R.id.price);
        final CheckBox checkBox = helper.getView(R.id.checkbox);
        LinearLayout add = helper.getView(R.id.add);
        final LinearLayout minus = helper.getView(R.id.minus);
        AppTools.expandViewTouchDelegate(checkBox, 100, 100, 100, 100);
        quantity.setFilters(inputFilter);
        priceET.setFilters(inputFilter);
        //quantity.setText(item.getQuantity());

        String priceStr = item.getPrice();
        String unitDiscountPercentageStr = item.getUnitDiscountPercentage();
        String unitDiscountAmountStr = item.getUnitDiscountAmount();
        double price = TextUtils.isEmpty(priceStr) ? 0 : Double.valueOf(priceStr);
        double unitDiscountPercentage = TextUtils.isEmpty(unitDiscountPercentageStr) ? 0 : Double.valueOf(unitDiscountPercentageStr);
        double unitDiscountAmount = TextUtils.isEmpty(unitDiscountAmountStr) ? 0 : Double.valueOf(unitDiscountAmountStr);
        if (price != 0) {
            if (unitDiscountAmount != 0) {
                price = price - unitDiscountAmount;
            }

            if (unitDiscountPercentage != 0) {
                price = price * unitDiscountPercentage / 100;
            }

            price = Math.round(price * 100) * 0.01d;
        }

        priceET.setText(String.format("%.2f", price));
        if (TextUtils.isEmpty(item.getPrice())) {
            priceET.setEnabled(true);
        } else priceET.setEnabled(false);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                z[0]++;
                quantity.setVisibility(View.VISIBLE);
                minus.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.VISIBLE);
                quantity.setText(z[0] + "");
                checkBox.setChecked(false);
                checkBox.setChecked(true);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                z[0]--;
                if (z[0] == 0) {
                    quantity.setVisibility(View.GONE);
                    minus.setVisibility(View.GONE);
                    checkBox.setVisibility(View.GONE);
                    quantity.setText(z[0] + "");
                    checkBox.setChecked(true);
                    checkBox.setChecked(false);
                } else {
                    quantity.setText(z[0] + "");
                    checkBox.setChecked(false);
                    checkBox.setChecked(true);
//                    quantity.requestFocus();
//                    quantity.setSelection(quantity.getText().length()); //加上这句
                }
            }
        });

//        quantity.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                try {
//                    z[0] = Integer.parseInt(editable.toString().trim());
//                    if (!TextUtils.isEmpty(editable.toString())) {
//                        if (Integer.parseInt(editable.toString().trim()) != 0) {
//                            checkBox.setChecked(true);
//                        } else {
//                            checkBox.setChecked(false);
//                        }
//                    } else {
//                        checkBox.setChecked(false);
//                        quantity.setVisibility(View.GONE);
//                        minus.setVisibility(View.GONE);
//                        checkBox.setVisibility(View.GONE);
//                    }
//                } catch (NumberFormatException e) {
//                    z[0] = 0;
//                    e.printStackTrace();
//                }
//            }
//        });
        quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SelecProductListAdapterEditetxtDialog editetxtDialog = new SelecProductListAdapterEditetxtDialog(mContext);
                editetxtDialog.setnumber(quantity.getText().toString().trim());
                editetxtDialog.setYesOnclickListener(new SelecProductListAdapterEditetxtDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        Log.e("edittext", editetxtDialog.getnumber());
                        if (!TextUtils.isEmpty(editetxtDialog.getnumber())) {
                            if (Integer.parseInt(editetxtDialog.getnumber().trim()) != 0) {
                                z[0] = Integer.parseInt(editetxtDialog.getnumber().trim());
                                quantity.setText(editetxtDialog.getnumber().trim());
                                checkBox.setChecked(false);
                                checkBox.setChecked(true);
                                editetxtDialog.dismiss();
                            } else {
                                editetxtDialog.dismiss();
                            }
                        } else {
                            editetxtDialog.dismiss();
                        }
                    }
                });
                editetxtDialog.setNoOnclickListener(new SelecProductListAdapterEditetxtDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        editetxtDialog.dismiss();
                    }
                });
                editetxtDialog.show();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!checkedList.contains(item)) {
                        item.setPrice(priceET.getText().toString().trim());
                        item.setQuantity(quantity.getText().toString().trim());
                        checkedList.add(item);
                    }
                } else {
                    checkedList.remove(item);
                }
            }
        });

        if (checkedList.contains(item)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        quantity.setOnFocusChangeListener(null);
        quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mCheckBox = checkBox;
            }
        });
        priceET.setOnFocusChangeListener(null);
        priceET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mCheckBox = checkBox;
                }
            }
        });

        priceET.removeTextChangedListener(this);
        quantity.removeTextChangedListener(this);
        priceET.addTextChangedListener(this);
        quantity.addTextChangedListener(this);
    }

    public List<Product> getCheckedList() {
        return checkedList;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mCheckBox != null && mCheckBox.isChecked()) {
            mCheckBox.setChecked(false);
            mCheckBox.setChecked(true);
        }
    }
}
