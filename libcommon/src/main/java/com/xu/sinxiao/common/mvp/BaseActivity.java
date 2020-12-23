package com.xu.sinxiao.common.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.xu.sinxiao.common.DialigUtils;
import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.StatusBarUtils;
import com.xu.sinxiao.common.ToastUtils;
import com.xu.sinxiao.common.databinding.LayoutErrorBinding;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity implements IView {

    public static void start(Context context) {
        Intent intent = new Intent(context, BaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void start(Context context, HashMap<String, Object> exts) {
        Intent intent = new Intent(context, BaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (exts != null) {

        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        StatusBarUtils.setStatusBarDarkTheme(this, true);
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
    public void showDataView() {
        DialigUtils.dissmissLoadingDailig();
    }

    @Override
    public void launch(@NonNull Intent intent) {

    }

    @Override
    public void finishNow() {
        finish();
    }

    private View errorView;

    @Override
    public void showErrorView(String info) {
        LayoutErrorBinding databind = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_error, null, false);
        databind.txtTip.setText(info);
        errorView = databind.getRoot();
        getWindow().addContentView(errorView,
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    public void dissmissErrorView() {
        if (errorView != null) {
            getWindowManager().removeViewImmediate(errorView);
            errorView = null;
        }
    }

    @Override
    public void freshData(Object object) {

    }
}
