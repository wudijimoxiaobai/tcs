package com.csscaps.common;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.TextView;

import com.csscaps.common.base.BaseFragment;
import com.csscaps.common.widget.PatternView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Leon on 2016/9/27.
 */

public class GestureLock extends BaseFragment implements PatternView.OnPatternListener {
    private static
    @DrawableRes
    int background = -1;
    private PatternView patternView;
    private GestureHandler gestureHandler;

    private TextView forget;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_unlock;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (background != -1) {
            mView.setBackgroundResource(background);
        }
        patternView = (PatternView) mView.findViewById(R.id.patternView);
        patternView.setOnPatternListener(this);
        forget = (TextView) mView.findViewById(R.id.pattern_forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gestureHandler != null) {
                    gestureHandler.handleForget();
                }
            }
        });
    }

    @Override
    public void onPatternStart() {
        patternView.setDisplayMode(PatternView.DisplayMode.Correct);
    }

    @Override
    public void onPatternCleared() {

    }

    @Override
    public void onPatternCellAdded(List<PatternView.Cell> pattern) {

    }

    @Override
    public void onPatternDetected(List<PatternView.Cell> pattern) {
        if (gestureHandler != null && gestureHandler.unlock(pattern)) {
            patternView.clearPattern();
            getFragmentManager().beginTransaction().hide(this).commit();
        } else {
            patternView.setDisplayMode(PatternView.DisplayMode.Wrong);
        }
    }

    public void setGestureHandler(GestureHandler gestureHandler) {
        this.gestureHandler = gestureHandler;
    }

    public GestureHandler getGestureHandler() {
        return gestureHandler;
    }

    public static void setBackground(int background) {
        GestureLock.background = background;
    }

    public interface GestureHandler {
        boolean needLock();

        String packageName();

        boolean unlock(List<PatternView.Cell> pattern);

        void handleForget();
    }
}
