package com.csscaps.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 2016/9/20.
 */
public class GroupView extends LinearLayout {
    private boolean hasTitle = false;
    private boolean shouldHideChildren = true;
    private List<Integer> childrenHeight;
    private int subViewDecent = 28;

    public GroupView(Context context) {
        this(context, null);
    }

    public GroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        childrenHeight = new ArrayList<>();
        setOrientation(VERTICAL);
        LayoutParams lp = generateDefaultLayoutParams();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        setLayoutParams(lp);
    }

    public int getGroupSize() {
        return getChildCount() - 1;
    }

    public boolean isEmptyGroup() {
        return getGroupSize() == 0;
    }

    public void toggle() {
        if (childrenHeight.isEmpty()) {
            for (int i = 0; i < getChildCount(); i++) {
                childrenHeight.add(getChildAt(i).getMeasuredHeight());
            }
        }
        for (int i = 1; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp == null) lp = generateDefaultLayoutParams();
            lp.width = child.getMeasuredWidth();
            lp.height = shouldHideChildren ? 0 : childrenHeight.get(i);
            child.setLayoutParams(lp);
            child.requestLayout();
        }
        shouldHideChildren = !shouldHideChildren;
        invalidate();
    }

    public boolean isChildHide() {
        return isEmptyGroup() || !shouldHideChildren;
    }

    public void setTitleView(View title) {
        addView(title, 0);
        hasTitle = true;
    }

    public View getTitleView() {
        if (hasTitle) {
            return getChildAt(0);
        }
        return null;
    }

    public GroupView addItem(View view) {
        int paddingStart = 0;
        if (hasTitle) {
            paddingStart = getChildAt(0).getPaddingLeft();
        }
        view.setPadding(paddingStart + subViewDecent, 0, 0, 0);
        addView(view);
        return this;
    }

    public static class Builder {

        private GroupView groupView;

        public Builder(Context context) {
            this.groupView = new GroupView(context);
        }

        public Builder titleView(View title) {
            groupView.setTitleView(title);
            return this;
        }

        public Builder item(View view) {
            groupView.addView(view);
            return this;
        }

        public GroupView build() {
            return groupView;
        }

    }
}
