package com.moyacs.canary.main.deal.contract_tab3;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.main.deal.net_tab2.ChiCangDateBean;
import com.moyacs.canary.main.deal.net_tab3.FundServer;
import com.moyacs.canary.main.deal.net_tab3.PaymentDateBean;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.deal.net_tab3.UserAmountVo;
import com.moyacs.canary.main.deal.net_tab3.WithdrawalDataBean;
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
 * 作者：luoshen on 2018/4/18 0018 11:47
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class FundModulImpl implements FundCountract.FundModul {

    private CompositeDisposable mCompositeDisposable;
    private FundCountract.GetFundRequestListener listener;
    private final FundServer fundServer;

    public FundModulImpl(FundCountract.GetFundRequestListener listener) {
        this.listener = listener;
        mCompositeDisposable = new CompositeDisposable();
        fundServer = HttpServerManager.getInstance().create(FundServer.class);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getFund(int mt4id) {
     /*   fundServer.getFund()
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
                .subscribe(new Observer<Response<HttpResult<FundDataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<HttpResult<FundDataBean>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.getFundResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.getFundtResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.getFundtResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
        ServerManger.getInstance().getServer().getUserAmountInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult<UserAmountVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerResult<UserAmountVo> userAmountVoServerResult) {
                        listener.getFundResponseSucessed(userAmountVoServerResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.getFundtResponseFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getWithdrawal(int mt4id, String startDate, String endDate, int pageSize, int pageNumber) {
        fundServer.getWithdrawal(mt4id, startDate, endDate, pageSize, pageNumber)
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
                .subscribe(new Observer<Response<HttpResult<WithdrawalDataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<HttpResult<WithdrawalDataBean>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.getWithdrawalResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.getWithdrawalResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.getWithdrawalResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getPayment(int mt4id, String startDate, String endDate, int pageSize, int pageNumber) {
        fundServer.getPayment(mt4id, startDate, endDate, pageSize, pageNumber)
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
                .subscribe(new Observer<Response<HttpResult<PaymentDateBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //将订阅者添加到容器中，方便在 view 退出的时候，统一解除订阅
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response<HttpResult<PaymentDateBean>> httpResultResponse) {
                        int code = httpResultResponse.code();
                        LogUtils.d("服务器返回的响应码：  " + code);
                        if (code == 200) {
                            listener.getPaymentResponseSucessed(httpResultResponse.body());
                        } else {
                            try {
                                listener.getPaymentResponseFailed(httpResultResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
                        listener.getPaymentResponseFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getTradingRecords(int mt4id, String server, String startDate, String endDate) {
        fundServer.getTradingRecords(mt4id, server, startDate, endDate)
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
//                            listener.getTradingRecordsSucessed(httpResultResponse.body());
                        } else {
//                            try {
////                                listener.setTransactionRecordsListFailed(httpResultResponse.errorBody().string());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String throwable = HttpExceptionHandler.getThrowable(e);
//                        listener.setTransactionRecordsListFailed(throwable);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getTransactionRecordList(final String transactionStatus) {
        ServerManger.getInstance().getServer().getTransactionRecordList(SPUtils.getInstance().getString(AppConstans.USER_PHONE), transactionStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult<TransactionRecordVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerResult<TransactionRecordVo> transactionRecordVoServerResult) {
                        if (transactionRecordVoServerResult.isSuccess()) {
                            listener.setTransactionRecordList(transactionRecordVoServerResult.getData().getList(), transactionStatus);
                        } else {
                            listener.setTransactionRecordsListFailed(transactionRecordVoServerResult.getMsg(), transactionStatus);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.setTransactionRecordsListFailed(e.getMessage(), transactionStatus);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
