package com.xu.sinxiao.common.fragment.tabfragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.logger.Logger;
import com.xu.sinxiao.common.view.FreeTintImageView;

public class CustomTabItemLayout extends LinearLayout {
    private int mIndex;
    private ViewGroup mTabItem;
    private ViewGroup mTabIconContainer;
    private FreeTintImageView mIcon;
    private TextView mTextNewFlag;
    private TextView mText;
    @DrawableRes
    private int[] mIconResIds;

    private CustomTabItemLayout(Context context) {
        super(context);
        inflate(this.getContext(), R.layout.widget_tab_item, this);
        this.mTabItem = (ViewGroup) this.findViewById(R.id.tab_item);
        this.mTabIconContainer = (ViewGroup) this.findViewById(R.id.tab_item_icon_container);
        this.mIcon = (FreeTintImageView) this.findViewById(R.id.tab_item_icon);
        this.mTextNewFlag = (TextView) this.findViewById(R.id.tab_item_new_flag);
        this.mText = (TextView) this.findViewById(R.id.tab_item_text);
    }

    public void setSelected(boolean selected) {
        this.mTabItem.setSelected(selected);
        if (this.mIconResIds != null && this.mIconResIds.length == 2) {
            this.mIcon.setImageResource(selected ? this.mIconResIds[1] : this.mIconResIds[0]);
        }

    }

    public void setIcon(@DrawableRes int resId) {
        this.mIcon.setImageResource(resId);
    }

    public void setIconVisible(boolean visible) {
        this.mTabIconContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setIconResIds(@DrawableRes int[] mIconResIds) {
        this.mIconResIds = mIconResIds;
    }

    public void setText(@NonNull String text) {
        this.mText.setText(text);
    }

    public void setTextVisible(boolean visible) {
        this.mText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public String getText() {
        return this.mText.getText().toString();
    }

    public void setNewFlag(String text) {
        this.mTextNewFlag.setText(text);
    }

    public void setNewFlagVisible(boolean visible) {
        this.mTextNewFlag.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public int getIndex() {
        return this.mIndex;
    }

    void setIndex(int index) {
        this.mIndex = index;
    }

    public void setColorStateList(@NonNull ColorStateList list) {
        if (this.mIconResIds != null && this.mIconResIds.length == 1) {
            this.mIcon.setColorStateList(list);
        }

        this.mText.setTextColor(list);
    }

    public static CustomTabItemLayout build(Context context, @DrawableRes int[] mIconRids, String text) {
        CustomTabItemLayout tabItem = new CustomTabItemLayout(context);
        tabItem.setIconResIds(mIconRids);
        if (mIconRids != null && mIconRids.length > 0) {
            tabItem.setIconVisible(true);
            tabItem.setIcon(mIconRids[0]);
        } else {
            tabItem.setIconVisible(false);
        }

        tabItem.setText(text);
        if (TextUtils.isEmpty(text)) {
            tabItem.setTextVisible(false);
        } else {
            tabItem.setTextVisible(true);
        }

        return tabItem;
    }
}