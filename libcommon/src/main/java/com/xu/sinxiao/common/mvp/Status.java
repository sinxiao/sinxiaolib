package com.xu.sinxiao.common.mvp;

import java.io.Serializable;

/**
 * 使用com.xu.sinxiao.common.base.Status 来替换
 *
 * @param <T>
 */
@Deprecated
public class Status<T extends Serializable> implements Serializable {

    public static <T extends Serializable> Status build(int statusCode, T t) {
        Status<T> status = new Status();
        status.setStatusCode(statusCode);
        status.setData(t);
        return status;
    }

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_ERROR = 2;
    public static final int STATUS_EMPTY = 3;
    private int statusCode;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}