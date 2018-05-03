package com.csscaps.common.widget;

import android.content.Context;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.csscaps.common.utils.DeviceUtils;

/**
 * Created by tl on 2016/10/20.
 */
public class MyLinearLayout extends LinearLayout implements MyLoadListView.ScrollTopListener, MyScrollView.OnScrollListener {

    private float sY, sX;
    private Context context;
    private View topView;
    private int sLocation, eLocation, dLocation, cLocation, by, l;
    private boolean isTop = true, isBottom, isStrat = true, isIntercept = false, isOnTop = false;
    private MyLoadListView listView;
    private TopBottomListener mTopBottomListener;
    private int widthMeasureSpec;

    public MyLinearLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }


    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        sLocation = DeviceUtils.dip2px(context, -180);
        dLocation = sLocation;
        scrollTo(0, sLocation);

    }

    private void initChildScrollListener() {
        ViewGroup viewGroup = (ViewGroup) getChildAt(1);
        View view = viewGroup.getChildAt(0);

        if (view instanceof SwipeRefreshLayout) {
            this.listView = (MyLoadListView) ((SwipeRefreshLayout) view).getChildAt(0);
            listView.setScrollTopListener(this);
            ViewGroup.LayoutParams lp = listView.getLayoutParams();
            lp.height = sLocation - eLocation;
        }

        if (view instanceof MyScrollView) {
            MyScrollView scrollView = (MyScrollView) view;
            scrollView.setOnScrollListener(this);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) scrollView.getLayoutParams();
            lp.height = sLocation - eLocation;
        }

        LinearLayout.LayoutParams lp1 = (LayoutParams) viewGroup.getLayoutParams();
        lp1.height = sLocation - eLocation;
        measureChildren(widthMeasureSpec, lp1.height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        topView = getChildAt(0);
        eLocation = -getMeasuredHeight() + topView.getMeasuredHeight();
        l = (sLocation - eLocation) / 3;
        initChildScrollListener();
        if (isBottom) scrollTo(0, eLocation);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = false;
                sX = ev.getRawX();
                sY = ev.getRawY();
                int[] location = new int[2];
                topView.getLocationOnScreen(location);
                RectF rectF = new RectF(location[0], location[1], location[0] + topView.getWidth(), location[1] + topView.getHeight());
                if (rectF.contains(sX, sY) && isTop) {
                    isOnTop = true;
                    isIntercept = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float mY = ev.getRawY();
                float mX = ev.getRawX();
                int msY = (int) (mY - sY);
                int msX = (int) (mX - sX);
                if (Math.abs(msY) > Math.abs(msX) && Math.abs(msY) > 10) {

                    if (msY > 0 && !isStrat && !isOnTop) {
                        break;
                    }

                    pullUpDown(msY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isTop) {
                    if (cLocation < sLocation - l) {
                        by = (int) ((Math.sqrt(8 * Math.abs(cLocation - eLocation) + 1) - 1) / 2);
                        handler.sendMessage(handler.obtainMessage(1, false));
                        dLocation = eLocation;
                    } else {
                        by = (int) ((Math.sqrt(8 * Math.abs(cLocation - sLocation) + 1) - 1) / 2);
                        handler.sendMessage(handler.obtainMessage(1, true));
                        dLocation = sLocation;
                    }
                } else if (isBottom) {
                    if (cLocation > eLocation + l) {
                        by = (int) ((Math.sqrt(8 * Math.abs(cLocation - sLocation) + 1) - 1) / 2);
                        handler.sendMessage(handler.obtainMessage(1, true));
                        dLocation = sLocation;
                    } else {
                        by = (int) ((Math.sqrt(8 * Math.abs(cLocation - eLocation) + 1) - 1) / 2);
                        handler.sendMessage(handler.obtainMessage(1, false));
                        dLocation = eLocation;
                    }
                }
                isOnTop = false;
                break;
        }
        return isIntercept || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isIntercept;
    }

    private void pullUpDown(int m) {
        cLocation = dLocation - m;
        if (eLocation < cLocation && cLocation < sLocation) {
            scrollTo(0, cLocation);
            isIntercept = true;
        }

        if (cLocation <= eLocation) {//到底部
            scrollTo(0, eLocation);
            dLocation = eLocation;
            isBottom = true;

        }

        if (cLocation >= sLocation) {//到顶部
            scrollTo(0, sLocation);
            dLocation = sLocation;
            isTop = true;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    boolean b = (boolean) msg.obj;
                    int scrollY = getScrollY();
                    if (scrollY >= sLocation) {
                        isBottom = false;
                        isTop = true;
                        if (mTopBottomListener != null) mTopBottomListener.top();
                        break;
                    }

                    if (scrollY <= eLocation) {
                        isBottom = true;
                        isTop = false;
                        if (mTopBottomListener != null) mTopBottomListener.bottom();
                        break;
                    }

                    if (by <= 1) {
                        by = 1;
                    }
                    if (b) {
                        scrollBy(0, by);
                    } else {
                        scrollBy(0, -by);
                    }
                    by--;
                    sendMessageDelayed(handler.obtainMessage(1, b), 5);
                    break;
            }
        }
    };


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        try {
            View vtop = view.getChildAt(0);
            int t = vtop.getTop();
            int vt = view.getPaddingTop();
            if (isIntercept) {
                return;
            }
            // 判断滚动到顶部
            if (listView.getFirstVisiblePosition() == 0 && t == vt) {
                isStrat = true;
            } else {
                isStrat = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onScroll(int scrollY) {
        if (scrollY == 0) {
            isStrat = true;
        } else {
            isStrat = false;
        }

    }

    public void setTopBottomListener(TopBottomListener topBottomListener) {
        mTopBottomListener = topBottomListener;
    }

    public interface TopBottomListener {

        void top();

        void bottom();
    }

}
