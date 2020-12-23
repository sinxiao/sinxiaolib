package com.xu.sinxiao.common.javascript;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.xu.sinxiao.common.logger.Logger;

import java.util.Objects;

public class JavascriptInterfaceUtils {
    public static interface JavaScriptCallBack {
        boolean isLogin();

        String getUserJson();

        void gotoPage(String page);

        void onRevValue(String action, String value);
    }

    private JavaScriptCallBack javaScriptCallBack;

    public void setJavaScriptCallBack(JavaScriptCallBack javaScriptCallBack) {
        this.javaScriptCallBack = javaScriptCallBack;
    }

    private WebView webView;

    private Context context;

    private JavascriptInterfaceUtils() {
    }

    public void init(Context context) {
        this.context = context;
    }

    private volatile static JavascriptInterfaceUtils instance = null;

    public synchronized static JavascriptInterfaceUtils getInstance() {
        if (instance == null) {
            instance = new JavascriptInterfaceUtils();
        }
        return instance;
    }

    public void isLogin() {
        if (javaScriptCallBack != null) {
            if (javaScriptCallBack.isLogin()) {
                sendValueToJavaScript("isLogin", "1");
            } else {
                sendValueToJavaScript("isLogin", "0");
            }
        }

    }

    public void getUserJosn() {
        if (javaScriptCallBack != null) {
            sendValueToJavaScript("getUserJosn", javaScriptCallBack.getUserJson());
        }
    }

    /**
     * 有几种跳转页面：
     * 资产页面：wallets, 登录页面： login ,我的页面：mine ,注册页面：register,
     * 打开二维码识别界面： scanQrd
     *
     * @param page
     */
    public void gotoPage(String page) {
        Objects.requireNonNull(context, "plz init(context) first");
        Logger.e("gotoPage >>> " + page);
        if (!TextUtils.isEmpty(page)) {
            if (javaScriptCallBack != null) {
                javaScriptCallBack.gotoPage(page);
            }
//            if (page.equals("wallets")) {
//                DefaultFragmentActivity.start(context, DefaultFragmentActivity.PAGE_WALLET);
//            } else if (page.equals("mine")) {
//                DefaultFragmentActivity.start(context, DefaultFragmentActivity.PAGE_MINE);
//            } else if (page.equals("login")) {
//                LoginActivity.start(context);
//            } else if (page.equals("register")) {
//                RegisterActivity.start(context);
//            } else if (page.equals("scanQrd")) {
//                HashMap<String, String> exts = new HashMap<>();
//                exts.put(ScanQrdActivity.KEY_Hide_Select_Image, "1");
//                ScanQrdActivity.startScanQrdActivity(context, new ScanQrdActivity.ScanListener() {
//                    @Override
//                    public void onScanSucess(QrdScanResult scanResult) {
//                        onRevValue("scanQrd", scanResult.getValue());
//                    }
//
//                    @Override
//                    public void onScanFail(String msg) {
//                        JSONObject jsonObject = new JSONObject();
//                        try {
//                            jsonObject.put("status", "onScanFail");
//                            jsonObject.put("msg", msg);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        onRevValue("scanQrd", jsonObject.toString());
//                    }
//
//                    @Override
//                    public void onScanCancled() {
//                        JSONObject jsonObject = new JSONObject();
//                        try {
//                            jsonObject.put("status", "onScanCancled");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        onRevValue("scanQrd", jsonObject.toString());
//                    }
//                }, exts);
//            } else {
//
//            }
        }
    }

    /* //声明mlocationClient对象
     public AMapLocationClient mlocationClient = null;
     //声明mLocationOption对象
     public AMapLocationClientOption mLocationOption = null;

     public void requestLocation() {
         if (mlocationClient == null) {
             mlocationClient = new AMapLocationClient(context);
             //初始化定位参数
             mLocationOption = new AMapLocationClientOption();
         }

         //设置定位监听
         mlocationClient.setLocationListener(new AMapLocationListener() {
             @Override
             public void onLocationChanged(AMapLocation aMapLocation) {

             }
         });
         //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
         mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
         //设置定位间隔,单位毫秒,默认为2000ms
         mLocationOption.setInterval(2000);
         //设置定位参数
         mlocationClient.setLocationOption(mLocationOption);
         // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
         // 注意设 置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
         // 在定位结束后，在合适的生命周期调用onDestroy()方法
         // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
         //启动定位
         mlocationClient.startLocation();
     }
 */
    @JavascriptInterface
    public void callFromJS(String action, String value) {
        Logger.e("callFromJS >>>" + action + " value >>> " + value);
        if (action.equals("login")) {
            gotoPage(action);
            return;
        } else if (action.equals("wallets")) {
            gotoPage(action);
            return;
        }
        if (action.equals("gotoPage")) {
            gotoPage(value);
        } else if (action.equals("requestLocation")) {

        } else if (action.equals("isLogin")) {
            isLogin();
        } else if (action.equals("getUserJosn")) {
            getUserJosn();
        } else if (action.equals("goWebView")) {
            goWebView(context, value);
        } else {
            if (javaScriptCallBack != null) {
                javaScriptCallBack.onRevValue(action, value);
            }
        }
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public void loginOk(String name, String pwd) {
        Objects.requireNonNull(webView, "plz setWebView");
        if (webView != null) {
            webView.post(() -> {
                webView.evaluateJavascript("javascript:loginOk('" + name + "','" + pwd + "')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {

                    }
                });
            });
        }
    }

    public void goWebView(Context context, String url) {
        Objects.requireNonNull(context, "Context can not be null");
//        Logger.e("goWebView ok >>> " + url);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Logger.e("goWebView ok >>>  2222");
        context.startActivity(intent);
//        Logger.e("goWebView ok >>> 11111 ");
    }

    public void loginOut() {
        Objects.requireNonNull(webView, "plz setWebView");
        if (webView != null) {
            webView.post(() -> {
                webView.evaluateJavascript("javascript:loginOut()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {

                    }
                });
            });
        }
    }

    public void sendValueToJavaScript(String action, String value) {
        Objects.requireNonNull(webView, "plz setWebView");
        if (webView != null) {
            webView.post(() -> {
                // 'xxx,yyyyy'
                webView.evaluateJavascript("javascript:onRevValue('" + action + "','" + value + "')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {

                    }
                });
            });
        }
    }


}
