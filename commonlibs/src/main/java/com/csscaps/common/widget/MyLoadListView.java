package com.csscaps.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.csscaps.common.R;

/**
 * Created by tanglei on 16/6/21.
 * 滚动到底部加载
 */
public class MyLoadListView extends ListView implements AbsListView.OnScrollListener {

    private OnLoadListener onLoadListener;
    private ScrollTopListener mScrollTopListener;
    private View footView;
    private ProgressBar mProgressBar;
    private TextView mFootTv;
    private String LOADING="正在加载...";
    private String LOADED="无更多数据";

    public MyLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        footView = LayoutInflater.from(context).inflate(R.layout.foot_view_layout, null);
        mProgressBar= (ProgressBar) footView.findViewById(R.id.progress_bar);
        mFootTv= (TextView) footView.findViewById(R.id.foot_tv);
        setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            // 当不滚动时
            case OnScrollListener.SCROLL_STATE_IDLE:
                // 判断滚动到底部
                if (this.getLastVisiblePosition() == (this.getCount() - 1)) {
                    View vBottom = view.getChildAt(this.getChildCount() - 1);
                    int h = vBottom.getBottom();
                    int h1 = view.getBottom();
                    if (h >= h1 && onLoadListener != null) {
                        onLoadListener.onLoad();
                    }
                }
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                View vBottom = view.getChildAt(this.getChildCount() - 1);
                int h = vBottom.getBottom()+2;
                int h1 = view.getBottom();
                if (h >= h1 && this.getFooterViewsCount() == 0 && onLoadListener != null) {
                    ListAdapter adapter = this.getAdapter();
                    this.addFooterView(footView);
                    this.setAdapter(adapter);
                }
                break;
        }
    }


    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
       if(mScrollTopListener!=null) mScrollTopListener.onScroll(absListView, i,  i1,  i2);
    }

    /**
     * 注册下拉刷新 滚动到底部加载接口监听
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }


    /**
     * * 下拉刷新 滚动到底部加载接口
     */
    public interface OnLoadListener {

        /*** 加载*/
        void onLoad();
    }

    public void noMoreData(){
        mProgressBar.setVisibility(GONE);
        mFootTv.setText(LOADED);
    }

    public void refreshData(){
        mProgressBar.setVisibility(VISIBLE);
        mFootTv.setText(LOADING);
    }


    public  interface  ScrollTopListener{
         void onScroll(AbsListView absListView, int i, int i1, int i2);
    }

    public void setScrollTopListener(ScrollTopListener scrollTopListener) {
        mScrollTopListener = scrollTopListener;
    }
}
