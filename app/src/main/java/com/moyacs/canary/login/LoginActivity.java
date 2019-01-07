package com.moyacs.canary.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.moyacs.canary.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tablayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private ArrayList<Fragment> mFragments;
    private String[] mTitles = {"登录", "注册"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initTabLayoutData();
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(myPagerAdapter);
        tabLayout.setViewPager(viewPager);
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {

    }

    /**
     * 初始化 Tablayout 的 fragment 数据源
     */
    private void initTabLayoutData() {
        mFragments = new ArrayList<>();
        mFragments.add(new LoginFragment());
        mFragments.add(new RegistFragment());
    }

    @OnClick(R.id.iv_goback)
    public void onViewClicked() {
        finish();
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
