package com.xu.sinxiao.common;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast 工具类
 */
public class ToastUtils {
    public ToastUtils() {
        new InternalError("Toast工具类不能够初始化对象");
    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void show(final Context context, final CharSequence message) {
        if (isShow && (!TextUtils.isEmpty(message))) {
            showToast(context, message, Toast.LENGTH_SHORT);
        }
    }

    public static void show(final Context context, final int message) {
        String msg = context.getString(message);
        if (isShow && (!TextUtils.isEmpty(msg))) {
            showToast(context, msg, Toast.LENGTH_SHORT);
        }
    }

    private static void showToast(final Context context, final String infor, final int duration) {
        UIExecutor.postRunable(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, infor, duration == Toast.LENGTH_LONG ?
                        Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

    }

    private static void showToast(final Context context, final CharSequence infor, final int duration) {
        UIExecutor.postRunable(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, infor, duration == Toast.LENGTH_LONG ?
                        Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow && (!TextUtils.isEmpty(message))) {
            showToast(context, message, Toast.LENGTH_LONG);
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(final Context context, final int message) {
        UIExecutor.postRunable(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    showToast(context, context.getString(message), Toast.LENGTH_SHORT);
                }
            }
        });
    }

}
