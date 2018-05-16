package com.csscaps.tcs.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.csscaps.common.base.BaseFragment;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.ProductListAdapter;
import com.csscaps.tcs.database.TcsDatabase;
import com.csscaps.tcs.database.table.Product;
import com.csscaps.tcs.dialog.AddProductDialog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;

import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/8.
 */

public class ProductManagementFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, Action1<Product>, AdapterView.OnItemClickListener {

    @BindView(R.id.back)
    TextView mBack;
    @BindView(R.id.add)
    TextView mAdd;
    @BindView(R.id.select)
    TextView mSelect;
    @BindView(R.id.all_select)
    CheckBox mAllSelect;
    @BindView(R.id.list_view)
    ListView mListView;

    private List<Product> data;
    private ProductListAdapter mProductListAdapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.product_mangement_fragment;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBack.setTag(0);
        mSelect.setTag(0);
        AppTools.expandViewTouchDelegate(mAllSelect, 100, 100, 100, 100);
        mAllSelect.setOnCheckedChangeListener(this);
        ObserverActionUtils.addAction(this);
        data = select().from(Product.class).queryList();
        mProductListAdapter = new ProductListAdapter(mContext, R.layout.product_list_item, data);
        mListView.setAdapter(mProductListAdapter);
        mListView.setOnItemClickListener(this);
    }


    @OnClick({R.id.back, R.id.add, R.id.select})
    public void onClick(View view) {
        int tag = 0;
        try {
            tag = (int) view.getTag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (view.getId()) {
            case R.id.back:
                switch (tag) {
                    case 0:
                        getActivity().finish();
                        break;
                    case 1:
                        mBack.setTag(0);
                        mSelect.setTag(0);
                        mBack.setText(getResources().getString(R.string.back));
                        mSelect.setText((getResources().getString(R.string.select)));
                        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext, android.R.color.transparent));
                        mProductListAdapter.setShowCheckBox(false);
                        mAllSelect.setText(getString(R.string.no));
                        mAllSelect.setChecked(false);
                        mAdd.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case R.id.add:
                AddProductDialog mAddProductDialog = new AddProductDialog();
                mAddProductDialog.show(getChildFragmentManager(), "AddProductDialog");
                break;
            case R.id.select:
                switch (tag) {
                    case 0:
                        mBack.setTag(1);
                        mSelect.setTag(1);
                        mBack.setText(getResources().getString(R.string.cancel));
                        mSelect.setText((getResources().getString(R.string.delete)));
                        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext, R.drawable.cb_check_selector));
                        mAllSelect.setText(null);
                        mProductListAdapter.setShowCheckBox(true);
                        mAdd.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        List<Product> list = mProductListAdapter.getCheckedProductList();
                        FlowManager.getDatabase(TcsDatabase.class)
                                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                                        new ProcessModelTransaction.ProcessModel<Product>() {
                                            @Override
                                            public void processModel(Product model, DatabaseWrapper wrapper) {
                                                model.delete();
                                            }
                                        }).addAll(list).build())
                                .build()
                                .execute();
                        data.removeAll(list);
                        list.clear();
                        mProductListAdapter.notifyDataSetChanged();
                        break;
                }
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            mProductListAdapter.setAllSelect(true);
        } else {
            mProductListAdapter.setAllSelect(false);
        }
    }

    @Override
    public void call(Product product) {
        if (!data.contains(product)) data.add(product);
        mProductListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.product_popuwindow_layout, null);
        AppTools.measureView(linearLayout);
        int w = linearLayout.getMeasuredWidth();
        int h = linearLayout.getMeasuredHeight();
        final PopupWindow popupWindow = AppTools.getPopupWindow(linearLayout, w, h);
        popupWindow.showAsDropDown(view, (int) ((view.getWidth() - w) / 2f), (int) -(view.getHeight() + h * 3f / 5));
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Product product = data.get(i);
                switch (view.getId()) {
                    case R.id.edit:
                        AddProductDialog mAddProductDialog = new AddProductDialog();
                        mAddProductDialog.edit(product);
                        mAddProductDialog.show(getChildFragmentManager(), "AddProductDialog");
                        break;
                    case R.id.delete:
                        if (product.delete()) {
                            data.remove(i);
                            mProductListAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        };
        linearLayout.findViewById(R.id.edit).setOnClickListener(clickListener);
        linearLayout.findViewById(R.id.delete).setOnClickListener(clickListener);
    }
}
