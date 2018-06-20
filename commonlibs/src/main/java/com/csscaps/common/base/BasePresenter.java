package com.csscaps.common.base;

import android.content.Context;

import com.tax.fcr.library.network.IPresenter2;

/**
 * Created by tanglei on 16/6/16.
 * Presenter基类
 */
public abstract class BasePresenter<V> implements IPresenter2 {
    protected V view;
    public Context mContext;
    private boolean isDetached;

    public BasePresenter(V view, Context mContext) {
        this.view = view;
        this.mContext = mContext;
        if (view instanceof BaseActivity) ((BaseActivity) view).addPresenter(this);
        if (view instanceof BaseFragment) ((BaseFragment) view).addPresenter(this);
    }

    public Context getContext() {
        return mContext;
    }

    //解绑view
    public void onDetach() {
            view = null;
           isDetached =true;
    }

    @Override
    public boolean isDetached() {
        return isDetached;
    }
}
