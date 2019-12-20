package com.csscaps.tcs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.csscaps.common.base.BaseActivity;
import com.csscaps.common.utils.ObserverActionUtils;
import com.csscaps.common.utils.ToastUtil;
import com.csscaps.tcs.dialog.ExitDialog;
import com.csscaps.tcs.dialog.SynDataDialog;
import com.csscaps.tcs.fragment.HomeFragment;
import com.csscaps.tcs.fragment.MeFragment;
import com.csscaps.tcs.fragment.MgtFragment;
import com.csscaps.tcs.service.InvoiceNoService;
import com.csscaps.tcs.service.ReportDataService;
import com.csscaps.tcs.service.SynchronizeService;
import com.csscaps.tcs.service.UploadInvoiceService;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tax.fcr.library.network.Api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.csscaps.common.base.BaseApplication.getAppContext;

public class MainActivity extends BaseActivity implements Action1<Object> {
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    List<Fragment> mFragments;
    private int lastIndex;
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
    public void initView(Bundle savedInstanceState) {
        ObserverActionUtils.addAction(this);
        mNavigation.setItemIconTintList(null);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        TCSApplication.unlockSdcard();
        initData();
        initSdDB();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    public void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new MgtFragment());
        mFragments.add(new MeFragment());
        // 初始化展示MessageFragment
        setFragmentPosition(0);
    }

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.FramePage, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setFragmentPosition(0);
                    return true;
                case R.id.navigation_mgt:
                    setFragmentPosition(1);
                    return true;
                case R.id.navigation_me:
                    setFragmentPosition(2);
                    return true;
            }
            return false;
        }
    };

    @OnClick(R.id.syn)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.syn:
                if (TextUtils.isEmpty(Api.getBaseUrl())) {
                    ToastUtil.showShort(getString(R.string.hit56));
                    return;
                }
                startService(new Intent(this, SynchronizeService.class));
                mSynDataDialog.show(getSupportFragmentManager(), "SynDataDialog");
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                ObserverActionUtils.mapAction.clear();
                ((TCSApplication) getAppContext()).cancelTimer();
                stopService(new Intent(getAppContext(), SynchronizeService.class));
                stopService(new Intent(getAppContext(), UploadInvoiceService.class));
                stopService(new Intent(getAppContext(), InvoiceNoService.class));
                stopService(new Intent(getAppContext(), ReportDataService.class));
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
