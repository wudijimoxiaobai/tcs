package com.csscaps.common.utils;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.csscaps.common.Constant;
import com.csscaps.common.base.BaseApplication;
import com.csscaps.common.permission.ConstantPermission;
import com.csscaps.common.permission.PermissionsChecker;
import com.tax.fcr.library.utils.Logger;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * AUTHOR: Alex
 * DATE: 12/11/2015 09:47
 */
public class AppTools {


    /**
     * 获取日期
     *
     * @param textView
     * @return
     */
    public static void obtainData(final Context activity, final TextView textView, final String pattern) {
        final DatePickerDialog datePickerDialog;
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.set(year, monthOfYear, dayOfMonth);
                Date date = new Date(cal.getTimeInMillis());
                textView.setText(DateUtils.dateToStr(date, pattern));
                textView.setTag(DateUtils.getDateToString_YYYY_MM_DD_EN(date));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    /**
     * 获取日期
     *
     * @return
     */
    public static void obtainData(Context context, final Handler handler, final int what) {
        final DatePickerDialog datePickerDialog;
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.set(year, monthOfYear, dayOfMonth);
                Date date = new Date(cal.getTimeInMillis());
                handler.sendMessage(handler.obtainMessage(what, date));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    /**
     * 调用系统相册
     *
     * @param activity
     */
    public static void getSystemImage(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, Constant.SELECT_PICTURE);
    }

    /**
     * 调用系统相册 Fragment
     *
     * @param fragment
     */
    public static void getSystemImage(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(intent, Constant.SELECT_PICTURE);
    }

    /**
     * Fragment调用系统相机
     *
     * @param fragment
     */
    public static String getSystemCamera(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasApk(intent)) {
            if (PermissionsChecker.getPermissionsChecker().lacksPermissions(fragment.getActivity(), ConstantPermission.PERMISSIONS_PICTURE)) {
                PermissionsChecker.getPermissionsChecker().startPermissionsActivity(fragment.getActivity(), ConstantPermission.PERMISSIONS_PICTURE);
            } else {
                String path = getImageSavePath(fragment.getActivity()) + "/" + System.currentTimeMillis() + ".jpg";
                File tempFile = new File(path);
                //獲取系統版本
                int currentapiVersion = Build.VERSION.SDK_INT;
                if (currentapiVersion < 24) {
                    // 从文件中创建uri
                    Uri uri = Uri.fromFile(tempFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                } else {
                    //兼容android7.0 使用共享文件的形式
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                    Uri uri = fragment.getActivity().getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                fragment.startActivityForResult(intent, Constant.CAMERA_REQUEST_CODE);
                return path;
            }
        }
        return "";
    }

    /**
     * 调用系统相机 Activity
     *
     * @param activity
     */
    public static String getSystemCamera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasApk(intent)) {
            if (PermissionsChecker.getPermissionsChecker().lacksPermissions(activity, ConstantPermission.PERMISSIONS_PICTURE)) {
                PermissionsChecker.getPermissionsChecker().startPermissionsActivity(activity, ConstantPermission.PERMISSIONS_PICTURE);
            } else {
                String path = getImageSavePath(activity) + "/" + System.currentTimeMillis() + ".jpg";
                File tempFile = new File(path);
                //獲取系統版本
                int currentapiVersion = Build.VERSION.SDK_INT;
                if (currentapiVersion < 24) {
                    // 从文件中创建uri
                    Uri uri = Uri.fromFile(tempFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                } else {
                    //兼容android7.0 使用共享文件的形式
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                    Uri uri = activity.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                activity.startActivityForResult(intent, Constant.CAMERA_REQUEST_CODE);
                return path;
            }
        }
        return "";
    }


    /**
     * 方法名:获取图片文件存取路径
     * <p>
     * 功能说明：获取图片文件存取路径
     * </p>
     *
     * @return
     */
    public static String getImageSavePath(Context context) {
        if (AppTools.getSDPath().equals("")) {
            return context.getFilesDir().getPath();
        }
        File file = new File(AppTools.getSDPath() + "/image");
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file.getPath();
            }
            return "";
        }
        return file.getPath();
    }

    /**
     * 方法名:获取文件存取路径
     * <p>
     * 功能说明：获取文件存取路径
     * </p>
     *
     * @return
     */
    public static String getFileSavePath(Context context, String dirPath) {
        if (AppTools.getSDPath().equals("")) {
            return context.getFilesDir().getPath();
        }
        File file = new File(AppTools.getSDPath() + dirPath);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file.getPath();
            }
            return "";
        }
        return file.getPath();
    }

    /**
     * 方法名: 判断SD卡是否存在
     * <p>
     * 功能说明： 判断SD卡是否存在, 如果存在返回SD卡路径 , 如果不存在返回路径为空
     * </p>
     *
     * @return
     */
    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            File sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }
        return "";
    }

    /**
     * 更具uri获取绝对路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getAbsolutePath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (uri.toString().contains("file://")) {

            String path = uri.toString().replace("file://", "");

            return Uri.decode(path);
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null,
                null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    /**
     * 忽略map中指定字段
     *
     * @param map
     * @param property
     * @return
     */
    public static Map<String, String> ignoreProperty(Map<String, String> map, String property) {
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (property.equals(key)) {
                iterator.remove();        //删除createTime字段
                map.remove(key);
            }
        }
        return map;
    }


    /**
     * 发短信
     *
     * @param phone
     */
    public static void sendSMS(Context context, String phone) {
        Uri smsToUri = Uri.parse("smsto:" + (TextUtils.isEmpty(phone) ? "" : phone));
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        if (!hasApk(intent)) {
            ToastUtil.showShort("无法发送短信");
            return;
        }
        intent.putExtra("sms_body", "");
        context.startActivity(intent);

    }

    /**
     * 打电话
     *
     * @param context
     * @param phone
     */
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (!hasApk(intent)) {
            ToastUtil.showShort("此设备无打电话功能");
            return;
        }
        intent.setData(Uri.parse("tel:" + (TextUtils.isEmpty(phone) ? "" : phone)));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(intent);
    }

//    /**
//     * 异步加载图片
//     *
//     * @param context
//     * @param url
//     * @param imageView
//     */
//    public static void setImageViewPicture(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).placeholder(R.drawable.defaultavatar).error(R.drawable.defaultavatar).transform(new GlideRoundTransform(context)).into(imageView);
//    }
//
//    /**
//     * 异步加载方块图片
//     *
//     * @param context
//     * @param url
//     * @param imageView
//     */
//    public static void setImageViewClub(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).placeholder(R.drawable.defaultavatar).error(R.drawable.defaultavatar).into(imageView);
//    }

    /**
     * 切换动画效果
     *
     * @param view
     * @param alpha
     * @param startScaleX
     * @param startScaleY
     * @param duration
     */
    public static void fadeInView(View view, float alpha, float startScaleX, float startScaleY, int duration) {
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", alpha, 1f);
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", startScaleX, 1f);
        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", startScaleY, 1f);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorAlpha).with(animatorScaleX).with(animatorScaleY);
        animatorSet.setDuration(duration);
        animatorSet.start();
    }

    /**
     * 千分位
     *
     * @param s
     * @return
     */
    public static String getNumKb(String s) {
        if (!TextUtils.isEmpty(s)) {
            NumberFormat formatter = new DecimalFormat("###,###");
            String result = formatter.format(Double.parseDouble(s));
            return result;
        }
        return s;
    }

    /**
     * 千分位 两位小数
     *
     * @param s
     * @return
     */
    public static String getNumKbDot(String s) {
        if (!TextUtils.isEmpty(s)) {
            NumberFormat formatter = new DecimalFormat("###,##0.00");
            String result = formatter.format(Double.parseDouble(s));
            return result;
        }
        return s;
    }

    public static String getNumKbDotSpecile(String s) {
        if (!TextUtils.isEmpty(s)) {
            NumberFormat formatter = new DecimalFormat("###,##0.0000");
            String result = formatter.format(Double.parseDouble(s));
            return result;
        }
        return s;
    }

    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 方法名:getFileByte
     * <p>
     * 功能说明：将字节数转换成文件流
     * </p>
     *
     * @param filePath
     * @return
     */
    public static String getFileByte(String filePath) {
        int count;
        int num = 0;
        File file = new File(filePath);
        long len = file.length();
        if (file.exists()) {
            FileInputStream fis = null;
            StringBuffer sb = new StringBuffer();
            try {
                fis = new FileInputStream(file);
                byte[] buffer = new byte[(int) len];
                while ((count = fis.read(buffer)) != -1) {
                    sb.append(Base64.encodeToString(buffer, Base64.DEFAULT));
                    num = count++;
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 遍历参数
     *
     * @param map
     */
    public static void ergodicParameters(Map<String, String> map) {
        if (map != null) {
            Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                Log.i("Retrofit", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        }
    }


    /**
     * 读取图片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转至0°
     *
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }


    /**
     * 图片压缩
     *
     * @param fromPath
     * @param toPath
     * @param w
     * @param h
     */
    public static void compressImage(String fromPath, final String toPath, int w, int h) {
        Glide.with(BaseApplication.getAppContext()).load(fromPath).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                FileOutputStream out = null;
                try {
                    File f = new File(toPath);
                    if (f.exists()) {
                        f.delete();
                    }
                    out = new FileOutputStream(new File(toPath));
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    ExifInterface exifInterface = null;
                    try {

                        exifInterface = new ExifInterface(toPath);
                        int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                        int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                        Logger.i("h= " + height + " w= " + width);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    try {
                        if (out != null)
                            out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                return false;
            }
        }).into(w, h);

    }

    /**
     * 图片压缩
     *
     * @param path
     * @param w
     * @param h
     */
    public static void compressImage(final String path, int w, int h) {
        compressImage(path, path, w, h);
    }

    /**
     * 判断手机号码是否合法
     *
     * @param mobiles
     * @return
     */
    public static boolean isPhone(String mobiles) {
        Pattern p = Pattern.compile("1[34578]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    /**
     * 判断字符是否为中文
     * <p/>
     * Í
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断系统中是否存在可以启动相应的apk
     *
     * @return 存在返回true，不存在返回false
     */
    public static boolean hasApk(Intent intent) {
        PackageManager packageManager = BaseApplication.getAppContext().getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     *
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top, final int bottom, final int left, final int right) {

        try {
            ((View) view.getParent()).post(new Runnable() {
                @Override
                public void run() {
                    Rect bounds = new Rect();
                    view.setEnabled(true);
                    view.getHitRect(bounds);

                    bounds.top -= top;
                    bounds.bottom += bottom;
                    bounds.left -= left;
                    bounds.right += right;

                    TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                    if (View.class.isInstance(view.getParent())) {
                        ((View) view.getParent()).setTouchDelegate(touchDelegate);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将javaBean转换成Map
     *
     * @param javaBean
     * @return
     */
    public static Map<String, Object> javaBeanToMap(Object javaBean) {
        Map<String, Object> map = new IdentityHashMap<String, Object>();
        Method[] methods = javaBean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    Object value = method.invoke(javaBean, (Object[]) null);
                    map.put(field, null == value ? "" : value);
                }
            } catch (Exception e) {
            }
        }
        return map;
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }


    /**
     * 获得popupwindow
     *
     * @param contentView
     * @param width
     * @param height
     * @return PopupWindow
     */
    public static PopupWindow getPopupWindow(View contentView, int width, int height) {
        PopupWindow mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(width);
        mPopWindow.setHeight(height);
        mPopWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        mPopWindow.setBackgroundDrawable(dw);
        mPopWindow.setOutsideTouchable(true);
        return mPopWindow;
    }

    /**
     * 整数(秒数)转换为时分秒格式(xx:xx:xx)
     *
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    /**
     * 小于10的数字 前面补0
     *
     * @param i
     * @return
     */
    public static String unitFormat(int i) {
        DecimalFormat df=new DecimalFormat("00");
        String retStr=df.format(i);
        return retStr;
    }


    /**
     * 显示和隐藏的软件盘切换显示
     *
     * @param context
     */
    public static void showHideSoftInput(Context context) {
        if (context == null) return;
        InputMethodManager inManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 隐藏软键盘
     *
     * @param et
     */
    public static void hideSoftInput(EditText et) {
        InputMethodManager inManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * 显示软件盘
     *
     * @param et
     */
    public static void showSoftInput(EditText et) {
        InputMethodManager inManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inManager.showSoftInput(et, 0);
    }


    /**
     * 测量View的大小
     *
     * @param child
     */
    public static void measureView(View child) {
        int childMeasureWidth, childMeasureHeight;
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //的宽度信息
        if (lp.width > 0) {
            childMeasureWidth = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
        } else {
            childMeasureWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//未指定
        }
        //的高度信息
        if (lp.height > 0) {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
        } else {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//未指定
        }
        //将宽和高设置给child
        child.measure(childMeasureWidth, childMeasureHeight);
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        //全屏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 修改TextView 部分字体颜色
     *
     * @param textView
     * @param subStr
     * @param color
     */
    public static void modifyTextViewPartColor(TextView textView, String[] subStr, String[] color) {
        String textStr = textView.getText().toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(textStr);
        for (int i = 0; i < subStr.length; i++) {
            int index = 0, strat = 0, end = 0;
            String str = textStr;
            while (index != -1) {
                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color[i]));
                index = str.indexOf(subStr[i]);
                if (index != -1) {
                    strat = index + end;
                    end = strat + subStr[i].length();
                    builder.setSpan(span, strat, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    str = textStr.substring(end);
                }
            }
        }
        textView.setText(builder);
    }

    /**
     * 修改TextView 第position重复关键字 字体颜色
     *
     * @param textView
     * @param subStr
     * @param color
     */
    public static void modifyTextViewPartColor(TextView textView, String[] subStr, String[] color, int position) {
        String textStr = textView.getText().toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(textStr);
        for (int i = 0; i < subStr.length; i++) {
            int index = 0, strat = 0, end = 0, p = 0;
            String str = textStr;
            while (index != -1) {
                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color[i]));
                index = str.indexOf(subStr[i]);
                if (index != -1) {
                    strat = index + end;
                    end = strat + subStr[i].length();
                    if (p == position)
                        builder.setSpan(span, strat, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    str = textStr.substring(end);
                    p++;
                }

            }

            if (position > p - 1) {
                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color[i]));
                index = textStr.indexOf(subStr[i]);
                strat = index;
                end = strat + subStr[i].length();
                builder.setSpan(span, strat, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(builder);
    }

    /**
     * @param view
     * @return
     */
    public static Bitmap view2Bitmap(View view) {
        measureView(view);
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        view.layout(0, 0, measuredWidth, measuredHeight);
        Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 截取Int 数组
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static int[] subInt(int[] src, int begin, int count) {
        int[] bs = new int[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    /**
     * 截取Byte 数组
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subByte(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    /**
     * Bitmap 转 Bytes
     *
     * @param bm
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines) {
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }


    /**
     * 更改应用语言
     * @param context
     */

    public static void changeAppLanguage(Context context, String language) {
        Locale locale=new Locale(language);
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, metrics);
    }
}
