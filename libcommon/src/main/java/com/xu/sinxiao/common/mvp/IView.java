package com.xu.sinxiao.common.mvp;

import android.content.Intent;

import androidx.annotation.NonNull;

public interface IView {

    void showToast(String info);

    void showDialog(String info);

    void showError(String error);

    void showLoading();

    void dissLoading();

    /**
     * @param intent {@code intent} 不能为 {@code null}
     */
    void launchActivity(@NonNull Intent intent);

    void finishNow();

    void freshData(Object object);
}
