package com.csscaps.tcs.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
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
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.TaxItemSelectAdapter;
import com.csscaps.tcs.adapter.TaxTypeSelectAdapter;
import com.csscaps.tcs.database.table.TaxItem;
import com.csscaps.tcs.database.table.TaxItem_Table;
import com.csscaps.tcs.database.table.TaxType;
import com.csscaps.tcs.database.table.TaxType_Table;
import com.csscaps.tcs.model.RelatedTaxItem;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;
import static com.tax.fcr.library.utils.NetworkUtils.mContext;

/**
 * Created by tl on 2018/5/14.
 * 添加商品
 */

@SuppressLint("ValidFragment")
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

    private int listViewW;
    private List<TaxItem> mTaxItemList;
    private List<TaxType> mTaxTypeList;
    private List<String> listTax = new ArrayList<>();
    private Map<String, Integer> map = new HashMap<>();


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
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 0.7f);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.8f);
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
        ListView listView = (ListView) LayoutInflater.from(getContext()).inflate(R.layout.tax_list_view, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(listViewW, LinearLayout.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(lp);
        View view1 = new View(getContext());
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
        view1.setLayoutParams(lp2);
        view1.setBackgroundColor(ContextCompat.getColor(mContext,R.color.divider));
        mListLayout.addView(listView);
        mListLayout.addView(view1);
        TaxTypeSelectAdapter taxTypeAdapter = new TaxTypeSelectAdapter(getContext(), R.layout.tax_item_select_layout, mTaxTypeList, mHandler);
        listView.setAdapter(taxTypeAdapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int index = mListLayout.indexOfChild(adapterView);
        int count = mListLayout.getChildCount();
        for (int j = index + 2; j < count; j++) {
            mListLayout.removeViewAt(index + 2);
        }
        switch (index) {
            case 0://税种
                TaxTypeSelectAdapter taxTypeAdapter = ((TaxTypeSelectAdapter) adapterView.getAdapter());
                taxTypeAdapter.setSelectedPosition(i);
                taxTypeAdapter.notifyDataSetChanged();
                TaxType taxType = mTaxTypeList.get(i);
                String taxTypeId = taxType.getTaxtype_uid();
                List<TaxItem> list = SQLite.select().from(TaxItem.class).where(TaxItem_Table.taxtype_uid.eq(taxTypeId)).and(TaxItem_Table.node_level.eq("1")).queryList();
                addTaxItemList(list, taxTypeId);
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
                    String flag = taxItemAdapter.getFlag() + "," + taxItemId;
                    addTaxItemList(list1, flag);
                }
                break;
        }
    }


    /**
     * 添加税目
     *
     * @param list
     */
    private void addTaxItemList(List<TaxItem> list, String flag) {
        ListView listView = (ListView) LayoutInflater.from(getContext()).inflate(R.layout.tax_list_view, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(listViewW, LinearLayout.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(lp);
        listView.setDividerHeight(2);
        View view = new View(getContext());
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp1);
        view.setBackgroundColor(ContextCompat.getColor(mContext,R.color.divider));
        mListLayout.addView(listView);
        mListLayout.addView(view);
        TaxItemSelectAdapter taxItemAdapter = new TaxItemSelectAdapter(getContext(), R.layout.tax_item_select_layout, list, mHandler);
        taxItemAdapter.setFlag(flag);
        String flags[] = flag.split(",");
        for (String str : listTax) {
            String strs[] = str.split(",");
            if (str.startsWith(flag) && strs.length == flags.length + 2) {
                taxItemAdapter.initSelectedPosition(Integer.valueOf(strs[strs.length - 1]));
                break;
            }
        }
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            String strs[] = str.split(",");
            switch (msg.what) {
                case 0://移除
                    listTax.remove(str);
                    if (map.containsKey(strs[0]) && !"-1".equals(strs[strs.length - 1])) {
                        int v = map.get(strs[0]);
                        if (v - 1 > 0) {
                            map.put(strs[0], v - 1);
                        } else {
                            map.keySet().remove(strs[0]);
                        }

                    }
                    break;
                case 1://添加
                    listTax.add(str);
                    if (map.containsKey(strs[0])) {
                        int v = map.get(strs[0]);
                        map.put(strs[0], v + 1);
                    } else {
                        map.put(strs[0], 1);
                    }
                    break;

            }
        }
    };


    @OnClick({R.id.cancel, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.select:
                if (listTax.size() > map.keySet().size()) {
                    ToastUtil.showShort(getString(R.string.hit25));
                } else if (listTax.size() == 0) {
                    ToastUtil.showShort(getString(R.string.hit26));
                } else {
                    relate();
                    dismiss();
                }
                break;
        }
    }


    private void relate() {
        RelatedTaxItem relate = new RelatedTaxItem();
        List<TaxItem> mTaxItemList = new ArrayList<>();
        List<TaxType> mTaxTypeList = new ArrayList<>();
        for (String str : listTax) {
            String strs[] = str.split(",");
            int length = strs.length;
            if (length > 2) {
                TaxItem taxItem = select().from(TaxItem.class).where(TaxItem_Table.taxable_item_uid.eq(strs[length-2])).querySingle();
                mTaxItemList.add(taxItem);
            } else {
                TaxType taxType = select().from(TaxType.class).where(TaxType_Table.taxtype_uid.eq(strs[0])).querySingle();
                mTaxTypeList.add(taxType);
            }
        }
        relate.setTaxItemList(mTaxItemList);
        relate.setTaxTypeList(mTaxTypeList);
        ObserverActionUtils.subscribe(relate,AddProductDialog.class);
    }


}
