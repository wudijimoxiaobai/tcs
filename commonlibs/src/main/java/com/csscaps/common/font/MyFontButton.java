package com.csscaps.common.font;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.csscaps.common.utils.FontManager;


/**
 * Created by tanglei on 16/7/8.
 */
public class MyFontButton extends AppCompatButton {
    public MyFontButton(Context context) {
        super(context);
        init(context);
    }

    public MyFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyFontButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setTypeface(FontManager.sTypeface);
    }
}
