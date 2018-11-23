package com.csscaps.tcs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.DateUtils;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.activity.InvoiceInformationManagementActivity;
import com.csscaps.tcs.activity.InvoiceIssuingActivity;
import com.csscaps.tcs.activity.InvoiceManagementActivity;
import com.csscaps.tcs.activity.OnlineDeclarationActivity;
import com.csscaps.tcs.activity.SystemManagementActivity;
import com.csscaps.tcs.dialog.ExitDialog;
import com.csscaps.tcs.dialog.SynDataDialog;
import com.csscaps.tcs.service.SynchronizeService;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tax.fcr.library.network.Api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.csscaps.common.base.BaseApplication.getAppContext;

public class MainActivity extends BaseActivity implements Action1<Object> {

    @BindView(R.id.user)
    TextView mUser;
    @BindView(R.id.time_date)
    TextView mTimeDate;
    @BindView(R.id.first_row)
    LinearLayout mFirstRow;
    @BindView(R.id.second_row)
    LinearLayout mSecondRow;

    private String name;
    private SynDataDialog mSynDataDialog = new SynDataDialog();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitPresenters() {
        startService(new Intent(this, SynchronizeService.class).putExtra("autoSyn", true));
    }

    @Override
    protected void parseArgumentsFromIntent(Intent argIntent) {
        name = argIntent.getStringExtra("name");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initSdDB();
        mUser.setText(name);
        mHandler.sendEmptyMessage(0);
        ObserverActionUtils.addAction(this);
      /*  int w= (int) (DeviceUtils.getScreenWidth(this)*2f/3f);
        int h= (int) ((DeviceUtils.getScreenHeight(this)-DeviceUtils.dip2Px(this,85)-DeviceUtils.getScreenStatusBarHeight(this))*1f/3f);
        mFirstRow.getLayoutParams().width=w;
        mFirstRow.getLayoutParams().height=h;
        mSecondRow.getLayoutParams().width=w;
        mSecondRow.getLayoutParams().height=h;*/
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            String time = DateUtils.getDateToString_HH_MM_SS_EN(DateUtils.getDateNow());
            String date = DateUtils.getDateToString_YYYY_MM_DD_EN(DateUtils.getDateNow());
            int mWeekday = DateUtils.getDayOfWeek(DateUtils.getDateNow());
            String week = getResources().getStringArray(R.array.weekday)[mWeekday - 1];
//            String format = getResources().getString(R.string.date_time);
//            mTimeDate.setText(String.format(format, time, date, week));
            mTimeDate.setText(date + " " + week);
            sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeMessages(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessage(0);
    }

    @OnClick({R.id.syn, R.id.exit, R.id.invoice_information_management, R.id.invoice_issuing, R.id.invoice_management, R.id.statistics, R.id.declaration, R.id.system_management})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                exit();
                break;
            case R.id.syn:
                if (TextUtils.isEmpty(Api.getBaseUrl())) {
                    ToastUtil.showShort(getString(R.string.hit56));
                    return;
                }
                startService(new Intent(this, SynchronizeService.class));
                mSynDataDialog.show(getSupportFragmentManager(), "SynDataDialog");
                break;
            case R.id.invoice_issuing:
                startActivity(new Intent(this, InvoiceIssuingActivity.class));
                break;
            case R.id.invoice_management:
                startActivity(new Intent(this, InvoiceManagementActivity.class));
                break;
            case R.id.declaration:
                startActivity(new Intent(this, OnlineDeclarationActivity.class));
                break;
            case R.id.statistics:
              /*  byte b[] = new byte[170];
                FMUtil.readFM(0, b);
                Logger.i(0 + " ---readFM---  " + ConvertUtils.bytes2HexString(b));*/
               /* Invoice invoice=new Invoice();
                invoice.setInvoice_no(String.valueOf(1));
                SdcardDBUtil.saveSDDB(invoice, SDInvoiceDatabase.class);
*/
                break;
            case R.id.system_management:
                startActivity(new Intent(this, SystemManagementActivity.class));
                break;
            case R.id.invoice_information_management:
                startActivity(new Intent(this, InvoiceInformationManagementActivity.class));
                break;
        }
    }

    @Override
    public void call(Object o) {
        mSynDataDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                SdcardUtil.lockSdcard();
                finish();
                System.exit(0);
            }
        };
        ExitDialog exitDialog = new ExitDialog(handler);
        exitDialog.show(getSupportFragmentManager(), "ExitDialog");
    }

    //初始化sd卡备份数据库
    private void initSdDB() {
        //sd卡数据库文件夹
        Map<String, String> map = System.getenv();
        String SDPath = map.get("SECONDARY_STORAGE");
        File file = new File(SDPath + "/FCR");
        FileDatabaseContext mSdDatabaseContext = new FileDatabaseContext(this, file, false);
        FlowManager.init(mSdDatabaseContext);
    }

    @Override
    protected void onDestroy() {
        SdcardUtil.lockSdcard();
        super.onDestroy();
    }

    private void copy() {
        String dataXmlPath = getAppContext().getFilesDir().getAbsolutePath() + "/English/data.xml";
        try {
            FileInputStream is = new FileInputStream(new File(dataXmlPath));
            File file = new File("/sdcard/data.xml");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1)
                fos.write(buf, 0, len);
            fos.flush();
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
