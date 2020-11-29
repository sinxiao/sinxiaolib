package com.xu.sinxiao.common.callback;

public interface ResultListener<T> extends IError {

    void onRevData(T t);

    void onError(int code, String error);
}
