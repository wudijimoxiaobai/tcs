package com.csscaps.tcs.activity;

import android.widget.EditText;

import com.csscaps.common.base.BaseActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by tl on 2018/5/21.
 */

public abstract class BaseDetailsActivity<T> extends BaseActivity {

    protected void tIntoTextView(T t) {
        Class thisClass = this.getClass();
        Class tClass = t.getClass();
        Class editTextClass = EditText.class;
        Field[] fields = thisClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fName = field.getName();
            String fieldTypeName = field.getType().getName();
            if (fieldTypeName.equals("android.widget.TextView")) {
                StringBuffer sb = new StringBuffer(fName);
                sb.delete(0, 1);
                String methodName = "get" + sb.toString();
                try {
                    Method method = tClass.getMethod(methodName);
                    Object str = method.invoke(t);
                    Method setText = editTextClass.getMethod("setText", CharSequence.class);
                    Object object = field.get(this);
                    setText.invoke(object, String.valueOf(str));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
