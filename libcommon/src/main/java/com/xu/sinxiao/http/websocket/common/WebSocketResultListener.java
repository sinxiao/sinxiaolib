package com.xu.sinxiao.http.websocket.common;

import com.xu.sinxiao.common.callback.ResultStrListener;

public interface WebSocketResultListener<T> extends com.xu.sinxiao.common.callback.ResultListener<T>, ResultStrListener {
    Class<T> getClz();

    void onRevData(T t);

    void onError(int code, String error);

    void onRevData(String data);
}
