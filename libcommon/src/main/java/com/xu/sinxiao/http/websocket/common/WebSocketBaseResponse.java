package com.xu.sinxiao.http.websocket.common;

import java.io.Serializable;

public class WebSocketBaseResponse<T> implements Serializable {
    private int id;
    private String status;
    private String type;
    private T result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }


}
