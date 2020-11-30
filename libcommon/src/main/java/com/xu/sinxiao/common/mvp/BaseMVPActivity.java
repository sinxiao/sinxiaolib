package com.xu.sinxiao.common.mvp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.databinding.RootFramelayoutBinding;

public abstract class BaseMVPActivity extends AppCompatActivity implements IView {
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
        Toast toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void showDialog(String info) {
        new AlertDialog.Builder(this).setTitle("Info").setMessage(info).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    @Override
    public void showError(String error) {
        showToast(error);
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
    public void launchActivity(@NonNull Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void finishNow() {
        finish();
    }


    @Override
    abstract public void freshData(Object object);
}
