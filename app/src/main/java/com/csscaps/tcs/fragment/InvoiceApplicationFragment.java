package com.csscaps.tcs.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.R;
import com.csscaps.tcs.action.IInvoiceApplicationAction;
import com.csscaps.tcs.adapter.InvoiceNoAdapter;
import com.csscaps.tcs.database.table.InvoiceType;
import com.csscaps.tcs.presenter.InvoiceApplicationPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.raizlabs.android.dbflow.sql.language.SQLite.select;

public class InvoiceApplicationFragment extends BaseFragment implements IInvoiceApplicationAction {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_view)
    ListView mListView;

    private InvoiceApplicationPresenter mPresenter;
    private InvoiceNoAdapter mInvoiceNoAdapter;
    private List<InvoiceType> data;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_invoice_application;
    }

    @Override
    protected void onInitPresenters() {
        mPresenter = new InvoiceApplicationPresenter(this, mContext);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        data = select().from(InvoiceType.class).queryList();
        mInvoiceNoAdapter = new InvoiceNoAdapter(mContext, R.layout.invoice_no_item_layout, data);
        mListView.setAdapter(mInvoiceNoAdapter);
        mPresenter.refresh(false);
    }

    @Override
    public void callBack() {
        mInvoiceNoAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.refresh)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.refresh:
                mPresenter.refresh(true);
                break;
        }
    }
}
