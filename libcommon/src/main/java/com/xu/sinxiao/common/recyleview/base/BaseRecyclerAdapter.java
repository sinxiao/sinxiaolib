package com.xu.sinxiao.common.recyleview.base;

import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseHolder> {
    private BaseHolderFactory mHolderFactory;
    private List<BaseHolderData> mDataList = new ArrayList();
    private RecyclerView mRecyclerView;

    public BaseRecyclerAdapter(@NonNull BaseHolderFactory factory) {
        this.mHolderFactory = factory;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public BaseHolder onCreateViewHolder(ViewGroup parent, int layoutResId) {
        String holderClassName = "";
        Iterator var4 = this.mDataList.iterator();

        while (var4.hasNext()) {
            BaseHolderData data = (BaseHolderData) var4.next();
            if (data.getLayoutRes() == layoutResId) {
                holderClassName = data.getHolderClassName();
                break;
            }
        }

        if (TextUtils.isEmpty(holderClassName)) {
            throw new RuntimeException("The holder class name is null!");
        } else {
            BaseHolder holder = this.mHolderFactory.buildHolder(parent, layoutResId, holderClassName);
            holder.setAdapter(this);
            holder.setParentView(parent);
            return holder;
        }
    }

    public int getItemViewType(int position) {
        return ((BaseHolderData) this.mDataList.get(position)).getLayoutRes();
    }

    public void onBindViewHolder(BaseHolder holder, int position) {
        holder.onBindView((BaseHolderData) this.mDataList.get(position), position);
    }

    public void onViewRecycled(BaseHolder holder) {
        super.onViewRecycled(holder);
        holder.onUnbindView();
    }

    public int getItemCount() {
        return this.mDataList.size();
    }

    @Nullable
    public BaseHolderData getItem(int position) {
        return 0 <= position && position < this.mDataList.size() ? (BaseHolderData) this.mDataList.get(position) : null;
    }

    public int getItemPosition(BaseHolderData item) {
        return item != null && this.mDataList != null && !this.mDataList.isEmpty() ? this.mDataList.indexOf(item) : -1;
    }

    public RecyclerView getAttachedRecyclerView() {
        return this.mRecyclerView;
    }

    public int getItemSpanSize(int spanCount, int position) {
        if (position >= 0 && position < this.mDataList.size()) {
            BaseHolderData data = (BaseHolderData) this.mDataList.get(position);
            return data != null ? data.getItemSpanSize(spanCount) : 1;
        } else {
            return 1;
        }
    }

    @NonNull
    public List<? extends BaseHolderData> getDataList() {
        return this.mDataList;
    }

    public void updateData(@NonNull List<? extends BaseHolderData> dataList) {
        this.mDataList.clear();
        this.mDataList.addAll(dataList);
    }

    public void updateDataAndNotify(@NonNull List<? extends BaseHolderData> dataList) {
        this.updateData(dataList);
        this.notifyDataSetChanged();
    }
}
