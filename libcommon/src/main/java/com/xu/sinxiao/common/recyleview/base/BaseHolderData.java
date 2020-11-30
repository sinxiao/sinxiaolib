package com.xu.sinxiao.common.recyleview.base;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

public abstract class BaseHolderData {
    public static final int DEF_ITEM_SPAN = 1;
    public int mItemSpanSize = 1;
    public Object mExtraData;
    public IOnClickListener mItemClickListener;
    public IOnLongClickListener mItemLongClickListener;
    public BaseHolder mHolder;
    public ItemTouchHelper mItemTouchHelper;
    public boolean mDecorationEnable = true;

    public BaseHolderData() {
    }

    @LayoutRes
    public abstract int getLayoutRes();

    @NonNull
    public abstract String getHolderClassName();

    public int getItemSpanSize(int spanCount) {
        return this.mItemSpanSize >= 1 && this.mItemSpanSize <= spanCount ? this.mItemSpanSize : spanCount;
    }

    public Context getContext() {
        return null != this.mHolder ? this.mHolder.getContext() : null;
    }

    public BaseHolder getBindHolder() {
        return this.mHolder;
    }

    public int getBindPosition() {
        return null != this.mHolder ? this.mHolder.getBindPosition() : 0;
    }

    public void notifyDataChanged() {
        if (this.mHolder != null) {
            this.mHolder.notifyDataChanged();
        }

    }
}
