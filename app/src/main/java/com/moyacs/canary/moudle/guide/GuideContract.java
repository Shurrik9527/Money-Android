package com.moyacs.canary.moudle.guide;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public interface GuideContract {

    interface View extends BaseViews<Presenter> {

        void jumpHomeActivity();
        void showViewPager(Integer[] pics);

    }

    interface Presenter extends BasePresenter {
        void loadViews();
    }
}
