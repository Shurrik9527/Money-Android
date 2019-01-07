package com.moyacs.canary.network;

import io.reactivex.observers.ResourceObserver;

public abstract class BaseObservable<T> extends ResourceObserver<T> {
    @Override
    public void onError(Throwable e) {
        onComplete();
    }

    @Override
    public void onNext(T t) {
        ServerResult result = (ServerResult) t;
        if (result.isSuccess()) {
            requestSuccess(t);
        } else {
            onError(new Throwable(result.getMsg()));
        }
    }

    @Override
    public void onComplete() {

    }

    protected abstract void requestSuccess(T data);
}