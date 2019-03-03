package com.moyacs.canary.main.homepage.profitrank;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.main.homepage.profitrank.profitlines.ProfitLinesFragment;
import com.moyacs.canary.main.homepage.profitrank.profitorder.ProfitOrderActivity;
import com.moyacs.canary.main.homepage.profitrank.profitrate.ProfitRateFragment;
import com.moyacs.canary.widget.SwitchSlidingViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 盈利榜
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class ProfitRangActivity extends BaseActivity {


    @BindView(R.id.profit_tablayout)
    SlidingTabLayout profitTablayout;
    @BindView(R.id.profit_viewpager)
    SwitchSlidingViewPager profitViewpager;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.top_title_tv)
    TextView topTitleTv;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.profit_bottom_iv)
    ImageView profitBottomIv;
    @BindView(R.id.profit_bootom_title_tv)
    TextView profitBootomTitleTv;
    @BindView(R.id.profit_show_lists_tv)
    TextView profitShowListsTv;
    @BindView(R.id.profit_show_tv)
    TextView profitShowTv;
    @BindView(R.id.profit_start_deal_btn)
    Button profitStartDealBtn;
    @BindView(R.id.profit_bootom_rl)
    RelativeLayout profitBootomRl;

    //问号弹窗
    private PopupWindow popWinForMoreOption;
//    private ProfitLinesFragment mProfitLinesFragment;
//    private ProfitRateFragment mProfitRateFragment;

    private String[] mTitles = {"盈利率榜%", "盈利额榜$"};

    private List<Fragment> mFragments;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_profit;
    }

    @Override
    protected void initView() {
        ivRight.setVisibility(View.VISIBLE);
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {
        topTitleTv.setText(getString(R.string.profit_top_title));
        initFragments();
        profitViewpager.setOffscreenPageLimit(2);
        profitViewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        profitTablayout.setViewPager(profitViewpager);
        profitTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    if (profitViewpager != null) {
                        profitViewpager.setCurrentItem(0);
                    }
                } else {
                    if (profitViewpager != null) {
                        profitViewpager.setCurrentItem(1);
                    }
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    /**
     * 初始化 FragmentPagerAdapter 数据源
     */
    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new ProfitRateFragment());
        mFragments.add(new ProfitLinesFragment());
    }



    @OnClick({R.id.iv_back, R.id.profit_show_lists_tv, R.id.profit_start_deal_btn,R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.profit_show_lists_tv:
                startActivity(new Intent(ProfitRangActivity.this, ProfitOrderActivity.class));
                break;
            case R.id.profit_start_deal_btn:
                EventBus.getDefault().post(new EvenVo(EvenVo.WATCH_CHI_CHAN));
                finish();
                break;
            case R.id.iv_right:
                showPopWinDown();
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
        profitTablayout.setCurrentTab(0);
    }

    /**
     * 产品详情页更多弹窗
     */
    private void showPopWinDown() {
        int w = (int) getResources().getDimension(R.dimen.margin_200dp);
        int h = (int) getResources().getDimension(R.dimen.margin_222dp);
        if (popWinForMoreOption == null) {
            View view = View.inflate(ProfitRangActivity.this, R.layout.layout_profitrang_help, null);
            popWinForMoreOption = new PopupWindow(view, w, h);
            popWinForMoreOption.setFocusable(true);//影响listView的item点击
            popWinForMoreOption.setOutsideTouchable(true);
            popWinForMoreOption.setBackgroundDrawable(new BitmapDrawable());
        }
        popWinForMoreOption.showAsDropDown(ivRight, -w + (int) getResources().getDimension(R.dimen.margin_40dp), 0);
    }


}
