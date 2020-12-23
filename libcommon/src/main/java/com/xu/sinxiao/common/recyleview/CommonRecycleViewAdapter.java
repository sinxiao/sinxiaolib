package com.xu.sinxiao.common.recyleview;

import androidx.annotation.NonNull;

import com.xu.sinxiao.common.recyleview.base.BaseHolderFactory;
import com.xu.sinxiao.common.recyleview.base.BaseRecyclerAdapter;

public class CommonRecycleViewAdapter extends BaseRecyclerAdapter {
    public CommonRecycleViewAdapter() {
        this(new SimpleHolderFactory());
    }

    public CommonRecycleViewAdapter(@NonNull BaseHolderFactory factory) {
        super(factory);
    }
}
