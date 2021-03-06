package com.moyacs.canary.main.deal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.main.deal.capital.CapitalFragment;
import com.moyacs.canary.main.deal.order.HoldOrderFragment;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.widget.SwitchSlidingViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
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
    private HoldOrderFragment positionFragment;
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            positionFragment.isVisibleToUser();
        }
    }

    /**
     * 初始化 FragmentPagerAdapter 数据源
     */
    private void initFragments() {
        mFragments = new ArrayList<>();
        positionFragment = new HoldOrderFragment();
        mFragments.add(positionFragment);
        mFragments.add(new CapitalFragment());
    }


    @OnClick({R.id.desView, R.id.btn_help_trade})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.desView://标题左侧
                break;
            case R.id.btn_help_trade://标题右侧
                if (TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
                    DialogUtils.login_please("请先登录", getContext());
                } else {
                    RongIM.getInstance().startPrivateChat(getActivity(), AppConstans.CUSTOM_SERVER_ID, "客服");
                }
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

    public void setSelectHoldPosition() {
        tabLayout.setCurrentTab(0);
    }
}
