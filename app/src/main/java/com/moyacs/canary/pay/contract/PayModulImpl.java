/*
package com.moyacs.canary.pay.contract;


import com.moyacs.canary.network.HttpServerManager;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.pay.net.PayServer;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

*/
/**
 * 作者：luoshen on 2018/4/15 0015 10:53
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 *//*


public class PayModulImpl implements PayContract.PayModul {

    private PayContract.SubmitOrderRequestListener listener;

    private CompositeDisposable mCompositeDisposable;
    private final PayServer payServer;

    public PayModulImpl(PayContract.SubmitOrderRequestListener listener) {
        this.listener = listener;
        mCompositeDisposable = new CompositeDisposable();
        payServer = HttpServerManager.getInstance().create(PayServer.class);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void submitOrder(Map<String, Object> map) {
      */
/*  payServer.closeOrder(server, mt4id, symbol, type, volume, sl, tp, ticket, price, expiredDate)
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
                            listener.submitOrderResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.submitOrderResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.submitOrderResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });*//*

        ServerManger.getInstance().getServer().transactionSell(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerResult<String> stringServerResult) {
                        listener.submitOrderResponseSucessed(stringServerResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.submitOrderResponseFailed("服务器异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
*/
