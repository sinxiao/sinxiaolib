package com.xu.sinxiao.http.websocket;

//import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketHolder {
//    private String sendUUID = null;

    interface OnDataListener {
        void onDataRevListener(String url, String requestUUID, String data);
    }

    private OnDataListener emptyDataListener = new OnDataListener() {
        @Override
        public void onDataRevListener(String url, String requestUUID, String data) {
//            Logger.e(TAG, "=====  " + url + "  data >>> " + data);
        }
    };
    private String TAG = "WebSocketSender";
    private OkHttpClient okHttpClient;
    private String url;
    private WebSocket webSocket;
    private boolean close = false;
    private boolean connect = false;
    private OnDataListener onDataListener = emptyDataListener;

    public void setOnDataListener(OnDataListener onDataListener) {
        this.onDataListener = onDataListener;
    }


    public WebSocketHolder(OkHttpClient okHttpClient, String url) {
        this.okHttpClient = okHttpClient;
        this.url = url;
        TAG = "WebSocketSender:" + url;
        connect = false;
        connet();
    }

    public void connet() {
//        Logger.e("begin connet ...");
        if (!connect && okHttpClient != null) {
            Request request = new Request.Builder().url(url).build();
            webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    connect = true;
//                    Logger.e("WebSocketHolder onOpen");
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
//                    Logger.e("WebSocketHolder onMessage text >>>" + text);
                    connect = true;
                    String id = "";
                    try {
                        JSONObject jsonObject = new JSONObject(text);
                        id = jsonObject.optString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onDataListener.onDataRevListener(url, id, text);

                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {
//                    Logger.e("WebSocketHolder onMessage ByteString  >>> " + bytes.hex());
                    connect = true;
                    String id = "";
                    try {
                        JSONObject jsonObject = new JSONObject(new String(bytes.toByteArray()));
                        id = jsonObject.optString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onDataListener.onDataRevListener(url, id, bytes.hex());
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    //关闭中....
//                    Logger.e(TAG, "===== onClosing");
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
//                    Logger.e("WebSocketHolder onClosed ::: " + url);
                    connect = false;
                    if (!close) {
                        connet();
                    } else {
                        webSocket.cancel();
                    }
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                    Logger.e(" WebSocketHolder onFailure ::: " + url);
                    connect = false;
                    if (!close) {
//                        Logger.e(" begin reconnect WebSocketHolder url ::: " + url);
                        connet();
                    }
                }
            });
        }
    }

    public String send(String data) {
//        Logger.e(url + "  send data >>> " + data);
        String id = "";
        try {
            JSONObject jsonObject = new JSONObject(data);
            id = jsonObject.optString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean bool = webSocket.send(data);
        if (bool) {
        } else {
            return "";
        }
        return id;
    }

    public void close() {
        close = true;
        webSocket.close(1, "error");
    }
}
