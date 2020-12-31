package com.sharpen.common.bean.base;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

public class UiCollect {
    public static final Map<TabLayout.Tab, String> tab = new HashMap<>();
    public static final Map<TextView, String> textView = new HashMap<>();

    /**
     * 将视图元素及翻译key放到map中
     * @param view
     * @param key
     */
    public static void pTab(TabLayout.Tab view, String key){
        tab.put(view, key);
    }
    public static void pTv(TextView view, String key){
        textView.put(view, key);
    }

    static {

    }


}
