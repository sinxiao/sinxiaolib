package com.xu.sinxiao.common.mvp;

import java.io.Serializable;
import java.util.Objects;

public class MvpEvent<T> implements Serializable {
    public static final String TYPE_INIT = "init";
    public static final String TYPE_BACK = "back";
    public static final String TYPE_ENTER = "enter";
    private String type;
    private Object object;
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public static MvpEvent init(String name, String type) {
        MvpEvent mvpEvent = new MvpEvent();
        mvpEvent.name = name;
        mvpEvent.type = type;
        return mvpEvent;
    }

    public static MvpEvent initWithObject(String name, String type, Object object) {
        MvpEvent mvpEvent = new MvpEvent();
        mvpEvent.name = name;
        mvpEvent.type = type;
        mvpEvent.object = object;
        return mvpEvent;
    }

    public static <T> MvpEvent init(String name, String type, T value) {
        MvpEvent<T> mvpEvent = new MvpEvent<>();
        mvpEvent.name = name;
        mvpEvent.type = type;
        mvpEvent.value = value;
        return mvpEvent;
    }


}
