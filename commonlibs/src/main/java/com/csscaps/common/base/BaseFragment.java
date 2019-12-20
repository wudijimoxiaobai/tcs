package com.csscaps.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csscaps.common.utils.HandleBackInterface;
import com.csscaps.common.utils.HandleBackUtil;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToolbarUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;

/**
 * Created by tanglei on 16/6/17.
 * Fragment基类
 */
public abstract class BaseFragment extends Fragment implements HandleBackInterface {
    protected ArrayList<BasePresenter> mPresenters = new ArrayList<>();
    protected ArrayList<Action1> mAction1s = new ArrayList<>();
    protected View mView;
    protected Context mContext;
    private Unbinder unbinder;

    /**
     * 获取layout的id，具体由子类实现
     *
     * @return
     */
    protected abstract int getLayoutResId();

    protected void parseArgumentsFromIntent(Intent argIntent) {
    }

    //初始化presenters，
    protected abstract void onInitPresenters();


    public abstract void initView(Bundle savedInstanceState);


    protected void onCreateWithArguments(Bundle args) {
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutResId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder=ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            onCreateWithArguments(args);
        }
        if (getActivity().getIntent() != null) {
            parseArgumentsFromIntent(getActivity().getIntent());
        }
        onInitPresenters();
        initView(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        for (BasePresenter presenter : mPresenters) {
            if (presenter != null) {
                presenter.onDetach();
                if(presenter instanceof Action1){
                    ObserverActionUtils.removeAction((Action1)presenter);
                }
            }
        }
        mPresenters.clear();
        if(this instanceof Action1){
            ObserverActionUtils.removeAction((Action1)this);
        }
        for (Action1 action1:mAction1s) {
            ObserverActionUtils.removeAction(action1);
        }
    }

    public void addPresenter(BasePresenter presenter) {
        mPresenters.add(presenter);
    }

    public void addAction1(Action1 action1) {mAction1s.add(action1);}
    @Override
    public boolean onBackPressed() {
        return HandleBackUtil.handleBackPress(this);
    }
}
