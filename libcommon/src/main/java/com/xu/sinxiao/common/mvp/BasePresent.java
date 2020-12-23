package com.xu.sinxiao.common.mvp;

import android.content.Context;

import androidx.databinding.ViewDataBinding;

public abstract class BasePresent<T extends ViewDataBinding> implements IPresent {
    protected Context context;
    protected BaseDataBindView<T> view;

    public BasePresent(Context context, BaseDataBindView<T> view) {
        this.context = context;
        this.view = view;
    }

    public void destory() {
        context = null;
        view = null;
    }
}
