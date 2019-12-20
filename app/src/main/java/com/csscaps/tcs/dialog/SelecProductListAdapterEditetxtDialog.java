package com.csscaps.tcs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csscaps.tcs.R;

import butterknife.BindView;

public class SelecProductListAdapterEditetxtDialog extends Dialog {

    private EditText mSelecAdapterEdittext;
    private  TextView mConfiem;
    private  TextView mCancel;

    private String Speoplename;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确认按钮被点击了的监听器


    public SelecProductListAdapterEditetxtDialog(@NonNull Context context) {
        super(context, R.style.dialog_theme3);
    }

    /**
     * 设置取消按钮的显示内容和监听
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确认按钮的显示内容和监听
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittext_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    private void initEvent() {
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
        mConfiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });

    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了
        if (Speoplename != null) {
            mSelecAdapterEdittext.setText(Speoplename);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mSelecAdapterEdittext = findViewById(R.id.selec_adapter_edittext);
        mConfiem = findViewById(R.id.confiem);
        mCancel = findViewById(R.id.cancel);
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setnumber(String message) {
        Speoplename = message;
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public String getnumber() {
        return mSelecAdapterEdittext.getText().toString();
    }
}
