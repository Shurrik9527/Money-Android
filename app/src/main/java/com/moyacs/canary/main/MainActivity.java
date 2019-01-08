package com.moyacs.canary.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.main.deal.DealFragment;
import com.moyacs.canary.main.homepage.HomepageFragment;
import com.moyacs.canary.main.market.MarketFragment;
import com.moyacs.canary.main.me.MeFragment2;
import com.moyacs.canary.service.SocketService;
import com.moyacs.canary.widget.SwitchSlidingViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import www.moyacs.com.myapplication.R;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_content)
    SwitchSlidingViewPager vpContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CommonTabLayout commonTabLayout;
    private String[] mTitles = {"首页", "行情", "交易", "我"};
    private int[] mIconUnselectIds = {
            R.mipmap.main_tab_home_default, R.mipmap.main_tab_quotations_default,
            R.mipmap.main_tab_weipan_1_normal_1, R.mipmap.main_tab_me_default};
    private int[] mIconSelectIds = {
            R.mipmap.main_tab_home_selected, R.mipmap.main_tab_quotations_selected,
            R.mipmap.main_tab_weipan_1_selected_1, R.mipmap.main_tab_me_selected};
    private ArrayList<Fragment> fragments;
    private Intent serviceIntent;
    private MarketFragment marketFragment;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        commonTabLayout = findViewById(R.id.commonTabLayout);
        initViewPager();
        initCommonTabLayout();
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {
        serviceIntent = new Intent(this, SocketService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
    }

    /**
     * 初始化 底部导航栏
     */
    private void initCommonTabLayout() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:
                        initToolbar(getResources().getString(R.string.app_name));
                        break;
                    case 1:
                        initToolbar(getResources().getString(R.string.app_name) + "行情");
                        //如果 行情页面显示的是 自选 tab ，就弹出提示登录
                        int type_showTab = marketFragment.getType_showTab();
                        if (type_showTab == 0 && !isLogin()) {
                            return;
                        }
                        break;
                    case 2:
                        if (!isLogin()) {
                            return;
                        }
                        toolbar.setVisibility(View.GONE);
                        break;
                    case 3:
                        toolbar.setVisibility(View.GONE);
                        break;
                }
                vpContent.setCurrentItem(position);
            }


            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    /**
     * 初始化 viewpager
     */
    private void initViewPager() {
        getFragmentList();
        //设置viewpager 预加载个数
        vpContent.setOffscreenPageLimit(4);
        vpContent.setSmoothScroll(false);
        vpContent.setCanScroll(false);
        vpContent.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        //默认显示第一个 tab
        vpContent.setCurrentItem(0);
    }

    private void initToolbar(String title) {
        toolbar.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

    private void getFragmentList() {
        fragments = new ArrayList<>();
        fragments.add(new HomepageFragment());
        marketFragment = new MarketFragment();
        fragments.add(marketFragment);
        fragments.add(new DealFragment());
        fragments.add(new MeFragment2());
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

    private boolean isLogin() {
        String userPhone = SPUtils.getInstance().getString(AppConstans.USER_PHONE, "");
        if (TextUtils.isEmpty(userPhone)) {
            DialogUtils.login_please("请先登录", MainActivity.this);
            return false;
        }
        return true;
    }
}
