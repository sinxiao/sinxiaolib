package com.xu.sinxiao.common.mvp;

public interface IPresent {
    void fetchData();

    void dispatchEvent(MvpEvent event);

    void destory();
}
