/*
package com.moyacs.canary.main.market.contract;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.MarketServer;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.network.HttpExceptionHandler;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.HttpServerManager;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

*/
/**
 * 作者：luoshen on 2018/3/7 0007 10:18
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 *//*


public class MarketMudulImpl implements MarketContract.MarketModul {

    private MarketContract.MarketListRequestListener listener;
    private CompositeDisposable mCompositeDisposable;
    private final MarketServer marketServer;

    public MarketMudulImpl(MarketContract.MarketListRequestListener listener) {
        this.listener = listener;
        mCompositeDisposable = new CompositeDisposable();
        marketServer = HttpServerManager.getInstance().create(MarketServer.class);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void getMyChoiceList(String username, String server) {

    }

    @Override
    public void getMarketList(String page, String pageSize) {

    }

    @Override
    public void getTradeList() {

    }
}
*/
