package com.xu.sinxiao.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.Utils;


/**
 *
 */
public class BaseWebView extends WebView {
    private Context mContext;
    public ProgressBar mProgressBar;
    private OnProgressListener onProgressListener;

    public BaseWebView(Context context) {
        super(context);
        this.mContext = context;
        initializeOptions();
    }


    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initializeOptions();
    }

    private void initializeOptions() {
        mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, Utils.dp2px(mContext, 2), 0, 0));
        mProgressBar.incrementProgressBy(5);
        mProgressBar.setMax(100);
        mProgressBar.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progressbar_webview_drawable));
        addView(mProgressBar);
        setWebChromeClient(new MyWebChromeClient());
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (onProgressListener != null) {
                    onProgressListener.onProgressChanged(newProgress);
                }
                if (newProgress == 100) {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(GONE);
                    }
                } else {
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(VISIBLE);
                        mProgressBar.setProgress(newProgress);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    public void setProgressBarVisible(int visible) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(visible);
            this.removeView(mProgressBar);
        }
    }
   /* @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }*/

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public interface OnProgressListener {
        void onProgressChanged(int newProgress);
    }
}
