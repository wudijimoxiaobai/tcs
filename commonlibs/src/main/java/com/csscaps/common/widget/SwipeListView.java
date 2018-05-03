package com.csscaps.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.csscaps.common.R;

/**
 * Created by Ashur on 2016/10/20.
 */

public class SwipeListView extends FrameLayout {
    private SwipeRefreshLayout mSwipeView;
    private LoadListView mListView;
    private View mEmptyView;
    private View mLoadingView;
    private View mErrorView;

    public SwipeListView(Context context) {
        this(context, null);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.swipe_list_layout, this, true);
        mSwipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mListView = (LoadListView) findViewById(R.id.list);
        mEmptyView = findViewById(R.id.empty);
        mLoadingView = findViewById(R.id.loading);
        mErrorView = findViewById(R.id.error);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeListView);
        if (a != null) {
            final Drawable d = a.getDrawable(R.styleable.SwipeListView_slv_divider);
            if (d != null) {
                mListView.setDivider(d);
            }

            final int dividerHeight = a.getDimensionPixelSize(R.styleable.SwipeListView_slv_dividerHeight, 0);
            if (dividerHeight != 0) {
                mListView.setDividerHeight(dividerHeight);
            }

            final Drawable selector = a.getDrawable(R.styleable.SwipeListView_slv_listSelector);
            if (selector != null) {
                mListView.setSelector(selector);
            }

            a.recycle();
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mSwipeView.setOnRefreshListener(listener);
    }

    public void setOnLoadListener(OnLoadListener listener) {
        mListView.setOnLoadListener(listener);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
       mListView.setOnItemClickListener(listener);
    }

    public void setAdapter(ListAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public void setNoMoreFlag(boolean flag) {
        mListView.setNoMoreFlag(flag);
    }

    public void setErrorFlag(boolean flag) {
        mListView.setErrorFlag(flag);
    }

    public void showLoading() {
        hideAll();
        mLoadingView.setVisibility(VISIBLE);
    }

    public void showEmpty() {
        hideAll();
        mEmptyView.setVisibility(VISIBLE);
    }

    public void showError() {
        hideAll();
        mErrorView.setVisibility(VISIBLE);
    }

    public void showList() {
        hideAll();
        mListView.setVisibility(VISIBLE);
    }

    public void hideLoading() {
        mLoadingView.setVisibility(GONE);
    }

    public void hideEmpty() {
        mEmptyView.setVisibility(GONE);
    }

    public void hideError() {
        mErrorView.setVisibility(GONE);
    }

    public void hideList() {
        mListView.setVisibility(GONE);
    }

    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        mSwipeView.setColorSchemeResources(colorResIds);
    }

    public int getHeaderViewsCount() {
        return mListView.getHeaderViewsCount();
    }

    public void addHeaderView(View v) {
        mListView.addHeaderView(v);
    }

    public void setRefreshing(boolean isRefreshing) {
        mSwipeView.setRefreshing(isRefreshing);
    }

    public void hideAll() {
        hideLoading();
        hideEmpty();
        hideError();
        hideList();
    }

    public interface OnRefreshListener extends SwipeRefreshLayout.OnRefreshListener {
    }

    public interface OnLoadListener extends LoadListView.OnLoadListener {
    }

    public interface OnItemClickListener extends AbsListView.OnItemClickListener {
    }
}
