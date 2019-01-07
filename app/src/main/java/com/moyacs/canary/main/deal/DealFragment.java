package com.moyacs.canary.main.deal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.base.BaseFragment3;
import com.moyacs.canary.widget.SwitchSlidingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/2 0002 10:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：交易页面
 */

public class DealFragment extends BaseFragment2 {
    private static final String TAG = "DealFragment";
    Unbinder unbinder;
    @BindView(R.id.desView)
    RelativeLayout desView;
    @BindView(R.id.tv_lable_01)
    TextView tvLable01;
    @BindView(R.id.tab_01)
    LinearLayout tab01;
    @BindView(R.id.changeTradeView)
    LinearLayout changeTradeView;
    @BindView(R.id.btn_help_trade)
    RelativeLayout btnHelpTrade;
    @BindView(R.id.titleBar)
    LinearLayout titleBar;
    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;
    @BindView(R.id.viewpager)
    SwitchSlidingViewPager viewpager;

    // tablayout 标题数据源
    private String[] mTitles = { "持仓","资金"};
    //当前 Fragment 视图是否创建完成
    private boolean isViewCreated;

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        Log.i(TAG, "onCreateView:   addChildInflaterView");
        View rootView = inflater.inflate(R.layout.fragment_deal, null, false);
        unbinder = ButterKnife.bind(this, rootView);
        initFragments();
        viewpager.setOffscreenPageLimit(2);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(myPagerAdapter);
        tablayout.setViewPager(viewpager);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isViewCreated) {
            return;
        }
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (isVisibleToUser) {
            for (int i = 0; i < fragments.size(); i++) {
                BaseFragment3 fragment = (BaseFragment3) fragments.get(i);
                fragment.lazyLoadData();

            }
        }
        Log.i(TAG, "setUserVisibleHint:  " + isVisibleToUser);
    }

    private List<Fragment> mFragments;

    /**
     * 初始化 FragmentPagerAdapter 数据源
     */
    private void initFragments() {
        mFragments = new ArrayList<>();
        Deal_tab2_Fragment deal_tab2_fragment = new Deal_tab2_Fragment();
        mFragments.add(deal_tab2_fragment);
        mFragments.add(new Deal_tab3_Fragment());
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView:   onDestroyView");
        unbinder.unbind();
    }


    @OnClick({R.id.desView, R.id.btn_help_trade})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.desView://标题左侧
                break;
            case R.id.btn_help_trade://标题右侧
                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
