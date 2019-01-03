package com.moyacs.canary.pay.tixian.contract;

import com.moyacs.canary.base.BaseModul;
import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseRequestListener;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.network.HttpResult;

/**
 * 作者：luoshen on 2018/4/15 0015 10:44
 * 邮箱：rsf411613593@gmail.com
 * 说明：挂单，买涨，买跌   提现
 */

public interface WithdrawCountract {

    interface WithdrawView extends BaseView {


        /**
         * 提现成功
         *
         * @param result
         */
        void withdrawSucess(Object result);

        /**
         * 提现失败
         *
         * @param errormsg
         */
        void withdrawFailed(String errormsg);




    }

    interface WithdrawPresenter extends BasePresenter {

        /**
         *  提现接口
         * @param mt4id
         * @param type  WireTransfer 暂时写死
         * @param amount 金额
         * @param accountNumber 账号
         * @param accountName   账户名
         * @param bankName  发卡行
         * @param bankAddress   开户地址
         */
        void withdraw(int mt4id,
                      String type,
                      double amount,
                      String accountNumber,
                      String accountName,
                      String bankName,
                      String bankAddress);
    }

    interface WithdrawModul extends BaseModul {

        /**
         *  提现接口
         * @param mt4id
         * @param type  WireTransfer 暂时写死
         * @param amount 金额
         * @param accountNumber 账号
         * @param accountName   账户名
         * @param bankName  发卡行
         * @param bankAddress   开户地址
         */
        void withdraw(int mt4id,
                      String type,
                      double amount,
                      String accountNumber,
                      String accountName,
                      String bankName,
                      String bankAddress);
    }

    interface WithdrawRequestListener extends BaseRequestListener {

        /**
         * 提现成功
         *
         * @param result
         */
        void withdrawResponseSucessed(HttpResult<Object> result);

        /**
         * 提现失败
         *
         * @param errormsg
         */
        void withdrawResponseFailed(String errormsg);

    }
}
