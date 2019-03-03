package com.moyacs.canary.network;
import io.reactivex.observers.ResourceObserver;
/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public abstract class ResponseObservable<T> extends ResourceObserver<T>{


    @Override
    public void onError(Throwable e) {
        onComplete();
    }

    @Override
    public void onNext(T t) {
        HttpResponse result = (HttpResponse) t;
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
