package com.moyacs.canary.main.market.contract;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.TradeVo;

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
         */
        void setMarketOptionalList(List<MarketDataBean> result);

        /**
         * 获取自选列表失败
         */
        void getMarketOptionalListFiled(String msg);

        /**
         * 获取行情列表成功
         */
        void setMarketTypeList(List<MarketDataBean> result);

        /**
         * 获取行情列表失败
         */
        void getMarkTypeListFiled(String msg);

        /**
         * 获取可交易列表
         */
        void setTradList(List<TradeVo.Trade> list);

        /**
         * 获取可交易列表失败
         */
        void getTradListFiled(String msg);
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
}
