package com.moyacs.canary.main.homepage.profitrank.profitlines;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;
import com.moyacs.canary.bean.ProfitLinesVo;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 盈利率契约
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public interface ProfitLinesContract {

    interface ProfitLinesView extends BaseViews<ProfitLinesContract.Presenter> {

        /**
         *
         * @param mProfitLinesVo
         */
        void showProfitLines(ProfitLinesVo mProfitLinesVo, boolean isRefresh);

        /**
         * 结束刷新
         */
        void refreshFinish();
        //结束加载更多
        void loadMoreFinish();
    }


    interface Presenter extends BasePresenter {
        /**
         * 盈利率列表
         */
        void getProfitLinesList(boolean isrefresh);

    }

}
