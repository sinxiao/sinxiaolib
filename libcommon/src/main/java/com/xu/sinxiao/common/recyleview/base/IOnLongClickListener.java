package com.xu.sinxiao.common.recyleview.base;

import android.view.View;

public interface IOnLongClickListener<T extends BaseHolderData> {
    boolean onLongClick(View var1, T var2, int var3);
}
