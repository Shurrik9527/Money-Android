package com.moyacs.canary.main.deal.capital;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;
import com.moyacs.canary.bean.TransactionRecordVo;
import com.moyacs.canary.bean.UserAmountVo;


import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public interface CapitalContract {

    interface CapitalView extends BaseViews<CapitalPresenter> {
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

    interface CapitalPresenter extends BasePresenter {
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
