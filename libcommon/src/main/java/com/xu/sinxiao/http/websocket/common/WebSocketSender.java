package com.xu.sinxiao.http.websocket.common;

import com.xu.sinxiao.common.BuildConfig;
import com.xu.sinxiao.common.GsonUtils;
import com.xu.sinxiao.common.callback.ResultListener;
import com.xu.sinxiao.common.callback.ResultStrListener;
import com.xu.sinxiao.common.logger.Logger;
import com.xu.sinxiao.http.websocket.ParameterizedTypeImpl;
import com.xu.sinxiao.http.websocket.WebSocketAdapter;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class WebSocketSender {
    private WebSocketAdapter webSocketAdapter;
    private static WebSocketSender webSocketService;

    public static WebSocketSender getInstance() {
        if (webSocketService == null) {
            webSocketService = new WebSocketSender();
        }
        return webSocketService;
    }

    private WebSocketSender() {
        webSocketAdapter = WebSocketAdapter.getInstance();
    }

    private SecureRandom secureRandom;

    public int genRandomId() {
        if (secureRandom == null) {
            secureRandom = new SecureRandom();
        }
        return 1000 + secureRandom.nextInt(10000) + secureRandom.nextInt(10000) + secureRandom.nextInt(1000);
    }

    //acountId is >>>>   rMmBkZwYJqr3B3jESBfE5rPZ7n1KpokAwm

    public void init(List<String> urls) {
        webSocketAdapter.connet(urls);
    }


    public <T> void send(String info, final WebSocketResultListener<T> resultListener) {
        if (BuildConfig.DEBUG) {
            if (info != null) {
                Logger.e("send info >>> " + info);
            }
        }
        webSocketAdapter.send(info, new ResultStrListener() {
            @Override
            public void onRevData(String data) {
                if (resultListener != null) {
                    resultListener.onRevData(data);
                }
                if (BuildConfig.DEBUG) {
                    if (data != null) {
                        Logger.e("rev  data >>> " + data);
                    }
                }
                if (resultListener != null && resultListener.getClz() != null) {

                    Type type = new ParameterizedTypeImpl(WebSocketBaseResponse.class, new Class[]{resultListener.getClz()});
                    try {
                        if (data.contains("\"transactions\"") && data.contains("\"Balance\": \"")) {
                            data = data.replaceAll("\"Balance\": \"", "\"BalanceStr\": \"");
                        }
                        if (data.contains("\"transactions\"") && data.contains("\"Balance\":\"")) {
                            data = data.replaceAll("\"Balance\":\"", "\"BalanceStr\": \"");
                        }

                        if (data.contains("\"transactions\"") && data.contains("\"Amount\":\"")) {
                            data = data.replaceAll("\"Amount\":\"", "\"AmountStr\": \"");
                        }

                        if (data.contains("\"transactions\"") && data.contains("\"LimitAmount\":\"")) {
                            data = data.replaceAll("\"LimitAmount\":\"", "\"LimitAmountStr\": \"");
                        }
                        if (data.contains("\"transactions\"") && data.contains("\"TakerGets\":\"")) {
                            data = data.replaceAll("\"TakerGets\":\"", "\"TakerGetsStr\": \"");
                        }
                        if (data.contains("\"transactions\"") && data.contains("\"TakerPays\":\"")) {
                            data = data.replaceAll("\"TakerPays\":\"", "\"TakerPaysStr\": \"");
                        }

                        if (data.contains("\"taker_gets\":\"")) {
                            data = data.replaceAll("\"taker_gets\":\"", "\"taker_getsStr\": \"");
                        }
                        if (data.contains("\"taker_pays\":\"")) {
                            data = data.replaceAll("\"taker_pays\":\"", "\"taker_paysStr\": \"");
                        }
                        if (!resultListener.getClz().getName().equals(String.class.getName())) {
                            //Logger.e("json >> " + data);
                            WebSocketBaseResponse<T> response = GsonUtils.parserBean(data, type);
                            if (resultListener != null) {
                                resultListener.onRevData(response.getResult());
                            }
                        } else {
                            if (resultListener != null) {
                                resultListener.onRevData(data);
                            }
                        }

                    } catch (Exception e) {
                        if (BuildConfig.DEBUG) {
                            Logger.e("data >>> " + data);
                        }
                        if (resultListener != null) {
                            resultListener.onError(100, "convert the data error");
                        }
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(int code, String error) {
                if (resultListener != null) {
                    resultListener.onError(code, error);
                }
            }
        });
    }

}
