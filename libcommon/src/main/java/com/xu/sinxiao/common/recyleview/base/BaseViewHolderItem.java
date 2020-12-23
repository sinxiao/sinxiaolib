package com.xu.sinxiao.common.recyleview.base;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

public abstract class BaseViewHolderItem<T extends ViewDataBinding> extends BaseHolderData {
    public BaseViewHolderItem() {
    }

    public abstract void bindView(T var1, int var2);

    @NonNull
    public String getHolderClassName() {
        return BaseRecvHolder.class.getName();
    }
}
