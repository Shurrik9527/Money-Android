package com.moyacs.canary.pay.contract;


import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.MarketServer;
import com.moyacs.canary.network.HttpExceptionHandler;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.HttpServerManager;
import com.moyacs.canary.pay.net.PayServer;

import java.io.IOException;
import java.util.List;

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

public class PayModulImpl implements PayCountract.PayModul {

    private PayCountract.SubmitOrderRequestListener listener;

    private CompositeDisposable mCompositeDisposable;
    private final PayServer payServer;

    public PayModulImpl(PayCountract.SubmitOrderRequestListener listener) {
        this.listener = listener;
        mCompositeDisposable = new CompositeDisposable();
        payServer = HttpServerManager.getInstance().create(PayServer.class);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void submitOrder(String server, int mt4id, String symbol, String type, int volume,
                            double sl, double tp, String ticket,double price,String expiredDate) {
        payServer.submitOrder(server, mt4id, symbol, type, volume, sl, tp, ticket,price,expiredDate)
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
                });
    }

}
