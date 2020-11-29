package com.xu.sinxiao.http.websocket;

import com.xu.sinxiao.common.callback.ResultStrListener;

import java.util.Objects;

public class RequestTask {
    private String uuid;
    private long start;
    private long end;
    private String data = null;
    //    private WebSocketResultListener<T> dataListener;
    private ResultStrListener resultStrListener;

//    public WebSocketResultListener<T> getDataListener() {
//        return dataListener;
//    }

    public String getUuid() {
        return uuid;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setData(String data) {
        this.data = data;
    }

    public RequestTask(String uuid, ResultStrListener resultStrListener) {
//        this.dataListener = dataListener;
        this.resultStrListener = resultStrListener;
        this.uuid = uuid;
    }

    public void setResult() {
        Objects.requireNonNull(data, " plz set Data first");
        if (resultStrListener != null) {
            resultStrListener.onRevData(data);
        }
//        Type type = new ParameterizedTypeImpl(WebSocketBaseResponse.class, new Class[]{dataListener.getClz()});
//        try {
//            if (data.contains("\"transactions\"") && data.contains("\"Balance\": \"")) {
//                data = data.replaceAll("\"Balance\": \"", "\"BalanceStr\": \"");
//            }
//            if (data.contains("\"transactions\"") && data.contains("\"Balance\":\"")) {
//                data = data.replaceAll("\"Balance\":\"", "\"BalanceStr\": \"");
//            }
//
//            if (data.contains("\"transactions\"") && data.contains("\"Amount\":\"")) {
//                data = data.replaceAll("\"Amount\":\"", "\"AmountStr\": \"");
//            }
//
//            if (data.contains("\"transactions\"") && data.contains("\"LimitAmount\":\"")) {
//                data = data.replaceAll("\"LimitAmount\":\"", "\"LimitAmountStr\": \"");
//            }
//            if (data.contains("\"transactions\"") && data.contains("\"TakerGets\":\"")) {
//                data = data.replaceAll("\"TakerGets\":\"", "\"TakerGetsStr\": \"");
//            }
//            if (data.contains("\"transactions\"") && data.contains("\"TakerPays\":\"")) {
//                data = data.replaceAll("\"TakerPays\":\"", "\"TakerPaysStr\": \"");
//            }
//            //Logger.e("json >> " + data);
//            WebSocketBaseResponse<T> response = GsonUtils.parserBean(data, type);
//            if (response != null) {
//                dataListener.onRevDataOk(response.getResult());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            dataListener.error(100, "convert the data error");
//        }
    }
}
