package com.csscaps.tcs.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tl on 2018/6/6.
 */

@SuppressLint("ValidFragment")
public class ExitDialog extends DialogFragment  {

    private Handler mHandler;

    public ExitDialog(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exit_dialog, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = DeviceUtils.dip2Px(getContext(), 360);
        dialogWindow.setLayout(width, -2);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }


    @OnClick({R.id.cancel, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                dismiss();
                mHandler.sendEmptyMessage(0);
                break;
        }
    }


}
