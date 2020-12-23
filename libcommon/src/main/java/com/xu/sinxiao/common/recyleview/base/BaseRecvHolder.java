package com.xu.sinxiao.common.recyleview.base;

import android.view.View;

import androidx.databinding.DataBindingUtil;

public class BaseRecvHolder extends BaseHolder<BaseViewHolderItem> {
    public BaseRecvHolder(View itemView) {
        super(itemView);
    }

    public void bindView(BaseViewHolderItem data, int position) {
        data.bindView(DataBindingUtil.bind(this.itemView), position);
    }
}
