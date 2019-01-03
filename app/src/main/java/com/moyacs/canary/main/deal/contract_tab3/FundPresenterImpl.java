package com.moyacs.canary.main.deal.contract_tab3;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.main.deal.net_tab2.ChiCangDateBean;
import com.moyacs.canary.main.deal.net_tab3.FundDataBean;
import com.moyacs.canary.main.deal.net_tab3.PaymentDateBean;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.deal.net_tab3.UserAmountVo;
import com.moyacs.canary.main.deal.net_tab3.WithdrawalDataBean;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerResult;

import java.util.List;

/**
 * 作者：luoshen on 2018/4/18 0018 11:45
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class FundPresenterImpl implements FundCountract.FundPresenter, FundCountract.GetFundRequestListener {

    private FundCountract.FundView view;
    private FundCountract.FundModul modul;

    public FundPresenterImpl(FundCountract.FundView view) {
        this.view = view;
        modul = new FundModulImpl(this);

    }

    @Override
    public void unsubscribe() {
        modul.unsubscribe();
    }

    @Override
    public void getFund(int mt4id) {
        modul.getFund(mt4id);
    }

    @Override
    public void getWithdrawal(int mt4id, String startDate, String endDate, int pageSize, int pageNumber) {
        modul.getWithdrawal(mt4id, startDate, endDate, pageSize, pageNumber);
    }

    @Override
    public void getPayment(int mt4id, String startDate, String endDate, int pageSize, int pageNumber) {
        modul.getPayment(mt4id, startDate, endDate, pageSize, pageNumber);
    }

    @Override
    public void getTradingRecords(int mt4id, String server, String startDate, String endDate) {
        modul.getTradingRecords(mt4id, server, startDate, endDate);
    }

    @Override
    public void beforeRequest() {
        view.showLoadingDailog();
    }

    @Override
    public void afterRequest() {
        view.dismissLoadingDialog();
    }

    @Override
    public void getFundResponseSucessed(ServerResult<UserAmountVo> result) {
        if (result == null) {
            view.getFundFailed("数据为空");
        }
        int result1 = result.getMsgCode();
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        Log.i("ChiCangPresenterImpl", "result.toString()  : " + result.toString());
        if (result.isSuccess()) {
            view.getFundSucess(result.getData());
        } else {
            view.getFundFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getFundtResponseFailed(String errormsg) {
        view.getFundFailed(errormsg);
    }

    @Override
    public void getWithdrawalResponseSucessed(HttpResult<WithdrawalDataBean> result) {
        if (result == null) {
            view.getWithdrawalFailed("数据为空");
        }
        int result1 = result.getCode();
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        Log.i("ChiCangPresenterImpl", "result.toString()  : " + result.toString());
        if (result1 == HttpConstants.result_sucess) {
            view.getWithdrawalSucess(result.getDataObject());
        } else {
            view.getWithdrawalFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getWithdrawalResponseFailed(String errormsg) {
        view.getWithdrawalFailed(errormsg);
    }

    @Override
    public void getPaymentResponseSucessed(HttpResult<PaymentDateBean> result) {
        if (result == null) {
            view.getPaymentFailed("数据为空");
        }
        int result1 = result.getCode();
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        Log.i("ChiCangPresenterImpl", "result.toString()  : " + result.toString());
        if (result1 == HttpConstants.result_sucess) {
            view.getPaymentSucess(result.getDataObject());
        } else {
            view.getPaymentFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getPaymentResponseFailed(String errormsg) {
        view.getPaymentFailed(errormsg);
    }

    @Override
    public void getTradingRecordsSucessed(ServerResult<TransactionRecordVo> result) {
        view.dismissLoadingDialog();
        if (result == null) {
            view.getTradingRecordsFailed("数据为空");
        }
        int result1 = result.getMsgCode();
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        Log.i("ChiCangPresenterImpl", "result.toString()  : " + result.toString());
        if (result.isSuccess()) {
            view.getTradingRecordsSucessed(result.getData().getList());
        } else {
            view.getTradingRecordsFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getTradingRecordsFailed(String errormsg) {
        view.dismissLoadingDialog();
    }
}
