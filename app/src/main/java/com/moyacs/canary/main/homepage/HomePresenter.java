package com.moyacs.canary.main.homepage;

import android.util.Log;

import com.moyacs.canary.bean.BannerJsons;
import com.moyacs.canary.bean.HomeDealChanceBean;
import com.moyacs.canary.bean.HomeDealChanceVo;
import com.moyacs.canary.bean.TradeVo;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.HttpResponse;
import com.moyacs.canary.network.ResponseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 首页接口平台
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class HomePresenter implements HomeContract.HomePresenter{

    private static final String TAG =HomePresenter.class.getName();
    private HomeContract.HomeView mView;
    private CompositeDisposable disposable;
    public HomePresenter(HomeContract.HomeView view) {
        this.mView = view;
        disposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void getBannerList() {
        disposable.add(ServerManger.getInstance().getServer().getBannerList("0", "0")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<BannerJsons>>() {
                    @Override
                    protected void requestSuccess(ServerResult<BannerJsons> data) {
                        mView.setBannerList(data.getData().getList());
                    }
                }));
    }

    @Override
    public void getDealChanceList() {
//        disposable.add(ServerManger.getInstance().getServer().getDealChanceList()
//                .compose(RxUtils.rxSchedulerHelper())
//                .subscribeWith(new BaseMoaObservable<HttpResult<List<DealChanceBean>>>() {
//                    @Override
//                    protected void requestSuccess(HttpResult<List<DealChanceBean>> data) {
//                        if(mView!=null){
//                            mView.setDealChanceList(data.getDataObject());
//                            mView.refreshFinish();
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        super.onComplete();
//                        mView.refreshFinish();
//                    }
//                }));


        disposable.add(ServerManger.getInstance().getServer().tradingOpportunitiesList()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new ResponseObservable<HttpResponse<HomeDealChanceVo>>() {
                    @Override
                    protected void requestSuccess(HttpResponse<HomeDealChanceVo> data) {
                        if(mView!=null&&data!=null){
                            mView.refreshFinish();

                            //test
                            List<HomeDealChanceBean> list = new ArrayList<>();
                            HomeDealChanceBean beans = new HomeDealChanceBean();
                            beans.setCreateTime("02-27");
                            beans.setOperatingMode("原油");
                            beans.setRisePercentage(80);
                            beans.setThemeText("美日短线暴跌");
                            beans.setTitle("建议覅的设备覅但是覅士大夫");
                            beans.setTechnologicalAnalysisImg("http://img.mp.itc.cn/upload/20160730/afcfbf496451438faa9c6cdb5396f862_th.jpg");
                            beans.setUserName("高老师");
                            list.add(beans);
                            list.add(beans);
                            list.add(beans);
                            mView.setDealChanceList(list);

//                            HomeDealChanceVo bean =data.getData();
//                            if(bean!=null||bean.getList()!=null){
//                                mView.setDealChanceList(bean.getList());
//                            }
                        }
                    }
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(mView!=null){
                            mView.refreshFinish();
                        }
                    }
                }));

    }

    @Override
    public void getTradList() {
        disposable.add(ServerManger.getInstance().getServer().getTradList("0", "0")
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<TradeVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<TradeVo> data) {
                        mView.setTradList(data.getData().getList());
                    }
                }));
    }
}
