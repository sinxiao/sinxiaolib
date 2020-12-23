package com.xu.sinxiao.common.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.appcompat.widget.DrawableUtils;

public class ActionBarHelper {
    private Context mContext;
    private ViewGroup mActionBar;
    private ViewGroup mContent;
    private View mStatusBar;
    private View mTitleContainer;
    private View mTitleBackContainer;
//    private FreeTintImageView mTitleBackImageView;
    private ViewGroup mTitleLeftContainer;
    private ViewGroup mTitleRightContainer;
    private TextView mTitleTextView;
    private int mStatusBarHeight;
    private int mTitleHeight;
    private int mTitleKeyIconSize;
    private int mTitleKeyTextSize;
    private int mTitleKeyTextHorizontalMargin;
    private boolean mStatusBarFloatAble = false;
    private boolean mTitleFloatAble = false;
    private View.OnClickListener mTitleBackKeyClickListener;

    public ActionBarHelper(Context context) {
        this.mContext = context;
    }

    public void init(ViewGroup actionBar, ViewGroup content) {
        this.mActionBar = actionBar;
        this.mContent = content;
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
//        inflater.inflate(layout.layout_action_bar, this.mActionBar, true);
//        this.mStatusBar = this.findSubViewById(id.action_bar_status);
//        this.mTitleContainer = this.findSubViewById(id.action_bar_title_container);
//        this.mTitleBackContainer = this.findSubViewById(id.action_bar_back_key_container);
//        this.mTitleBackImageView = (FreeTintImageView)this.findSubViewById(id.action_bar_back_key_icon);
//        this.mTitleLeftContainer = (ViewGroup)this.findSubViewById(id.action_bar_left_container);
//        this.mTitleTextView = (TextView)this.findSubViewById(id.action_bar_title);
//        this.mTitleRightContainer = (ViewGroup)this.findSubViewById(id.action_bar_right_container);
        this.initListener();
        this.initResources();
        this.notifyStatusBarHeight();
        this.notifyContentTopMargin();
    }

    protected <T extends View> T findSubViewById(@IdRes int id) {
        return this.mActionBar.findViewById(id);
    }

    private void initListener() {
        this.mTitleBackContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (null != ActionBarHelper.this.mTitleBackKeyClickListener) {
                    ActionBarHelper.this.mTitleBackKeyClickListener.onClick(v);
                }

            }
        });
        ViewTreeObserver vto = this.mActionBar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                ActionBarHelper.this.autoLayoutTitleTextView();
            }
        });
    }

    private void initResources() {
        this.mStatusBarHeight = this.getStatusBarHeight(this.mContext);
//        this.mTitleHeight = (int)this.mContext.getResources().getDimension(dimen.action_bar_title_height);
//        this.mTitleKeyIconSize = (int)this.mContext.getResources().getDimension(dimen.action_bar_key_icon_size);
//        this.mTitleKeyTextSize = (int)this.mContext.getResources().getDimension(dimen.action_bar_key_text_size);
//        this.mTitleKeyTextHorizontalMargin = (int)this.mContext.getResources().getDimension(dimen.action_bar_key_text_horizontal_margin);
    }

    private int getStatusBarHeight(Context ctx) {
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private void autoLayoutTitleTextView() {
        int leftPadding;
        int rightPadding;
        if (17 == this.mTitleTextView.getGravity()) {
            int lPadding = (0 == this.mTitleBackContainer.getVisibility() ? this.mTitleBackContainer.getWidth() : 0);
            lPadding += this.mTitleLeftContainer.getWidth();
            int rPadding = this.mTitleRightContainer.getWidth();
            leftPadding = Math.max(lPadding, rPadding);
            rightPadding = leftPadding;
        } else {
            leftPadding = 0 == this.mTitleBackContainer.getVisibility() ? this.mTitleBackContainer.getWidth() : 0;
            leftPadding += this.mTitleLeftContainer.getWidth();
            rightPadding = this.mTitleRightContainer.getWidth();
        }

        this.mTitleTextView.setPadding(leftPadding, 0, rightPadding, 0);
    }

    private void notifyStatusBarHeight() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)this.mStatusBar.getLayoutParams();
        lp.height = this.mStatusBarHeight;
    }

    private void notifyTitleHeight() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)this.mTitleContainer.getLayoutParams();
        lp.height = this.mTitleHeight;
    }

    private void notifyContentTopMargin() {
        android.widget.FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams)this.mContent.getLayoutParams();
        int oldTopMargin = lp.topMargin;
        int newTopMargin = this.getContentTopMargin();
        if (oldTopMargin != newTopMargin) {
            lp.topMargin = newTopMargin;
        }

    }

    private int getContentTopMargin() {
        int margin = 0;
        if (!this.mStatusBarFloatAble && this.mStatusBar.getVisibility() == 0) {
            margin += this.mStatusBarHeight;
        }

        if (!this.mTitleFloatAble && this.mTitleContainer.getVisibility() == 0) {
            margin += this.mTitleHeight;
        }

        return margin;
    }

    public void setStatusBarFloatAble(boolean enable) {
        this.mStatusBarFloatAble = enable;
        this.notifyContentTopMargin();
    }

    public void setTitleFloatAble(boolean enable) {
        this.mTitleFloatAble = enable;
        this.notifyContentTopMargin();
    }

    public void setBackgroundColor(@ColorInt int color) {
        this.mActionBar.setBackgroundColor(color);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        this.mActionBar.setBackgroundDrawable(drawable);
    }

    public void setTitleVisible(boolean visible) {
        int visibility = visible ? 0 : 8;
//        this.mTitleContainer.setVisibility(visibility);
        this.notifyContentTopMargin();
    }

    public void setTitle(String text) {
        this.mTitleTextView.setText(text);
    }

    public void setTitleTextColor(int color) {
        this.mTitleTextView.setTextColor(color);
    }

    public void setTitleTextSize(int textSize) {
        this.mTitleTextView.setTextSize(0, (float)textSize);
    }

    public void setTitleGravity(int gravity) {
        this.mTitleTextView.setGravity(gravity);
    }

    public void setTitleBackgroundColor(@ColorInt int color) {
        this.mTitleContainer.setBackgroundColor(color);
    }

    public void setTitleBackgroundDrawable(Drawable drawable) {
        this.mTitleContainer.setBackgroundDrawable(drawable);
    }

    public void setStatusBarVisible(boolean visible) {
        int visibility = visible ? 0 : 8;
//        this.mStatusBar.setVisibility(visibility);
        this.notifyContentTopMargin();
    }

    public void setStatusBarColor(@ColorInt int color) {
        this.mStatusBar.setBackgroundColor(color);
    }

    public void setTitleBackKeyVisible(boolean visible) {
        int visibility = visible ? 0 : 8;
//        this.mTitleBackContainer.setVisibility(visibility);
    }

    public void setTitleBackKeyColor(int color) {
//        this.mTitleBackImageView.setColorStateList(DrawableUtils.buildColorStateList(ColorUtils.alphaColor(0.5F, color), color, ColorUtils.alphaColor(0.5F, color), color));
    }

    public void setTitleBackKeyColor(ColorStateList colorStateList) {
//        this.mTitleBackImageView.setColorStateList(colorStateList);
    }

    public void setTitleBackKeyClickListener(View.OnClickListener listener) {
        this.mTitleBackKeyClickListener = listener;
    }

    public int getTitleHeight() {
        return this.mTitleHeight;
    }

    public void setTitleHeight(int titleHeight) {
        this.mTitleHeight = titleHeight;
        this.notifyTitleHeight();
    }

    public int getTitleKeyIconSize() {
        return this.mTitleKeyIconSize;
    }

    public void setTitleKeyIconSize(int titleKeyIconSize) {
        this.mTitleKeyIconSize = titleKeyIconSize;
    }

    public int getTitleKeyTextSize() {
        return this.mTitleKeyTextSize;
    }

    public void setTitleKeyTextSize(int titleKeyTextSize) {
        this.mTitleKeyTextSize = titleKeyTextSize;
    }

    public int getTitleKeyTextHorizontalMargin() {
        return this.mTitleKeyTextHorizontalMargin;
    }

    public void setTitleKeyTextHorizontalMargin(int titleKeyTextHorizontalMargin) {
        this.mTitleKeyTextHorizontalMargin = titleKeyTextHorizontalMargin;
    }

    public TextView addTitleLeftKey(String text, View.OnClickListener listener) {
        return this.addTitleLeftKey(text, -1, listener);
    }

    public TextView addTitleLeftKey(String text, @ColorInt int color, View.OnClickListener listener) {
        RelativeLayout rl = new RelativeLayout(this.mContext);
        RelativeLayout.LayoutParams rlLp = new RelativeLayout.LayoutParams(-2, this.mTitleHeight);
        rl.setLayoutParams(rlLp);
        rl.setMinimumWidth(this.mTitleHeight);
        rl.setGravity(17);
        rl.setPadding(this.mTitleKeyTextHorizontalMargin, 0, this.mTitleKeyTextHorizontalMargin, 0);
        rl.setOnClickListener(listener);
        TextView tv = new TextView(this.mContext);
        tv.setText(text);
        tv.setTextSize(0, (float)this.mTitleKeyTextSize);
        tv.setGravity(17);
//        tv.setTextColor(DrawableUtils.buildColorStateList(color, color, ColorUtils.alphaColor(0.6F, color), color));
        rl.addView(tv);
        this.mTitleLeftContainer.addView(rl);
        this.autoLayoutTitleTextView();
        return tv;
    }

//    public ImageView addTitleLeftKey(@DrawableRes int resId, View.OnClickListener listener) {
//        return this.addTitleLeftKey(resId, -1, listener);
//    }

//    public ImageView addTitleLeftKey(@DrawableRes int resId, @ColorInt int color, View.OnClickListener listener) {
//        RelativeLayout rl = new RelativeLayout(this.mContext);
//        android.widget.RelativeLayout.LayoutParams llLp = new android.widget.RelativeLayout.LayoutParams(this.mTitleHeight, this.mTitleHeight);
//        rl.setLayoutParams(llLp);
//        rl.setGravity(17);
//        rl.setOnClickListener(listener);
////        FreeTintImageView iv = new FreeTintImageView(this.mContext);
//        android.widget.FrameLayout.LayoutParams ivLp = new android.widget.FrameLayout.LayoutParams(this.mTitleKeyIconSize, this.mTitleKeyIconSize);
////        iv.setLayoutParams(ivLp);
////        iv.setScaleType(ImageView.ScaleType.FIT_XY);
////        iv.setImageResource(resId);
////        iv.setColorStateList(DrawableUtils.buildColorStateList(color, color, ColorUtils.alphaColor(0.6F, color), color));
////        rl.addView(iv);
//        this.mTitleLeftContainer.addView(rl);
//        this.autoLayoutTitleTextView();
//        return iv;
//    }

    public void clearTitleLeftKey() {
        this.mTitleLeftContainer.removeAllViews();
    }

    public TextView addTitleRightKey(String text, View.OnClickListener listener) {
        return this.addTitleRightKey(text, -1, listener);
    }

    public TextView addTitleRightKey(String text, @ColorInt int color, View.OnClickListener listener) {
        RelativeLayout rl = new RelativeLayout(this.mContext);
        RelativeLayout.LayoutParams rlLp = new RelativeLayout.LayoutParams(-2, this.mTitleHeight);
        rl.setLayoutParams(rlLp);
        rl.setMinimumWidth(this.mTitleHeight);
        rl.setGravity(17);
        rl.setPadding(this.mTitleKeyTextHorizontalMargin, 0, this.mTitleKeyTextHorizontalMargin, 0);
        rl.setOnClickListener(listener);
        TextView tv = new TextView(this.mContext);
        tv.setText(text);
        tv.setTextSize(0, (float)this.mTitleKeyTextSize);
        tv.setGravity(17);
//        tv.setTextColor(DrawableUtils.buildColorStateList(color, color, ColorUtils.alphaColor(0.6F, color), color));
        rl.addView(tv);
        this.mTitleRightContainer.addView(rl);
        this.autoLayoutTitleTextView();
        return tv;
    }

//    public ImageView addTitleRightKey(@DrawableRes int resId, View.OnClickListener listener) {
//        return this.addTitleRightKey(resId, -1, listener);
//    }

//    public ImageView addTitleRightKey(@DrawableRes int resId, @ColorInt int color, View.OnClickListener listener) {
//        RelativeLayout rl = new RelativeLayout(this.mContext);
//        android.widget.RelativeLayout.LayoutParams llLp = new android.widget.RelativeLayout.LayoutParams(this.mTitleHeight, this.mTitleHeight);
//        rl.setLayoutParams(llLp);
//        rl.setGravity(17);
//        rl.setOnClickListener(listener);
////        FreeTintImageView iv = new FreeTintImageView(this.mContext);
////        android.widget.FrameLayout.LayoutParams ivLp = new android.widget.FrameLayout.LayoutParams(this.mTitleKeyIconSize, this.mTitleKeyIconSize);
////        iv.setLayoutParams(ivLp);
////        iv.setScaleType(ImageView.ScaleType.FIT_XY);
////        iv.setImageResource(resId);
////        iv.setColorStateList(DrawableUtils.buildColorStateList(color, color, ColorUtils.alphaColor(0.6F, color), color));
////        rl.addView(iv);
//        this.mTitleRightContainer.addView(rl);
//        this.autoLayoutTitleTextView();
//        return iv;
//    }

    public View addTitleRightKey(View view) {
        this.mTitleRightContainer.addView(view);
        this.autoLayoutTitleTextView();
        return view;
    }

    public void clearTitleRightKey() {
        this.mTitleRightContainer.removeAllViews();
    }
}
