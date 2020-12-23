package com.xu.sinxiao.common.viewholder;


import com.xu.sinxiao.common.R;
import com.xu.sinxiao.common.databinding.ItemEmptyBinding;
import com.xu.sinxiao.common.recyleview.base.BaseViewHolderItem;

public class EmptyItemHolder extends BaseViewHolderItem<ItemEmptyBinding> {

    @Override
    public void bindView(ItemEmptyBinding itemEmptyBinding, int i) {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_empty;
    }
}
