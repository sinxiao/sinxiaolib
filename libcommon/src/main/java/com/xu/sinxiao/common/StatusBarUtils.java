package com.xu.sinxiao.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.xu.sinxiao.common.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtils {
    public static final int TYPE_MIUI = 0;
    public static final int TYPE_FLYME = 1;
    public static final int TYPE_M = 3;


    public static void setStatusBarTransparent(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        activity.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.holo_red_light));//设置要显示的颜色（Color.TRANSPARENT为透明）
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));//设置要显示的颜色（Color.TRANSPARENT为透明）
        setStatusBarDarkTheme(activity, true);
    }

    public static void setStatusBarColor(Activity activity, int color) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        activity.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.holo_red_light));//设置要显示的颜色（Color.TRANSPARENT为透明）
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));//设置要显示的颜色（Color.TRANSPARENT为透明）
    }

    public static int getStatusBarHeight(Context ctx) {
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setRootViewFitsSystemWindows(Activity activity, boolean fitSystemWindows) {
        if (Build.VERSION.SDK_INT >= 19) {
            ViewGroup winContent = (ViewGroup) activity.findViewById(android.R.id.content);
            if (winContent.getChildCount() > 0) {
                ViewGroup rootView = (ViewGroup) winContent.getChildAt(0);
                if (rootView != null) {
                    rootView.setFitsSystemWindows(fitSystemWindows);
                }
            }
        }
    }

    public static boolean setStatusBarDarkTheme(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= 23) {
                setStatusBarFontIconDark(activity, 3, dark);
            } else if (OSUtils.isMiui()) {
                setStatusBarFontIconDark(activity, 0, dark);
            } else {
                if (!OSUtils.isFlyme()) {
                    return false;
                }

                setStatusBarFontIconDark(activity, 1, dark);
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean setStatusBarFontIconDark(Activity activity, int type, boolean dark) {
        switch (type) {
            case 0:
                return setMiuiUI(activity, dark);
            case 1:
                return setFlymeUI(activity, dark);
            case 2:
            case 3:
            default:
                return setCommonUI(activity, dark);
        }
    }

    public static boolean setCommonUI(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= 23) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (dark) {
                    vis |= 8192;
                } else {
                    vis &= -8193;
                }

                if (decorView.getSystemUiVisibility() != vis) {
                    decorView.setSystemUiVisibility(vis);
                }

                return true;
            }
        }

        return false;
    }

    public static boolean setFlymeUI(Activity activity, boolean dark) {
        try {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt((Object) null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }

            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
            return true;
        } catch (Exception var8) {
            Logger.e(var8.getMessage());
            return false;
        }
    }

    public static boolean setMiuiUI(Activity activity, boolean dark) {
        try {
            Window window = activity.getWindow();
            Class<?> clazz = activity.getWindow().getClass();
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getDeclaredMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
            extraFlagField.setAccessible(true);
            if (dark) {
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);
            }

            return true;
        } catch (Exception var8) {
            Logger.e(var8.getMessage());
            return false;
        }
    }
}
