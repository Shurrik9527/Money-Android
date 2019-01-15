package com.moyacs.canary.main.market.contract;

import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.ToastUtils;

import io.reactivex.disposables.CompositeDisposable;

public class OptionalPresenter implements OptionalContract.Presenter {
    private OptionalContract.View view;
    private CompositeDisposable disposable;

    public OptionalPresenter(OptionalContract.View view) {
        this.view = view;
        disposable = new CompositeDisposable();
    }

    @Override
    public void getMyChoice() {
        disposable.add(ServerManger.getInstance().getServer()
                .getMyChoiceList()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<TradeVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TradeVo> data) {
                        view.setMyChoice(data.getData().getList());
                    }
                }));
    }

    @Override
    public void getOptionalList() {
        disposable.add(ServerManger.getInstance().getServer()
                .getTradList("0", "0")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<TradeVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TradeVo> data) {
                        view.setOptionalList(data.getData().getList());
                    }
                }));
    }

    @Override
    public void addOptional(String symbolCode) {
        view.showLoadingDialog();
        disposable.add(ServerManger.getInstance().getServer().saveOptional(symbolCode)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        view.addOptionalSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showShort("添加失败");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        view.dismissLoadingDialog();
                    }
                }));
    }

    @Override
    public void deleteOptional(String symbolCode) {
        view.showLoadingDialog();
        disposable.add(ServerManger.getInstance().getServer().deleteOptional(symbolCode)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        view.deleteOptionalSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showShort("删除失败");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        view.dismissLoadingDialog();
                    }
                }));
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }
}
