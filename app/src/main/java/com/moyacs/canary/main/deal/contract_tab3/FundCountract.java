package com.moyacs.canary.main.deal.contract_tab3;

import com.moyacs.canary.base.BaseModul;
import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseRequestListener;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.main.deal.net_tab2.ChiCangDateBean;
import com.moyacs.canary.main.deal.net_tab3.FundDataBean;
import com.moyacs.canary.main.deal.net_tab3.PaymentDateBean;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.deal.net_tab3.UserAmountVo;
import com.moyacs.canary.main.deal.net_tab3.WithdrawalDataBean;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerResult;

import java.util.List;

import retrofit2.http.Query;

/**
 * 作者：luoshen on 2018/4/18 0018 11:33
 * 邮箱：rsf411613593@gmail.com
 * 说明：资金页面相关接口封装
 */

public interface FundCountract {
    interface FundView extends BaseView {
        /**
         * 获取资金成功
         *
         * @param result
         */
        void getFundSucess(UserAmountVo result);

        /**
         * 获取资金失败
         *
         * @param errormsg
         */
        void getFundFailed(String errormsg);

        /**
         * 获取出金成功
         *
         * @param result
         */
        void getWithdrawalSucess(WithdrawalDataBean result);

        /**
         * 获取出金失败
         *
         * @param errormsg
         */
        void getWithdrawalFailed(String errormsg);

        /**
         * 获取入金成功
         *
         * @param result
         */
        void getPaymentSucess(PaymentDateBean result);

        /**
         * 获取入金失败
         *
         * @param errormsg
         */
        void getPaymentFailed(String errormsg);

        /**
         * 获取交易记录成功
         *
         * @param result
         */
        void getTradingRecordsSucessed(List<TransactionRecordVo.Record> result);

        /**
         * 获取交易记录失败
         *
         * @param errormsg
         */
        void getTradingRecordsFailed(String errormsg);

        void setTransactionRecordList(List<TransactionRecordVo.Record> list, String typ);

        void setTransactionRecordsListFailed(String errormsg, String type);
    }

    interface FundPresenter extends BasePresenter {
        /**
         * 获取资金相关
         *
         * @param mt4id MT4ID
         */
        void getFund(int mt4id);

        /**
         * 获取入金记录
         *
         * @param mt4id
         * @param startDate
         * @param endDate
         * @param pageSize
         * @param pageNumber
         */
        void getWithdrawal(int mt4id,
                           String startDate,
                           String endDate,
                           int pageSize,
                           int pageNumber);

        /**
         * 获取出金记录
         *
         * @param mt4id
         * @param startDate
         * @param endDate
         * @param pageSize
         * @param pageNumber
         */
        void getPayment(int mt4id,
                        String startDate,
                        String endDate,
                        int pageSize,
                        int pageNumber);

        /**
         * 获取交易记录
         *
         * @param mt4id
         * @param server    DEMO  LIVE
         * @param startDate 开始时间
         * @param endDate   结束时间呢
         */
        void getTradingRecords(int mt4id,
                               String server,
                               String startDate,
                               String endDate);

        void getTransactionRecordList(String transactionStatus);
    }

    interface FundModul extends BaseModul {
        /**
         * 获取资金相关
         *
         * @param mt4id MT4ID
         */
        void getFund(int mt4id);

        /**
         * 获取入金记录
         *
         * @param mt4id
         * @param startDate
         * @param endDate
         * @param pageSize
         * @param pageNumber
         */
        void getWithdrawal(int mt4id,
                           String startDate,
                           String endDate,
                           int pageSize,
                           int pageNumber);

        /**
         * 获取出金记录
         *
         * @param mt4id
         * @param startDate
         * @param endDate
         * @param pageSize
         * @param pageNumber
         */
        void getPayment(int mt4id,
                        String startDate,
                        String endDate,
                        int pageSize,
                        int pageNumber);

        /**
         * 获取交易记录
         *
         * @param mt4id
         * @param server    DEMO  LIVE
         * @param startDate 开始时间
         * @param endDate   结束时间呢
         */
        void getTradingRecords(int mt4id,
                               String server,
                               String startDate,
                               String endDate);

        /**
         * 获取记录
         *
         * @param transactionStatus 0除挂单外所有 1持仓 3挂单（已成功的则归到持仓）
         */
        void getTransactionRecordList(String transactionStatus);
    }

    interface GetFundRequestListener extends BaseRequestListener {

        /**
         * 资金相关成功
         *
         * @param result
         */
        void getFundResponseSucessed(ServerResult<UserAmountVo> result);

        /**
         * 资金相关失败
         *
         * @param errormsg
         */
        void getFundtResponseFailed(String errormsg);

        /**
         * 获取出金列表成功
         *
         * @param result
         */
        void getWithdrawalResponseSucessed(HttpResult<WithdrawalDataBean> result);

        /**
         * 获取出金列表失败
         *
         * @param errormsg
         */
        void getWithdrawalResponseFailed(String errormsg);

        /**
         * 获取入金列表成功
         *
         * @param result
         */
        void getPaymentResponseSucessed(HttpResult<PaymentDateBean> result);

        /**
         * 获取入金列表失败
         *
         * @param errormsg
         */
        void getPaymentResponseFailed(String errormsg);

        /**
         * 获取交易记录成功
         *
         * @param result
         */
        void getTradingRecordsSucessed(ServerResult<TransactionRecordVo> result);

        /**
         * 获取交易记录失败
         *
         * @param errormsg
         */
        void setTransactionRecordsListFailed(String errormsg, String type);


        /**
         * 设置交易记录
         *
         * @param list
         */
        void setTransactionRecordList(List<TransactionRecordVo.Record> list, String type);
    }
}
