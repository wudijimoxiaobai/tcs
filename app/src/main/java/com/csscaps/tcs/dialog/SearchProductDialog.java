package com.csscaps.tcs.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.R;
import com.csscaps.tcs.model.SearchProductCondition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchProductDialog extends DialogFragment {
    @BindView(R.id.product_name)
    EditText mProductName;
    @BindView(R.id.local_name)
    EditText mLocalName;

    private Handler mHandler;
    private SearchProductCondition mSearchProductCondition = new SearchProductCondition();

    public SearchProductDialog(Handler handler) {
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
        View view = inflater.inflate(R.layout.search_product_dialog, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        int height = (int) (DeviceUtils.getScreenHeight(getContext()) * 0.3f);
        int width = (int) (DeviceUtils.getScreenWidth(getContext()) * 1f);
        dialogWindow.setLayout(width, height);
        dialogWindow.setWindowAnimations(R.style.scale_anim);
    }

    private void initView() {
    }

    @OnClick({R.id.cancel, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                dismiss();
                mSearchProductCondition.setProductName(mProductName.getText().toString().trim());
                mSearchProductCondition.setLocalName(mLocalName.getText().toString().trim());
                mHandler.sendMessage(mHandler.obtainMessage(0, mSearchProductCondition));
                break;
        }
    }
}
