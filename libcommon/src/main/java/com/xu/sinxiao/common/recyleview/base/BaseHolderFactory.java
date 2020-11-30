package com.xu.sinxiao.common.recyleview.base;

import android.view.ViewGroup;

public abstract class BaseHolderFactory {
    public BaseHolderFactory() {
    }

    public abstract BaseHolder buildHolder(ViewGroup var1, int var2, String var3);
}
