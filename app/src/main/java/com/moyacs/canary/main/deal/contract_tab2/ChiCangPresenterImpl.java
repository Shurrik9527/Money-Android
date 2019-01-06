package com.moyacs.canary.main.deal.contract_tab2;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.main.deal.contract_tab2.ChiCangCountract.ChiCangPresenter;
import com.moyacs.canary.main.deal.net_tab2.ChiCangDateBean;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerResult;

import java.util.List;

/**
 * 作者：luoshen on 2018/4/17 0017 09:37
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class ChiCangPresenterImpl implements ChiCangPresenter, ChiCangCountract.GetChiCangListRequestListener {
    private ChiCangCountract.ChiCangView view;
    private ChiCangCountract.ChiCangModul modul;

    public ChiCangPresenterImpl(ChiCangCountract.ChiCangView view) {
        this.view = view;
        modul = new ChiCangModulImpl(this);
    }

    @Override
    public void unsubscribe() {
        modul.unsubscribe();
    }

    @Override
    public void getChiCangList() {
        modul.getChiCangList();
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
    public void getChiCangListResponseSucessed(ServerResult<TransactionRecordVo> result) {
        if (result == null) {
            view.getChiCangListFailed("数据为空");
        }
        if (result.isSuccess()) {
            view.getChiCangListSucess(result.getData().getList());
        } else {
            view.getChiCangListFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getChiCangListResponseFailed(String errormsg) {
        view.getChiCangListFailed(errormsg);
    }
}
