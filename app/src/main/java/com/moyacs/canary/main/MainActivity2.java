package com.moyacs.canary.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.moyacs.canary.base.BaseActivity2;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.login.LoginActivity2;
import com.moyacs.canary.main.deal.DealFragment;
import com.moyacs.canary.main.homepage.HomepageFragment2;
import com.moyacs.canary.main.market.MarketFragment2;
import com.moyacs.canary.main.me.MeFragment2;
import com.moyacs.canary.service.SocketService;
import com.moyacs.canary.widget.SwitchSlidingViewPager;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;

public class MainActivity2 extends BaseActivity2 {


    private static final String TAG = "MainActivity2";
    @BindView(R.id.vp_content)
    SwitchSlidingViewPager vpContent;
    //    @BindView(R.id.bottom_navigation_bar)
//    BottomNavigationBar mBottomNavigationBar;
    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;

    private String[] mTitles = {"首页", "行情", "交易", "我"};
    private int[] mIconUnselectIds = {
            R.mipmap.main_tab_home_default, R.mipmap.main_tab_quotations_default,
            R.mipmap.main_tab_weipan_1_normal_1, R.mipmap.main_tab_me_default};
    private int[] mIconSelectIds = {
            R.mipmap.main_tab_home_selected, R.mipmap.main_tab_quotations_selected,
            R.mipmap.main_tab_weipan_1_selected_1, R.mipmap.main_tab_me_selected};
    private ArrayList<Fragment> fragments;
    private Intent intent;
    private MarketFragment2 marketFragment2;


    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_msg).setVisible(false);
    }

    @Override
    protected void initIntentData(Intent intent) {
        viewPager = vpContent;
    }

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        intent = new Intent(this, SocketService.class);
        startService(intent);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_main, null, false);
        ButterKnife.bind(this, inflate);

        initViewPager();
        initCommonTabLayout();
        return inflate;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

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
                vpContent.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private static SwitchSlidingViewPager viewPager;

    /**
     * 用于其他页面跳转主页 指定的 fragment
     */
    public static void setViewPagerCurrentItme(int currentItem) {
        if (currentItem > 3) {
            return;
        }
        if (viewPager != null) {
            viewPager.setCurrentItem(currentItem);
        }
    }

    /**
     * 记录上一个显示的 tab 页面
     */
    private int oldPosition = 0;
    /**
     * 记录 tab 点击的 position，用于返回上一个 position 位置
     */
    ArrayList<Integer> position_list = new ArrayList<>();

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
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                position_list.add(position);
                commonTabLayout.setCurrentTab(position);
                switch (position) {
                    case 0:
                        initToolbar(getResources().getString(R.string.app_name));
                        showToolbar();
                        break;
                    case 1:
                        initToolbar(getResources().getString(R.string.app_name) + "行情");
                        showToolbar();
                        //如果 行情页面显示的是 自选 tab ，就弹出提示登录
                        int type_showTab = marketFragment2.getType_showTab();
                        if (type_showTab == 0) {
                            String userPhone = SPUtils.getInstance().getString(AppConstans.USER_PHONE, "");
                            if (TextUtils.isEmpty(userPhone)) {
                                DialogUtils.login_please("请先登录", MainActivity2.this);
                            }
                        }
                        break;
                    case 2:
                        hideToolbar();
                        String userPhone = SPUtils.getInstance().getString(AppConstans.USER_PHONE, "");
                        if (TextUtils.isEmpty(userPhone)) {
                            LemonHello.getInformationHello("请先登录", "")
                                    .setContentFontSize(14)
                                    //取消图标
//                                  .setIconWidth(0)
                                    .addAction(new LemonHelloAction("取消", Color.GRAY, new LemonHelloActionDelegate() {
                                        @Override
                                        public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                            helloView.hide();
                                            Log.i(TAG, "onClick: position  :" + position);
                                            Log.i(TAG, "onClick: oldPosition   :" + oldPosition);
                                            commonTabLayout.setCurrentTab(oldPosition);
                                            vpContent.setCurrentItem(oldPosition);

                                        }
                                    }))
                                    .addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                                        @Override
                                        public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                                            startActivity(new Intent(MainActivity2.this, LoginActivity2.class));
                                            lemonHelloView.hide();

                                        }
                                    }))
                                    .show(MainActivity2.this);
                        }
                        break;
                    case 3:
                        hideToolbar();
                        break;
                }

                if (position_list.size() < 2) {
                    return;
                }
                oldPosition = position_list.get(position_list.size() - 2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //默认显示第一个 tab
        vpContent.setCurrentItem(0);
    }


    private List<Fragment> getFragmentList() {
        fragments = new ArrayList<>();
        fragments.add(new HomepageFragment2());
        marketFragment2 = new MarketFragment2();
        fragments.add(marketFragment2);
        fragments.add(new DealFragment());
        fragments.add(new MeFragment2());
        return fragments;
    }

    /**
     * 隐藏标题栏 toolbar
     */
    private void hideToolbar() {
        int visibility = appBarLayout.getVisibility();
        if (visibility != View.GONE) {
            appBarLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示标题栏 toolbar
     */
    private void showToolbar() {
        int visibility = appBarLayout.getVisibility();
        if (visibility != View.VISIBLE) {
            appBarLayout.setVisibility(View.VISIBLE);
        }
    }

    private class MyPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
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


}
