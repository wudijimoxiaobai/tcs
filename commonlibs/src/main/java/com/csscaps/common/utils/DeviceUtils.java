package com.csscaps.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.csscaps.common.base.BaseApplication;
import com.tax.fcr.library.utils.NetworkUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 设备工具类
 * <p/>
 * 创建时间: 2014-8-19 下午2:10:48 <br/>
 *
 * @author hwp
 * @since v0.0.1
 */
public abstract class DeviceUtils {
    private final static String TAG = DeviceUtils.class.getSimpleName();

    /**
     * 当前手机时区名称。如：GMT+0800
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeZoneName(Context context) {
        String timeZoneDisplayName = "GMT+0800";
        try {
            // GMT+0800
            timeZoneDisplayName = TimeZone.getDefault().getDisplayName(true,
                    TimeZone.SHORT);
            long offset = TimeZone.getDefault().getRawOffset();
            int zone = (int) (offset / (60 * 60 * 1000));
            zone = zone * 100;
            if (zone >= 0) {
                timeZoneDisplayName = String.format("GMT+%04d", Math.abs(zone));
            } else {
                timeZoneDisplayName = String.format("GMT-%04d", Math.abs(zone));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeZoneDisplayName;
    }

    /**
     * 当前手机时区编号。如：Asia/Shanghai
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeZoneID(Context context) {
        String timeZoneID = "Asia/Shanghai";
        try {
            // Asia/Shanghai
            timeZoneID = TimeZone.getDefault().getID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeZoneID;
    }

    /**
     * 获取手机语言信息(例如：en、zh) <br>
     * (设置成简体中文的时候，getLanguage()返回的是zh,getCountry()返回的是cn)
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getLanguage(Context context) {
        // 获取系统当前使用的语言
        String language = Locale.getDefault().getLanguage();
        return language;
    }

    /**
     * 获取手机国家信息(例如：EN、CN) <br>
     * (设置成简体中文的时候，getLanguage()返回的是zh,getCountry()返回的是cn)
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getCountry(Context context) {
        // 获取区域
        String country = Locale.getDefault().getCountry();
        return country;
    }

    /**
     * 获取当前设备的唯一标识字符串，自己组装的
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String DeviceId, SimSerialNumber, AndroidId;
        DeviceId = "" + tm.getDeviceId();
        SimSerialNumber = "" + tm.getSimSerialNumber();
        AndroidId = ""
                + Secure.getString(
                context.getContentResolver(),
                Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(AndroidId.hashCode(),
                ((long) DeviceId.hashCode() << 32) | SimSerialNumber.hashCode());
        return deviceUuid.toString();
    }

    /**
     * 获取系统唯一标识码(IMEI)<br>
     * 获取机串IMEI: 仅仅只对Android手机有效
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String DeviceId = tm.getDeviceId();
        if (DeviceId == null) {
            return "";
        } else {
            return DeviceId;
        }
    }

    /**
     * 获取系统的ANDROID_ID
     */
    public static String getANDROID_ID(Context context) {
        String ANDROID_ID = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        if (ANDROID_ID == null) {
            return "";
        } else {
            return ANDROID_ID;
        }
    }

    /**
     * @param context
     * @return String
     * @Description: 获取卡串IMSI
     */
    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String id = tm.getSubscriberId();
        if (id == null) {
            return "";
        } else {
            return id;
        }
    }

    /**
     * @return String
     * @Description: 获取手机型号
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * @return String
     * @Description: 获取系统版本
     */
    public static String getOS() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取SIM卡的可用状态
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean isSimActive() {
        TelephonyManager tm = (TelephonyManager) BaseApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return false;
        }
        return TelephonyManager.SIM_STATE_READY == tm.getSimState();
    }

    /**
     * 获取设备是否是横屏(AndroidPad)设备
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean getLandscapeDevice(Context context) {
        Activity activity = (Activity) context;
        int orientation = activity.getResources().getConfiguration().orientation;
        int displayRotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        return (orientation == Configuration.ORIENTATION_PORTRAIT && displayRotation % 2 != 0)
                || (orientation == Configuration.ORIENTATION_LANDSCAPE && displayRotation % 2 == 0);
    }

    /**
     * @Description: 获取手机屏幕宽像素
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * @Description: 获取手机屏幕高像素
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 状态栏的高度
     *
     * @param activity 必须传入Activity
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getScreenStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);// /取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
        int screenWidth = frame.width();
        int screenHeight = frame.height();
        // 状态栏的高度，所以frame.height,frame.width分别是系统的高度的宽度
        int statusBarHeight = frame.top;

        View v = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);// /获得根视图
        // 状态栏标题栏的总高度/ statusBarHeight是上面所求的状态栏的高度
        int contentTop = v.getTop();
        // 所以标题栏的高度为contentTop-statusBarHeight
        int titleBarHeight = contentTop - statusBarHeight;
        // 视图区域可以使用的宽度
        int realViewWidth = v.getWidth();
        // 视图的高度，不包括状态栏和标题栏
        int realViewHeight = v.getHeight();
        String strDeviceSizeInfo = "屏幕宽度" + screenWidth + "\n" + "屏幕高度"
                + screenHeight + "\n" + "状态栏高度" + statusBarHeight + "\n"
                + "标题栏高度" + titleBarHeight + "\n" + "可用宽度" + realViewWidth
                + "\n" + "可用高度（不含状态栏和标题栏）" + realViewHeight;
        Log.i("Test",strDeviceSizeInfo);

        return statusBarHeight;
    }

    /**
     * 获取屏幕分辨率（单位px）（eg:480x800）
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getResolution_px(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels + "x" + dm.heightPixels;
    }

    /**
     * 获取屏幕密度系数 eg 160、240、320<br>
     * 谷歌定义 densityDpi=160为基准。density=densityDpi/标准dpi(160)
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getDensityDpi(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * 获取屏幕密度系数 eg 1.5、2.0、3.0<br>
     * 单位dip/dp的换算值。 一般用在设定控件大小中，这样可以适配在多个分辨率上面。<br>
     * px=1dp*density
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获取屏幕字体缩放比例 单位sp的换算值。 一般用在设定字体大小中，这样可以适配在多个分辨率上面。 px=1sp*scaledDensity
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static float getScaledDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.scaledDensity;
    }

    /**
     * dip转为px
     *
     * @param context
     * @param dip
     * @return px
     */
    public static int dip2Px(Context context, int dip) {
        Resources resources = context.getResources();
        float flo_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, resources.getDisplayMetrics());
        int px = Math.round(flo_px);
        return px;

    }

    public static int sp2Px(Context context, int sp) {
        Resources resources = context.getResources();
        float ipx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                resources.getDisplayMetrics());
        return Math.round(ipx);
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param context
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param spValue
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转为mm
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2mm(Context context, float pxValue) {
        float x = context.getResources().getDisplayMetrics().xdpi;
        return pxValue / x * 25.4f;
    }

    /**
     * 将mm值转为px
     *
     * @param context
     * @param mmValue
     * @return
     */
    public static float mm2px(Context context, float mmValue) {
        float x = context.getResources().getDisplayMetrics().xdpi;
        return mmValue * x / 25.4f;
    }

    /**
     * 获取用于显示的版本号(显示如：1.0.0)
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 获取用于升级的版本号(内部识别号)
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // TODO: 以下操作时对安装包，类文件，以及APP文件的安装、卸载处理

    /**
     * 安装APK程序代码
     *
     * @param context
     * @param apkPath
     */
    public static void ApkInstall(Context context, String apkPath) {
        File fileAPK = new File(apkPath);
        if (fileAPK.exists()
                && fileAPK.getName().toLowerCase().endsWith(".apk")) {
            Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(fileAPK),
                    "application/vnd.android.package-archive");
            context.startActivity(install);// 安装
        }
    }

    /**
     * 卸载APK程序代码
     *
     * @param context
     * @param packageName
     */
    public static void ApkUnInstall(Context context, String packageName) {
        if (isPackageExists(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                    packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    /**
     * 检测该包名所对应的应用是否存在（eg.com.org）
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageExists(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取应用程序的完整包名
     *
     * @param context
     * @return eg. com.xxx.xxx
     */
    public static String getAppPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    /**
     * 获取当前实例所在的父包名
     *
     * @param context
     * @return eg. com.xxx.xxx
     */
    public static String getPackageNameClass(Context context) {
        if (context == null || "".equals(context)) {
            return "";
        }
        return context.getPackageName();
    }


    /**
     * 检测该包名所对应类是否存在（eg.com.org.MainActivity）
     *
     * @param className
     * @return
     */
    public static boolean isClassExists(String className) {
        if (className == null || "".equals(className)) {
            return false;
        }
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    /**
     * 检测当前系统声音是否为正常模式
     *
     * @return
     */
    public boolean isAudioNormal() {
        AudioManager mAudioManager = (AudioManager) BaseApplication.getAppContext()
                .getSystemService(Context.AUDIO_SERVICE);
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isVersionCompat(int VersionCode) {
        int currentVersion = Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    getAppPackageName(context), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取当前设置的电话号码 <a
     * href="http://www.open-open.com/lib/view/open1331537862874.html" >参考资料</a>
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public String getNativePhoneNumber(Context context) {
        String NativePhoneNumber = "";
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        NativePhoneNumber = telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * 获取手机的IMSI码,并判断是中国移动\中国联通\中国电信 <BR>
     * 需要加入权限 android.permission.READ_PHONE_STATE <BR>
     * <a href="http://www.open-open.com/lib/view/open1331537862874.html"
     * >参考资料</a>
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getNetworkOperators(Context context) {
        String strOperators = "unknown";
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = telephonyManager.getSubscriberId();

        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (IMSI != null) {
            if (IMSI.equals("46000") || IMSI.equals("46002")
                    || IMSI.equals("46007")) {
                strOperators = "中国移动";
            } else if (IMSI.equals("46001")) {
                strOperators = "中国联通";
            } else if (IMSI.equals("46003")) {
                strOperators = "中国电信";
            }
        }
        return strOperators;
    }

    /**
     * 获取当前网络类型 <a
     * href="http://blog.csdn.net/shakespeare001/article/details/7505932"
     * >参考资料</a>
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getNetworkType(Context context) {
        String strNetworkType = "unknown";
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return strNetworkType;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            strNetworkType = "WIFI";
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            switch (networkInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_CDMA:// 网络类型为CDMA
                case TelephonyManager.NETWORK_TYPE_EDGE:// 网络类型为EDGE
                case TelephonyManager.NETWORK_TYPE_GPRS:// 网络类型为GPRS
                    strNetworkType = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:// 网络类型为EVDO0
                case TelephonyManager.NETWORK_TYPE_EVDO_A:// 网络类型为EVDOA
                case TelephonyManager.NETWORK_TYPE_HSDPA:// 网络类型为HSDPA
                case TelephonyManager.NETWORK_TYPE_HSPA:// 网络类型为HSPA
                case TelephonyManager.NETWORK_TYPE_HSUPA:// 网络类型为HSUPA
                case TelephonyManager.NETWORK_TYPE_UMTS:// 网络类型为UMTS
                    strNetworkType = "3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    strNetworkType = "4G";
                    break;
            }
        }
        return strNetworkType;
    }

    /**
     * 获取物理层面上的mac地址
     *
     * @return
     */
    public static String getMacAddress() {
        String result = "";
        String Mac = "";
        String line = "";

        try {
            Process proc = Runtime.getRuntime().exec("busybox ifconfig");
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            // 执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine()) != null && line.contains("HWaddr")) {
                result += line;
                break;
            }

            if (result.length() > 0 && result.contains("HWaddr")) {
                Mac = result.substring(result.indexOf("HWaddr") + 6,
                        result.length() - 1);
                if (Mac.length() > 1) {
                    result = Mac.toLowerCase();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    /**
     * 获取系统的MAC地址 (错误返回12个0 )
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getMacAddress(Context context) {
        String strMacAddress = "";
        switch (NetworkUtils.getNetworkType()) {
            case 0:
                strMacAddress = "000000000000";
                break;
            case 1:
                strMacAddress = getMacAddressByWifi(context);
                break;
            default:
                // strMacAddress = getLocalIpAddressByGPRS();
                break;
        }
        return strMacAddress;
    }

    /**
     * 获取设备Wifi的MAC地址 (错误返回12个0 )
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getMacAddressByWifi(Context context) {
        String macAddress = "000000000000";
        try {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress())) {
                    macAddress = info.getMacAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }


    /**
     * 10-13 18:31:03.030: D/hwp(8318): ====Inter-| Receive | Transmit 10-13
     * 18:31:03.030: D/hwp(8318): face |bytes packets errs drop fifo frame
     * compressed multicast|bytes packets errs drop fifo colls carrier
     * compressed 10-13 18:31:03.030: D/hwp(8318): gre0: 0 0 0 0 0 0 0 0 0 0 0 0
     * 0 0 0 0 10-13 18:31:03.030: D/hwp(8318): lo: 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     * 0 0 10-13 18:31:03.030: D/hwp(8318): tunl0: 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     * 0 10-13 18:31:03.030: D/hwp(8318): sit0: 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     * 10-13 18:31:03.030: D/hwp(8318): eth0: 407447144 272089 0 9 0 0 0 0
     * 10088949 132641 0 0 0 0 0 0 10-13 18:31:03.030: D/hwp(8318): ip6tnl0: 0 0
     * 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     * <p/>
     * 10-13 18:43:17.880: D/hwp(8318): ====Inter-| Receive | Transmit 10-13
     * 18:43:17.880: D/hwp(8318): face |bytes packets errs drop fifo frame
     * compressed multicast|bytes packets errs drop fifo colls carrier
     * compressed 10-13 18:43:17.880: D/hwp(8318): gre0: 0 0 0 0 0 0 0 0 0 0 0 0
     * 0 0 0 0 10-13 18:43:17.880: D/hwp(8318): lo: 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     * 0 0 10-13 18:43:17.880: D/hwp(8318): tunl0: 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     * 0 10-13 18:43:17.880: D/hwp(8318): sit0: 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     * 10-13 18:43:17.880: D/hwp(8318): eth0: 407539125 272565 0 9 0 0 0 0
     * 10622328 133247 0 0 0 0 0 0 10-13 18:43:17.880: D/hwp(8318): ip6tnl0: 0 0
     * 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     * <p/>
     * 10-13 18:46:20.090: D/hwp(8318): ====Inter-| Receive | Transmit 10-13
     * 18:46:20.090: D/hwp(8318): face |bytes packets errs drop fifo frame
     * compressed multicast|bytes packets errs drop fifo colls carrier
     * compressed 10-13 18:46:20.090: D/hwp(8318): gre0: 0 0 0 0 0 0 0 0 0 0 0 0
     * 0 0 0 0 10-13 18:46:20.090: D/hwp(8318): lo: 6975 81 0 0 0 0 0 0 6975 81
     * 0 0 0 0 0 0 10-13 18:46:20.090: D/hwp(8318): tunl0: 0 0 0 0 0 0 0 0 0 0 0
     * 0 0 0 0 0 10-13 18:46:20.090: D/hwp(8318): sit0: 0 0 0 0 0 0 0 0 0 0 0 0
     * 0 0 0 0 10-13 18:46:20.090: D/hwp(8318): wlan0: 8136 37 0 14 0 0 0 0
     * 10571 83 0 1 0 0 0 0 10-13 18:46:20.090: D/hwp(8318): eth0: 407543628
     * 272585 0 9 0 0 0 0 10626183 133266 0 0 0 0 0 0 10-13 18:46:20.090:
     * D/hwp(8318): ip6tnl0: 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 10-13 18:46:20.090:
     * D/hwp(8318): p2p0: 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     */
    public static void getNetSpeed() {
        try {
            String macFileString = "/proc/net/dev";
            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(new FileReader(
                    macFileString));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            String macAddress = fileData.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getCpuInfo() {
        try {
            String macFileString = "/proc/cpuinfo";
            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(new FileReader(
                    macFileString));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            String macAddress = fileData.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getNetUpandDown() {
        // /proc/uid_stat/{uid}/tcp_rcv记录该uid应用下载流量字节,/proc/uid_stat/{uid}/tcp_snd
        try {
            String downNet = "/proc/uid_stat/{" + 123 + "}/tcp_rcv";
            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(new FileReader(downNet));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            String macAddress = fileData.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];

        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();

        }
        return s;
    }

    private static void write_key(String filename, String str) {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(str.getBytes());
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String read_key(String filename) {
        File file = new File(filename);
        char[] str = new char[50];
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String macAddrhex = in.readLine();
            String macAddr = toStringHex(macAddrhex);
            in.close();
            return macAddr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取以太网的mac地址（设备自带网线接口时）
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getMacAddressOfEthernet() {
        try {
            String macFileString = "/sys/class/net/eth0/address";
            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(new FileReader(
                    macFileString));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            String macAddress = fileData.toString();
            return macAddress.toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前网络下的IP地址 <a
     * href="http://www.cnblogs.com/android100/p/Android-get-ip.html" >参考资料</a>
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getIPAddress(Context context) {
        String strIPAddress = "";
        switch (NetworkUtils.getNetworkType()) {
            case 0:
                strIPAddress = "0.0.0.0";
                break;
            case 1:
                strIPAddress = getLocalIpAddressByWifi(context);
                break;
            default:
                strIPAddress = getLocalIpAddressByGPRS();
                break;
        }
        return strIPAddress;
    }

    /**
     * 获取Wifi网络下的IP地址
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getLocalIpAddressByWifi(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
                    + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
        } catch (Exception ex) {
        }
        return "0.0.0.0";
    }

    /**
     * 获取GPRS移动网络或以太网下的IP地址
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getLocalIpAddressByGPRS() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "0.0.0.0";
    }


    /**
     * 判断是否具有ROOT权限
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean checkPermissionRoot() {
        boolean res = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                res = false;
            } else {
                res = true;
            }
        } catch (Exception e) {

        }
        return res;
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    public static String getDevicesName() {
        return Build.DEVICE;
    }

    /**
     * 获取网络连接方式wifi或者ETHERNET
     *
     * @param context
     * @return
     */
    public static String getNetworkTypeWifiOrBrand(Context context) {
        String strNetworkType = "neterror";
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return strNetworkType;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            strNetworkType = "WIFI";
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            strNetworkType = "MOBILE";
        } else {
            strNetworkType = "ETHERNET";
        }
        return strNetworkType;
    }

    public static long getMaxCpuFreq() {
        String result = "0";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return Long.parseLong(result.trim());
    }

    public static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Print exception
            e.printStackTrace();
            // Default to return 1 core
            return 1;
        }
    }

    /**
     * 获取mac地址的后4位，装换成10进制去,之后去转化后的后6位为SNR吗
     *
     * @param mac
     * @return
     */
    public static String getIMEIByMAC(String mac) {
        String TAC = "000888";
        String FAC = "01";
        String SP = "0";
        StringBuilder SNRstr = new StringBuilder();
        if (!TextUtils.isEmpty(mac)) {
            String[] hexadecimal = mac.split(":");
            if (hexadecimal != null && hexadecimal.length > 4) {
                int hexadecimalLength = hexadecimal.length;
                for (int i = hexadecimalLength - 3; i < hexadecimalLength; i++) {
                    SNRstr.append(hexadecimal[i]);
                }
            }
        }

        int SNRint = Integer.parseInt(SNRstr.toString(), 16);
        String SNR = String.format("%06d", SNRint);
        if (SNR.length() > 6) {
            SNR = SNR.substring(SNR.length() - 6, SNR.length());
        }

        StringBuilder IMEI = new StringBuilder().append(TAC).append(FAC).append(SNR).append(SP);
        return IMEI.toString();
    }

    /**
     * 获取系统设置屏幕亮度
     * @param context
     * @return
     */
    public static int getSystemBrightness(Context context) {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    /**
     *  设置屏幕亮度
     * @param brightness 0-255  -1:跟随系统亮度
     * @param activity
     */
    public static void setBrightness(int brightness, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if(brightness==-1){
            lp.screenBrightness =WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        }else{
            lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        }
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 检查是否有虚拟按键
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 隐藏虚拟按键
     * @param window
     */
    public static void setHideVirtualKey(Window window){
        //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT>=19){
            uiOptions |= 0x00001000;
        }else{
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }

    /**
     * 通过反射，获取包含虚拟键的整体屏幕高度
     *
     * @return
     */
    public static int getHasVirtualKey(Activity activity) {
        int dpi = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

}
