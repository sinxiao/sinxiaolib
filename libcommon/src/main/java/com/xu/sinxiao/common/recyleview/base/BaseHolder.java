package com.xu.sinxiao.common.recyleview.base;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseHolder<T extends BaseHolderData> extends RecyclerView.ViewHolder {
    protected ViewGroup mParentView;
    private Resources mResources;
    private BaseRecyclerAdapter mAdapter;
    private int mBindPosition = -1;
    private T mData;

    public abstract void bindView(T var1, int var2);

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public void onBindView(T data, int position) {
        this.mBindPosition = position;
        this.mData = data;
        this.mData.mHolder = this;
        this.itemView.setOnClickListener(this.getItemClickListener());
        this.itemView.setOnLongClickListener(this.getItemLongClickListener());
        this.bindView(data, position);
    }

    public void onUnbindView() {
        this.mBindPosition = -1;
    }

    public final void setAdapter(BaseRecyclerAdapter adapter) {
        this.mAdapter = adapter;
    }

    public final void setParentView(ViewGroup parent) {
        this.mParentView = parent;
    }

    public final BaseRecyclerAdapter getAdapter() {
        return this.mAdapter;
    }

    public final T getBindData() {
        return this.mData;
    }

    public final int getBindPosition() {
        return this.mBindPosition;
    }

    public final void notifyDataChanged() {
        if (this.isBindView() && this.mAdapter != null) {
            this.mAdapter.notifyItemChanged(this.mBindPosition);
        }

    }

    public final boolean isBindView() {
        return this.mBindPosition != -1;
    }

    protected final Context getContext() {
        return this.itemView.getContext();
    }

    @NonNull
    protected final Resources getResources() {
        if (null == this.mResources) {
            this.mResources = this.getContext().getResources();
        }

        return this.mResources;
    }

    protected final View.OnClickListener getItemClickListener() {
        return null != this.getBindData() && this.getBindData().mItemClickListener != null ? new View.OnClickListener() {
            public void onClick(View v) {
                if (BaseHolder.this.getBindData().mItemClickListener != null) {
                    try {
                        Object object = BaseHolder.this.getBindData();
                        BaseHolder.this.getBindData().mItemClickListener.onClick(v, object, BaseHolder.this.getBindPosition());
                    }catch (Exception e){
//                         e.printStackTrace();
                    }
                }

            }
        } : null;
    }

    protected final View.OnLongClickListener getItemLongClickListener() {
        return null != this.getBindData() && this.getBindData().mItemLongClickListener != null ? new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                return BaseHolder.this.getBindData().mItemLongClickListener != null ? BaseHolder.this.getBindData().mItemLongClickListener.onLongClick(v, BaseHolder.this.getBindData(), BaseHolder.this.getBindPosition()) : false;
            }
        } : null;
    }

    protected final <T extends View> T findSubViewById(@IdRes int id) {
        return this.itemView.findViewById(id);
    }
}
