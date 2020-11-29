package com.xu.sinxiao.common.callback;

public interface ResultObjectInterface extends IError {
    void onRevData(Object data);

    void onError(int code, String error);
}
