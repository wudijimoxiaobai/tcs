package com.csscaps.tcs.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class CalendarDayTextView extends TextView {
        public boolean isToday;
        public boolean isSigned;

        public void setEmptyColor ( int emptyColor){
        this.emptyColor = emptyColor;
    }

        public void setFillColor ( int fillColor){
        this.fillColor = fillColor;
    }

        private int emptyColor = Color.parseColor("#00ff00");
        private int fillColor = Color.parseColor("#00ff00");

        public boolean isSigned () {
        return isSigned;
    }

        public void setSigned ( boolean signed){
        isSigned = signed;
    }

        private Paint mPaint;
    public CalendarDayTextView(Context context) {
        super(context);
        initview();
    }

    public CalendarDayTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview();
    }
        private void initview () {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#00ff00"));
        mPaint.setStrokeWidth(2);
    }

        @Override
        protected void onDraw (Canvas canvas){
        if (isToday) {
            if (isSigned) {
                //绘制实心圆
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(fillColor);
                canvas.save();
                //移动到当前控件的中心，以中心为圆点绘制实心圆
                canvas.translate(getWidth() / 2, getHeight() / 2);
                canvas.drawCircle(0, 0, getWidth() / 2 - 2, mPaint);
                canvas.restore();
                //此处必须将圆移动回开始位置，否则文本显示会受到影响
                canvas.translate(0, 0);
            } else {
                //绘制空心圆
                mPaint.setColor(emptyColor);
                canvas.save();
                canvas.translate(getWidth() / 2, getHeight() / 2);
                canvas.drawCircle(0, 0, getWidth() / 2 - 2, mPaint);
                canvas.restore();
                canvas.translate(0, 0);
            }

        }
        super.onDraw(canvas);
    }

        public boolean isToday () {
        return isToday;
    }

        public void setToday ( boolean today){
        isToday = today;
    }
    }
