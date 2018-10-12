package com.csscaps.tcs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.csscaps.common.utils.AppSP;
import com.csscaps.common.utils.AppTools;
import com.csscaps.common.utils.DeviceUtils;
import com.csscaps.tcs.database.table.User;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by tl on 2018/5/4.
 */

public class LaunchActivity extends Activity {
    //默认用户名
    private final String DEFAULT_USER = "admin";
    //默认密码
    private final String DEFAULT_PASSWORD = "12345678";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = DeviceUtils.getScreenHeight(this);
        params.gravity = Gravity.TOP;
        window.setAttributes(params);
        int p = AppSP.getInt("language", 0);
        String str[] = getResources().getStringArray(R.array.language_no);
        AppTools.changeAppLanguage(this, str[p]);
        List<User> list = SQLite.select().from(User.class).queryList();
        if (list.size() == 0) {
            User admin = new User();
            admin.setUserName(DEFAULT_USER);
            admin.setPassword(DEFAULT_PASSWORD);
            admin.setRole(0);
            admin.setStatus(0);
            admin.save();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!LaunchActivity.this.isFinishing()) {
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 1500);
        initSdDB();
    }

    //初始化sd卡备份数据库
    private void initSdDB() {
        //sd卡数据库文件夹
        Map<String, String> map = System.getenv();
        String SDPath=  map.get("SECONDARY_STORAGE");
        File file = new File(SDPath+"/FCR");
        FileDatabaseContext mSdDatabaseContext = new FileDatabaseContext(this, file, false);
        FlowManager.init(mSdDatabaseContext);
//        SDCardUtil.sdcardSetPassword();
//        SDCardUtil.lockSdcard();
    }
}
