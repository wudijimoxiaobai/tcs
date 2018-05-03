package com.csscaps.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.csscaps.common.R;

/**
 * Created by Ashur on 2016/10/20.
 */

public class LoadListView extends ListView implements AbsListView.OnScrollListener {
    private View mFooterLoadingView;
    private View mFooterNoMoreView;
    private View mFooterRetryView;
    private OnLoadListener mOnLoadListener;
    private boolean isNoMoreData = false;
    private boolean isError = false;

    public LoadListView(Context context) {
        this(context, null);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterLoadingView = LayoutInflater.from(context).inflate(R.layout.footer_loading_layout, null);
        mFooterNoMoreView = LayoutInflater.from(context).inflate(R.layout.footer_nomore_layout, null);
        mFooterRetryView = LayoutInflater.from(context).inflate(R.layout.footer_retry_layout, null);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                if (this.getLastVisiblePosition() == (this.getCount() - 1)) {
                    View vBottom = view.getChildAt(this.getChildCount() - 1);
                    int h = vBottom.getBottom();
                    int h1 = view.getBottom();
                    if (h >= h1 && mOnLoadListener != null && !isNoMoreData) {
                        mOnLoadListener.onLoad();
                    }
                }
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                View vBottom = view.getChildAt(this.getChildCount() - 1);
                int h = vBottom.getBottom() + 2;
                int h1 = view.getBottom();
                if (h >= h1 && this.getFooterViewsCount() == 0 && mOnLoadListener != null && !isNoMoreData) {
                    this.addFooterView(mFooterLoadingView);
                }
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mOnLoadListener = listener;
    }

    public interface OnLoadListener {
        void onLoad();
    }

    public void setNoMoreFlag(boolean flag) {
        this.isNoMoreData = flag;
        if (this.getFooterViewsCount() > 0 && getAdapter() instanceof HeaderViewListAdapter) {
            this.removeFooterView(mFooterRetryView);
            this.removeFooterView(mFooterLoadingView);
            this.removeFooterView(mFooterNoMoreView);
        }
        if (flag) this.addFooterView(mFooterNoMoreView);
        else this.addFooterView(mFooterLoadingView);
    }

    public void setErrorFlag(boolean flag) {
        this.isError = flag;
        if (this.getFooterViewsCount() > 0 && getAdapter() instanceof HeaderViewListAdapter) {
            this.removeFooterView(mFooterRetryView);
            this.removeFooterView(mFooterLoadingView);
            this.removeFooterView(mFooterNoMoreView);
        }
        if (flag) this.addFooterView(mFooterRetryView);
        else this.addFooterView(mFooterLoadingView);
    }
}
