package com.xu.sinxiao.common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class UIExecutor {

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void postRunable(Runnable task) {
        handler.post(task);
    }

    public static void postRunable(Runnable task, long afterMillis) {
        handler.postDelayed(task, afterMillis);
    }

}
