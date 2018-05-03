package com.csscaps.common.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csscaps.common.R;
import com.csscaps.common.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by tl on 2017/9/19.
 * 自定义标题栏View
 */
public class TopTitleView extends RelativeLayout {

    @BindView(R2.id.left)
    TextView mLeft;
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.right)
    TextView mRight;

    private OnLRListener monLRListener;
    private Unbinder mUnbinder;

    public TopTitleView(Context context) {
        super(context, null);
    }

    public TopTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.top_title_view_layout, this);
        mUnbinder= ButterKnife.bind(this);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopTitleView);
        Drawable leftBackground = ta.getDrawable(R.styleable.TopTitleView_leftBackground);
        String leftText = ta.getString(R.styleable.TopTitleView_leftText);
        Drawable rightBackground = ta.getDrawable(R.styleable.TopTitleView_rightBackground);
        String rightText = ta.getString(R.styleable.TopTitleView_rightText);
        String title = ta.getString(R.styleable.TopTitleView_title);
        int titleTextColor=ta.getColor(R.styleable.TopTitleView_titleTextColor, Color.BLACK);
        int leftTextColor= ta.getColor(R.styleable.TopTitleView_leftTextColor, Color.BLACK);
        int rightTextColor= ta.getColor(R.styleable.TopTitleView_rightTextColor, Color.BLACK);
        ta.recycle();
        mTitle.setText(title);
        mTitle.setTextColor(titleTextColor);
        mLeft.setText(leftText);
        mLeft.setTextColor(leftTextColor);
        mLeft.setBackground(leftBackground);
        mRight.setText(rightText);
        mRight.setTextColor(rightTextColor);
        mRight.setBackground(rightBackground);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUnbinder.unbind();
    }

    @OnClick(R2.id.left)
    public void onClickLeft(View view) {
        if (monLRListener != null) {
            monLRListener.onLeft();
        } else {
            Context context = getContext();
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
    }

    @OnClick(R2.id.right)
    public void onClickRight(View view) {
        if (monLRListener != null) monLRListener.onRight();
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * 设置左文字
     *
     * @param leftText
     */
    public void setLeftText(String leftText) {
        mLeft.setText(leftText);
    }

    /**
     * 设置右文字
     *
     * @param rightText
     */
    public void setRightText(String rightText) {
        mRight.setText(rightText);
    }

    /**
     * 设置左背景
     *
     * @param resId
     */
    public void setLeftBackground(int resId) {
        mLeft.setBackground(ContextCompat.getDrawable(getContext(), resId));
    }

    /**
     * 设置右背景
     *
     * @param resId
     */
    public void setRightBackground(int resId) {
        mRight.setBackground(ContextCompat.getDrawable(getContext(), resId));
    }

    /**
     * 设置左右监听
     *
     * @param monLRListener
     */
    public void setOnLRListener(OnLRListener monLRListener) {
        this.monLRListener = monLRListener;
    }


    public interface OnLRListener {
        void onLeft();

        void onRight();
    }
}
