package com.xu.sinxiao.http;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class HttpService {

    private OkHttpClient client;
    private static final int CONNECTIOPN_TIME_OUT_DFARUT = 5000;
    private LogInterceptor logInterceptor = null;

    private HttpService() {
        client = new OkHttpClient();
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public void setShowLog(boolean bl) {
        if (bl) {
            if (logInterceptor == null) {
                logInterceptor = new LogInterceptor();
            }
            addIntercepter(logInterceptor);
        } else {
            if (logInterceptor != null) {
                if (client != null) {
                    client.interceptors().remove(logInterceptor);
                }
                logInterceptor = null;
            }
        }
    }

    public void addIntercepter(Interceptor interceptor) {

//        interceptor = new Interceptor() {
//            @NotNull
//            @Override
//            public Response intercept(@NotNull Chain chain) throws IOException {
//                return null;
//            }
//        };
        if (client != null) {
            client.interceptors().add(interceptor);
        }
    }

    private static HttpService INSTANCE = new HttpService();

    public static final HttpService getInstance() {
        return INSTANCE;
//        new OkHttpClient.Builder().setConnectTimeout$okhttp(CONNECTIOPN_TIME_OUT_DFARUT);
//        return INSTANCE;
    }



    /**
     *
     */
    public void init() {

    }


}
