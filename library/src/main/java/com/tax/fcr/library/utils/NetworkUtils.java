package com.tax.fcr.library.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 网络访问工具类
 * <p/>
 * 创建时间: 2014-8-19 下午2:03:22 <br/>
 *
 * @author hwp
 * @since v0.0.1
 */
public class NetworkUtils {
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static final String CTWAP = "ctwap";
    public static final String CMWAP = "cmwap";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
    public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
    public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
    public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
    public static Context mContext;

    /**
     * 判断网络连接是否连通
     *
     * @return
     */
    public static boolean netWorkJudge() {
        return isNetworkAvailable(mContext);
    }

    /**
     * 检查网络连接,返回网络连接状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isAvailable());

    }

    // public static void handleForceUpdate(final Context context) {
    // // 设置提示信息
    // String dialogMessage = TextUtils.isEmpty(AppPreferences.getInstance()
    // .getVersionDescription()) ? context
    // .getString(R.string.forced_updating_message) : AppPreferences
    // .getInstance().getVersionDescription();
    //
    // // 提示对话框
    // AlertDialog.Builder builder = new Builder(context);
    // builder.setTitle(R.string.dialog_title_tip);
    // builder.setMessage(dialogMessage);
    // builder.setCancelable(false);
    // builder.setPositiveButton(R.string.yes, new OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // new AppUpdateHandle(context).doWebUpdate();
    // }
    // });
    // builder.setNegativeButton(R.string.no, new OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // dialog.dismiss();
    // ((Activity) context).finish();
    // }
    // });
    // builder.create().show();
    // }

    /**
     * 检查是否支持非market应用的安装
     */
    public static boolean checkAppSettings(Context context) {
        int result = Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 跳转到系统网络设置页面
     *
     * @param context
     * @author hwp
     * @since v0.0.1
     */
    public static void showNetworkSetting(Context context) {
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        context.startActivity(wifiSettingsIntent);
    }

    /**
     * 判断当前网络模式是否为CTWAP
     *
     * @param context
     * @return
     */
    public static boolean isCtwapNet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
        if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
            return false;
        } else if (netWrokInfo.getTypeName().equals("mobile")
                && netWrokInfo.getExtraInfo().equals("ctwap")) {
            return true;
        }
        return false;
    }

    /**
     * 是否是CMWAP
     *
     * @return
     * @author liuyun
     * @since v0.1
     */
    public static boolean isCMWAP() {
        boolean available = false;

        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        // ExtraInfo在某些手机中为空值
        String extraInfo = info.getExtraInfo();
        if (TextUtils.isEmpty(extraInfo)) {
            available = false;
        } else {
            available = info.getTypeName().equalsIgnoreCase("mobile")
                    && info.getExtraInfo().equalsIgnoreCase("cmwap");
        }
        return available;
    }

    public static String getApnType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
        if (netWrokInfo.getTypeName().equals("mobile")) {
            return netWrokInfo.getExtraInfo();
        }
        return null;
    }

    /**
     * 判断Network具体类型（联通移动wap，电信wap，其他net）
     **/
    public static int checkNetworkType(Context context) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager
                    .getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                return TYPE_OTHER_NET;
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    return TYPE_OTHER_NET;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    // 注意二：
                    // 判断是否电信wap:
                    // 不要通过getExtraInfo获取接入点名称来判断类型，
                    // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
                    // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
                    // 所以可以通过这个进行判断！
                    final Cursor c = context.getContentResolver().query(
                            PREFERRED_APN_URI, null, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        final String user = c.getString(c
                                .getColumnIndex("user"));
                        if (!TextUtils.isEmpty(user)) {
                            if (user.startsWith(CTWAP)) {
                                return TYPE_CT_WAP;
                            }
                        }
                    }
                    c.close();
                    // 注意三：
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断
                    String netMode = mobNetInfoActivity.getExtraInfo();
                    if (netMode != null) {
                        // 通过apn名称判断是否是联通和移动wap
                        netMode = netMode.toLowerCase();
                        if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
                                || netMode.equals(UNIWAP)) {
                            return TYPE_CM_CU_WAP;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return TYPE_OTHER_NET;
        }
        return TYPE_OTHER_NET;
    }

    /**
     * @return true:是wap网络; false:不是wap网络.
     */
    public static boolean isWapInternet(Context context) {
        int type = checkNetworkType(context);
        return type == TYPE_CM_CU_WAP || type == TYPE_CT_WAP;
    }

    /**
     * 移动，联通的WAP 代理：10.0.0.172
     *
     * @return true:是wap网络; false:不是wap网络.
     */
    public static boolean isWAP_YD_LT(Context context) {
        int type = checkNetworkType(context);
        return type == TYPE_CM_CU_WAP;
    }

    /**
     * 电信的WAP 代理：10.0.0.200
     *
     * @return true:是wap网络; false:不是wap网络.
     */
    public static boolean isWAP_DX(Context context) {
        int type = checkNetworkType(context);
        return type == TYPE_CT_WAP;
    }

    /**
     * 获取当前网络类型
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getCurrentNetworkType22() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            Log.d("NetworkUtil", info.toString());
            return info.getType();
        }
        return -1;
    }

    /**
     * 检查Wifi网络连接,返回Wifi网络连接状态
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean isNetworkAvailableByWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (mWifi != null && mWifi.isConnected());
    }

    /**
     * 检查Moblie手机移动网络连接,返回Moblie手机移动网络连接状态
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean isNetworkAvailableByMobile(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMoblie = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (mMoblie != null && mMoblie.isConnected());
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 枚举网络状态 <br>
     * NET_NO：没有网络 <br>
     * NET_2G：2g网络 <br>
     * NET_3G：3g网络 <br>
     * NET_4G：4g网络 <br>
     * NET_WIFI：wifi <br>
     * NET_UNKNOWN：未知网络
     */
    public static enum SelfNetState {
        NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
    }

    ;

    /**
     * 获取我们自己定义的网络类型
     *
     * @param context
     * @return NET_NO：没有网络 <br>
     * NET_2G：2g网络 <br>
     * NET_3G：3g网络 <br>
     * NET_4G：4g网络 <br>
     * NET_WIFI：wifi <br>
     * NET_UNKNOWN：未知网络
     */
    public static SelfNetState getNetworkType_Self(Context context) {
        SelfNetState networkState = SelfNetState.NET_NO;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()
                && networkInfo.isAvailable()) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    networkState = SelfNetState.NET_WIFI;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (networkInfo.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            networkState = SelfNetState.NET_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            networkState = SelfNetState.NET_3G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            networkState = SelfNetState.NET_4G;
                            break;
                        default:
                            networkState = SelfNetState.NET_UNKNOWN;
                    }
                    break;
                default:
                    networkState = SelfNetState.NET_UNKNOWN;
            }

        }
        return networkState;
    }

    public synchronized static String[] getEthdata() {
        final String DEV_FILE = "/proc/net/dev";// 系统流量文件
        String[] ethdata = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0"};
        final String ETHLINE = "  eth0";// eth是以太网信息 tiwlan0 是 Wifi rmnet0是GPRS

        FileReader fstream = null;
        try {
            fstream = new FileReader(DEV_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Could not read " + DEV_FILE);
        }
        BufferedReader in = new BufferedReader(fstream, 500);
        String line;
        String[] segs;
        String[] netdata;
        int k;
        int j;
        try {
            while ((line = in.readLine()) != null) {
                segs = line.trim().split(":");
                if (line.startsWith(ETHLINE)) {
                    netdata = segs[1].trim().split(" ");
                    for (k = 0, j = 0; k < netdata.length; k++) {
                        if (netdata[k].length() > 0) {
                            ethdata[j] = netdata[k];
                            j++;
                        }
                    }
                }
            }
            fstream.close();
            in.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return ethdata;

    }


}