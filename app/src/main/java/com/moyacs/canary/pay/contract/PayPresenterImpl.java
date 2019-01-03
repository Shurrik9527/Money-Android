package com.moyacs.canary.pay.contract;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonObject;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;

/**
 * 作者：luoshen on 2018/4/15 0015 10:51
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class PayPresenterImpl implements PayCountract.PayPresenter, PayCountract.SubmitOrderRequestListener {


    private PayCountract.PayView view;

    private PayCountract.PayModul modul;

    public PayPresenterImpl(PayCountract.PayView view) {
        this.view = view;
        modul = new PayModulImpl(this);
    }

    @Override
    public void unsubscribe() {
        modul.unsubscribe();
    }

    @Override
    public void submitOrder(String server, int mt4id, String symbol, String type, int volume, double
            sl, double tp, String ticket, double price, String expiredDate) {
        modul.submitOrder(server, mt4id, symbol, type, volume, sl, tp, ticket, price, expiredDate);
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
    public void submitOrderResponseSucessed(HttpResult<Object> result) {
        int result1 = result.getCode();
        String s = result.toString();
        Log.i("xxx", "submitOrderResponseSucessed:     " + s);
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.submitOrderSucess(result.getDataObject());
        } else {
            view.submitOrderFailed("服务器返回数据错误");
        }
    }

    @Override
    public void submitOrderResponseFailed(String errormsg) {
        view.submitOrderFailed(errormsg);
    }

}
