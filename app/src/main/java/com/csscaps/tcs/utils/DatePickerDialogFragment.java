package com.csscaps.tcs.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.csscaps.tcs.R;

/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class DatePickerDialogFragment extends DialogFragment {

    protected DatePicker mDatePicker;
    private int mSelectedYear = -1, mSelectedMonth = -1;
    private OnDateChooseListener mOnDateChooseListener;
    private boolean mIsShowAnimation = true;
    protected TextView mGenerate, mCancel;
    private DatePicker mDatePicker2;
    String sData1;
    String sData2;

    public void setOnDateChooseListener(OnDateChooseListener onDateChooseListener) {
        mOnDateChooseListener = onDateChooseListener;
    }

    public void showAnimation(boolean show) {
        mIsShowAnimation = show;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_date, container);

        mDatePicker = view.findViewById(R.id.dayPicker_dialog);
        mDatePicker2 = view.findViewById(R.id.dayPicker_dialog2);
        mGenerate = view.findViewById(R.id.slide_generate);
        mCancel = view.findViewById(R.id.slide_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDateChooseListener != null) {
                    if (mDatePicker.getMonth() < 10) {
                        if (mDatePicker2.getMonth() < 10) {
                            sData1 = "0" + String.valueOf(mDatePicker.getMonth());
                            sData2 = "0" + String.valueOf(mDatePicker2.getMonth());
                        } else {
                            sData1 = "0" + String.valueOf(mDatePicker.getMonth());
                            sData2 = String.valueOf(mDatePicker2.getMonth());
                        }
                    } else {
                        sData1 =  String.valueOf(mDatePicker.getMonth());
                        sData2 =  String.valueOf(mDatePicker2.getMonth());
                    }
                    mOnDateChooseListener.onDateChoose(mDatePicker.getYear() + "-" + sData1,
                            mDatePicker2.getYear() + "-" + (sData2));
                }
                dismiss();
            }
        });

        if (mSelectedYear > 0) {
            setSelectedDate();
        }

        initChild();
        return view;
    }

    protected void initChild() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.SlideDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定

        dialog.setContentView(R.layout.dialog_date);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        Window window = dialog.getWindow();
        if (window != null) {
            if (mIsShowAnimation) {
                window.getAttributes().windowAnimations = R.style.DatePickerDialogAnim;
            }
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER; // 紧贴底部
            //lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            // lp.dimAmount = 0.35f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        return dialog;
    }

    public void setSelectedDate(int year, int month, int day) {
        mSelectedYear = year;
        mSelectedMonth = month;
        setSelectedDate();
    }

    private void setSelectedDate() {
        if (mDatePicker != null) {
            mDatePicker.setDate(mSelectedYear, mSelectedMonth, false);
            mDatePicker2.setDate(mSelectedYear, mSelectedMonth + 1, false);
        }
    }

    public interface OnDateChooseListener {
        void onDateChoose(String s1, String s2);
    }



}
