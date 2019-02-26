package com.moyacs.canary.main.deal.order;

import com.moyacs.canary.bean.TransactionRecordVo;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class HoldOrderPresenter implements HoldOrderContract.HoldOrderPresenter{
    private HoldOrderContract.HoldOrderView mView;
    private CompositeDisposable disposable;
    public HoldOrderPresenter(HoldOrderContract.HoldOrderView view) {
        this.mView = view;
        disposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void getRecordList() {
        disposable.add(ServerManger.getInstance().getServer().getTransactionRecordList(SharePreferencesUtil.getInstance().getUserPhone(),
                "1")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<TransactionRecordVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TransactionRecordVo> data) {
                        if(mView!=null) {
                            mView.showRecordList(data.getData().getList());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null) {
                            mView.getRecordListFailed("服务器异常"+e.getMessage());
                        }
                    }
                }));

    }

    @Override
    public void closeOrder(Map<String, Object> map) {
        ServerManger.getInstance().getServer().transactionSell(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        if(mView!=null){
                            mView.closeOrderState(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.closeOrderState(false);
                            mView.showMessageTips(e.getMessage()+"");
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }
}
