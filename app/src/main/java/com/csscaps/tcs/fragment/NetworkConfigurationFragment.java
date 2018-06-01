package com.csscaps.tcs.fragment;

import android.os.Bundle;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.tcs.R;

import butterknife.OnClick;

/**
 * Created by tl on 2018/6/1.
 */

public class NetworkConfigurationFragment extends BaseFragment {
    @Override
    protected int getLayoutResId() {
        return R.layout.network_config_fragment;
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
