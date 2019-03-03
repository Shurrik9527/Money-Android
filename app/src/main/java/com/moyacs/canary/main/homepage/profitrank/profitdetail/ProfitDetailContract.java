package com.moyacs.canary.main.homepage.profitrank.profitdetail;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;
import com.moyacs.canary.bean.ProfitDetailBean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 盈利榜详情
 * @date 2019/2/28
 * @email 252774645@qq.com
 */
public interface ProfitDetailContract {

    interface View extends BaseViews<ProfitDetailContract.Presenter> {

        /**
         *
         * @param mProfitDetailBean
         */
        void showProfitDetail(ProfitDetailBean mProfitDetailBean);

    }


    interface Presenter extends BasePresenter {
        /**
         * 盈利率列表
         */
        void getProfitDetail();

    }
}
