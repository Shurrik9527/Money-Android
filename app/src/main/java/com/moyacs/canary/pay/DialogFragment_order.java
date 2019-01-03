package com.moyacs.canary.pay;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.moyacs.canary.base.BaseActivity2;
import com.moyacs.canary.widget.SwitchSlidingViewPager;
import com.moyacs.canary.widget.UnderLineTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/4/12 0012 11:59
 * 邮箱：rsf411613593@gmail.com
 * 说明：挂单和市价单父布局
 */

public class DialogFragment_order extends DialogFragment {

    @BindView(R.id.gobackView)
    ImageView gobackView;
    @BindView(R.id.tv_yin)
    UnderLineTextView tvYin;
    @BindView(R.id.tv_price01)
    TextView tvPrice01;
    @BindView(R.id.tv_symbol)
    TextView tvSymbol;
    @BindView(R.id.tv_sell)
    TextView tvSell;
    @BindView(R.id.tv_rateChange)
    TextView tvRateChange;
    @BindView(R.id.text_giftname)
    TextView textGiftname;
    @BindView(R.id.tabView)
    RelativeLayout tabView;

    @BindView(R.id.viewpager)
    SwitchSlidingViewPager viewpager;
    Unbinder unbinder1;
    @BindView(R.id.tv_shijiadan)
    TextView tvShijiadan;
    @BindView(R.id.rl_shijiadan)
    RelativeLayout rlShijiadan;
    @BindView(R.id.tv_guadan)
    TextView tvGuadan;
    @BindView(R.id.rl_guadan)
    RelativeLayout rlGuadan;
    private View inflate;
    /***
     * 记录点击的状态
     */
    private int type_order;
    private ShiJiaDanFragment2 shiJiaDanFragment2;
    /**
     * 小数位
     */
    private int digit;
    /**
     * 止损 止盈 点位
     */
    private int stops_level;
    private String price_sell;
    private GuaDanFragment guaDanFragment;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        symbol_cn = arguments.getString("symbol_cn");
        symbol = arguments.getString("symbol");
        textColor = arguments.getInt("textColor");
        type_order = arguments.getInt("type_order");
        digit = arguments.getInt("digit");
        stops_level = arguments.getInt("stops_level");
        price_buy = arguments.getString("price_buy");
        price_sell = arguments.getString("price_sell");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        //去出标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //SlidingTabLayout 不能与 scrollview 一块使用
        inflate = inflater.inflate(R.layout.popwindow_create_order_new, container, false);
        unbinder1 = ButterKnife.bind(this, inflate);
        initViewPager();
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewsDate();
    }

    /**
     * 初始化相关数据
     */
    private void initViewsDate() {
        //品种名称
        tvYin.setText(symbol);
//        tvYin.setTextColor(textColor);
        tvSymbol.setText(symbol_cn);
//        tvSymbol.setTextColor(textColor);
        //品种买入价
        if (price_buy == null || price_buy.equals("null")) {
            price_buy = "";
        }
        tvPrice01.setText(price_buy);
        tvPrice01.setTextColor(getResources().getColor(R.color.color_opt_gt));
        //卖出价
        if (price_sell == null || price_sell.equals("null")) {
            price_sell = "";
        }
        tvSell.setText(price_sell);
        tvSell.setTextColor(getResources().getColor(R.color.color_opt_lt));
    }

    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        int screenHeight = ScreenUtils.getScreenHeight() / 3 * 2;
        params.height = screenHeight;
        win.setAttributes(params);


    }


    //fragment 数据源
    private ArrayList<Fragment> mFragments;
    // tablayout 标题数据源
    private String[] mTitles = {"市价单", "挂单"};


    private void initViewPager() {
        mFragments = new ArrayList<>();

        shiJiaDanFragment2 = new ShiJiaDanFragment2();
        Bundle bundle = new Bundle();
        bundle.putInt("type_order", type_order);
        bundle.putInt("digit", digit);
        bundle.putInt("stops_level", stops_level);
        bundle.putString("price_buy", price_buy);
        bundle.putString("price_sell", price_sell);
        bundle.putString("symbol", symbol);

        shiJiaDanFragment2.setArguments(bundle);
        mFragments.add(shiJiaDanFragment2);
        guaDanFragment = new GuaDanFragment();

        Bundle bundle2 = new Bundle();
        //表示挂单
        bundle2.putInt("type_order", 3);
        bundle2.putInt("digit", digit);
        bundle2.putInt("stops_level", stops_level);
        bundle2.putString("price_buy", price_buy);
        bundle2.putString("price_sell", price_sell);
        bundle2.putString("symbol", symbol);
        guaDanFragment.setArguments(bundle);
        mFragments.add(guaDanFragment);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(myPagerAdapter);
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        //默认选中市价单,选中的字体为加粗，未选中为默认字体
        viewpager.setCurrentItem(0);
        rlShijiadan.setSelected(true);
        tvShijiadan.setTextColor(getResources().getColor(R.color.dialog_btn_blue));
        tvShijiadan.setTypeface(Typeface.DEFAULT_BOLD);
        rlGuadan.setSelected(false);
        tvGuadan.setTextColor(getResources().getColor(R.color.grey));
        tvGuadan.setTypeface(Typeface.DEFAULT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }

    @OnClick({R.id.rl_shijiadan, R.id.rl_guadan})
    public void onViewClicked(View view) {

        rlShijiadan.setSelected(false);
        tvShijiadan.setTextColor(getResources().getColor(R.color.grey));
        tvShijiadan.setTypeface(Typeface.DEFAULT);
        rlGuadan.setSelected(false);
        tvGuadan.setTextColor(getResources().getColor(R.color.grey));
        tvGuadan.setTypeface(Typeface.DEFAULT);
        switch (view.getId()) {
            case R.id.rl_shijiadan:
                rlShijiadan.setSelected(true);
                tvShijiadan.setTextColor(getResources().getColor(R.color.dialog_btn_blue));
                tvShijiadan.setTypeface(Typeface.DEFAULT_BOLD);

                viewpager.setCurrentItem(0);
                break;
            case R.id.rl_guadan:
                rlGuadan.setSelected(true);
                tvGuadan.setTextColor(getResources().getColor(R.color.dialog_btn_blue));
                tvGuadan.setTypeface(Typeface.DEFAULT_BOLD);

                viewpager.setCurrentItem(1);
                break;
        }
    }

    /**
     * viewPager 页面改变监听
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            rlShijiadan.setSelected(false);
            tvShijiadan.setTextColor(getResources().getColor(R.color.grey));
            rlGuadan.setSelected(false);
            tvGuadan.setTextColor(getResources().getColor(R.color.grey));
            switch (position) {
                case 0:
                    rlShijiadan.setSelected(true);
                    tvShijiadan.setTextColor(getResources().getColor(R.color.dialog_btn_blue));
                    break;
                case 1:
                    rlGuadan.setSelected(true);
                    tvGuadan.setTextColor(getResources().getColor(R.color.dialog_btn_blue));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

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
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    //品种中文名称
    private String symbol_cn;
    //品种英文名称
    private String symbol;
    //品种和买入价颜色
    private int textColor;
    //买入价
    private String price_buy;

    /**
     * 设置最新价格，根据 netty socket 数据
     *
     * @param isGetNewPrice
     * @param price_new
     * @param price_sell
     */
    public void setNewPrice(boolean isGetNewPrice, String price_new, String price_sell) {
        if (tvPrice01 == null) {
            return;
        }
        if (isGetNewPrice) {
            tvPrice01.setText(price_new);
            tvSell.setText(price_sell);
        } else {
            tvPrice01.setText(price_buy);
            tvSell.setText(this.price_sell);
        }

        shiJiaDanFragment2.setPrice(price_new, price_sell);
    }

}
