package com.xu.sinxiao.common.recyleview.base;

import android.view.View;

public interface IOnClickListener<T extends BaseHolderData> {
    void onClick(View var1, T var2, int var3);
}
