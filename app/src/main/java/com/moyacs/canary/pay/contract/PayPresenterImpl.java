package com.moyacs.canary.pay.contract;

import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：luoshen on 2018/4/15 0015 10:51
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class PayPresenterImpl implements PayContract.PayPresenter {
    private PayContract.PayView view;
    private CompositeDisposable disposable;

    public PayPresenterImpl(PayContract.PayView view) {
        this.view = view;
        disposable = new CompositeDisposable();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void closeOrder(Map<String, Object> map) {
        view.showLoadingDialog();
        ServerManger.getInstance().getServer().transactionSell(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        view.closeOrderSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.closeOrderFailed("服务器异常");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        view.dismissLoadingDialog();
                    }
                });
    }
}
