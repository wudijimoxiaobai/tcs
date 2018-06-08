package com.csscaps.tcs.activity;

import android.support.v4.app.Fragment;

import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.ApproveInvoiceFragment;
import com.csscaps.tcs.fragment.InvoiceApplicationFragment;
import com.csscaps.tcs.fragment.InvoiceQueryFragment;
import com.csscaps.tcs.fragment.RequestInvoiceFragment;

/**
 * Created by tl on 2018/5/22.
 */

public class InvoiceManagementActivity extends BaseTabActivity {

    @Override
    protected Fragment[] getFragments() {
        InvoiceApplicationFragment  mInvoiceApplicationFragment=new InvoiceApplicationFragment();
        RequestInvoiceFragment  mRequestInvoiceFragment=new RequestInvoiceFragment();
        ApproveInvoiceFragment  mApproveInvoiceFragment=new ApproveInvoiceFragment();
        InvoiceQueryFragment mInvoiceQueryFragment=new InvoiceQueryFragment();
        return new Fragment[]{mInvoiceApplicationFragment,mRequestInvoiceFragment,mApproveInvoiceFragment,mInvoiceQueryFragment};
    }

    @Override
    protected int getTabTitleResourcesId() {
        return R.array.tab2;
    }

}
