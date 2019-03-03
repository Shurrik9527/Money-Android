package com.moyacs.canary.main.homepage.profitrank.profitlines;

import com.moyacs.canary.bean.ProfitLinesBean;
import com.moyacs.canary.bean.ProfitLinesVo;
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
public class ProfitLinesPresenter implements ProfitLinesContract.Presenter{

    private ProfitLinesContract.ProfitLinesView mView;
    private CompositeDisposable disposable;
    public ProfitLinesPresenter(ProfitLinesContract.ProfitLinesView view) {
        this.mView = view;
        disposable = new CompositeDisposable();
        mView.setPresenter(this);
    }



    @Override
    public void unsubscribe() {

    }

    @Override
    public void getProfitLinesList(boolean isrefresh) {
        ProfitLinesBean bean = new ProfitLinesBean();
        bean.setId("1");
        bean.setName("heguogui");
        bean.setRate("20");
        List<ProfitLinesBean> mlists = new ArrayList<>();
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
        mlists.add(bean);
        mlists.add(bean);
        mlists.add(bean);

        ProfitLinesVo vo = new ProfitLinesVo();
        vo.setList(mlists);
        mView.showProfitLines(vo,isrefresh);
    }
}
