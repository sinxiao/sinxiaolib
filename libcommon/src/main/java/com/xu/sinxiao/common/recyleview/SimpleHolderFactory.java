package com.xu.sinxiao.common.recyleview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xu.sinxiao.common.recyleview.base.BaseHolder;
import com.xu.sinxiao.common.recyleview.base.BaseHolderFactory;

import java.lang.reflect.InvocationTargetException;

public class SimpleHolderFactory extends BaseHolderFactory {
    public SimpleHolderFactory() {
    }

    public BaseHolder buildHolder(ViewGroup parent, int layoutResId, String holderClassName) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        BaseHolder holder = null;

        try {
            Class clazz = Class.forName(holderClassName);
            holder = (BaseHolder) clazz.getConstructor(View.class).newInstance(view);
        } catch (ClassNotFoundException var7) {
            var7.printStackTrace();
        } catch (InstantiationException var8) {
            var8.printStackTrace();
        } catch (IllegalAccessException var9) {
            var9.printStackTrace();
        } catch (InvocationTargetException var10) {
            var10.printStackTrace();
        } catch (NoSuchMethodException var11) {
            var11.printStackTrace();
        }

        return holder;
    }
}
