package com.xu.sinxiao.common.mvp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

public interface IView {

    void showToast(String info);

    void showDialog(String info);

    void showError(String error);

    void showLoading();

    void dissLoading();

    /**
     * 显示数据层
     */
    void showDataView();

    /**
     * @param intent {@code intent} 不能为 {@code null}
     */
    void launch(@NonNull Intent intent);

    void finishNow();

    void showErrorView(String info);

    void freshData(Object object);
}
