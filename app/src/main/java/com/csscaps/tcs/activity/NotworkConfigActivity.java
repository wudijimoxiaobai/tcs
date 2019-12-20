package com.csscaps.tcs.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.RegexUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.BuildConfig;
import com.csscaps.tcs.R;
import com.csscaps.tcs.RTCUtil;
import com.csscaps.tcs.ServerConstants;
import com.tax.fcr.library.network.Api;
import com.tax.fcr.library.network.IPresenter;
import com.tax.fcr.library.network.RequestModel;

import butterknife.BindView;
import butterknife.OnClick;

public class NotworkConfigActivity extends BaseActivity implements IPresenter {
    @BindView(R.id.newwork_config_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_edt_notwork_config_address)
    EditText mServerAddress;
    @BindView(R.id.fragment_edt_notwork_config_port)
    EditText mServerPort;

    private String serverAddress;
    private String serverPort;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_notwork;
    }

    @Override
    protected void onInitPresenters() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        serverAddress = AppSP.getString("serverAddress");
        serverPort = AppSP.getString("serverPort");
//        uploadAddress = AppSP.getString("uploadAddress");
        mServerAddress.setText(serverAddress);
        mServerPort.setText(serverPort);
//        mUploadAddress.setText(uploadAddress);
        mServerAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    serverAddress = mServerAddress.getText().toString().trim();
                    AppSP.putString("serverAddress", serverAddress);
                }
            }
        });

        mServerPort.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    serverPort = mServerPort.getText().toString().trim();
                    AppSP.putString("serverPort", serverPort);
                }
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    @OnClick({R.id.fragment_btn_notwork})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_btn_notwork:
                mServerAddress.clearFocus();
                mServerPort.clearFocus();
                test();
                break;
        }
    }

    private void test() {
        if (TextUtils.isEmpty(serverAddress)) {
            ToastUtil.showShort(getString(R.string.hit38));
            return;
        } else {
            if (!RegexUtils.isIP(serverAddress)) {
                ToastUtil.showShort(getString(R.string.hit43));
                return;
            }
        }

        if (TextUtils.isEmpty(serverPort)) {
            ToastUtil.showShort(getString(R.string.hit39));
            return;
        } else {
            int port = Integer.valueOf(serverPort);
            if (port < 1 || port > 65535) {
                ToastUtil.showShort(getString(R.string.hit42));
                return;
            }
        }

       /* if (TextUtils.isEmpty(uploadAddress)) {
            ToastUtil.showShort(getString(R.string.hit41));
            return;
        }*/
        String url;
        if (BuildConfig.https) {
            url = String.format(getString(R.string.url_https_format), serverAddress, serverPort);
        } else url = String.format(getString(R.string.url_http_format), serverAddress, serverPort);
        Api.setBaseUrl(url);
        RequestModel requestModel = new RequestModel();
        requestModel.setFuncid(ServerConstants.A000);
        Api.post(this, requestModel);
    }

    @Override
    public void onSuccess(String requestPath, String objectString) {
        ToastUtil.showLong(getString(R.string.hit40));
//        mContext.startService(new Intent(mContext, SynchronizeService.class).putExtra("autoSyn", true));
        RTCUtil.setRTCFormServer();
    }

    @Override
    public void onFailure(String requestPath, String errorMes) {
        switch (errorMes) {
            case Api.ERR_NETWORK:
                ToastUtil.showShort(getString(R.string.hit3));
                break;
            case Api.FAIL_CONNECT:
                ToastUtil.showShort(getString(R.string.hit4));
                break;
        }
    }
}
