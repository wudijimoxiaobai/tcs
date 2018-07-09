package com.csscaps.common.utils;

/**
 * Created by tl on 2017/10/9.
 * 短时间内按钮多次触发工具类
 */

public class FastDoubleClickUtil {

    private static long lastClickTime = 0;
    private static long DIFF = 1000;
    private static int lastViewId = -1;

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int viewId) {
        return isFastDoubleClick(viewId, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int viewId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastViewId == viewId && lastClickTime > 0 && timeD < diff) {
//            Logger.i("短时间内按钮多次触发");
            return true;
        }
        lastClickTime = time;
        lastViewId = viewId;
        return false;
    }
}
