package com.csscaps.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.csscaps.common.R;


/**
 * Created by TL on 2016/8/19.
 */
public class CommonDialog extends Dialog {

    private Context context;
    private Window dialogWindow;
    private IDialogContentView mDialogContentView;
    private int layoutResId, w = ViewGroup.LayoutParams.WRAP_CONTENT, h = ViewGroup.LayoutParams.WRAP_CONTENT;

    public CommonDialog(Context context, int layoutResId) {
        super(context, R.style.dialog_theme);
        this.context = context;
        this.layoutResId = layoutResId;
        dialogWindow = getWindow();
    }

    public CommonDialog(Context context, int layoutResId, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.layoutResId = layoutResId;
        dialogWindow = getWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(layoutResId, null);
        setContentView(view);
        mDialogContentView.setView(view, this);
    }

    @Override
    public void show() {
        super.show();
        android.view.WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = w;
        lp.height = h;
        dialogWindow.setAttributes(lp);
    }


    public CommonDialog setAnimStyleId(int animStyleId) {
        dialogWindow.setWindowAnimations(animStyleId);
        return this;
    }

    public CommonDialog setGravity(int gravity) {
        dialogWindow.setGravity(gravity);
        return this;
    }

    public CommonDialog setWH(int w, int h) {
        this.w = w;
        this.h = h;
        return this;
    }

    public CommonDialog setW(int w) {
        this.w = w;
        return this;
    }

    public CommonDialog setH(int h) {
        this.h = h;
        return this;
    }

    public void setDialogContentView(IDialogContentView dialogContentView) {
        mDialogContentView = dialogContentView;
    }

    public IDialogContentView getDialogContentView() {
        return mDialogContentView;
    }
}
