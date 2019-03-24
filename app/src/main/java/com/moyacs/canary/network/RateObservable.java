package com.moyacs.canary.network;

import io.reactivex.observers.ResourceObserver;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/18
 * @email 252774645@qq.com
 */
public abstract class RateObservable<T> extends ResourceObserver<T> {
    @Override
    public void onError(Throwable e) {
        onComplete();
    }

    @Override
    public void onNext(T t) {
        RateResult result = (RateResult) t;
        if (result.getSuccess()==1) {
            requestSuccess(t);
        }
    }

    @Override
    public void onComplete() {

    }

    protected abstract void requestSuccess(T data);
}
