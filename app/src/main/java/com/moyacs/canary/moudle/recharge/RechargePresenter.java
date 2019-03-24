package com.moyacs.canary.moudle.recharge;


import android.text.TextUtils;
import android.util.Log;

import com.moyacs.canary.bean.PayBean;
import com.moyacs.canary.bean.RateBean;
import com.moyacs.canary.bean.UserAmountVo;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.RateObservable;
import com.moyacs.canary.network.RateResult;
import com.moyacs.canary.network.ResponseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerOtherManager;
import com.moyacs.canary.network.ServerResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;
import retrofit2.Response;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public class RechargePresenter implements RechargeContract.Presenter{

    private static final String TAG =RechargePresenter.class.getName();

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
        list.add("3");
        list.add("10");
        list.add("50");
        list.add("100");
        list.add("300");
        list.add("500");
        list.add("800");
        list.add("2000");
        mView.showRechargeAmount(list);


    }

    @Override
    public void getRate(String price) {
        ServerOtherManager.getInstance().initUrl(HttpConstants.SERVER_RATE_HOST);
        try {
            disposable.add(ServerOtherManager.getInstance().getServer().rate("finance.rate","USD","CNY","40626","2cb664dfcb09f13e0ea92d51125cb9bb","json").compose(RxUtils.rxSchedulerHelper()).subscribeWith(new RateObservable<RateResult<RateBean>>() {
                @Override
                protected void requestSuccess(RateResult<RateBean> data) {
                    if(mView!=null){
                        mView.showTotalRMBAcount(price,data.getResult().getRate());
                    }
                }
            }));
        }catch (Exception e){

        }

//        //由于返回的数据与异常数据 不一致 改为原始解析
//        try {
//            disposable.add(ServerOtherManager.getInstance().getServer().rate("finance.rate","USD","CNY","41041","2b57bc5d7946f80858084ad938b7f117","json").compose(RxUtils.rxSchedulerHelper()).subscribe(new Consumer<Response<Object>>() {
//                @Override
//                public void accept(Response<Object> objectResponseObservable) throws Exception {
//
//                   int state = objectResponseObservable.code();
//                   if(state==200){
//                       String result = objectResponseObservable.body().toString();
//                       if(!TextUtils.isEmpty(result)){
//                           try {
//                               JSONObject object =new JSONObject(result);
//                               if(!object.isNull("success")){
//                                   if(object.getInt("success")==1){
//                                       JSONObject mJSONObject =object.getJSONObject("result");
//                                       if(!mJSONObject.isNull("rate")){
//                                           String rate =mJSONObject.getString("rate");
//                                           if(mView!=null){
//                                                mView.showTotalRMBAcount(price,rate);
//                                           }
//                                       }
//                                   }else {
//                                       mView.showMessageTips("实时汇率为空");
//                                   }
//                               }else {
//                                   mView.showMessageTips("实时汇率异常");
//                               }
//                           } catch (JSONException e) {
//                               e.printStackTrace();
//                           }
//                       }
//                   }
//
//                }
//            }));
//        }catch (Exception e){
//
//        }


    }

    @Override
    public void unsubscribe() {

    }
}
