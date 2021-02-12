package com.xu.sinxiao.http;

import java.util.Vector;

import okhttp3.Interceptor;

public class HttpInterceptorHelper {
    private static final HttpInterceptorHelper httpInterceptorHelper = new HttpInterceptorHelper();
    private Vector<Interceptor> intercepts = null;

    private HttpInterceptorHelper() {
        intercepts = new Vector<Interceptor>();
    }

    public static HttpInterceptorHelper getInstance() {
        return httpInterceptorHelper;
    }

    public void addHttpInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            intercepts.add(interceptor);
        }
    }

    public Vector<Interceptor> getIntercepts() {
        return intercepts;
    }

    public void clear() {
        if (intercepts != null) {
            intercepts.clear();
        }
    }
}
