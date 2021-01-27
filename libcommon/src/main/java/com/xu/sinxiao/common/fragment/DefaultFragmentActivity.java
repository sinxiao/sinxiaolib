package com.xu.sinxiao.common.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xu.sinxiao.common.Configer;
import com.xu.sinxiao.common.GsonUtils;
import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.Utils;
import com.xu.sinxiao.common.callback.ResultStrListener;
import com.xu.sinxiao.common.databinding.DefaultFramelayoutBinding;
import com.xu.sinxiao.common.db.model.ParameterizedTypeImpl;
import com.xu.sinxiao.common.logger.Logger;
import com.xu.sinxiao.common.mvp.BaseActivity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultFragmentActivity extends BaseActivity {
    private static final String KEY = "sinxiao_map_fragemnt";

    private static ResultStrListener registerListener;

    public static void register(ResultStrListener resultStrListener) {
        if (resultStrListener != null) {
            DefaultFragmentActivity.registerListener = resultStrListener;
            DefaultFragmentActivity.registerListener.onRevData("regok");
        }
    }

    private static ConcurrentHashMap<String, Class> concurrentHashMap = new ConcurrentHashMap<>();
    private static HashMap<String, String> values = new HashMap<>();

    private DefaultFramelayoutBinding defaultFramelayoutBinding;

    public static void register(String page, Fragment fragment) {
        concurrentHashMap.put(page, fragment.getClass());
        values.put(page, fragment.getClass().getName());
        String map = GsonUtils.toJson(values);
        Utils.saveStringToSpf(Configer.getInstance().getContext(), KEY, map);
    }

    private void intiValues() {
        String json = Utils.getStringValue(KEY);
        ParameterizedTypeImpl parameterizedType = new ParameterizedTypeImpl(HashMap.class, new Type[]{String.class, String.class});
        HashMap<String, String> map = GsonUtils.parserBean(json, parameterizedType);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                try {
                    concurrentHashMap.put(entry.getKey(), Class.forName(entry.getValue()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void start(Context context, String name) {
        Intent intent = new Intent(context, DefaultFragmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("page", name);
        if (concurrentHashMap.get(name) == null) {
            if (registerListener != null) {
                registerListener.onRevData("");
            }
        }
        context.startActivity(intent);
    }

    private static Bundle pickValueToBundle(Bundle bundle, HashMap<String, Object> values) {

        if (values != null && !values.isEmpty()) {
            for (Map.Entry<String, Object> map : values.entrySet()) {
                Object value = map.getValue();
                String key = map.getKey();
                if (value instanceof String) {
                    bundle.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    bundle.putInt(key, Integer.parseInt((String) value));
                } else if (value instanceof Float) {
                    bundle.putFloat(key, (Float) value);
                } else if (value instanceof Double) {
                    bundle.putDouble(key, (Double) value);
                } else if (value instanceof Serializable) {
                    bundle.putSerializable(key, (Serializable) value);
                } else {
//                    intent.putExtra(key, value);
                }
            }
        }
        return bundle;
    }

    private static Intent pickValueToIntent(Intent intent, HashMap<String, Object> values) {
        if (values != null && !values.isEmpty()) {
            for (Map.Entry<String, Object> map : values.entrySet()) {
                Object value = map.getValue();
                String key = map.getKey();
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
            }
        }
        return intent;
    }

    public static void start(Context context, String name, HashMap<String, Object> values) {
        Intent intent = new Intent(context, DefaultFragmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("page", name);
        if (values != null && !values.isEmpty()) {
            intent = pickValueToIntent(intent, values);
        }
        context.startActivity(intent);
    }

    private BaseFragment nowFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String page = getIntent().getStringExtra("page");
        Bundle bundle = getIntent().getExtras();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Class fragment = concurrentHashMap.get(page);
        if (fragment == null) {
            intiValues();
            fragment = concurrentHashMap.get(page);
        }
        BaseFragment pageFragment = null;
        if (fragment == null) {
            Logger.e("fragment is >> null   page name >> " + page);
            try {
                try {
                    pageFragment = (BaseFragment) Class.forName(page).newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            Logger.e("page is >> " + page);
            BaseFragment baseFragment = null;
            if (pageFragment != null) {
                baseFragment = pageFragment;
            } else {
                baseFragment = (BaseFragment) fragment.newInstance();
            }

            if (baseFragment != null && bundle != null) {
                baseFragment.setArguments(bundle);
            }
            if (baseFragment instanceof BaseFragment) {
//                BaseFragment baseFragmentt = (BaseFragment) fragment;
                baseFragment.setFragmentRouterInterface(fragmentRouterInterface);
            }
            defaultFramelayoutBinding = DataBindingUtil.setContentView(
                    this, R.layout.default_framelayout);
            fragmentTransaction.replace(defaultFramelayoutBinding.defaultLayout.getId(), baseFragment)
                    .commit();
            nowFragment = baseFragment;
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BaseFragment.FragmentRouterInterface fragmentRouterInterface = new BaseFragment.FragmentRouterInterface() {
        @Override
        public void goFragment(String page, HashMap<String, Object> values) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Class fragment = concurrentHashMap.get(page);
            if (fragment == null) {
                finishNow();
            }
            BaseFragment baseFragment = null;
            try {
                baseFragment = (BaseFragment) fragment.newInstance();
                Bundle fragmentBundle = new Bundle();
                pickValueToBundle(fragmentBundle, values);
                if (baseFragment != null && fragmentBundle != null) {
                    baseFragment.setArguments(fragmentBundle);
                }
                if (baseFragment instanceof BaseFragment) {
//                    BaseFragment baseFragment = (BaseFragment) fragment;
                    baseFragment.setFragmentRouterInterface(fragmentRouterInterface);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            fragmentTransaction.hide(nowFragment).add(defaultFramelayoutBinding
                    .defaultLayout.getId(), baseFragment).addToBackStack(null).commit();
            nowFragment = baseFragment;
        }

        @Override
        public void goFragment(String page) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Class fragment = concurrentHashMap.get(page);
            if (fragment == null) {
                finishNow();
            }
            BaseFragment baseFragment = null;
            try {
                baseFragment = (BaseFragment) fragment.newInstance();
                if (baseFragment instanceof BaseFragment) {
                    baseFragment.setFragmentRouterInterface(fragmentRouterInterface);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            fragmentTransaction.hide(nowFragment).add(defaultFramelayoutBinding
                    .defaultLayout.getId(), baseFragment).addToBackStack(null).commit();
            nowFragment = baseFragment;
        }

        @Override
        public void goBack() {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
        }

        @Override
        public void finishAll() {
            finishNow();
        }
    };
}
