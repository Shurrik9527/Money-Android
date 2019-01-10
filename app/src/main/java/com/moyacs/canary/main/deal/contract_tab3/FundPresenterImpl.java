package com.moyacs.canary.main.deal.contract_tab3;

import com.blankj.utilcode.util.SPUtils;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.deal.net_tab3.UserAmountVo;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 作者：luoshen on 2018/4/18 0018 11:45
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class FundPresenterImpl implements FundContract.FundPresenter {

    private FundContract.FundView view;
    private CompositeDisposable disposable;

    public FundPresenterImpl(FundContract.FundView view) {
        this.view = view;
        disposable = new CompositeDisposable();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void getAccountInfo() {
        disposable.add(ServerManger.getInstance().getServer().getUserAmountInfo()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<UserAmountVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<UserAmountVo> data) {
                        view.setAccountInfo(data.getData());
                    }
                }));
    }

    @Override
    public void getTransactionRecordList(final String transactionStatus) {
        ServerManger.getInstance().getServer()
                .getTransactionRecordList(SPUtils.getInstance().getString(AppConstans.USER_PHONE), transactionStatus)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new BaseObservable<ServerResult<TransactionRecordVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TransactionRecordVo> data) {
                        view.setTradingRecordList(data.getData().getList(), transactionStatus);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getTradingRecordsFailed("服务器异常", transactionStatus);
                    }
                });
    }

    @Override
    public void getWithdrawal() {

    }

    @Override
    public void getPayment() {

    }
}
