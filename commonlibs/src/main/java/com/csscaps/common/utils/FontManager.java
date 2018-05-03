package com.csscaps.common.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.csscaps.common.base.BaseApplication;
import com.tax.fcr.library.utils.Logger;


/**
 * Created by tanglei on 16/7/8.
 * 字体工具类
 */
public class FontManager {

    public static Typeface sTypeface = Typeface.createFromAsset(BaseApplication.getAppContext().getAssets(), "lantinghei.ttf");

    public static void applyFont(final Context context, final View root, final String fontName) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof AbsListView) continue;
                    applyFont(context, viewGroup.getChildAt(i), fontName);
                }
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
        } catch (Exception e) {
            Logger.e(String.format("Error occured when trying to apply %s font for %s view", fontName, root));
            e.printStackTrace();
        }
    }

    public static void applyFont(final View root) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (viewGroup.getChildAt(i) instanceof AbsListView) continue;
                    applyFont(viewGroup.getChildAt(i));
                }
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(sTypeface);
        } catch (Exception e) {
            Logger.e(String.format("Error occured when trying to apply %s font for %s view", "lantianghei", root));
            e.printStackTrace();
        }
    }
}
