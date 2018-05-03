package com.csscaps.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.csscaps.common.R;
import com.csscaps.common.utils.ObserverActionUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;


/**
 * Created by tanglei on 16/6/16.
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected ArrayList<BasePresenter> mPresenters = new ArrayList<>();
    protected ArrayList<Action1> mAction1s = new ArrayList<>();
    private Unbinder unbinder;


    protected abstract int getLayoutResId();

    protected abstract void onInitPresenters();

    public abstract void initView(Bundle savedInstanceState);

    protected void parseArgumentsFromIntent(Intent argIntent) {
    }

    private void initialize(Bundle savedInstanceState) {
        setContentView(getLayoutResId());
        if (getIntent() != null) {
            parseArgumentsFromIntent(getIntent());
        }
        unbinder=ButterKnife.bind(this);
        onInitPresenters();
        initView(savedInstanceState);
    }

    public void addPresenter(BasePresenter presenter) {
        mPresenters.add(presenter);
    }

    public void addAction1(Action1 action1) {mAction1s.add(action1);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        for (BasePresenter presenter : mPresenters) {
            if (presenter != null) {
                presenter.onDetach();
                if (presenter instanceof Action1) {
                    ObserverActionUtils.removeAction((Action1) presenter);
                }

            }
        }
        mPresenters.clear();
        if (this instanceof Action1) {
            ObserverActionUtils.removeAction((Action1) this);
        }

        for (Action1 action1:mAction1s) {
            ObserverActionUtils.removeAction(action1);
        }
    }
}
