package com.xu.sinxiao.http.websocket;

import com.xu.sinxiao.common.callback.ResultListener;
import com.xu.sinxiao.common.callback.ResultStrListener;

import java.util.List;

public interface IWebSocketHandle {
    void connet(List<String> url);

    void send(String data);

    void send(String data, ResultStrListener dataListener);

    void close();
}
