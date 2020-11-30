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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
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

    public static void start(Context context, String name, HashMap<String, Object> values) {
        Intent intent = new Intent(context, DefaultFragmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("page", name);
        if (values != null && !values.isEmpty()) {
            Iterator<String> iterator = values.keySet().iterator();
            do {
                String key = iterator.next();
                Object value = values.get(key);
                if (value instanceof String) {
                    intent.putExtra(key, (String) value);
                } else if (value instanceof Integer) {
                    intent.putExtra(key, Integer.parseInt((String) value));
                } else if (value instanceof Float) {
                    intent.putExtra(key, (Float) value);
                } else if (value instanceof Double) {
                    intent.putExtra(key, (Double) value);
                } else if (value instanceof Serializable) {
                    intent.putExtra(key, (Serializable) value);
                } else {
//                    intent.putExtra(key, value);
                }
            } while (iterator.hasNext());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String page = getIntent().getStringExtra("page");
        Bundle bundle = getIntent().getExtras();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = concurrentHashMap.get(page);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
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
