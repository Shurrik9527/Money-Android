package com.moyacs.canary.pay.tixian.contract;


import com.moyacs.canary.network.HttpExceptionHandler;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.HttpServerManager;
import com.moyacs.canary.pay.tixian.net.WithdrawServer;
import com.moyacs.canary.util.LogUtils;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * 作者：luoshen on 2018/4/15 0015 10:53
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class WithdrawModulImpl implements WithdrawCountract.WithdrawModul {

    private WithdrawCountract.WithdrawRequestListener listener;

    private CompositeDisposable mCompositeDisposable;
    private final WithdrawServer withdrawServer;

    public WithdrawModulImpl(WithdrawCountract.WithdrawRequestListener listener) {
        this.listener = listener;
        mCompositeDisposable = new CompositeDisposable();
        withdrawServer = HttpServerManager.getInstance().create(WithdrawServer.class);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }


    @Override
    public void withdraw(int mt4id, String type, double amount, String accountNumber, String accountName, String bankName, String bankAddress) {
        withdrawServer.withdraw(mt4id, type, amount, accountNumber, accountName, bankName, bankAddress)
                .subscribeOn(Schedulers.io())//指定网络请求所在的线程
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        listener.beforeRequest();

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        listener.afterRequest();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//指定的是它之后（下方）执行的操作所在的线程
                .subscribe(new Observer<Response<HttpResult<Object>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<HttpResult<Object>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.withdrawResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.withdrawResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.withdrawResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
