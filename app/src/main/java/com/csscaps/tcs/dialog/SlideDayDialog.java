package com.csscaps.tcs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.csscaps.tcs.R;
import com.csscaps.tcs.utils.MyCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SlideDayDialog extends Dialog {

    private TextView mGenerate;
    private TextView mCancel;
    private MyCalendar day;
    String data;
    String aData;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    private TextView tvday;


    public SlideDayDialog(@NonNull Context context, @StyleRes int themeResId, String s) {
        super(context, themeResId);
        aData = s;
    }

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param yesOnclickListener
     */
    public void setYesOnclickListener(onYesOnclickListener yesOnclickListener) {
        this.yesOnclickListener = yesOnclickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_dialog);

        day = findViewById(R.id.mycalendar);
        mGenerate = findViewById(R.id.slide_generate);
        mCancel = findViewById(R.id.slide_cancel);
        tvday = findViewById(R.id.tvmonth);
        int length = aData.length();
        data = aData;
        if(length >= 2){
            String str = aData.substring(length-2, length);
            tvday.setText(str);
        }
        day.setOnCalendarClick(new MyCalendar.MyCalendarclickListener() {
            @Override
            public void onCalendarItemClick(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("dd");
                tvday.setText(format.format(date)+"");
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                data = format2.format(date);
            }
        });

        //空白处不能取消动画;
        setCanceledOnTouchOutside(false);

        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        mGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesOnclick(data);
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    public interface onYesOnclickListener {
        public void onYesOnclick(String s);
    }

}
