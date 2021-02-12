package com.sharpen.common.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.xu.sinxiao.common.Configer;
import com.xu.sinxiao.common.Utils;

/**
 * 支持通过在主进程里，和不在主进程里进行初始化
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String currentProcessName = getCurrentProcessName(this);
        if (TextUtils.equals(currentProcessName, getPackageName())) {
//            LogUtils.e("ltx", "initMainProcess start:" + System.currentTimeMillis());
            initMainProcess();
//            LogUtils.e("ltx", "initMainProcess end:" + System.currentTimeMillis());
        } else {
            initNotMainProcess();
        }
    }

    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (mActivityManager != null && !Utils.isEmptyList(mActivityManager.getRunningAppProcesses()))
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        return null;
    }

    public void initMainProcess() {
        Configer.getInstance().init(this);
    }

    public void initNotMainProcess() {

    }

}
