package com.csscaps.tcs.activity;


import android.os.Bundle;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.tcs.R;

/**
 * Created by tl on 2018/5/24.
 */

public class ProductListActivity extends BaseActivity {

//    private PrintUtil mPrintUtil;

    @Override
    protected int getLayoutResId() {
        return R.layout.product_list_activity;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
//        mPrintUtil=new PrintUtil();
//        mPrintUtil.init();
    }

    @Override
    protected void onResume() {
//        mPrintUtil.optPrinter(true);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
//        mPrintUtil.optPrinter(false);
        super.onDestroy();
    }

   /* public PrintUtil getPrintUtil() {
        return mPrintUtil;
    }*/
}
