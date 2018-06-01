package com.csscaps.tcs.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.csscaps.tcs.R;
import com.csscaps.tcs.adapter.BaseManagementListAdapter;
import com.csscaps.tcs.adapter.InvoiceQueryListAdapter;
import com.csscaps.tcs.database.table.Invoice;
import com.csscaps.tcs.database.table.Invoice_Table;
import com.csscaps.tcs.dialog.BaseAddDialog;

import java.util.List;

import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

/**
 * Created by tl on 2018/5/22.
 */

public class InvoiceQueryFragment extends BaseManagementListFragment<Invoice> {


    @Override
    protected int getLayoutResId() {
        return R.layout.invoice_query_fragment;
    }

    @Override
    protected int getPopupWindowLayout() {
        return 0;
    }

    @Override
    protected List<Invoice> getData() {
        return select().from(Invoice.class).orderBy(Invoice_Table.client_invoice_datetime,false).queryList();
    }

    @Override
    protected BaseManagementListAdapter getAdapter(List<Invoice> data) {
        return new InvoiceQueryListAdapter(mContext, R.layout.invoice_query_list_item, data);
    }

    @Override
    protected BaseAddDialog getDialog() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mListView.setOnItemClickListener(null);
    }

    @Override
    @OnClick({R.id.back, R.id.search, R.id.select})
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
                    case 0://返回
                        getActivity().finish();
                        break;
                    case 1://取消
                        mBack.setTag(0);
                        mSelect.setTag(0);
//                        mBack.setText(getString(R.string.back));
                        mSelect.setText((getString(R.string.select)));
                        Drawable drawable = ContextCompat.getDrawable(mContext,R.mipmap.select);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        mSelect.setCompoundDrawables(drawable,null,null,null);
                        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext, android.R.color.transparent));
                        mBaseManagementListAdapter.setShowCheckBox(false);
                        mAllSelect.setText(getString(R.string.no));
                        mAllSelect.setChecked(false);
//                        mAdd.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case R.id.select:
                switch (tag) {
                    case 0://选择
                        mBack.setTag(1);
                        mSelect.setTag(1);
//                        mBack.setText(getString(R.string.cancel));
                        mSelect.setText((getString(R.string.upload)));
                        mAllSelect.setButtonDrawable(ContextCompat.getDrawable(mContext, R.drawable.cb_check_selector));
                        mAllSelect.setText(null);
                        mBaseManagementListAdapter.setShowCheckBox(true);
//                        mAdd.setVisibility(View.INVISIBLE);
                        break;
                    case 1://上传

                        break;
                }
                break;
            case R.id.search:
                break;
        }
    }
}
