package com.xu.sinxiao.common;

import android.util.Log;

public class Utils {
    public static void showLog(String info) {
        Log.d("Utils", " ===== " + info);
    }

    public static void showError(String error) {
        Log.e("Utils", " ===== " + error);
    }
}
