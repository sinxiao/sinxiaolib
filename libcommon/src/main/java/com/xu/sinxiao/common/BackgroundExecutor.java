package com.xu.sinxiao.common;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class BackgroundExecutor {
    private static ExecutorService executors = Executors.newFixedThreadPool(16, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }
    });
    private static HandlerThread handlerThread = new HandlerThread("BackgroundExecutor");
    private static Handler handler = null;

    public static void post(Runnable task) {
        if (handler == null) {
            handlerThread.setPriority(Thread.MIN_PRIORITY);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }
//        handler.post(task);
        executors.submit(task);
    }

    public static void postAfter(final Runnable task, long after) {
        if (handler == null) {
            handlerThread.setPriority(Thread.MIN_PRIORITY);
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                executors.submit(task);
            }
        }, after);
    }
}
