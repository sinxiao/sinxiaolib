package com.xu.sinxiao.http;

import android.text.TextUtils;
import android.util.Log;

import com.xu.sinxiao.common.BackgroundExecutor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dns;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***
 * https://blog.csdn.net/dodod2012/article/details/88718160
 * https://blog.csdn.net/rootmego/article/details/84327467
 * https://blog.csdn.net/sbsujjbcy/article/details/51612832
 * 优点：防止域名劫持，防止智能DNS策略失效，提高响应速度
 * 缺点：
 * 过于底层，容灾不好做，除非强制关闭Httpdns。
 * 服务器返回的ip如果不正确，这次请求就挂了，甚至下次也可能挂了。
 * OkHttp默认对解析结果有一定时间的缓存，万一ttl过期了，okhttp可能依然会去使用，这时候也是有风险的。
 *
 */
public class HttpDns implements Dns {

    // 需要进行httpdns处理转换的host列表，这里我们只对部分跟我们公司服务相关的域名
    // 利用HttpDns进行解析，其他的还是用默认的域名解析方式
    private CopyOnWriteArrayList<String> mHostList;
    private Map<String, DnsInfo> mDnsMap = new ConcurrentHashMap<>();
    private OkHttpClient mOkHttpClient;

    public HttpDns() {
        mHostList = new CopyOnWriteArrayList();
    }

    public HttpDns(List<String> hostList) {
        mHostList = new CopyOnWriteArrayList<>();
        mHostList.addAll(hostList);

        BackgroundExecutor.post(() -> {
            for (String host : mHostList) {
                DnsInfo dnsInfo = mDnsMap.get(host);
                if (dnsInfo == null || dnsInfo.isExpired() || !dnsInfo.isIpValid()) {
                    updateCacheForHost(host);
                }
            }
        });
    }

    /**
     * 预加载的 URL 缓存
     *
     * @param hostList
     */
    public void preUpdateHostList(List<String> hostList) {
        mHostList = new CopyOnWriteArrayList<>();
        mHostList.addAll(hostList);

        BackgroundExecutor.post(() -> {
            for (String host : mHostList) {
                DnsInfo dnsInfo = mDnsMap.get(host);
                if (dnsInfo == null || dnsInfo.isExpired() || !dnsInfo.isIpValid()) {
                    updateCacheForHost(host);
                }
            }
        });
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (hostname == null) {
            throw new UnknownHostException("host == null");
        } else {
            if (mHostList != null && mHostList.contains(hostname)) {
                try {
                    String ipAddr = getIpStrForHost(hostname);
                    if (!TextUtils.isEmpty(ipAddr)) {
                        List<InetAddress> addresses = new ArrayList<>();
                        addresses.add(InetAddress.getByName(ipAddr));
                        Log.d("httpdns", "HttpDns hint, host:" + hostname + "  ip:" + ipAddr);
                        return addresses;
                    }
                } catch (Exception ignored) {
                }
            } else {
//                String ipAddr = getIpStrForHost(hostname);
                DnsInfo dnsInfo = mDnsMap.get(hostname);
                if (dnsInfo != null && dnsInfo.isIpValid() && !dnsInfo.isExpired()) {
                    //如果可以获得合法的dns info,返回ip str
                    List<InetAddress> addresses = new ArrayList<>();
                    addresses.add(InetAddress.getByName(dnsInfo.getIpStr()));
                    return addresses;
                } else {
                    BackgroundExecutor.post(() -> {
                        updateCacheForHost(hostname);
                    });
                }
            }
            //未能从httpdns获取ip地址,使用系统dns获取
            return SYSTEM.lookup(hostname);
        }
    }


    static class DnsInfo implements Serializable {
        String ip;
        String ip2;
        String host;
        long initTime;

        boolean isIpValid() {
            if (TextUtils.isEmpty(ip)) {
                return false;
            }
            return true;
        }

        /**
         * 每24小时更新一下
         *
         * @return
         */
        boolean isExpired() {
            if (System.currentTimeMillis() - initTime > diff) {
                return false;
            }
            return true;
        }

        String getIpStr() {
            if (!TextUtils.isEmpty(ip)) {
                return ip;
            } else if (!TextUtils.isEmpty(ip2)) {
                return ip2;
            }
            return "";
        }

    }

    private static final long diff = 24 * 60 * 60 * 1000;

    public String getIpStrForHost(String host) {
        if (!TextUtils.isEmpty(host)) {
            DnsInfo dnsInfo = mDnsMap.get(host);
            if (dnsInfo != null && dnsInfo.isIpValid() && !dnsInfo.isExpired()) {
                //如果可以获得合法的dns info,返回ip str
                return dnsInfo.getIpStr();
            } else {
                //无法获得合法的dns info, 异步更新host对应的cache
                updateCacheForHost(host);
            }
        }
        return null;
    }

    public void updateCacheForHost(final String host) {
        //https://www.dnspod.cn/httpdns/demo
        String url = String.format("http://119.29.29.29/d?dn=%s&ttl=1", host);
        Request request = new Request.Builder().url(url).build();
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder().build();
        }
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = response.body().string();
                Log.d("httpdns", "get ip for host(" + host + ") success :" + data);
                DnsInfo dnsInfo = parseResponseString(host, data);
                if (dnsInfo != null) {
                    mDnsMap.put(host, dnsInfo);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    //返回的数据格式为：   36.152.44.96;36.152.44.95,256
    private DnsInfo parseResponseString(String host, String data) {
        DnsInfo dnsInfo = new DnsInfo();
        if (data.contains(";")) {
            String[] datas = data.split(";");
            if (datas != null && datas.length >= 2) {
                dnsInfo.ip = datas[0];
                if (datas[1].contains(",")) {
                    String[] ips = datas[1].split(",");
                    dnsInfo.ip2 = ips[0];
                } else {
                    dnsInfo.ip2 = datas[1];
                }
            } else if (datas != null && datas.length == 1) {
                dnsInfo.ip = datas[0];
            }
        } else if (data.contains(",")) {
            String[] datas = data.split(",");
            dnsInfo.ip = datas[0];
        }
        dnsInfo.host = host;
        dnsInfo.initTime = System.currentTimeMillis();
        return dnsInfo;
    }

}
