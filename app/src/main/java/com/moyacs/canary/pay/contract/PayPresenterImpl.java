package com.moyacs.canary.pay.contract;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonObject;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerResult;

import java.util.Map;

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
    public void submitOrder(Map<String, Object> map) {
        modul.submitOrder(map);
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
    public void submitOrderResponseSucessed(ServerResult<String> result) {
        if (result.isSuccess()) {
            view.submitOrderSucess(result.getMsg());
        } else {
            view.submitOrderFailed("服务器返回数据错误");
        }
    }

    @Override
    public void submitOrderResponseFailed(String errormsg) {
        view.submitOrderFailed(errormsg);
    }

}
