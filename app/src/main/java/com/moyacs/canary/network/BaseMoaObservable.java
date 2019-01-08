package com.moyacs.canary.network;

import io.reactivex.observers.ResourceObserver;

/**
 * @Author: Akame
 * @Date: 2019/1/8
 * @Description:
 */
public abstract class BaseMoaObservable<T> extends ResourceObserver<T> {
    @Override
    public void onError(Throwable e) {
        onComplete();
    }

    @Override
    public void onNext(T t) {
        HttpResult result = (HttpResult) t;
        if (result.isSuccess()) {
            requestSuccess(t);
        } else {
            onError(new Throwable(result.getMessage()));
        }
    }

    @Override
    public void onComplete() {

    }

    protected abstract void requestSuccess(T data);
}