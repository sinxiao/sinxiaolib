package com.xu.sinxiao.common.recyleview;

import androidx.annotation.NonNull;

import com.xu.sinxiao.common.recyleview.base.BaseHolderFactory;
import com.xu.sinxiao.common.recyleview.base.BaseRecyclerAdapter;

public class CommonRecycleView extends BaseRecyclerAdapter {
    public CommonRecycleView() {
        this(new SimpleHolderFactory());
    }

    public CommonRecycleView(@NonNull BaseHolderFactory factory) {
        super(factory);
    }
}
