package com.moyacs.canary.main.homepage.profitrank.profitrate;

import com.moyacs.canary.bean.ProfitRateBean;
import com.moyacs.canary.bean.ProfitRateVo;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ProfitRatePresenter implements ProfitRateContract.Presenter{

    private ProfitRateContract.ProfitRateView  mView;
    private CompositeDisposable disposable;
    public ProfitRatePresenter(ProfitRateContract.ProfitRateView view) {
        this.mView = view;
        disposable = new CompositeDisposable();
        mView.setPresenter(this);
    }


    @Override
    public void getProfitRateList(boolean isrefresh) {

        ProfitRateBean bean = new ProfitRateBean();
        bean.setId("1");
        bean.setName("heguogui");
        bean.setRate("20");
        List<ProfitRateBean> mlists = new ArrayList<>();
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);
        ProfitRateVo vo = new ProfitRateVo();
        vo.setList(mlists);
        mView.showProfitRate(vo,isrefresh);

    }

    @Override
    public void unsubscribe() {

    }
}
