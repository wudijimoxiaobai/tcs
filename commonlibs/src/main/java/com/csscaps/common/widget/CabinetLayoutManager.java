package com.csscaps.common.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tax.fcr.library.utils.Logger;

/**
 * Created by Leon on 10/19/2016.
 */

public class CabinetLayoutManager extends RecyclerView.LayoutManager {

    private int horizontalOffSet = -20;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        layoutCabinet(recycler, state);
    }

    private void layoutCabinet(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int itemCount = state.getItemCount();
        detachAndScrapAttachedViews(recycler);
        if (itemCount <= 0 || state.isPreLayout()) return;


        int screenWidth = getHorizontalSpace();
        int screenHeight = getVerticalSpace();
        int width = getViewWidth(recycler);
        int height = getViewHeight(recycler);
        int space = getBoundOffset(recycler);

        if (itemCount < 3) {
            fit3Items(recycler, itemCount, screenWidth, width, height);
        } else {
            fitMoreItems(recycler, itemCount, space, width, height);
        }
    }

    private void fit3Items(RecyclerView.Recycler recycler, int num, int screenWidth, int width, int height) {
        for (int i = 0; i < num; i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int leftOffSet = screenWidth / (num + 1) * (i + 1) - width / 2 + horizontalOffSet;
            layoutDecorated(view, leftOffSet, 0, width + leftOffSet, height);
        }
    }

    private void fitMoreItems(RecyclerView.Recycler recycler, int to, int space, int width, int height) {
        for (int i = 0; i < to; i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int leftOffSet = space * (i + 1) + width * i + horizontalOffSet;
            layoutDecorated(view, leftOffSet, 0, width + leftOffSet, height);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {

        if (isNoChild() || isNoNeedScrollHorizontally(recycler)) {
            Logger.d("No Need Scroll.");
            return 0;
        }

        Logger.i("dx : " + dx);
        if (dx < 0) { // l -> r
            int leftSpace = getDecoratedLeft(getChildAt(0)) - getBoundOffset(recycler);
            dx = Math.max(leftSpace, dx);
            Logger.i("\t>>>> " + dx);
        } else { // l <- r
            int rightSpace = getBoundOffset(recycler) + getDecoratedRight(getChildAt(getItemCount() - 1)) - getHorizontalSpace();
            dx = Math.min(dx, rightSpace);
            Logger.i("\t<<<< " + dx);
        }

        offsetChildrenHorizontal(-dx);
        horizontalOffSet += -dx;
        layoutCabinet(recycler, state);

        return dx;
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    private int getViewWidth(RecyclerView.Recycler recycler) {
        View v = recycler.getViewForPosition(0);
        measureChildWithMargins(v, 0, 0);
        return getDecoratedMeasuredWidth(v);
    }

    private int getViewHeight(RecyclerView.Recycler recycler) {
        View v = recycler.getViewForPosition(0);
        measureChildWithMargins(v, 0, 0);
        return getDecoratedMeasuredHeight(v);
    }

    public boolean isNoChild() {
        return getItemCount() == 0;
    }

    public boolean isNoNeedScrollHorizontally(RecyclerView.Recycler recycler) {
        int viewSpan = getBoundOffset(recycler) * 2 + getDecoratedRight(getChildAt(getChildCount() - 1)) - getDecoratedLeft(getChildAt(0));
        return viewSpan <= getHorizontalSpace();
    }

    private int getBoundOffset(RecyclerView.Recycler recycler) {
        return getHorizontalSpace() / 5 - getViewWidth(recycler) / 2;
    }
}
