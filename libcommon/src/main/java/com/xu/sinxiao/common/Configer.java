package com.xu.sinxiao.common;

import android.content.Context;

import com.xu.sinxiao.common.db.DataBaseService;

import java.util.Objects;

public class Configer {
    private String packageName = null;
    private Context context = null;
    private static Configer configer;

    public static Configer getInstance() {
        if (configer == null) {
            configer = new Configer();
        }
        return configer;
    }

    public Context getContext() {
        Objects.requireNonNull(context, " plz init(Context) first .");
        return context;
    }

    public String getPackageName() {
        Objects.requireNonNull(packageName, " plz init(Context) first .");
        return packageName;
    }

    private Configer() {
    }

    public void init(Context context) {
        this.context = context;
        packageName = context.getPackageName();
        RSAEncryptUtil.getInstance().init(context, null);
        DataBaseService.getInstance().init(context);
    }
}
