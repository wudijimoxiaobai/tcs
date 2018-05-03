package com.csscaps.common.font;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.csscaps.common.utils.FontManager;


/**
 * Created by tanglei on 16/7/8.
 */
public class MyFontEditText extends AppCompatEditText {
    public MyFontEditText(Context context) {
        super(context);
        init(context);
    }

    public MyFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyFontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setTypeface( FontManager.sTypeface);

    }
}
