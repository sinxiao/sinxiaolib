package com.xu.sinxiao.common.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.mvp.BaseActivity;
import com.xu.sinxiao.common.view.BaseWebView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cheng on 2018/5/9.
 */
@SuppressLint("JavascriptInterface")
public class WebViewActivity extends BaseActivity {
    public static String EXTRA_URL = "EXTRA_URL";
    public static String EXTRA_TITLE_BAR_VISIBLE = "EXTRA_TITLE_BAR_VISIBLE";
    public static String EXTRA_TITLE = "EXTRA_TITLE";
    private BaseWebView webView;
    private TextView titleTv;
    //    List<BookShare> bookList1 = new ArrayList<>();
    String imgStr = "";
    Bitmap qrBitmap;
    Bitmap qrBgBitmap;
    Bitmap mergeBitmap;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean takeVideoPending;
    List<String> mPermissionList = new ArrayList<>();

    @SuppressLint("JavascriptInterface")
    public static void startActivity(Context activity, String title, String url) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_URL, url);
        activity.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        setStatusBarFontColor();
        webView = findViewById(R.id.webview);
        titleTv = findViewById(R.id.title);
        RelativeLayout rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(v -> finish());
        initWebView();
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);
//        titleTv.setText("我的矿池");

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        String url = getIntent().getStringExtra(EXTRA_URL);
        if (!TextUtils.isEmpty(url))
            webView.loadUrl(url);

    }

    protected void setStatusBarFontColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    private void initWebView() {

        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(WebViewActivity.this,
                "android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

//        webView.clearCache(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    webView.mProgressBar.setProgress(100);
                    webView.mProgressBar.setVisibility(View.INVISIBLE);

                } else {
                    webView.mProgressBar.setProgress(newProgress);
                    webView.mProgressBar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                webView.mProgressBar.setVisibility(View.VISIBLE);
            }
        });

        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.mProgressBar.setProgress(100);
                webView.mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                webView.mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    // 如果不做任何处理，浏览网页，点击系统“Back”键，整个Browser会调用finish()而结束自身，
    // 如果希望浏览的网 页回退而不是推出浏览器，需要在当前Activity中处理并消费掉该Back事件。
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            WebViewActivity.this.finish();
            return true;

        } else {
            WebViewActivity.this.finish();
            return true;
        }
//        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ScreenM.getScreenM().popActivity(this);
    }


}
