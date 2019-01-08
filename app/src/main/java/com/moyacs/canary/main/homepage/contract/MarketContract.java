package com.moyacs.canary.main.homepage.contract;

import com.moyacs.canary.base.BaseModul;
import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseRequestListener;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.main.homepage.net.BannerDate;
import com.moyacs.canary.main.homepage.net.DealChanceDate;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerResult;

import java.util.List;

/**
 * 作者：luoshen on 2018/3/5 0005 15:21
 * 邮箱：rsf411613593@gmail.com
 * 说明：约束类， MVP 三层合一 ，方便查看业务逻辑
 */

public interface MarketContract {

    interface MarketView extends BaseView {
        /**
         * 获取行情列表成功
         *
         * @param result
         */
        void setMarketList(List<MarketDataBean> result);

        /**
         * 获取 banner 列表成功
         *
         * @param result
         */
        void setBannerList(List<BannerDate.Banner> result);

        /**
         * 获取 交易机会 列表成功
         *
         * @param result
         */
        void setDealChanceList(List<DealChanceDate> result);

        /**
         * 获取可以交易的外汇列表
         *
         * @param list 外汇列表
         */
        void setTradList(List<TradeVo.Trade> list);
    }

    interface MarketPresenter extends BasePresenter {
        /**
         * 获取行情列表
         */
        void getMarketList(String server, String type);

        /**
         * banner 列表
         */
        void getBannerList();

        /**
         * 交易机会列表
         *
         * @param size
         * @param page
         */
        void getDealChanceList(int size, int page);

        void getTradList();
    }
}
