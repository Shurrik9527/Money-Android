package com.moyacs.canary.moudle.recharge;


import com.moyacs.canary.bean.PayBean;
import com.moyacs.canary.bean.UserAmountVo;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public class RechargePresenter implements RechargeContract.Presenter{


    private final RechargeContract.View mView;
    private CompositeDisposable disposable;
    public RechargePresenter(RechargeContract.View view) {
        mView = view;
        mView.setPresenter(this);
        disposable = new CompositeDisposable();
    }

    @Override
    public void rechargePay(String type, String url, String amount) {


        disposable.add(ServerManger.getInstance().getServer().rechargePay(type,url,amount)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<PayBean>>() {
                    @Override
                    protected void requestSuccess(ServerResult<PayBean> data) {
                        if(mView!=null){
                            mView.showPayResult(data.getData());
                        }
                    }
                }));

    }

    @Override
    public void getRechargeAmount() {

        List<String> list = new ArrayList<>();
        list.add("100");
        list.add("200");
        list.add("500");
        list.add("1000");
        list.add("2000");
        list.add("5000");
        mView.showRechargeAmount(list);



    }

    @Override
    public void unsubscribe() {

    }
}
