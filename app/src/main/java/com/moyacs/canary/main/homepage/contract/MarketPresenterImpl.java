package com.moyacs.canary.main.homepage.contract;

import com.moyacs.canary.main.homepage.net.BannerDate;
import com.moyacs.canary.main.homepage.net.DealChanceDate;
import com.moyacs.canary.main.homepage.net.HomePageServer;
import com.moyacs.canary.main.market.net.MarketDataBean;
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
    private MarketContract.MarketView mView;
    private CompositeDisposable disposable;
    private HomePageServer homePageServer;

    public MarketPresenterImpl(MarketContract.MarketView view) {
        this.mView = view;
        disposable = new CompositeDisposable();
        homePageServer = HttpServerManager.getInstance().create(HomePageServer.class);
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void getMarketList(String server, String type) {
        disposable.add(homePageServer.getMarketList(server, type)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseMoaObservable<HttpResult<List<MarketDataBean>>>() {
                    @Override
                    protected void requestSuccess(HttpResult<List<MarketDataBean>> data) {
                        mView.setMarketList(data.getDataObject());
                    }
                }));
    }

    @Override
    public void getBannerList() {
        disposable.add(ServerManger.getInstance().getServer().getBannerList("0", "0")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<BannerDate>>() {
                    @Override
                    protected void requestSuccess(ServerResult<BannerDate> data) {
                        mView.setBannerList(data.getData().getList());
                    }
                }));
    }

    @Override
    public void getDealChanceList() {
        disposable.add(homePageServer.getDealChanceList()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseMoaObservable<HttpResult<List<DealChanceDate>>>() {
                    @Override
                    protected void requestSuccess(HttpResult<List<DealChanceDate>> data) {
                        mView.setDealChanceList(data.getDataObject());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mView.refreshFinish();
                    }
                }));
    }

    @Override
    public void getTradList() {
        disposable.add(ServerManger.getInstance().getServer().getTradList("0", "0")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<TradeVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TradeVo> data) {
                        mView.setTradList(data.getData().getList());
                    }
                }));
    }
}
