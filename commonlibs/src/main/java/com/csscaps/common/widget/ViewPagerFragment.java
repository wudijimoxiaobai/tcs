package com.csscaps.common.widget;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csscaps.common.R;
import com.csscaps.common.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 唐磊 on 2015/11/7.
 */

@SuppressLint("ValidFragment")
public class ViewPagerFragment extends Fragment {

    public ViewPager viewPager;
    public LinearLayout linearLayout;
    private int offset = 0;
    private int bmpW;// 动画图片宽度
    private Fragment fragmentList[];
    private List<TextView> textViewsList = new ArrayList<TextView>();
    private String str[];
    private int color[];
    private int count, fromX;
    private TextView tmpTextView;
    private ImageView cursor;
    private boolean aBoolean;
    private int textSize; //标题字体大小
    private int w, h;//标题栏宽，高
    private int backGroundColor;//标题栏宽背景色
    private boolean isShowCursor;
    private int pageNo = 0;

    public ViewPagerFragment(){
        super();
    }

    /**
     * @param color        颜色数组 大小为2
     * @param str          标题数组
     * @param fragmentList Fragment数组
     *                     标题数组大小必须和Fragment数组一样大 ，color[0]:指示色   color[1]:默认色
     */
    public ViewPagerFragment(int[] color, String str[], Fragment[] fragmentList) {
        super();
        this.color = color;
        this.str = str;
        this.fragmentList = fragmentList;
        count = fragmentList.length;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            this.color = savedInstanceState.getIntArray("color");
            this.str = savedInstanceState.getStringArray("str");
            count = savedInstanceState.getInt("count");

        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager_fragmet, null);
        linearLayout = (LinearLayout) view.findViewById(R.id.top_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_paper);
        cursor = (ImageView) view.findViewById(R.id.cursor);
        cursor.setBackgroundColor(getResources().getColor(color[0]));
        init();
        return view;
    }

    private void init() {
        initImageView();
        initTextView();
        initViewPager();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("str",str);
        outState.putIntArray("color",color);
        outState.putInt("count", count);
    }



    /**
     * 设置标题栏样式
     *
     * @param aBoolean        true：设置给定的大小和背景色；false：默认
     * @param w               宽 非空
     * @param h               高 非空
     * @param backGroundColor 背景色 非空 Color资源id
     */
    public ViewPagerFragment setNavigationWH(boolean aBoolean, int w, int h, int backGroundColor) {
        this.aBoolean = aBoolean;
        this.w = w;
        this.h = h;
        this.backGroundColor = backGroundColor;
        return this;
    }

    /**
     * 设置标题字体大小
     *
     * @param textSize
     */
    public ViewPagerFragment setNavigationTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    /**
     * @param isShowCursor true:不显示 cursor   默认显示
     * @return
     */
    public ViewPagerFragment setIsHideCursor(boolean isShowCursor) {
        this.isShowCursor = isShowCursor;
        return this;
    }


    @Override
    public void onStart() {
        if (aBoolean) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, h);
            lp.gravity = Gravity.CENTER;
            linearLayout.setLayoutParams(lp);
            if (backGroundColor != 0)
                linearLayout.setBackgroundColor(backGroundColor);
        }


        if (isShowCursor) {
            cursor.setVisibility(View.GONE);
        }
        super.onStart();
    }

    /**
     * 初始化头标
     */
    private void initTextView() {

        for (int i = 0; i < str.length; i++) {
            TextView tv = new TextView(getActivity());
            tv.setText(str[i]);
            if (textSize != 0) {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            }
            if (i == pageNo) {
                tmpTextView = tv;
                tv.setTextColor(getResources().getColor(color[0]));
            } else {
                tv.setTextColor(getResources().getColor(color[1]));
            }
            tv.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            tv.setLayoutParams(lp);
            linearLayout.addView(tv, i);
            tv.setOnClickListener(new MyOnClickListener(i));
            textViewsList.add(tv);
        }

    }


    /**
     * 初始化动画
     */
    private void initImageView() {
        offset = DeviceUtils.dip2px(getActivity(), 10);
        bmpW = w / count - 2 * offset;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(bmpW, DeviceUtils.dip2px(getActivity(), 2));
        lp.setMargins(offset, 0, 0, 0);
        cursor.setLayoutParams(lp);
    }


    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }


    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        viewPager.setAdapter(new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);// 设置保留不销毁的页数
        viewPager.setCurrentItem(pageNo);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * FragmentsAdapter
     */
    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList[arg0];
        }

        @Override
        public int getCount() {
            return count;
        }

    }


    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageSelected(int arg0) {
            ViewPagerFragment.this.pageNo = arg0;
            int x = w * arg0 / count;
            TranslateAnimation animation = new TranslateAnimation(fromX, x, 0, 0);
            animation.setFillAfter(true);
            animation.setDuration(300);
            cursor.startAnimation(animation);
            TextView tv = textViewsList.get(arg0);
            tv.setTextColor(getResources().getColor(color[0]));
            tmpTextView.setTextColor(getResources().getColor(color[1]));
            fromX = x;
            tmpTextView = tv;

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }

    }


    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;

    }
}

