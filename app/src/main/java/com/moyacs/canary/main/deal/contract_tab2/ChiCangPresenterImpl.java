package com.moyacs.canary.main.deal.contract_tab2;

import com.moyacs.canary.main.deal.contract_tab2.ChiCangCountract.ChiCangPresenter;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 作者：luoshen on 2018/4/17 0017 09:37
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class ChiCangPresenterImpl implements ChiCangPresenter {
    private ChiCangCountract.ChiCangView view;
    private CompositeDisposable disposable;

    public ChiCangPresenterImpl(ChiCangCountract.ChiCangView view) {
        this.view = view;
        disposable = new CompositeDisposable();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void getRecordList() {
        disposable.add(ServerManger.getInstance().getServer().getTransactionRecordList(SharePreferencesUtil.getInstance().getUserPhone(),
                "1")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<TransactionRecordVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TransactionRecordVo> data) {
                        view.setRecordList(data.getData().getList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getRecordListFailed("服务器异常"+e.getMessage());
                    }
                }));
    }
}
