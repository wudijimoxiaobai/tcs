package com.csscaps.tcs.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tl on 2018/5/22.
 */

public class InvoiceApplicationFragment extends BaseFragment {
    @BindView(R.id.list_view)
    ListView mListView;

    @Override
    protected int getLayoutResId() {
        return R.layout.invoice_application_fragment;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        refresh();

    }



    @OnClick({R.id.back, R.id.refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.refresh:
                break;
        }
    }

    private void refresh(){

    }
}
