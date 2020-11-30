package com.xu.sinxiao.common.mvp;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class HotData<T> {
    private int version = 0;
    private ConcurrentHashMap<String, HotDataListener<T>> listeners;

    public interface HotDataListener<T> {
        void onDataChanged(T data, int version);

        void onError();
    }

    private T data;

    public HotData(T data) {
        Objects.requireNonNull(data, " data can not set null");
        this.data = data;
        version++;
        listeners = new ConcurrentHashMap<String, HotDataListener<T>>();
    }

    public void observerOnData(HotDataListener<T> callBack) {
        Objects.requireNonNull(callBack, " callBack can not set null");
        listeners.put(callBack.toString(), callBack);
        if (data != null) {
            callBack.onDataChanged(data, version);
        }
    }

    public void removeCallback(HotDataListener<T> callBack) {
        if (listeners.containsKey(callBack.toString())) {
            listeners.remove(callBack.toString());
        }
    }

    public void clearListener() {
        listeners.clear();
    }

    public void destory() {
        version = 0;
        data = null;
        clearListener();
    }

    public void dispatchData(T data) {
        if (data == null) {
            for (HotDataListener dataListener : listeners.values()) {
                try {
                    dataListener.onError();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.data = data;
            version++;
            for (HotDataListener dataListener : listeners.values()) {
                try {
                    dataListener.onDataChanged(data, version);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
