package com.csscaps.common.font;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.csscaps.common.utils.FontManager;


/**
 * Created by tanglei on 16/7/8.
 */
public class MyFontTextView extends AppCompatTextView {
    public MyFontTextView(Context context) {
        super(context);
        init(context);
    }

    public MyFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setTypeface(FontManager.sTypeface);
    }
}
