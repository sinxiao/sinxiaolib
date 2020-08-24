package com.xu.sinxiao.common;

import android.os.Handler;
import android.os.HandlerThread;

public class BackgroundExecutor {
    private static HandlerThread handlerThread = new HandlerThread("BackgroundExecutor");
    private static Handler handler = null;

    public static void post(Runnable task) {
        if (handler == null) {
            handlerThread.setPriority(Thread.MAX_PRIORITY);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }
        handler.post(task);
    }

    public static void postAfter(Runnable task, long after) {
        if (handler == null) {
            handlerThread.setPriority(Thread.MAX_PRIORITY);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }

        handler.postDelayed(task, after);
    }
}
