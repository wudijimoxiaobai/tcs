package com.csscaps.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by Mingliang on 2016/8/19 019.
 */
public class TwoStateButton extends RadioButton {

    private ButtonState state = ButtonState.UNCHECKED;
    private OnStateChangedListener onStateChangedListener;

    public TwoStateButton(Context context) {
        this(context, null);
    }

    public TwoStateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public ButtonState getState() {
        return state;
    }

    public void setState(ButtonState state) {
        this.state = state;
        setBackgroundColor(state == ButtonState.CHECKED ? Color.BLUE : Color.WHITE);
    }

    public enum ButtonState {
        CHECKED, UNCHECKED
    }

    private void setup() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onStateChangedListener != null) {
                    state = state == ButtonState.CHECKED ? ButtonState.UNCHECKED : ButtonState.CHECKED;
                    setBackgroundColor(state == ButtonState.CHECKED ? Color.BLUE : Color.WHITE);
                    onStateChangedListener.onStateChanged(state);
                }
            }
        });
    }

    public interface OnStateChangedListener  {
        void onStateChanged(ButtonState state) ;
    }

    public OnStateChangedListener getOnStateChangedListener() {
        return onStateChangedListener;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }
}
