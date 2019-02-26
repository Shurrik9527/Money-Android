package com.moyacs.canary.main.market;

import com.moyacs.canary.bean.TradeVo;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 行情接口平台
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class MarketPresenter implements MarketContract.MarketPresenter{


    private MarketContract.MarketView view;
    private CompositeDisposable disposable;

    public MarketPresenter(MarketContract.MarketView view) {
        this.view = view;
        disposable = new CompositeDisposable();
        view.setPresenter(this);
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    public void getMyChoiceList() {
        disposable.add(ServerManger.getInstance().getServer().getMyChoiceList()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<TradeVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TradeVo> data) {
                        view.setMyChoiceList(data.getData().getList());
                    }

                    @Override
                    protected void onStart() {
                        super.onStart();
                        view.showLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        view.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getMyChoiceListFiled(e.getMessage());
                    }
                }));
    }


    @Override
    public void getTradeList() {
        disposable.add(ServerManger.getInstance().getServer().getTradList("0", "0")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<TradeVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TradeVo> data) {
                        view.setTradList(data.getData().getList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getTradListFiled("服务器异常");
                    }
                }));
    }
}
