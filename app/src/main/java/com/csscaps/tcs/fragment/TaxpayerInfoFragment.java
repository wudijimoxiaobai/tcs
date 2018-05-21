package com.csscaps.tcs.fragment;

import android.os.Bundle;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.R;

import butterknife.OnClick;

/**
 * Created by tl on 2018/5/21.
 */

public class TaxpayerInfoFragment extends BaseFragment {
    @Override
    protected int getLayoutResId() {
        return R.layout.taxpayer_info_fragment;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }


    @OnClick(R.id.back)
    public void onClick() {
        getActivity().finish();
    }
}
