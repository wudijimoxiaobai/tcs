package com.csscaps.tcs.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.TaxItemAdapter;
import com.csscaps.tcs.adapter.TaxTypeAdapter;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxItem_Table;
import com.csscaps.tcs.database.table.TaxType;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/8.
 * 税目展示
 */

public class TaxItemFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.h_scroll_view)
    HorizontalScrollView mHScrollView;
    @BindView(R.id.name_title)
    TextView mNameTitle;
    @BindView(R.id.code_title)
    TextView mCodeTitle;
    @BindView(R.id.description_title)
    TextView mDescriptionTitle;
    @BindView(R.id.parameter_title)
    TextView mParameterTitle;
    @BindView(R.id.tax_rate_title)
    TextView mTaxRateTitle;
    @BindView(R.id.formula_description_title)
    TextView mFormulaDescriptionTitle;
    @BindView(R.id.unit_title)
    TextView mUnitTitle;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.code)
    TextView mCode;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.parameter)
    TextView mParameter;
    @BindView(R.id.tax_rate)
    TextView mTaxRate;
    @BindView(R.id.formula_description)
    TextView mFormulaDescription;
    @BindView(R.id.unit)
    TextView mUnit;
    @BindView(R.id.tax_item_details_layout)
    ScrollView mTaxItemDetailsLayout;
    @BindView(R.id.list_layout)
    LinearLayout mListLayout;

    private List<TaxItem> mTaxItemList;
    private List<TaxType> mTaxTypeList;
    private int listViewW;

    @Override
    protected int getLayoutResId() {
        return R.layout.tax_item_fragment;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        listViewW = DeviceUtils.dip2Px(getContext(), 250);
        mTaxTypeList = select().from(TaxType.class).queryList();
        mTaxItemList = select().from(TaxItem.class).queryList();
        addTaxTypeList();
    }


    @OnClick({R.id.back, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.search:
                break;
        }
    }

    /**
     * 添加税种
     */
    private void addTaxTypeList() {
        ListView listView = (ListView) LayoutInflater.from(mContext).inflate(R.layout.tax_list_view, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(listViewW, LinearLayout.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(lp);
        View view1 = new View(mContext);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
        view1.setLayoutParams(lp2);
        view1.setBackgroundColor(Color.LTGRAY);
        mListLayout.addView(listView);
        mListLayout.addView(view1);
        TaxTypeAdapter taxTypeAdapter = new TaxTypeAdapter(mContext, R.layout.tax_item_layout, mTaxTypeList);
        listView.setAdapter(taxTypeAdapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mTaxItemDetailsLayout.setVisibility(View.INVISIBLE);
        int index = mListLayout.indexOfChild(adapterView);
        int count = mListLayout.getChildCount();
        for (int j = index + 2; j < count; j++) {
            mListLayout.removeViewAt(index + 2);
        }
        switch (index) {
            case 0://税种
                TaxTypeAdapter taxTypeAdapter = ((TaxTypeAdapter) adapterView.getAdapter());
                taxTypeAdapter.setSelectedPosition(i);
                taxTypeAdapter.notifyDataSetChanged();
                TaxType taxType = mTaxTypeList.get(i);
                String taxTypeId = taxType.getTaxtype_uid();
                List<TaxItem> list = SQLite.select().from(TaxItem.class).where(TaxItem_Table.taxtype_uid.eq(taxTypeId)).and(TaxItem_Table.node_level.eq("1")).queryList();
                addTaxItemList(list);
                break;
            default://税目
                TaxItemAdapter taxItemAdapter = (TaxItemAdapter) adapterView.getAdapter();
                taxItemAdapter.setSelectedPosition(i);
                taxItemAdapter.notifyDataSetChanged();
                List<TaxItem> data = taxItemAdapter.getData();
                TaxItem taxItem = data.get(i);
                if ("N".equals(taxItem.getIs_leaf())) {
                    String taxItemId = taxItem.getTaxable_item_uid();
                    List<TaxItem> list1 = SQLite.select().from(TaxItem.class).where(TaxItem_Table.parent_item_uidinteger.eq(taxItemId)).queryList();
                    addTaxItemList(list1);
                } else {
                    showTaxItemDetails(taxItem);
                }
                break;
        }
    }

    /**
     * 显示税目详情
     *
     * @param taxItem
     */
    private void showTaxItemDetails(TaxItem taxItem) {
        mName.setText(taxItem.getItem_name_in_english());
        mCode.setText(taxItem.getItem_codechar());
        mParameter.setText("");
        mDescription.setText(taxItem.getDescriptionchar());
        mTaxRate.setText(taxItem.getTax_rate());
        mFormulaDescription.setText(taxItem.getFormula_desc());
        mUnit.setText(taxItem.getUnit());
        mTaxItemDetailsLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 添加税目
     *
     * @param list
     */
    private void addTaxItemList(List<TaxItem> list) {
        ListView listView = (ListView) LayoutInflater.from(mContext).inflate(R.layout.tax_list_view, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(listViewW, LinearLayout.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(lp);
        View view = new View(mContext);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp1);
        view.setBackgroundColor(Color.LTGRAY);
        mListLayout.addView(listView);
        mListLayout.addView(view);
        TaxItemAdapter taxItemAdapter = new TaxItemAdapter(mContext, R.layout.tax_item_layout, list);
        listView.setAdapter(taxItemAdapter);
        listView.setOnItemClickListener(this);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int mListLayoutW = mListLayout.getMeasuredWidth();
                int mHScrollViewW = mHScrollView.getWidth();
                if (mListLayoutW > mHScrollViewW) {
                    int language = AppSP.getInt("language", 0);
                    switch (language) {
                        case 0:
                            mHScrollView.smoothScrollBy(mListLayoutW - mHScrollViewW, 0);
                            break;
                        case 1:
                            mHScrollView.smoothScrollBy(mHScrollViewW - mListLayoutW, 0);
                            break;
                    }
                }
            }
        });

    }


}
