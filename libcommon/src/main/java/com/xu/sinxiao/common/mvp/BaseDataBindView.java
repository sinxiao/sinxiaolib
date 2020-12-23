package com.xu.sinxiao.common.mvp;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.xu.sinxiao.common.fragment.BaseFragment;

public class BaseDataBindView<T extends ViewDataBinding> implements IView {
    public static <T extends ViewDataBinding> BaseDataBindView<T> instance(IView view, T t) {
        return new BaseDataBindView(view, t);
    }

    public static <T extends ViewDataBinding> BaseDataBindView<T> instanceFromObject(Object object, T t) {
        IView view = null;
        if (object instanceof IView) {
            view = (IView) object;
        }
        BaseFragment baseFragment = null;
        if (object instanceof BaseFragment) {
            baseFragment = (BaseFragment) object;
        }
        if (view != null && baseFragment != null) {
            return new BaseDataBindView(view, t, baseFragment);
        }
        return new BaseDataBindView(view, t);
    }

    public static <T extends ViewDataBinding> BaseDataBindView<T> instance(IView view, T t, BaseFragment baseFragment) {
        return new BaseDataBindView(view, t, baseFragment);
    }

    public BaseFragment getBaseFragment() {
        return baseFragment;
    }

    public void setBaseFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    private BaseFragment baseFragment;
    private IView view;
    private T dataBinding;

    public BaseDataBindView(IView iView, T dataBinding) {
        this.view = iView;
        this.dataBinding = dataBinding;

    }

    public BaseDataBindView(IView iView, T dataBinding, BaseFragment baseFragment) {
        this.view = iView;
        this.dataBinding = dataBinding;
        this.baseFragment = baseFragment;
    }

    public void showToast(String info) {
        if (view != null) {
            view.showToast(info);
        }
    }

    public void showDialog(String info) {
        if (view != null) {
            view.showDialog(info);
        }
    }

    public void showError(String error) {
        if (view != null) {
            view.showError(error);
        }
    }

    public void showLoading() {
        if (view != null) {
            view.showLoading();
        }
    }

    public void dissLoading() {
        if (view != null) {
            view.dissLoading();
        }
    }

    /**
     * 显示数据层
     */
    public void showDataView() {
        if (view != null) {
            view.showDataView();
        }
    }

    /**
     * @param intent {@code intent} 不能为 {@code null}
     */
    public void launch(@NonNull Intent intent) {
        if (view != null) {
            view.launch(intent);
        }
    }

    public void finishNow() {
        if (view != null) {
            view.finishNow();
        }
    }

    public void showErrorView(String info) {
        if (view != null) {
            view.showErrorView(info);
        }
    }

    public void freshData(Object object) {
        if (view != null) {
            view.freshData(object);
        }
    }

    public T getDataBind() {
        return dataBinding;
    }
}
