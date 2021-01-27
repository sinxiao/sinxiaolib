package com.xu.sinxiao.common.mvvm;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xu.sinxiao.common.base.Event;
import com.xu.sinxiao.common.base.Status;

/**
 * T 这里不做限制，建议继承 Status ，和Status 类似
 *
 * @param <T>
 */
public abstract class BaseViewModel<T extends Status> extends ViewModel {

    protected Bundle bundle;
    protected MutableLiveData<T> data = new MutableLiveData<>();

    public void dispatchEvent(Event event) {

        if (event != null) {
            if (event.getType() == Event.TYPE_INIT) {
                if (event.getValue() != null) {
                    if (event.getValue() instanceof Bundle) {
                        bundle = (Bundle) event.getValue();
                    }
                }
                fetchData();
            } else {
                onDispatchEvent(event);
            }
        }
    }

    //并不需要再依赖 freshStatus

    public abstract void onDispatchEvent(Event event);

    public abstract void fetchData();
}
