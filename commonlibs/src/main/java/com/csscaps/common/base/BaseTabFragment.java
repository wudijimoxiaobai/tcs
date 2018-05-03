package com.csscaps.common.base;

import android.support.annotation.StringRes;

/**
 * Want to write a tab-pager?
 *  - Ok, inherit from this class and register it on TabsCard
 *
 * Created by Mingliang on 2016/8/18 018.
 */
public abstract class BaseTabFragment extends BaseFragment {

    public abstract @StringRes int getTitleRes();

}
