package com.moyacs.canary.main.homepage.profitrank.profitdetail;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 盈利榜详情接口
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ProfitDetailPresenter implements ProfitDetailContract.Presenter{

    private ProfitDetailContract.View mView;
    private CompositeDisposable disposable;
    public ProfitDetailPresenter(ProfitDetailContract.View view) {
        this.mView = view;
        disposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void getProfitDetail() {

    }
}
