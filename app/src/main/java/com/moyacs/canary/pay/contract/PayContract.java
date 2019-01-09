package com.moyacs.canary.pay.contract;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseView;

import java.util.Map;

/**
 * 作者：luoshen on 2018/4/15 0015 10:44
 * 邮箱：rsf411613593@gmail.com
 * 说明：挂单，买涨，买跌
 */
public interface PayContract {
    interface PayView extends BaseView {
        /**
         * 平仓成功
         */
        void closeOrderSuccess();

        /**
         * 平仓失败
         */
        void closeOrderFailed(String msg);
    }

    interface PayPresenter extends BasePresenter {
        /**
         * 下单
         */
        void closeOrder(Map<String, Object> map);

    }
}
