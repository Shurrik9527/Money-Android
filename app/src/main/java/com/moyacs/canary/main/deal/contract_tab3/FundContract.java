package com.moyacs.canary.main.deal.contract_tab3;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.deal.net_tab3.UserAmountVo;

import java.util.List;

/**
 * 作者：luoshen on 2018/4/18 0018 11:33
 * 邮箱：rsf411613593@gmail.com
 * 说明：资金页面相关接口封装
 */

public interface FundContract {
    interface FundView extends BaseView {
        /**
         * 获取资产成功
         */
        void setAccountInfo(UserAmountVo result);

        /**
         * 获取交易记录成功 包括挂单
         */
        void setTradingRecordList(List<TransactionRecordVo.Record> result, String typ);

        /**
         * 获取交易记录失败 包括挂单
         */
        void getTradingRecordsFailed(String msg, String typ);

        /**
         * 获取提现记录成功
         */
        void setWithdrawalList();

        /**
         * 获取提现失败
         */
        void getWithdrawalFailed(String msg);

        /**
         * 获取充值记录成功
         */
        void setPaymentList();

        /**
         * 获取充值记录失败
         */
        void getPaymentListFailed(String msg);
    }

    interface FundPresenter extends BasePresenter {
        /**
         * 获取资金相关
         */
        void getAccountInfo();

        /**
         * 获取交易记录
         */
        void getTransactionRecordList(String transactionStatus);

        /**
         * 获取充值记录
         */
        void getWithdrawal();

        /**
         * 获取提现记录
         */
        void getPayment();
    }
}
