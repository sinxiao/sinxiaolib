package com.xu.sinxiao.http;

import android.util.Log;

import com.xu.sinxiao.common.BuildConfig;
import com.xu.sinxiao.common.callback.ResultStrListener;
import com.xu.sinxiao.common.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;

public class HttpService {

    private OkHttpClient client;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final int CONNECTIOPN_TIME_OUT_DFARUT = 5000;

    private HttpService() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // 包含header、body数据

        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        client = new OkHttpClient.Builder()
                //http数据log，日志中打印出HTTP请求&响应数据
                .addInterceptor(loggingInterceptor)
                .callTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90,
                        TimeUnit.SECONDS).writeTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(130, TimeUnit.SECONDS)
                .sslSocketFactory(getDefaultSSLSocketFactory(), getDefaultX509TrustManager())
                .hostnameVerifier(getDefaultHostnameVerifier())
                .cookieJar(new CookieJarImpl()).build();
//        client.socketFactory();
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    private static HttpService INSTANCE = new HttpService();

    public static final HttpService getInstance() {
        return INSTANCE;
//        new OkHttpClient.Builder().setConnectTimeout$okhttp(CONNECTIOPN_TIME_OUT_DFARUT);
//        return INSTANCE;
    }

    private static final String TAG = "HttpService";

    public void asyncGetRequest(String url, final ResultStrListener resultStrListener) {
        if (client == null) {
            client = new OkHttpClient();
        }
        Logger.e("request ::: " + url);
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (BuildConfig.DEBUG) {
                    Logger.d(TAG + "onFailure: " + e.getMessage());
                }

                if (resultStrListener != null) {
                    resultStrListener.onError(1000, e.getMessage());
                }
//                try{
//                    e.printStackTrace();
//                }catch (Exception e){
//
//                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.d(TAG, "onResponse: " + response.body().string());
                String body = response.body().string();
                if (BuildConfig.DEBUG) {
//                    Logger.d("code ::: " + response.code());
//                    Logger.d(TAG + " onResponse::  " + body);
                }
                if (resultStrListener != null) {
                    resultStrListener.onRevData(body);
                }
            }
        });
    }

    public void asyncPostRequest(String url, String json, final ResultStrListener resultStrListener) {
        if (client == null) {
            client = new OkHttpClient();
        }
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        final Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (resultStrListener != null) {
                    resultStrListener.onError(1000, e.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                Log.d(TAG, "onResponse: " + body);
                if (resultStrListener != null) {
                    resultStrListener.onRevData(body);
                }
            }
        });
    }

    public void asyncDeleteRequest(String url, final ResultStrListener resultStrListener) {
        final Request request = new Request.Builder().url(url).delete().build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (resultStrListener != null) {
                    resultStrListener.onError(1000, e.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                Log.d(TAG, "onResponse: " + body);
                if (resultStrListener != null) {
                    resultStrListener.onRevData(body);
                }
            }
        });
    }

    public void asyncDeleteRequest(String url, String json, final ResultStrListener resultStrListener) {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        final Request request = new Request.Builder().url(url).delete(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (resultStrListener != null) {
                    resultStrListener.onError(1000, e.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                Log.d(TAG, "onResponse: " + body);
                if (resultStrListener != null) {
                    resultStrListener.onRevData(body);
                }
            }
        });
    }

    public void asyncPutRequest(String url, String json, final ResultStrListener resultStrListener) {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        final Request request = new Request.Builder().url(url).put(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (resultStrListener != null) {
                    resultStrListener.onError(1000, e.getMessage());
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                Log.d(TAG, "onResponse: " + body);
                if (resultStrListener != null) {
                    resultStrListener.onRevData(body);
                }
            }
        });
    }

    /**
     * 这样你是完全放弃了hostname的校验，这也是相当不安全的
     *
     * @return
     */
    public synchronized HostnameVerifier getDefaultHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    /**
     * 这么做，这样将你是没有做任何校验的，这里推荐使用系统默认的，会在校验过程中发现有异常直接抛出
     *
     * @return
     */
    public synchronized SSLSocketFactory getDefaultSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new AssertionError();
        }
    }

    public X509TrustManager getDefaultX509TrustManager() {
        return new TrustAllCerts();
    }

    private static class TrustAllCerts implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 安全套接层工厂，用于创建SSLSocket。默认的SSLSocket是信任手机内置信任的证书列表，
     * 我们可以通过OKHttpClient.Builder的sslSocketFactory方法定义我们自己的信任策略，
     * 比如实现上面提到的我们只信任服务端证书的根证书
     * 可以载入服务器的公钥证书  一般以.cart结尾 （非服务器私钥）
     * 载入证书
     */
    public synchronized SSLSocketFactory getSSLSocketFactory(InputStream... certificates) {
        try {
            //用我们的证书创建一个keystore
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = "server" + Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //创建一个trustmanager，只信任我们创建的keystore
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(
                    null,
                    trustManagerFactory.getTrustManagers(),
                    new SecureRandom()
            );
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     */
    public void init() {

    }


}
