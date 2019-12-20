package com.csscaps.common.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToolbarUtils;

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
        unbinder = ButterKnife.bind(this);
        setStatusBar();
        onInitPresenters();
        initView(savedInstanceState);
    }
    protected void setStatusBar() {
        //这里做了两件事情，1.使状态栏透明并使contentView填充到状态栏 2.预留出状态栏的位置，防止界面上的控件离顶部靠的太近。这样就可以实现开头说的第二种情况的沉浸式状态栏了
        ToolbarUtils.setTransparent(this);
    }

    public void addPresenter(BasePresenter presenter) {
        mPresenters.add(presenter);
    }

    public void addAction1(Action1 action1) {
        mAction1s.add(action1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        for (Action1 action1 : mAction1s) {
            ObserverActionUtils.removeAction(action1);
        }
    }
}
