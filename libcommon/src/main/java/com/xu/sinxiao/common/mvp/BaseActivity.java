package com.xu.sinxiao.common.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xu.sinxiao.common.DialigUtils;
import com.xu.sinxiao.common.ToastUtils;

public class BaseActivity extends AppCompatActivity implements IView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void showToast(String info) {
        ToastUtils.show(this, info);
    }

    @Override
    public void showDialog(String info) {
        DialigUtils.showInforDialog(getApplicationContext(), info);
    }

    @Override
    public void showError(String error) {
        DialigUtils.showErrorDialog(getApplicationContext(), error);
    }

    @Override
    public void showLoading() {
        DialigUtils.showLoadingDialog(getApplicationContext());
    }

    @Override
    public void dissLoading() {
        DialigUtils.dissmissLoadingDailig();
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {

    }

    @Override
    public void finishNow() {
        finish();
    }

    @Override
    public void freshData(Object object) {

    }
}
