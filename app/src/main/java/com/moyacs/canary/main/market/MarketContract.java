package com.moyacs.canary.main.market;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;
import com.moyacs.canary.bean.TradeVo;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public interface MarketContract {


    interface MarketView extends BaseViews<MarketPresenter> {
        /**
         * 获取自选列表成功
         */
        void setMyChoiceList(List<TradeVo.Trade> result);

        /**
         * 获取自选列表失败
         */
        void getMyChoiceListFiled(String msg);

        /**
         * 获取可交易列表
         */
        void setTradList(List<TradeVo.Trade> list);

        /**
         * 获取可交易列表失败
         */
        void getTradListFiled(String msg);

        void refreshComplete();

    }

    interface MarketPresenter extends BasePresenter {
        /**
         * 获取自选列表
         */
        void getMyChoiceList();

        /**
         * 获取可以交易列表
         */
        void getTradeList();
    }
}
