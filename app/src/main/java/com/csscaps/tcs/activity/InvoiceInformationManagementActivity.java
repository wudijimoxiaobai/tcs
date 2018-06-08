package com.csscaps.tcs.activity;

import android.support.v4.app.Fragment;

import com.csscaps.tcs.R;
import com.csscaps.tcs.fragment.CustomerManagementFragment;
import com.csscaps.tcs.fragment.ProductManagementFragment;
import com.csscaps.tcs.fragment.TaxItemFragment;
import com.csscaps.tcs.fragment.TaxpayerInfoFragment;

/**
 * Created by tl on 2018/5/8.
 */

public class InvoiceInformationManagementActivity extends BaseTabActivity {

    @Override
    protected Fragment[] getFragments() {
        TaxItemFragment mTaxItemFragment = new TaxItemFragment();
        TaxpayerInfoFragment mTaxpayerInfoFragment = new TaxpayerInfoFragment();
        CustomerManagementFragment mCustomerManagementFragment = new CustomerManagementFragment();
        ProductManagementFragment mProductManagementFragment = new ProductManagementFragment();
        return new Fragment[]{mTaxItemFragment, mTaxpayerInfoFragment, mCustomerManagementFragment, mProductManagementFragment};
    }

    @Override
    protected int getTabTitleResourcesId() {
        return R.array.tab1;
    }

}
