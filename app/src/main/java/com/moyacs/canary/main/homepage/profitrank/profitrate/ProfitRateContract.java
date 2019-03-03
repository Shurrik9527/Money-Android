package com.moyacs.canary.main.homepage.profitrank.profitrate;
import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;
import com.moyacs.canary.bean.ProfitRateVo;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 盈利率契约
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public interface ProfitRateContract {

    interface ProfitRateView extends BaseViews<ProfitRateContract.Presenter> {

        /**
         *
         * @param mProfitRateVo
         */
        void showProfitRate(ProfitRateVo mProfitRateVo,boolean isRefresh);

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
        void getProfitRateList(boolean isrefresh);

    }

}
