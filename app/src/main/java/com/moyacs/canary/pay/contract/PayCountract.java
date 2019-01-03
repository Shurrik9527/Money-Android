package com.moyacs.canary.pay.contract;

import com.moyacs.canary.base.BaseModul;
import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseRequestListener;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.network.HttpResult;

import java.util.List;

import retrofit2.http.Field;

/**
 * 作者：luoshen on 2018/4/15 0015 10:44
 * 邮箱：rsf411613593@gmail.com
 * 说明：挂单，买涨，买跌
 */

public interface PayCountract {

    interface PayView extends BaseView {
        /**
         * 下单成功
         *
         * @param result
         */
        void submitOrderSucess(Object result);

        /**
         * 下单失败
         *
         * @param errormsg
         */
        void submitOrderFailed(String errormsg);






    }

    interface PayPresenter extends BasePresenter {
        /**
         * 下单
         *
         * @param server 交易环境  DEMO 或者 live
         * @param mt4id  MT4ID
         * @param symbol 品种代码
         * @param type   交易类型
         * @param volume 手数
         * @param sl     止损
         * @param tp     止盈
         * @param ticket 单号
         */
        void submitOrder(String server, int mt4id, String symbol, String type, int volume, double sl,
                         double tp, String ticket, double price, String expiredDate);

    }

    interface PayModul extends BaseModul {
        /**
         * 下单
         *
         * @param server 交易环境  DEMO 或者 live
         * @param mt4id  MT4ID
         * @param symbol 品种代码
         * @param type   交易类型
         * @param volume 手数
         * @param sl     止损
         * @param tp     止盈
         * @param ticket 单号
         */
        void submitOrder(String server, int mt4id, String symbol, String type, int volume, double sl,
                         double tp, String ticket, double price, String expiredDate);


    }

    interface SubmitOrderRequestListener extends BaseRequestListener {

        /**
         * 下单成功
         *
         * @param result
         */
        void submitOrderResponseSucessed(HttpResult<Object> result);

        /**
         * 下单失败
         *
         * @param errormsg
         */
        void submitOrderResponseFailed(String errormsg);


    }
}
