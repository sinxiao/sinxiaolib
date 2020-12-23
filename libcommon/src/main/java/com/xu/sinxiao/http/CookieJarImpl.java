package com.xu.sinxiao.http;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieJarImpl implements CookieJar {
    private final PersistentCookieStore cookieStore = PersistentCookieStore.getInstance();

    public CookieJarImpl() {
//        this.cookieManager = cookieManager;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        //本地可校验cookie，并根据需要存储
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        //从本地拿取需要的cookie
        return cookieStore.get(url);
    }
}
