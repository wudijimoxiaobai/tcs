package com.csscaps.tcs.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.TaxItemSelectAdapter;
import com.csscaps.tcs.adapter.TaxTypeSelectAdapter;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxItem_Table;
import com.csscaps.tcs.database.table.TaxType;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;
import static com.tax.fcr.library.utils.NetworkUtils.mContext;

/**
 * Created by tl on 2018/5/14.
 * 添加商品
 */

public class SelectTaxItemDialog extends AppCompatDialogFragment implements AdapterView.OnItemClickListener {


    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.select)
    TextView mSelect;
    @BindView(R.id.search)
    EditText mSearch;
    @BindView(R.id.list_layout)
    LinearLayout mListLayout;
    @BindView(R.id.h_scroll_view)
    HorizontalScrollView mHScrollView;

    private List<TaxItem> mTaxItemList;
    private List<TaxType> mTaxTypeList;
    private int listViewW;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_tax_item, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width= (int) (DeviceUtils.getScreenWidth(getContext())*0.7f);
        int height= (int) (DeviceUtils.getScreenHeight(getContext())*0.8f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }



    private void initView() {
        listViewW = DeviceUtils.dip2Px(getContext(), 250);
        mTaxTypeList = select().from(TaxType.class).queryList();
        mTaxItemList = select().from(TaxItem.class).queryList();
        addTaxTypeList();
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
//        mListLayout.addView(view1);
        TaxTypeSelectAdapter taxTypeAdapter = new TaxTypeSelectAdapter(mContext, R.layout.tax_item_select_layout, mTaxTypeList);
        listView.setAdapter(taxTypeAdapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int index = mListLayout.indexOfChild(adapterView);
        int count = mListLayout.getChildCount();
        for (int j = index + 1; j < count; j++) {
            mListLayout.removeViewAt(index + 1);
        }
        switch (index) {
            case 0://税种
                TaxTypeSelectAdapter taxTypeAdapter = ((TaxTypeSelectAdapter) adapterView.getAdapter());
                taxTypeAdapter.setSelectedPosition(i);
                taxTypeAdapter.notifyDataSetChanged();
                TaxType taxType = mTaxTypeList.get(i);
                String taxTypeId = taxType.getTaxtype_uid();
                List<TaxItem> list = SQLite.select().from(TaxItem.class).where(TaxItem_Table.taxtype_uid.eq(taxTypeId)).and(TaxItem_Table.node_level.eq("1")).queryList();
                addTaxItemList(list);
                break;
            default://税目
                TaxItemSelectAdapter taxItemAdapter = (TaxItemSelectAdapter) adapterView.getAdapter();
                taxItemAdapter.setSelectedPosition(i);
                taxItemAdapter.notifyDataSetChanged();
                List<TaxItem> data = taxItemAdapter.getData();
                TaxItem taxItem = data.get(i);
                if ("N".equals(taxItem.getIs_leaf())) {
                    String taxItemId = taxItem.getTaxable_item_uid();
                    List<TaxItem> list1 = SQLite.select().from(TaxItem.class).where(TaxItem_Table.parent_item_uidinteger.eq(taxItemId)).queryList();
                    addTaxItemList(list1);
                } else {

                }
                break;
        }
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
        mListLayout.addView(view);
        mListLayout.addView(listView);
        TaxItemSelectAdapter taxItemAdapter = new TaxItemSelectAdapter(mContext, R.layout.tax_item_select_layout, list);
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

    @OnClick({R.id.cancel, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.select:

                break;
        }
    }
}
