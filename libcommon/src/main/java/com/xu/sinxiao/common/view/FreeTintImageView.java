package com.xu.sinxiao.common.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.xu.sinxiao.common.DrawableUtils;
import com.xu.sinxiao.common.R;

public class FreeTintImageView extends AppCompatImageView {
    private ColorStateList mColorList;

    public FreeTintImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public FreeTintImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreeTintImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    public void setImageDrawable(Drawable drawable) {
        if (this.mColorList != null) {
            drawable = DrawableUtils.tintDrawable(drawable, this.mColorList);
        }

        super.setImageDrawable(drawable);
    }

    public void setImageResource(int resId) {
        this.setImageDrawable(this.getResources().getDrawable(resId));
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FreeTintImageView);
        this.mColorList = ta.getColorStateList(R.styleable.FreeTintImageView_ftiv_stateTint);
        ta.recycle();
        this.refreshDrawable();
    }

    private void refreshDrawable() {
        Drawable drawable = this.getDrawable();
        if (drawable != null) {
            this.setImageDrawable(drawable);
        }

    }

    public void setColorStateList(ColorStateList list) {
        this.mColorList = list;
        this.refreshDrawable();
    }
}