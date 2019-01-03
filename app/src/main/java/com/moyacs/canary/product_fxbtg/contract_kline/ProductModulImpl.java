package com.moyacs.canary.product_fxbtg.contract_kline;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.network.HttpExceptionHandler;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.HttpServerManager;
import com.moyacs.canary.product_fxbtg.net_kline.KLineData;
import com.moyacs.canary.product_fxbtg.net_kline.ProductServer;

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
 * 作者：luoshen on 2018/3/26 0026 13:22
 * 邮箱：rsf411613593@gmail.com
 * 说明：m 层，执行具体的网络请求操作
 */

public class ProductModulImpl implements ProductContract.ProductModul {

    private ProductContract.ProductKLineRequestListener listener;
    private CompositeDisposable mCompositeDisposable;
    private final ProductServer productServer;

    public ProductModulImpl(ProductContract.ProductKLineRequestListener listener) {
        mCompositeDisposable = new CompositeDisposable();
        this.listener = listener;
        productServer = HttpServerManager.getInstance().create(ProductServer.class);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }


    @Override
    public void getKLineData(String symbol, String startDate, String endDate, String period, String server) {
        productServer.getKLineData(symbol, startDate, endDate, period,server)
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
                .subscribe(new Observer<Response<HttpResult<List<KLineData>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<HttpResult<List<KLineData>>> httpResultResponse) {
                        Log.i("onNext", "httpResultResponse:  "+httpResultResponse.toString());
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.getKLineDataResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.getKLineDataResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.getKLineDataResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
