package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.R;
import com.csscaps.tcs.database.table.Invoice;
import com.suwell.to.ofd.ofdviewer.OFDView;

import butterknife.BindView;

/**
 * Created by tl on 2018/6/13.
 */

public class InvoiceDetailsFragment extends BaseFragment {

    @BindView(R.id.ofd_view)
    OFDView mOfdView;

    Invoice invoice;

    @Override
    protected int getLayoutResId() {
        return R.layout.invoice_details_fragment;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    protected void parseArgumentsFromIntent(Intent argIntent) {
       invoice= (Invoice) argIntent.getSerializableExtra("invoice");
    }

    @Override
    public void initView(Bundle savedInstanceState) {


    }


}
