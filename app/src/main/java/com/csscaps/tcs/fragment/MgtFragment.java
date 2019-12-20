package com.csscaps.tcs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.R;
import com.csscaps.tcs.activity.CustomerMgtActivity;
import com.csscaps.tcs.activity.OnlineDeclarationActivity;
import com.csscaps.tcs.activity.ProductMgtActivity;
import com.csscaps.tcs.activity.TaxItemActivity;

import butterknife.OnClick;
import butterknife.Unbinder;

public class MgtFragment extends BaseFragment {
    Unbinder unbinder;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mgt;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.declaration, R.id.customer_mgt, R.id.tax_item, R.id.product_mgt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.declaration:
                startActivity(new Intent(getActivity(), OnlineDeclarationActivity.class));
                break;
            case R.id.customer_mgt:
                startActivity(new Intent(getActivity(), CustomerMgtActivity.class));
                break;
            case R.id.tax_item:
                startActivity(new Intent(getActivity(), TaxItemActivity.class));
                break;
            case R.id.product_mgt:
                startActivity(new Intent(getActivity(), ProductMgtActivity.class));
                break;
        }
    }
}
