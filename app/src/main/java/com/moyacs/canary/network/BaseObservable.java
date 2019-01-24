package com.moyacs.canary.network;

import android.content.Intent;
import android.text.TextUtils;

import com.moyacs.canary.MyApplication;
import com.moyacs.canary.login.LoginActivity;
import com.moyacs.canary.util.ToastUtils;

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
            if (TextUtils.equals("-9", ((ServerResult) t).getMsgCode())) {
                ToastUtils.showShort("身份过期，请重新登录");
                MyApplication.instance.startActivity(new Intent(MyApplication.instance, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                onError(new Throwable(result.getMsg()));
            }
        }
    }

    @Override
    public void onComplete() {

    }

    protected abstract void requestSuccess(T data);
}