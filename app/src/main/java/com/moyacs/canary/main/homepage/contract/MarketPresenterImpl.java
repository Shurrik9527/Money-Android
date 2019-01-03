package com.moyacs.canary.main.homepage.contract;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.main.homepage.net.BannerDate;
import com.moyacs.canary.main.homepage.net.DealChanceDate;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;

import java.util.List;

/**
 * 作者：luoshen on 2018/3/7 0007 10:17
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class MarketPresenterImpl implements MarketContract.MarketPresenter, MarketContract.MarketListRequestListener {

    private MarketContract.MarketView view;

    private MarketContract.MarketModul modul;

    public MarketPresenterImpl(MarketContract.MarketView view) {
        this.view = view;
        modul = new MarketMudulImpl(this);
    }

    @Override
    public void unsubscribe() {
        modul.unsubscribe();
    }

    @Override
    public void getMarketList(String server,String type) {
        modul.getMarketList(server,type);
    }

    @Override
    public void getBannerList(int size) {
        modul.getBannerList(size);
    }

    @Override
    public void getDealChanceList(int size, int page) {
        modul.getDealChanceList(size, page);
    }

    @Override
    public void beforeRequest() {
        view.showLoadingDailog();
    }

    @Override
    public void afterRequest() {
        view.dismissLoadingDialog();
    }

    @Override
    public void getMarketListResponseSucessed(HttpResult<List<MarketDataBean>> result) {
        if (result == null) {
            view.getMarketListFailed("数据为空");
        }
        int result1 = result.getCode();
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.getMarketListSucessed(result.getDataObject());
        } else {
            view.getMarketListFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getMarketListResponseFailed(String errormsg) {
        view.getMarketListFailed(errormsg);
    }

    @Override
    public void getBannerListResponseSucessed(ServerResult<BannerDate> result) {
        if (result == null) {
            view.getBannerListFailed("数据为空");
        }
        int result1 = result.getMsgCode();
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.getBannerListSucessed(result.getData().getList());
        } else {
            view.getBannerListFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getBannerListResponseFailed(String errormsg) {
        view.getBannerListFailed(errormsg);
    }

    @Override
    public void getDealChanceListResponseSucessed(HttpResult<List<DealChanceDate>> result) {
        if (result == null) {
            view.getDealChanceListResponseFailed("数据为空");
        }
        int result1 = result.getCode();
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.getDealChanceListResponseSucessed(result.getDataObject());
        } else {
            view.getDealChanceListResponseFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getDealChanceListResponseFailed(String errormsg) {
        view.getDealChanceListResponseFailed(errormsg);
    }

    @Override
    public void doOnNext(List<MarketDataBean> result) {
        view.doOnNext(result);
    }
}
