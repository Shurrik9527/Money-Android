package com.moyacs.canary.product_fxbtg.contract_kline;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.product_fxbtg.contract_kline.ProductContract.ProductPresenter;
import com.moyacs.canary.product_fxbtg.net_kline.KLineData;

import java.util.List;

/**
 * 作者：luoshen on 2018/3/26 0026 13:18
 * 邮箱：rsf411613593@gmail.com
 * 说明：P 层 实现类
 */

public class ProductPresenterImpl implements ProductPresenter, ProductContract.ProductKLineRequestListener {

    private ProductContract.ProductView view;
    private ProductContract.ProductModul modul;

    public ProductPresenterImpl(ProductContract.ProductView view) {
        this.view = view;
        modul = new ProductModulImpl(this);
    }

    @Override
    public void unsubscribe() {
        modul.unsubscribe();
    }

    @Override
    public void getKLineData(String symbol, String startDate, String endDate, String period, String server) {
        modul.getKLineData(symbol, startDate, endDate, period, server);
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
    public void getKLineDataResponseSucessed(HttpResult<List<KLineData>> result) {
        if (result == null) {
            view.getKLineDataFailed("数据为空");
        }
        int result1 = result.getCode();
        Log.i("KLineDataSucessed", "getKLineDataResponseSucessed:  "+result.toString());
        LogUtils.d("服务器返回的数据字段  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.getKLineDataSucessed(result.getDataObject());
        } else {
            view.getKLineDataFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getKLineDataResponseFailed(String errormsg) {
        view.getKLineDataFailed(errormsg);
    }


}
