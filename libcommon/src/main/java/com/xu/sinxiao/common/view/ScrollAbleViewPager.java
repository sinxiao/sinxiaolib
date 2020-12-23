package com.xu.sinxiao.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class ScrollAbleViewPager extends ViewPager {
    private boolean scrollable = true;

    public ScrollAbleViewPager(Context context) {
        super(context);
    }

    public ScrollAbleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return !this.scrollable ? true : super.onTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !this.scrollable ? false : super.onInterceptTouchEvent(ev);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public boolean isScrollable() {
        return this.scrollable;
    }

}
