package com.xu.sinxiao.http;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.internal.Internal;

import static java.util.logging.Level.WARNING;

public class PersistenceCookieJar implements CookieJar {

    private List<Cookie> cache = new ArrayList<>();

    //Http请求结束，Response中有Cookie时候回调
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        //内存中缓存Cookie
        cache.addAll(cookies);
    }

    //Http发送请求前回调，Request中设置Cookie
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        //过期的Cookie
        List<Cookie> invalidCookies = new ArrayList<>();
        //有效的Cookie
        List<Cookie> validCookies = new ArrayList<>();

        for (Cookie cookie : cache) {

            if (cookie.expiresAt() < System.currentTimeMillis()) {
                //判断是否过期
                invalidCookies.add(cookie);
            } else if (cookie.matches(url)) {
                //匹配Cookie对应url
                validCookies.add(cookie);
            }
        }

        //缓存中移除过期的Cookie
        cache.removeAll(invalidCookies);

        //返回List<Cookie>让Request进行设置
        return validCookies;
    }
//    private final CookieHandler cookieHandler;
//
//    public PersistenceCookieJar(CookieHandler cookieHandler) {
//        this.cookieHandler = cookieHandler;
//    }
//
//
//    @Override
//    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//        if (cookieHandler != null) {
//            List<String> cookieStrings = new ArrayList<>();
//            for (Cookie cookie : cookies) {//遍历cookie集合
//                cookieStrings.add(cookie.toString());
//            }
//            Map<String, List<String>> multimap = Collections.singletonMap("Set-Cookie", cookieStrings);
//            try {
//                //具体的保存工作交给cookieHandler去处理
//                cookieHandler.put(url.uri(), multimap);
//            } catch (IOException e) {
////                Internal.logger.log(WARNING, "Saving cookies failed for " + url.resolve("/..."), e);
//            }
//        }
//    }
//
//    @Override
//    public List<Cookie> loadForRequest(HttpUrl url) {
//        // The RI passes all headers. We don't have 'em, so we don't pass 'em!
//        Map<String, List<String>> headers = Collections.emptyMap();
//        Map<String, List<String>> cookieHeaders;
//        try {
//            //从cookieHandler中取出cookie集合。
//            cookieHeaders = cookieHandler.get(url.uri(), headers);
//        } catch (IOException e) {
////            Internal.logger.log(WARNING, "Loading cookies failed for " + url.resolve("/..."), e);
//            return Collections.emptyList();
//        }
//
//        List<Cookie> cookies = null;
//        for (Map.Entry<String, List<String>> entry : cookieHeaders.entrySet()) {
//            String key = entry.getKey();
//            if (("Cookie".equalsIgnoreCase(key) || "Cookie2".equalsIgnoreCase(key))
//                    && !entry.getValue().isEmpty()) {
//                for (String header : entry.getValue()) {
//                    if (cookies == null) cookies = new ArrayList<>();
//                    cookies.addAll(decodeHeaderAsJavaNetCookies(url, header));
//                }
//            }
//        }
//
//        return cookies != null
//                ? Collections.unmodifiableList(cookies)
//                : Collections.<Cookie>emptyList();
//    }
//
//
//    //将请求Header转为OkHttp中HttpCookie
//    private List<Cookie> decodeHeaderAsJavaNetCookies(HttpUrl url, String header) {
//        List<Cookie> result = new ArrayList<>();
////        for (int pos = 0, limit = header.length(), pairEnd; pos < limit; pos = pairEnd + 1) {
////            //具体转换过程在此不做展示
////        }
//        return result;
//    }
}

//};

