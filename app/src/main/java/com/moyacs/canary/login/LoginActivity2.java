package com.moyacs.canary.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.moyacs.canary.base.BaseActivity2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

public class LoginActivity2 extends BaseActivity2 {
    @BindView(R.id.tablayout)
    SlidingTabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_goback)
    ImageView ivGoback;
    //    @BindView(R.id.rootView)
//    ScrollView rootView;
    //fragment 数据源
    private ArrayList<Fragment> mFragments;
    // tablayout 标题数据源
    private String[] mTitles = {"登录", "注册"};
    private Unbinder unbinder;

    @Override
    protected void updateOptionsMenu(Menu menu) {

    }

    @Override
    protected void initIntentData(Intent intent) {

    }

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_login, null, false);
        unbinder = ButterKnife.bind(this, view);
        initTabLayoutData();
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(myPagerAdapter);
        tablayout.setViewPager(viewpager);
        return view;
    }


    @Override
    protected boolean isshowToolbar() {
        return false;
    }

    /**
     * 初始化 Tablayout 的 fragment 数据源
     */
    private void initTabLayoutData() {
        mFragments = new ArrayList<>();
        LoginFragment loginFragment = new LoginFragment();
        mFragments.add(loginFragment);
        RegistFragment registFragment = new RegistFragment();
        mFragments.add(registFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
