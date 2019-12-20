package com.csscaps.tcs.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csscaps.tcs.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyCalendar extends LinearLayout {
    private ImageButton btnPre;
    private ImageButton btnGo;
    private TextView tvMonth;
    private GridView gridDate;
    private Calendar curDate = Calendar.getInstance();
    private String displayformat;
    private CalendarAdapter adapter;

    //相关属性
    private int titleColor;
    private int titleSize;
    private int goIcon;
    private int preIcon;
    private int dayInMonthColor;
    private int dayOutMonthColor;
    private int todayColor;
    private int todayEmptyColor;
    private int todayFillColor;

    public MyCalendar(Context context) {
        this(context, null);
    }

    public MyCalendar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MyCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyCalendar);
        //设置标题栏年月文本的显示格式
        displayformat = ta.getString(R.styleable.MyCalendar_dateformat);
        if (displayformat == null) {
            displayformat = "yyyy MMM";
        }
        titleColor = ta.getColor(R.styleable.MyCalendar_titleColor, Color.WHITE);
        titleSize = (int) ta.getDimension(R.styleable.MyCalendar_titleSize, 18);
//        goIcon=ta.getResourceId(R.styleable.MyCalendar_goIcon,R.mipmap.go);
//        preIcon=ta.getResourceId(R.styleable.MyCalendar_preIcon,R.mipmap.pre);
        dayInMonthColor = ta.getColor(R.styleable.MyCalendar_dayInMonthColor, Color.WHITE);
        dayOutMonthColor = ta.getColor(R.styleable.MyCalendar_dayOutMonthcolor, Color.GRAY);
        todayColor = ta.getColor(R.styleable.MyCalendar_todayColor, Color.BLUE);
        todayEmptyColor = ta.getColor(R.styleable.MyCalendar_todayEmptycircleColor, Color.CYAN);
        todayFillColor = ta.getColor(R.styleable.MyCalendar_todayFillcircleColor, Color.CYAN);
        ta.recycle();
        init(context);
    }

    private void init(Context context) {
        bindView(context);
        bindEvent();

    }

    private void bindView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_view, this, false);
        btnPre = (ImageButton) view.findViewById(R.id.btnpre);
        btnPre.setImageResource(preIcon);
        btnGo = (ImageButton) view.findViewById(R.id.btngo);
        btnGo.setImageResource(goIcon);
        tvMonth = (TextView) view.findViewById(R.id.tvmonth);
        tvMonth.setTextColor(titleColor);
        tvMonth.setTextSize(titleSize);
        gridDate = (GridView) view.findViewById(R.id.calendar_grid);
        addView(view);

    }

    private void bindEvent() {
        btnPre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //往前移动一个月份
                curDate.add(Calendar.MONTH, -1);
                //设置当月数据
                renderCalendar();
            }
        });
        btnGo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //往后移动一个月份
                curDate.add(Calendar.MONTH, 1);
                renderCalendar();
            }
        });
        renderCalendar();
    }

    private void renderCalendar() {
        SimpleDateFormat simpleformat = new SimpleDateFormat(displayformat);
        tvMonth.setText(simpleformat.format(curDate.getTime()));
        final ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();
        //将日历设置到当月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //获得当月第一天是星期几，如果是星期一则返回1此时1-1=0证明上个月没有多余天数
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        //将calendar在1号的基础上向前推prevdays天。
        calendar.add(Calendar.DAY_OF_MONTH, -prevDays);
        //最大行数是6*7也就是，1号正好是星期六时的情况
        int maxCellcount = 6 * 7;
        while (cells.size() < maxCellcount) {
            cells.add(calendar.getTime());
            //日期后移一天
            calendar.add(calendar.DAY_OF_MONTH, 1);
        }
        adapter = new CalendarAdapter(getContext(), cells);
        gridDate.setAdapter(adapter);
        gridDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (myCalendarclickListener != null) {
                    // (( CalendarDayTextView)view).setToday(true);
                    myCalendarclickListener.onCalendarItemClick((Date) parent.getItemAtPosition(position));
                    ((CalendarDayTextView) parent.getChildAt(position)).setSigned(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    private class CalendarAdapter extends ArrayAdapter<Date> {
        LayoutInflater layoutInflater;

        public CalendarAdapter(Context context) {
            this(context, null);
        }

        public CalendarAdapter(Context context, List<Date> objects) {
            super(context, R.layout.calendar_text_day, objects);
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Date date = getItem(position);
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.calendar_text_day, parent, false);
                ((CalendarDayTextView) convertView).setEmptyColor(todayEmptyColor);
                ((CalendarDayTextView) convertView).setFillColor(todayFillColor);

            }
            int day = date.getDate();
            //设置文本
            ((CalendarDayTextView) convertView).setText(String.valueOf(day));
            Date now = new Date();
            //设置颜色
            if (now.getDate() == day && now.getMonth() == date.getMonth() && now.getYear() == date.getYear()) {
                // ( (CalendarDayTextView) convertView).setTextColor(todayColor);
                // (( CalendarDayTextView)convertView).setToday(true);
            } else if (date.getMonth() == now.getMonth()) {
                ((CalendarDayTextView) convertView).setTextColor(dayInMonthColor);
            } else {
                ((CalendarDayTextView) convertView).setTextColor(dayOutMonthColor);
            }

            return convertView;
        }
    }


    public void setOnCalendarClick(MyCalendarclickListener li) {
        myCalendarclickListener = li;
    }

    //日期点击事件接口回调
    public MyCalendarclickListener myCalendarclickListener;

    public interface MyCalendarclickListener {
        void onCalendarItemClick(Date date);
    }

}