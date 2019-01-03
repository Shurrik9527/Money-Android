package com.moyacs.canary.main.market.contract;

import com.moyacs.canary.base.BaseModul;
import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseRequestListener;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.network.HttpResult;

import java.util.List;

/**
 * 作者：luoshen on 2018/3/5 0005 15:21
 * 邮箱：rsf411613593@gmail.com
 * 说明：约束类， MVP 三层合一 ，方便查看业务逻辑
 */

public interface MarketContract {

    interface MarketView extends BaseView {
        /**
         * 获取自选列表成功
         *
         * @param result
         */
        void getMarketListSucessed(List<MarketDataBean> result);

        /**
         * 获取自选列表失败
         *
         * @param errormsg
         */
        void getMarketListFailed(String errormsg);

        /**
         * 获取行情列表成功
         *
         * @param result
         */
        void getMarketListSucessed_type(List<MarketDataBean> result);

        /**
         * 获取行情列表失败
         *
         * @param errormsg
         */
        void getMarketListFailed_type(String errormsg);

        void getTradList(List<TradeVo.Trade> list);
    }

    interface MarketPresenter extends BasePresenter {
        /**
         * 获取自选列表
         */
        void getMarketList(String username, String server);

        /**
         * 获取行情列表
         *
         * @param server
         * @param type   1:外汇 2:贵金属 3:原油  4:全球指数
         */
        void getMarketList_type(String server, String type);

        /**
         * 获取可以交易列表
         */
        void getTradeList();
    }

    interface MarketModul extends BaseModul {
        /**
         * 获取自选列表
         */
        void getMarketList(String username, String server);

        /**
         * 获取行情列表
         *
         * @param server
         * @param type   1:外汇 2:贵金属 3:原油  4:全球指数
         */
        void getMarketList_type(String server, String type);

        /**
         * 获取可以交易的列表
         */
        void getTradeList();
    }

    interface MarketListRequestListener extends BaseRequestListener {

        /**
         * 行情自选请求成功
         *
         * @param result
         */
        void getMarketListResponseSucessed(HttpResult<List<MarketDataBean>> result);

        /**
         * 行情自选请求失败
         *
         * @param errormsg
         */
        void getMarketListResponseFailed(String errormsg);

        /**
         * 行情列表请求成功
         *
         * @param result
         */
        void getMarketListResponseSucessed_type(HttpResult<List<MarketDataBean>> result);

        /**
         * 行情列表请求失败
         *
         * @param errormsg
         */
        void getMarketListResponseFailed_type(String errormsg);

        void getTradeList(List<TradeVo.Trade> list);
    }
}
