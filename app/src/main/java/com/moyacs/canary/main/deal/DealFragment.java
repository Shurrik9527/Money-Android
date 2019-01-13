package com.moyacs.canary.main.deal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.widget.SwitchSlidingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/2 0002 10:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：交易页面
 */

public class DealFragment extends BaseFragment {
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
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewpager)
    SwitchSlidingViewPager viewPager;

    // tabLayout 标题数据源
    private String[] mTitles = {"持仓", "资金"};

    private List<Fragment> mFragments;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_deal;
    }

    @Override
    protected void initView() {
        initFragments();
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        tabLayout.setViewPager(viewPager);

    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {

    }

    /**
     * 初始化 FragmentPagerAdapter 数据源
     */
    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new HoldPositionFragment());
        mFragments.add(new CapitalFragment());
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
