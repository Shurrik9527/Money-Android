package com.moyacs.canary.moudle.guide;

import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public class GuidePresenter implements GuideContract.Presenter{

    private final GuideContract.View mView;

    public GuidePresenter(GuideContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void loadViews() {

        //最后一个布局需要带button
        Integer[] pics = {R.layout.guide_view1,
                R.layout.guide_view2, R.layout.guide_view3,R.layout.guide_view4};
        mView.showViewPager(pics);


    }

    @Override
    public void unsubscribe() {

    }
}
