package com.xu.sinxiao.rxbus;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.FlowableProcessor;
import io.reactivex.rxjava3.processors.PublishProcessor;

public class RxBus {

    private static volatile RxBus instance;
    private FlowableProcessor rxBus = PublishProcessor.create().toSerialized();

    private RxBus() {
    }

    public static RxBus getInstance() {
        if (instance == null) {
            Class var0 = RxBus.class;
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }

        return instance;
    }

    public void post(Object o) {
        try {
            this.rxBus.onNext(o);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public <T> Flowable<T> register(Class<T> eventType) {
        return this.rxBus.ofType(eventType);
    }
}

