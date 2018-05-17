package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.csscaps.tcs.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tl on 2018/5/17.
 */

public abstract class BaseAddDialog<T> extends AppCompatDialogFragment {

    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.save)
    TextView mSave;

    protected T t;
    protected boolean edit;

    public void edit(T t) {
        edit = true;
        this.t = t;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_theme3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
//        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.8f);
//        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 0.6f);
        dialogWindow.setLayout(-2, -2);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    @OnClick({R.id.cancel, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.save:
                save();
                break;
        }
    }


    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void save();
}
