package com.xu.sinxiao.common.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.databinding.DefaultFramelayoutBinding;
import com.xu.sinxiao.common.mvp.BaseActivity;

import java.util.concurrent.ConcurrentHashMap;

public class DefaultFragmentActivity extends BaseActivity {

    private final static ConcurrentHashMap<String, Fragment> concurrentHashMap = new ConcurrentHashMap<>();
    private DefaultFramelayoutBinding defaultFramelayoutBinding;

    public static void register(String page, Fragment fragment) {
        concurrentHashMap.put(page, fragment);
    }

    public static void start(Context context, String name) {
        Intent intent = new Intent(context, DefaultFragmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("page", name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String page = getIntent().getStringExtra("page");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = concurrentHashMap.get(page);
        try {
            fragment = fragment.getClass().newInstance();
            defaultFramelayoutBinding = DataBindingUtil.setContentView(
                    this, R.layout.default_framelayout);
            fragmentTransaction.replace(defaultFramelayoutBinding.defaultLayout.getId(), fragment)
                    .commitAllowingStateLoss();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
