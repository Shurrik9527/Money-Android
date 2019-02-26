package com.moyacs.canary.main.homepage;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;
import com.moyacs.canary.bean.BannerBean;
import com.moyacs.canary.bean.DealChanceBean;
import com.moyacs.canary.bean.TradeVo;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 首页契约
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public interface HomeContract {

    interface HomeView extends BaseViews<HomePresenter> {
        /**
         * 获取 banner 列表成功
         *
         * @param result
         */
        void setBannerList(List<BannerBean> result);

        /**
         * 获取 交易机会 列表成功
         *
         * @param result
         */
        void setDealChanceList(List<DealChanceBean> result);

        /**
         * 获取可以交易的外汇列表
         *
         * @param list 外汇列表
         */
        void setTradList(List<TradeVo.Trade> list);

        /**
         * 结束刷新
         */
        void refreshFinish();
    }


    interface HomePresenter extends BasePresenter {
        /**
         * banner 列表
         */
        void getBannerList();

        /**
         * 交易机会列表
         */
        void getDealChanceList();

        /**
         * 获取可以交易列表
         */
        void getTradList();
    }

}
