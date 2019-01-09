package com.moyacs.canary.main.market.contract;

import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.MarketServer;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.network.BaseMoaObservable;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.HttpServerManager;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 作者：luoshen on 2018/3/7 0007 10:17
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class MarketPresenterImpl implements MarketContract.MarketPresenter {
    private MarketContract.MarketView view;
    private CompositeDisposable disposable;
    private MarketServer marketServer;

    public MarketPresenterImpl(MarketContract.MarketView view) {
        this.view = view;
        disposable = new CompositeDisposable();
        marketServer = HttpServerManager.getInstance().create(MarketServer.class);
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void getMarketList(String username, String server) {
        disposable.add(marketServer.getMarketList_optional(username, server)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseMoaObservable<HttpResult<List<MarketDataBean>>>() {
                    @Override
                    protected void requestSuccess(HttpResult<List<MarketDataBean>> data) {
                        view.setMarketOptionalList(data.getDataObject());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getMarketOptionalListFiled("服务器异常");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        view.dismissLoadingDialog();
                    }
                }));
    }

    @Override
    public void getMarketList_type(String server, String type) {
        disposable.add(marketServer.getMarketList_type(server, type)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseMoaObservable<HttpResult<List<MarketDataBean>>>() {
                    @Override
                    protected void requestSuccess(HttpResult<List<MarketDataBean>> data) {
                        view.setMarketTypeList(data.getDataObject());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getMarkTypeListFiled("服务器异常");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        view.dismissLoadingDialog();
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
