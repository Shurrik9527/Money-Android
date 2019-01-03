package com.moyacs.canary.pay.tixian.contract;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.pay.WithdrawActivity;

/**
 * 作者：luoshen on 2018/4/15 0015 10:51
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class WithdrawPresenterImpl implements WithdrawCountract.WithdrawPresenter, WithdrawCountract.WithdrawRequestListener {


    private WithdrawCountract.WithdrawView view;

    private WithdrawCountract.WithdrawModul modul;

    public WithdrawPresenterImpl(WithdrawActivity view) {
        this.view = view;
        modul = new WithdrawModulImpl(this);
    }

    @Override
    public void unsubscribe() {
        modul.unsubscribe();
    }



    @Override
    public void withdraw(int mt4id, String type, double amount, String accountNumber, String accountName, String bankName, String bankAddress) {
        modul.withdraw(mt4id, type, amount, accountNumber, accountName, bankName, bankAddress);
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
    public void withdrawResponseSucessed(HttpResult<Object> result) {
        int result1 = result.getCode();
        String s = result.toString();
        Log.i("xxx", "submitOrderResponseSucessed:     "+s);
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.withdrawSucess(result.getDataObject());
        } else {
            view.withdrawFailed("服务器返回数据错误");
        }
    }

    @Override
    public void withdrawResponseFailed(String errormsg) {
        view.withdrawFailed(errormsg);
    }
}
