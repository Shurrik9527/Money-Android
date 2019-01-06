package com.moyacs.canary.pay.contract;

import com.moyacs.canary.base.BaseModul;
import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseRequestListener;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerResult;

import java.util.List;
import java.util.Map;

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
         */
        void submitOrder(Map<String, Object> map);

    }

    interface PayModul extends BaseModul {
        /**
         * 下单
         */
        void submitOrder(Map<String, Object> map);
    }

    interface SubmitOrderRequestListener extends BaseRequestListener {

        /**
         * 下单成功
         */
        void submitOrderResponseSucessed(ServerResult<String> result);

        /**
         * 下单失败
         *
         * @param errormsg
         */
        void submitOrderResponseFailed(String errormsg);
    }
}
