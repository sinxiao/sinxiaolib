package com.xu.sinxiao.common.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.xu.sinxiao.common.DialigUtils;
import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.ToastUtils;
import com.xu.sinxiao.common.databinding.RootFramelayoutBinding;

public abstract class BaseMVPActivity extends BaseActivity implements IView {
    protected IPresent present;

    public abstract IPresent createPresent();

    public abstract int getResView();

    public View getMainView() {
        return null;
    }

    /**
     * 一般是绑定，控件的事件
     */
    public abstract void initView();

    /**
     * 向控件上绑定数据
     */
    public abstract void initData();

    private RootFramelayoutBinding rootFragmentBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootFragmentBinding = DataBindingUtil.setContentView(this, R.layout.root_framelayout);
        View view = getMainView();
        if (view == null) {
            rootFragmentBinding.contentLayout.addView(LayoutInflater.from(this).inflate(getResView(), null));
            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
        } else {
            rootFragmentBinding.contentLayout.addView(view);
            rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
        }
        present = createPresent();
        initView();
        initData();
        if (present != null) {
            present.fetchData();
        }
    }

    @Override
    public void showToast(String info) {
        ToastUtils.show(this, info);
    }

    @Override
    public void showDialog(String info) {
        DialigUtils.showInforDialog(this, info);
    }

    @Override
    public void showError(String error) {
        DialigUtils.showErrorDialog(this, error);
    }

    @Override
    public void showLoading() {
        rootFragmentBinding.loadingLayout.setVisibility(View.VISIBLE);
        rootFragmentBinding.contentLayout.setVisibility(View.GONE);
    }

    @Override
    public void dissLoading() {
        rootFragmentBinding.contentLayout.setVisibility(View.VISIBLE);
        rootFragmentBinding.loadingLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        present.destory();
    }

    @Override
    public void launch(@NonNull Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showErrorView(String info) {
        rootFragmentBinding.txtTip.setText(info);
        rootFragmentBinding.layoutError.setVisibility(View.VISIBLE);
        rootFragmentBinding.contentLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void finishNow() {
        finish();
    }


    @Override
    abstract public void freshData(Object object);
}
