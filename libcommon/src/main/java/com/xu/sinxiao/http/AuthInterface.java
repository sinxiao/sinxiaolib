package com.xu.sinxiao.http;

import okhttp3.Request;

public interface AuthInterface {
    public boolean isNeedAuth(Request request);

    public boolean addAuth(Request request);
}
