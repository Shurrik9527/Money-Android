package com.moyacs.canary.product_fxbtg;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.kchart.fragment.KLineFragment;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.main.me.EvenVo;
import com.moyacs.canary.pay.GuaDanPopWindow;
import com.moyacs.canary.pay.OrderPopWindow;
import com.moyacs.canary.product_fxbtg.adapter.ProductAdapter;
import com.moyacs.canary.service.SocketQuotation;
import com.moyacs.canary.util.DateUtil;
import com.moyacs.canary.util.LogUtils;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.util.ViewListenerAbs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import www.moyacs.com.myapplication.R;

import static com.moyacs.canary.product_fxbtg.Product_constans.PARAM_KLINE_15M_WEIPAN_new;
import static com.moyacs.canary.product_fxbtg.Product_constans.PARAM_KLINE_1D_WEIPAN_new;
import static com.moyacs.canary.product_fxbtg.Product_constans.PARAM_KLINE_1M_WEIPAN_new;
import static com.moyacs.canary.product_fxbtg.Product_constans.PARAM_KLINE_30M_WEIPAN_new;
import static com.moyacs.canary.product_fxbtg.Product_constans.PARAM_KLINE_4H_WEIPAN_new;
import static com.moyacs.canary.product_fxbtg.Product_constans.PARAM_KLINE_5M_WEIPAN_new;
import static com.moyacs.canary.product_fxbtg.Product_constans.PARAM_KLINE_60M_WEIPAN_new;
import static com.moyacs.canary.product_fxbtg.Product_constans.PARAM_KLINE_WEEK_WEIPAN_new;

/**
 * 产品的详情页面
 * <p>
 * android:hardwareAccelerated="true"
 * 硬件加速问题  导致多层次的view重新绘制
 * activity中设置hardwareAccelerated true
 */
public class ProductActivity extends BaseActivity implements OnClickListener {
    private RecyclerView recyclerView, landRecycleViewCycle;
    private TextView tvTitle, tvTime, tvOpen, tvClosed, tvHigh, tvLow, tvRate,
            tvTitleLand, tvTimeLand, tvOpenLand, tvClosedLand, tvHighLand,
            tvLowLand, tvRateLand;
    private View fragmentContainer = null;
    private TextView tv_latest, tv_latest_land;
    private View klineFullscreenView;
    //前往交易大厅
    private View goWeiPanListView;
    //昨收价格
    private double close;
    //格式化过后的   涨跌值 + 涨跌幅 ，主要为了解决切换屏幕的时候，netty 获取数据不及时，造成的空白现象
    private String rate;
    //小数点位数，主要是买入价，默认为 5
    private int digit;
    //最新的买入价
    private String priceBuy;
    //时间
    private String time;
    //品种中文名称
    private String symbol_cn;
    private LruCache<String, Object> lruCache;
    //下单页面
    private OrderPopWindow orderPopWindow;
    private GuaDanPopWindow guaDanPopWindow;
    //字体颜色
    private int textColor = 0;
    //当前品种的英文名称
    private String symbol;
    //问号弹窗
    private PopupWindow popWinForMoreOption;
    //时间格式化
    private String simpleDateFormat = "dd-MM-yyyy HH:mm:ss";
    private ProductAdapter productAdapter; // 竖屏标题适配器
    private ProductAdapter landProductAdapter; //横屏标题适配器
    private List<KlineCycle> klineCycles;
    private TradeVo.Trade marketDataBean; // 交易信息  上个页面intent传过来

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product;
    }

    @Override
    protected void initView() {
        initKLineTitleList();
        //初始化 竖屏时候的 recycler_view
        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//linearLayoutManager is already attached recyclerView不能共用linearLayoutManager
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        productAdapter = new ProductAdapter(klineCycles, ProductAdapter.ITEM_NORMAL);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setItemViewCacheSize(klineCycles.size());

        //初始化 横屏时候的 recycler_view
        landRecycleViewCycle = findViewById(R.id.landRecycleViewCycle);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        landRecycleViewCycle.setLayoutManager(linearLayoutManager3);
        landProductAdapter = new ProductAdapter(klineCycles, ProductAdapter.ITEM_LAND);
        landRecycleViewCycle.setAdapter(landProductAdapter);
        landRecycleViewCycle.setItemViewCacheSize(klineCycles.size());

        View tradeLayout = findViewById(R.id.tradeLayout);
        goWeiPanListView = findViewById(R.id.goWeipanListView);
        klineFullscreenView = findViewById(R.id.kline_fullscreenView);
        tv_latest = findViewById(R.id.tv_latest);
        tvRate = findViewById(R.id.tv_rate);
        tvTitle = findViewById(R.id.tv_title);
        tvTime = findViewById(R.id.tv_time);
        tvOpen = findViewById(R.id.tv_open);
        tvClosed = findViewById(R.id.tv_closed);
        tvHigh = findViewById(R.id.tv_high);
        tvLow = findViewById(R.id.tv_low);
        fragmentContainer = findViewById(R.id.fragment_container);
        tv_latest_land = findViewById(R.id.tv_latest_land);
        tvRateLand = findViewById(R.id.tv_rate_land);
        tvTitleLand = findViewById(R.id.tv_title_land);
        tvTimeLand = findViewById(R.id.tv_time_land);
        tvOpenLand = findViewById(R.id.tv_open_land);
        tvClosedLand = findViewById(R.id.tv_closed_land);
        tvHighLand = findViewById(R.id.tv_high_land);
        tvLowLand = findViewById(R.id.tv_low_land);

        tradeLayout.setVisibility(View.VISIBLE);
        goWeiPanListView.setVisibility(View.GONE);
        landVisibleView(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void intListener() {
        findViewById(R.id.tv_buyUp).setOnClickListener(this);
        findViewById(R.id.tv_buyDown).setOnClickListener(this);
        findViewById(R.id.iconback).setOnClickListener(this);
        findViewById(R.id.tv_tradeclose).setOnClickListener(this);
        findViewById(R.id.gobackView).setOnClickListener(this);
        findViewById(R.id.tv_guadan).setOnClickListener(this);
        findViewById(R.id.goLiveView).setOnClickListener(this);
        findViewById(R.id.refreshView_land).setOnClickListener(this);
        goWeiPanListView.setOnClickListener(this);
        klineFullscreenView.setOnClickListener(this);
        productAdapter.setItemClickListener(productItemClickListener);
        landProductAdapter.setItemClickListener(productItemClickListener);
    }

    @Override
    protected void initData() {
        registerEventBus();
        initIntentData(getIntent());
        lineAnimate();
    }

    private ViewListenerAbs.ItemClickListener productItemClickListener = new ViewListenerAbs.ItemClickListener() {
        @Override
        public void onItemClickListener(View view, int pos) {
            String code = klineCycles.get(pos).getCode();
            //周期K线
            String[] startDataAndEndData = getStartDateAndEndDate(code);
            replaceFragment(getKlineFragment(code, startDataAndEndData[0], startDataAndEndData[1]));
            if (recyclerView != null)
                recyclerView.scrollToPosition(pos);
            if (landRecycleViewCycle != null)
                landRecycleViewCycle.scrollToPosition(pos);
        }
    };

    /**
     * 获取 EventBus 传递的 Socket 数据
     *
     * @param evenVo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyData(EvenVo<SocketQuotation> evenVo) {
        if (evenVo.getCode() == EvenVo.SOCKET_QUOTATION) {
            SocketQuotation quotation = evenVo.getT();
            //过滤数据，只有与当前页面品种相同才处理
            if (symbol.equals(quotation.getSymbolCode())) {
                //是否获取到新价格
                boolean isGetNewPrice = true;
                // 获取旧价格，String 类型
                String oldPrice = (String) tv_latest.getText();
                //获取最新买入价，并显示
                //新价格
                double price_new = quotation.getPrice();
                //格式化买入价
                String price_new_scale = NumberUtils.setScale(price_new, digit);
                //赋值最新买入价
                priceBuy = price_new_scale;
                //赋值最新卖出价
//                String priceSell = NumberUtils.setScale(quotation.getBid(), digit);

                //更新弹框数据
                if (orderPopWindow != null && orderPopWindow.isShow()) {
                    orderPopWindow.setNewPrice(isGetNewPrice, Double.parseDouble(priceBuy));
                }
                if (guaDanPopWindow != null && guaDanPopWindow.isShow()) {
                    guaDanPopWindow.setNewPrice(isGetNewPrice, priceBuy);
                }
                //时间
                String time1 = quotation.getMarketTime().getTime();
                //竖屏
                if (tv_latest != null) {
                    //设置最新买入价
                    tv_latest.setText(price_new_scale);
                    //设置时间
                    tvTime.setText(time1);
                }
                //横屏
                if (tv_latest_land != null) {
                    //设置最新买入价
                    tv_latest_land.setText(price_new_scale);
                    //设置时间
                    tvTimeLand.setText(time1);
                }
                //比较新价格与昨收，
                double compare = NumberUtils.compare(price_new, close);
                //新旧价格差
                double subtract;
                //涨跌幅
                double range;
                if (compare == 1) {//新价格 > 昨收   涨
                    //计算涨跌值 新 - 旧
                    subtract = NumberUtils.subtract(price_new, close);
                    String s = NumberUtils.doubleToString(subtract);
                    //字体颜色
                    textColor = getResources().getColor(R.color.color_opt_gt);
                    //计算涨跌幅
                    range = NumberUtils.divide(subtract, close, 4);
                    //涨跌幅格式化
                    String s2 = NumberUtils.setScale2(range);
                    //竖屏时候设置
                    if (tvRate != null) {
                        tvRate.setText("+" + s + "   +" + s2 + "%");
                    }
                    //横屏时候设置
                    if (tvRateLand != null) {
                        tvRateLand.setText("+" + s + "   +" + s2 + "%");
                    }
                    //设置 涨跌值 涨跌幅
                } else if (compare == -1) {//新价格 < 昨收   跌
                    //计算涨跌值  旧 - 新
                    subtract = NumberUtils.subtract(close, price_new);
                    String s = NumberUtils.doubleToString(subtract);
                    //字体颜色
                    textColor = getResources().getColor(R.color.trade_down);
                    //计算涨跌幅
                    range = NumberUtils.divide(subtract, close, 4);
                    //涨跌幅格式化
                    String s2 = NumberUtils.setScale2(range);
                    //设置 涨跌值 涨跌幅
                    rate = "-" + s + "   -" + s2 + "%";
                    //竖屏时候设置
                    if (tvRate != null) {
                        tvRate.setText(rate);
                    }
                    //横屏时候设置
                    if (tvRateLand != null) {
                        tvRateLand.setText(rate);
                    }
                } else {
                    //新价格 = 旧价格   平
                }
                //改变字体颜色
                tv_latest.setTextColor(textColor);
                //改变涨跌值和涨跌幅的颜色
                //竖屏
                if (tvRate != null) {
                    tvRate.setTextColor(textColor);
                }
                //横屏
                if (tvRateLand != null) {
                    tvRateLand.setTextColor(textColor);
                }
            }
        }

    }


    /**
     * 是否为横屏状态
     *
     * @param isLand 是否横屏
     */
    protected void landVisibleView(boolean isLand) {
        View layoutTop = findViewById(R.id.layoutTop);
        View layoutTop_land = findViewById(R.id.layoutTop_land);
        View layoutBottom = findViewById(R.id.layoutBottom);
        View layoutBottom_land = findViewById(R.id.layoutBottom_land);
        if (isLand) {
            //横屏状态
            if (layoutTop != null)
                layoutTop.setVisibility(View.GONE);
            if (layoutTop_land != null)
                layoutTop_land.setVisibility(View.VISIBLE);
            if (layoutBottom != null)
                layoutBottom.setVisibility(View.GONE);
            if (layoutBottom_land != null)//默认分时第一位
                layoutBottom_land.setVisibility(View.VISIBLE);
            if (landRecycleViewCycle != null && landRecycleViewCycle.getAdapter() != null) {
                landRecycleViewCycle.getAdapter().notifyDataSetChanged();
            }
        } else {
            //竖屏状态
            if (layoutTop != null)
                layoutTop.setVisibility(View.VISIBLE);
            if (layoutTop_land != null)
                layoutTop_land.setVisibility(View.GONE);
            if (layoutBottom != null)
                layoutBottom.setVisibility(View.VISIBLE);
            if (layoutBottom_land != null)
                layoutBottom_land.setVisibility(View.GONE);
            if (recyclerView != null && recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    /***
     *  初始化K线标题线数据
     * @return
     */
    private void initKLineTitleList() {
        klineCycles = new ArrayList<>();
        klineCycles.add(new KlineCycle("1分钟", PARAM_KLINE_1M_WEIPAN_new));
        klineCycles.add(new KlineCycle("5分钟", PARAM_KLINE_5M_WEIPAN_new));
        klineCycles.add(new KlineCycle("15分钟", PARAM_KLINE_15M_WEIPAN_new));
        klineCycles.add(new KlineCycle("30分钟", PARAM_KLINE_30M_WEIPAN_new));
        klineCycles.add(new KlineCycle("1小时", PARAM_KLINE_60M_WEIPAN_new));
        klineCycles.add(new KlineCycle("4小时", PARAM_KLINE_4H_WEIPAN_new));
        klineCycles.add(new KlineCycle("日线", PARAM_KLINE_1D_WEIPAN_new));
        klineCycles.add(new KlineCycle("周线", PARAM_KLINE_WEEK_WEIPAN_new));
    }

    /**
     * 图片线条动画
     */
    private void lineAnimate() {
        //使用Runnalbe()来加载,默认xml中不会播放
        final ImageView plineView = findViewById(R.id.plineView);
        plineView.post(() -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.p_head_line);
            plineView.startAnimation(animation);
        });
    }

    /**
     * 获取上个页面传递过来的值
     */
    protected void initIntentData(Intent intent) {
        //获取产品购买信息
        marketDataBean = (TradeVo.Trade) intent.getSerializableExtra("marketData");
        //品种中文名称
        symbol_cn = marketDataBean.getSymbolName();
        //品种英文名称
        symbol = marketDataBean.getSymbolCode();
        //excode
//        String excode = intent.getStringExtra("excode");
        String[] startDataAndEndData = getStartDateAndEndDate(PARAM_KLINE_1M_WEIPAN_new);
        replaceFragment(getKlineFragment(PARAM_KLINE_1M_WEIPAN_new, startDataAndEndData[0], startDataAndEndData[1]));
        //获取精度
        digit = marketDataBean.getDigit();
        //止损 止盈 点位
//        int stops_level = intent.getIntExtra(AppConstans.stops_level, 0);
        //设置买价
        priceBuy = String.valueOf(marketDataBean.getPriceBuy());
        tv_latest.setText(priceBuy);
        //获取传递过来的最新卖出价
//        String priceSell = intent.getStringExtra(AppConstans.price_sell);
        //设置涨跌值 涨跌幅
        tvRate.setText(marketDataBean.getRangValue() + "   " + marketDataBean.getRangString());
        //设置时间
        tvTime.setText(marketDataBean.getTime());
        //最高
        String high = NumberUtils.setScale(marketDataBean.getMaxPrice(), marketDataBean.getDigit());
        //最低
        String low = NumberUtils.setScale(marketDataBean.getMinPrice(), marketDataBean.getDigit());
        //今开
        String open = NumberUtils.setScale(marketDataBean.getOpen(), marketDataBean.getDigit());
        //昨收
        close = marketDataBean.getClose();
        //字体颜色
        this.textColor = getResources().getColor(marketDataBean.isUp() ? R.color.color_opt_gt : R.color.trade_down);
        //横屏
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //设置标题
            tvTitleLand.setText(symbol_cn);
            //最高
            tvHighLand.setText(high);
            //最低
            tvLowLand.setText(low);
            //今开
            tvOpenLand.setText(open);
            //昨收
            tvClosedLand.setText(NumberUtils.setScale(close, digit));
            //改变字体颜色
            tv_latest_land.setTextColor(this.textColor);
            tvRateLand.setTextColor(this.textColor);
        } else {//竖屏
            //设置标题
            tvTitle.setText(symbol_cn);
            //最高
            tvHigh.setText(high);
            //最低
            tvLow.setText(low);
            //今开
            tvOpen.setText(open);
            //昨收
            tvClosed.setText(NumberUtils.setScale(close, digit));
            //改变字体颜色
            tv_latest.setTextColor(this.textColor);
            tvRate.setTextColor(this.textColor);
        }
        lruCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 4));
        //获取最新买入价
        String price_sp = (String) lruCache.get(AppConstans.price_buy);
        //获取最新涨跌值和涨跌幅
        String rate_sp = (String) lruCache.get("rate");
        //获取字体颜色
        int textColor_sp = lruCache.get("textColor") == null ? 0 : (int) lruCache.get("textColor");
        //获取时间
        String time_sp = (String) lruCache.get("time");
        if (time_sp == null || time_sp.equals("")) {
            return;
        }
        //横屏
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tv_latest_land.setText(price_sp);
            tvRateLand.setText(rate_sp);
            tv_latest_land.setTextColor(textColor_sp);
            tvRateLand.setTextColor(textColor_sp);
            tvTimeLand.setText(time_sp);
        } else {
            tv_latest.setText(price_sp);
            tvRate.setText(rate_sp);
            tv_latest.setTextColor(textColor_sp);
            tvRate.setTextColor(textColor_sp);
            tvTime.setText(time_sp);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.kline_fullscreenView) {//更多按钮
            showPopWinDown();
        } else if (id == R.id.goLiveView) {
        } else if (id == R.id.gobackView) {//返回按钮
            finish();
        } else if (id == R.id.refreshView_land) {

        } else if (id == R.id.iconback) {//退出全屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (id == R.id.tv_buyUp) {//买涨
            if (TextUtils.equals(marketDataBean.getStatus(), "1")) {
                if (orderPopWindow == null) {
                    orderPopWindow = new OrderPopWindow(priceBuy, symbol_cn, textColor, symbol, marketDataBean, this);
                }
            } else {
                ToastUtils.showShort("当前产品不可交易");
            }
            orderPopWindow.showOrderPopwindow(v, true);
        } else if (id == R.id.tv_buyDown) {//买跌
            if (orderPopWindow == null) {
                orderPopWindow = new OrderPopWindow(priceBuy, symbol_cn, textColor, symbol, marketDataBean, this);
            }
            orderPopWindow.showOrderPopwindow(v, false);
        } else if (id == R.id.tv_tradeclose) {// 查看持仓
            EventBus.getDefault().post(new EvenVo(EvenVo.WATCH_CHI_CHAN));
            finish();
        } else if (id == R.id.line_productnotice) {//提醒
        } else if (id == R.id.line_product_fullscr) {//全屏
            //保存最新买入价
            lruCache.put(AppConstans.price_buy, priceBuy);
            //保存最新涨跌值和涨跌幅
            lruCache.put("rate", rate);
            //保存字体颜色
            lruCache.put("textColor", textColor);
            lruCache.put("time", time);
            if (Configuration.ORIENTATION_LANDSCAPE == getResources()
                    .getConfiguration().orientation) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            if (popWinForMoreOption != null && popWinForMoreOption.isShowing()) {
                popWinForMoreOption.dismiss();
            }
        } else if (id == R.id.line_product_line) {//辅助线

        } else if (id == R.id.line_productqa) {//帮助说明

        } else if (id == R.id.goWeipanListView) {

        } else if (id == R.id.tv_guadan) {
            //挂单
            if (guaDanPopWindow == null) {
                guaDanPopWindow = new GuaDanPopWindow(this, priceBuy, symbol_cn, textColor, symbol, marketDataBean);
            }
            guaDanPopWindow.showWindow(v);
        }
    }

    /**
     * 根据当前点击的条目计算开始时间和结束时间
     *
     * @param code 当前点击条目 对应 的 值
     * @return
     */
    private String[] getStartDateAndEndDate(String code) {
        String[] arr = new String[2];
        //获取当前时间戳
        long nowMills = System.currentTimeMillis();
        //当前的 code
        Integer code_int_custom = Integer.valueOf(code);
        //格式化之后的  结束时间
        String endDate = DateUtil.parseDateToStr(new Date(nowMills), simpleDateFormat);
        LogUtils.d("传递之前的  endDate    :  " + endDate);
        //计算往前推 50 个 code 的毫秒数 ,要先转 long 类型，否则计算失真
        long intervalMills = ((long) code_int_custom) * 60 * 1000 * 50;
        //计算开始时间的毫秒数
        long startMills = nowMills - intervalMills;
        //格式化之后的  开始时间
        String startDate = DateUtil.parseDateToStr(new Date(startMills), simpleDateFormat);
        LogUtils.d("传递之前的  startDate    :  " + startDate);
        arr[0] = startDate;
        arr[1] = endDate;
        return arr;
    }

    /**
     * 产品详情页更多弹窗
     */
    private void showPopWinDown() {
        int w = (int) getResources().getDimension(R.dimen.margin_143dp);
        int h = (int) getResources().getDimension(R.dimen.margin_175dp);
        if (popWinForMoreOption == null) {
            View view = View.inflate(ProductActivity.this, R.layout.layout_productmore, null);
            LinearLayout line_productnotice = view.findViewById(R.id.line_productnotice);
            TextView text_product_noticenum = view.findViewById(R.id.text_product_noticenum);
            LinearLayout line_product_fullscr = view.findViewById(R.id.line_product_fullscr);
            LinearLayout line_product_line = view.findViewById(R.id.line_product_line);
            ImageView img_product_oc = view.findViewById(R.id.img_product_oc);
            TextView text_product_oc = view.findViewById(R.id.text_product_oc);
            LinearLayout line_productqa = view.findViewById(R.id.line_productqa);
            text_product_noticenum.setText("(" + 0 + ")");
            line_productnotice.setOnClickListener(this);
            line_product_fullscr.setOnClickListener(this);
            line_product_line.setOnClickListener(this);
            line_productqa.setOnClickListener(this);
            img_product_oc.setImageResource(R.mipmap.img_product_helpline);
            text_product_oc.setText(R.string.product_closeline);
            popWinForMoreOption = new PopupWindow(view, w, h);
            popWinForMoreOption.setFocusable(true);//影响listView的item点击
            popWinForMoreOption.setOutsideTouchable(true);
            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popWinForMoreOption.setBackgroundDrawable(new BitmapDrawable());
        }
        popWinForMoreOption.showAsDropDown(klineFullscreenView, -w + (int) getResources().getDimension(R.dimen.margin_40dp), 0);
    }

    /**
     * 替换 Fragment
     *
     * @param fragment
     */
    protected void replaceFragment(Fragment fragment) {
        if (isFinishing() || fragmentContainer == null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 获取 KlineFragment 对象，并传递数据
     *
     * @param cycleCode
     * @param startDate
     * @param endDate
     * @return
     */
    protected KLineFragment getKlineFragment(String cycleCode, String startDate, String endDate) {
        Bundle bundle = new Bundle();
//        //代码，品种英文名称
//        bundle.putString("treaty", optional.getTreaty());
        //类型  “FXBTG"
//        bundle.putString("type", optional.getType());
        //一分钟，五分钟等的代号 ，一个 String 类型的数字
        bundle.putString("interval", cycleCode);
        //品种英文名称   代码
        bundle.putString("code", symbol);
        //开始时间
        bundle.putString("startDate", startDate);
        //结束时间
        bundle.putString("endDate", endDate);
        //小数位精度
        bundle.putInt("digit", digit);
        return KLineFragment.newInstance(bundle);
    }

}
