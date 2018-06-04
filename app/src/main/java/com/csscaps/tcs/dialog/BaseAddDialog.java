package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 0.618f);
        dialogWindow.setLayout(width, -2);
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


    protected void tIntoEditText( ) {
        Class thisClass = this.getClass();
        Class tClass = t.getClass();
        Class editTextClass = EditText.class;
        Field[] fields = thisClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fName = field.getName();
            String fieldTypeName = field.getType().getName();
            if (fieldTypeName.equals("android.widget.EditText")) {
                StringBuffer sb = new StringBuffer(fName);
                sb.delete(0, 1);
                String methodName = "get" + sb.toString();
                try {
                    Method method = tClass.getMethod(methodName);
                    Object str = method.invoke(t);
                    Method setText = editTextClass.getMethod("setText", CharSequence.class);
                    Object object = field.get(this);
                    setText.invoke(object, String.valueOf(str.equals("null")?"":str));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void editTextsIntoT() {
        Class thisClass = this.getClass();
        Class editTextClass = EditText.class;
        Class tClass = t.getClass();
        Field[] fields = thisClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fName = field.getName();
            String fieldTypeName = field.getType().getName();
            if (fieldTypeName.equals("android.widget.EditText")) {
                StringBuffer sb = new StringBuffer(fName);
                sb.delete(0, 1);
                String methodName = "set" + sb.toString();
                try {
                    Method getText = editTextClass.getMethod("getText");
                    Object object = field.get(this);
                    Editable editatle = (Editable) getText.invoke(object);
                    String str = editatle.toString().trim();
                    Method method = tClass.getMethod(methodName,String.class);
                    method.invoke(t, str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void save();
}
