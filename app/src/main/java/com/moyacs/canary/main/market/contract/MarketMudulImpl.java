package com.moyacs.canary.main.market.contract;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.MarketServer;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.network.HttpExceptionHandler;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.HttpServerManager;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;

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
 * 作者：luoshen on 2018/3/7 0007 10:18
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class MarketMudulImpl implements MarketContract.MarketModul {

    private MarketContract.MarketListRequestListener listener;
    private CompositeDisposable mCompositeDisposable;
    private final MarketServer marketServer;

    public MarketMudulImpl(MarketContract.MarketListRequestListener listener) {
        this.listener = listener;
        mCompositeDisposable = new CompositeDisposable();
        marketServer = HttpServerManager.getInstance().create(MarketServer.class);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getMarketList(String username, String server) {
        marketServer.getMarketList_optional(username, server)
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
                .subscribe(new Observer<Response<HttpResult<List<MarketDataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                        Log.i("onNext", "onSubscribe:  ");
                    }

                    @Override
                    public void onNext(Response<HttpResult<List<MarketDataBean>>> httpResultResponse) {
                        Log.i("onNext", "getMarketListResponseSucessed:  " + httpResultResponse.toString());
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.getMarketListResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.getMarketListResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("onNext", "onSubscribe:  ");
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.getMarketListResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("onNext", "onSubscribe:  ");
                    }
                });
    }

    @Override
    public void getMarketList_type(String page, String pageSize) {
        marketServer.getMarketList_type(page, pageSize)
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
                .subscribe(new Observer<Response<HttpResult<List<MarketDataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<HttpResult<List<MarketDataBean>>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.getMarketListResponseSucessed_type(httpResultResponse.body());
                        } else {
                            try {
                                listener.getMarketListResponseFailed_type(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.getMarketListResponseFailed_type(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getTradeList() {
        ServerManger.getInstance().getServer().getTradList("0", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult<TradeVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerResult<TradeVo> listServerResult) {
                        listener.getTradeList(listServerResult.getData().getList());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
