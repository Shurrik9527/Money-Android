package com.moyacs.canary.main.deal.capital;

import android.util.Log;

import com.moyacs.canary.bean.TransactionRecordVo;
import com.moyacs.canary.bean.UserAmountVo;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class CapitalPresenter implements CapitalContract.CapitalPresenter{

    private static final String TAG = CapitalPresenter.class.getName();
    private CapitalContract.CapitalView mView;
    private CompositeDisposable disposable;
    public CapitalPresenter(CapitalContract.CapitalView view) {
        this.mView = view;
        disposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void getAccountInfo() {
        disposable.add(ServerManger.getInstance().getServer().getUserAmountInfo()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<UserAmountVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<UserAmountVo> data) {
                        if(mView!=null){
                            mView.setAccountInfo(data.getData());
                        }
                    }
                }));
    }

    @Override
    public void getTransactionRecordList(String transactionStatus) {
        disposable.add(ServerManger.getInstance().getServer()
                .getTransactionRecordList(SharePreferencesUtil.getInstance().getUserPhone(), transactionStatus)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<TransactionRecordVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TransactionRecordVo> data) {
                        if(mView!=null){
                            mView.setTradingRecordList(data.getData().getList(), transactionStatus);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.getTradingRecordsFailed("服务器异常", transactionStatus);
                        }
                    }
                }));
    }

    @Override
    public void getWithdrawal() {

        disposable.add(ServerManger.getInstance().getServer()
                .withdrawRechangeReword("WITHDRAWAL")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        if(mView!=null){
                            Log.i(TAG,data.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.showMessageTips(e.getMessage()+"");
                        }
                    }
                }));


    }

    @Override
    public void getPayment() {
        disposable.add(ServerManger.getInstance().getServer()
                .withdrawRechangeReword("PAY")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        if(mView!=null){
                            Log.i(TAG,data.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.showMessageTips(e.getMessage()+"");
                        }
                    }
                }));
    }

    @Override
    public void unsubscribe() {

    }
}
