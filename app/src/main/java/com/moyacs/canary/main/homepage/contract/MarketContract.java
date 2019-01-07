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
        void getMarketListSucessed(List<MarketDataBean> result);

        /**
         * 获取行情列表失败
         *
         * @param errormsg
         */
        void getMarketListFailed(String errormsg);

        /**
         * 获取 banner 列表成功
         *
         * @param result
         */
        void getBannerListSucessed(List<BannerDate.Banner> result);

        /**
         * 获取 banner 列表失败
         *
         * @param errormsg
         */
        void getBannerListFailed(String errormsg);

        /**
         * 获取 交易机会 列表成功
         *
         * @param result
         */
        void getDealChanceListResponseSucessed(List<DealChanceDate> result);

        /**
         * 获取 交易机会 列表失败
         *
         * @param errormsg
         */
        void getDealChanceListResponseFailed(String errormsg);

        /**
         * 对数据进行二次处理 所有行情列表
         *
         * @param result
         */
        void doOnNext(List<MarketDataBean> result);

        /**
         * 获取可以交易的外汇列表
         *
         * @param list 外汇列表
         */
        void getTradListSuccess(List<TradeVo.Trade> list);

        /**
         * 可交易的外汇列表失败
         *
         * @param filedMsg 失败信息
         */
        void getTradListFiled(String filedMsg);
    }

    interface MarketPresenter extends BasePresenter {
        /**
         * 获取行情列表
         */
        void getMarketList(String server, String type);

        /**
         * banner 列表
         *
         * @param size
         */
        void getBannerList(int size);

        /**
         * 交易机会列表
         *
         * @param size
         * @param page
         */
        void getDealChanceList(int size, int page);

        void getTradList();

    }

    interface MarketModul extends BaseModul {
        /**
         * 获取行情列表
         */
        void getMarketList(String server, String type);

        /**
         * banner 列表
         *
         * @param size
         */
        void getBannerList(int size);

        /**
         * 交易机会列表
         *
         * @param size
         * @param page
         */
        void getDealChanceList(int size, int page);

        void getTradList();
    }

    interface MarketListRequestListener extends BaseRequestListener {

        /**
         * 行情列表请求成功
         *
         * @param result
         */
        void getMarketListResponseSucessed(HttpResult<List<MarketDataBean>> result);

        /**
         * 行情列表请求失败
         *
         * @param errormsg
         */
        void getMarketListResponseFailed(String errormsg);

        /**
         * 获取 banner 列表成功
         *
         * @param result
         */
        void getBannerListResponseSucessed(ServerResult<BannerDate> result);

        /**
         * 获取 banner 列表失败
         *
         * @param errormsg
         */
        void getBannerListResponseFailed(String errormsg);

        /**
         * 获取 交易机会 列表成功
         *
         * @param result
         */
        void getDealChanceListResponseSucessed(HttpResult<List<DealChanceDate>> result);

        /**
         * 获取 交易机会 列表失败
         *
         * @param errormsg
         */
        void getDealChanceListResponseFailed(String errormsg);

        /**
         * 对数据进行二次处理 所有行情列表
         *
         * @param result
         */
        void doOnNext(List<MarketDataBean> result);

        /**
         * 获取可以交易的外汇列表
         *
         * @param list 外汇列表
         */
        void getTradListSuccess(List<TradeVo.Trade> list);

        /**
         * 可交易的外汇列表失败
         *
         * @param filedMsg 失败信息
         */
        void getTradListFiled(String filedMsg);
    }
}
