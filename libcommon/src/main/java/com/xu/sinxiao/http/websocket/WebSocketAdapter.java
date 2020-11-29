package com.xu.sinxiao.http.websocket;

import android.text.TextUtils;

import com.xu.sinxiao.common.callback.ResultStrListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;

public class WebSocketAdapter implements IWebSocketHandle {

    private static WebSocketAdapter webSocketAdapter;
    private List<WebSocketHolder> webSocketHolders;
    private OkHttpClient okHttpClient;
    private ConcurrentHashMap<String, RequestTask> requestTaskConcurrentHashMap
            = new ConcurrentHashMap<>();

    private WebSocketAdapter() {
    }

    public static WebSocketAdapter getInstance() {
        if (webSocketAdapter == null) {
            webSocketAdapter = new WebSocketAdapter();
        }
        return webSocketAdapter;
    }

    private WebSocketHolder.OnDataListener onDataListener = new WebSocketHolder.OnDataListener() {
        @Override
        public void onDataRevListener(String url, String uuidReq, String data) {
            if (requestTaskConcurrentHashMap.containsKey(uuidReq)) {
                RequestTask requestTask = requestTaskConcurrentHashMap.get(uuidReq);
                try {
                    requestTask.setData(data);
                    requestTask.setResult();
                    requestTaskConcurrentHashMap.remove(uuidReq);
//                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void connet(List<String> urls) {
        //用OKHttpClient进行连接
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().build();
        }
        webSocketHolders = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            WebSocketHolder webSocketHolder = new WebSocketHolder(okHttpClient, urls.get(i));
            webSocketHolder.setOnDataListener(onDataListener);
            webSocketHolders.add(webSocketHolder);
        }
    }

    /**
     * 发送信息不需要服务器端返回信息
     *
     * @param data
     */
    @Override
    public void send(String data) {
        for (WebSocketHolder webSocketHolder : webSocketHolders) {
            if (!TextUtils.isEmpty(webSocketHolder.send(data))) {
//                break;
            }
        }
    }

//    @Override
//    public <T> void send(String data, WebSocketResultListener<T> dataListener) {
    public void send(String data, ResultStrListener dataListener) {
        for (WebSocketHolder webSocketHolder : webSocketHolders) {
            String uuid = webSocketHolder.send(data);
//            Logger.e("uuid is >>> " + uuid);
            if (!TextUtils.isEmpty(uuid)) {
                RequestTask requestTask = new RequestTask(uuid, dataListener);
                requestTaskConcurrentHashMap.put(uuid, requestTask);
                break;
            }
        }
    }

    @Override
    public void close() {
        for (WebSocketHolder webSocketHolder : webSocketHolders) {
            webSocketHolder.close();
        }
    }
}
