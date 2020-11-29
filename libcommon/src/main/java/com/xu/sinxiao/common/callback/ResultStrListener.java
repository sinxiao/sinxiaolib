package com.xu.sinxiao.common.callback;

public interface ResultStrListener extends IError {
    void onRevData(String data);

    void onError(int code, String error);
}
