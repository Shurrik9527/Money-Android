/*
package com.moyacs.canary.main.deal.contract_tab2;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.main.deal.net_tab2.ChiCangDateBean;
import com.moyacs.canary.main.deal.net_tab2.ChiCangTabServer;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.market.net.MarketDataBean;
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

*/
/**
 * 作者：luoshen on 2018/4/17 0017 09:40
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 *//*


public class ChiCangModulImpl implements ChiCangCountract.ChiCangModul {


    private ChiCangCountract.GetChiCangListRequestListener listener;
    private CompositeDisposable mCompositeDisposable;

    public ChiCangModulImpl(ChiCangCountract.GetChiCangListRequestListener listener) {
        this.listener = listener;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getChiCangList() {
     */
/*   chiCangTabServer.getRecordList(mt4id, server, startDate, endDate)
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
                .subscribe(new Observer<Response<HttpResult<List<ChiCangDateBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<HttpResult<List<ChiCangDateBean>>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.getChiCangListResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.getChiCangListResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.getChiCangListResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });*//*


        ServerManger.getInstance().getServer().getTransactionRecordList(SPUtils.getInstance().getString(AppConstans.USER_PHONE), "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult<TransactionRecordVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        listener.beforeRequest();
                    }

                    @Override
                    public void onNext(ServerResult<TransactionRecordVo> transactionRecordVoServerResult) {
                        listener.getChiCangListResponseSucessed(transactionRecordVoServerResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.afterRequest();
                        listener.getChiCangListResponseFailed("服务器异常，请稍后再试！");
                    }

                    @Override
                    public void onComplete() {
                        listener.afterRequest();
                    }
                });

    }
}
*/
