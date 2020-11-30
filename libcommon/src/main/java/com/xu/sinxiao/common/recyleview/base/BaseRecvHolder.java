package com.xu.sinxiao.common.recyleview.base;

import android.view.View;

import androidx.databinding.DataBindingUtil;

public class BaseRecvHolder extends BaseHolder<BaseRecvHolderData> {
    public BaseRecvHolder(View itemView) {
        super(itemView);
    }

    public void bindView(BaseRecvHolderData data, int position) {
        data.bindView(DataBindingUtil.bind(this.itemView), position);
    }
}
