package com.csscaps.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Mingliang on 2016/8/19 019.
 */
public class UnSwipeViewPager extends ViewPager {
    private static boolean scrollable = true;

    public UnSwipeViewPager(Context context) {
        super(context);
    }

    public UnSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static void setScrollable(boolean canScroll) {
        scrollable = canScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return scrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return scrollable && super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollable && super.onInterceptTouchEvent(ev);
    }
}
