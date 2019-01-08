package com.moyacs.canary.main.market.contract;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;

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
    public void getMarketList(String username, String server) {
        modul.getMarketList(username, server);
    }

    @Override
    public void getMarketList_type(String server, String type) {
        modul.getMarketList_type(server, type);
    }

    @Override
    public void getTradeList() {
        modul.getTradeList();
    }

    @Override
    public void beforeRequest() {
        view.showLoadingDialog();
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
        Log.i("getMarketListResponse", "getMarketListResponseSucessed:  " + result.toString());
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
    public void getMarketListResponseSucessed_type(HttpResult<List<MarketDataBean>> result) {
        if (result == null) {
            view.getMarketListFailed_type("数据为空");
        }
        int result1 = result.getCode();
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.getMarketListSucessed_type(result.getDataObject());
        } else {
            view.getMarketListFailed_type("服务器返回数据错误");
        }
    }

    @Override
    public void getMarketListResponseFailed_type(String errormsg) {
        view.getMarketListFailed_type(errormsg);
    }

    @Override
    public void getTradeList(List<TradeVo.Trade> list) {
        view.getTradList(list);
    }
}
