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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.moyacs.canary.base.BaseActivity2;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.kchart.fragment.KLineFragment;
import com.moyacs.canary.kchart.fragment.ProFormatConfig;
import com.moyacs.canary.main.MainActivity2;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.pay.DialogFragment_order;
import com.moyacs.canary.pay.GuaDanPopWindow;
import com.moyacs.canary.pay.OrderPopWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class ProductActivity extends BaseActivity2 implements OnClickListener {

    public static String TAG = "ProductActivity";

    ProductActivity context = this;

    RecyclerView recyclerView, landRecycleViewCycle;
    TextView tv_title, tv_time, tv_open, tv_closed, tv_high, tv_low, tv_rate,
            tv_buy, tv_sell,
            tv_title_land, tv_time_land, tv_open_land, tv_closed_land, tv_high_land,
            tv_low_land, tv_rate_land, tv_buy_land, tv_sell_land;
    View fragment_container = null;
    View goLiveView = null, refreshView_land = null;
    TextView tv_latest, tv_latest_land;
    View kline_fullscreenView;
    TextView tv_tradeclose;
    View tradeLayout, goWeipanListView;
    public static final String CODE_DK_SHOCK = "shock";
    public static final String CODE_DK_FUTURE = "future";

    public static final int RESULT_DK_VIEW = 100;


    View line_trade_bcjg;
    TextView text_trade_bcjg;
    //跟布局
    private View rootView;
    private String excode;
    //昨收价格
    private double close;
    //自选列表中的 字体颜色
    private boolean whatColor;
    //格式化过后的   涨跌值 + 涨跌幅 ，主要为了解决切换屏幕的时候，netty 获取数据不及时，造成的空白现象
    private String rate;
    //小数点位数，主要是买入价，默认为 5
    private int digit;
    //最新的买入价
    private String price_buy;

    //最新的卖出价
    private String price_sell;
    //时间
    private String time;
    //品种中文名称
    private String symbol_cn;
    private SPUtils spUtils;
    //时间
    private String time1;
    /**
     * 是否获取到新价格
     */
    private boolean isGetNewPrice = false;
    /**
     * 下单页面
     */
    private OrderPopWindow orderPopWindow;
    private DialogFragment_order dialogFragment_order;
    /**
     * 止损 止盈 点位
     */
    private int stops_level;
    private TextView tvGuaDan;
    private GuaDanPopWindow guaDanPopWindow;
    //产品购买 单价 过夜费等
    private TradeVo.Trade tradeVo;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        numberUtils = new NumberUtils();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 上一次的价格，String 类型
     */
    private String oldPrice;
    //旧价格
    private double price_old;
    //新价格
    private double price_new;
    //新旧价格差
    private double subtract;
    //涨跌幅
    private double range;
    //字体颜色
    private int textColor = 0;

    /**
     * 获取 EventBus 传递的 Socket 数据
     *
     * @param quotation
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyData(Quotation quotation) {

        //过滤数据，只有与当前页面品种相同才处理
        if (symbol.equals(quotation.getSymbol())) {
            isGetNewPrice = true;
            // 获取旧价格，String 类型
            oldPrice = (String) tv_latest.getText();
            //获取最新买入价，并显示
            price_new = quotation.getAsk();

            //格式化买入价
            String price_new_scale = NumberUtils.setScale(price_new, digit);
            //赋值最新买入价
            price_buy = price_new_scale;
            //赋值最新卖出价
            price_sell = NumberUtils.setScale(quotation.getBid(), digit);

          /*  //下单页面的价格更新
            if (dialogFragment_order != null) {
                dialogFragment_order.setNewPrice(isGetNewPrice, price_buy, price_sell);
            }*/

            if (orderPopWindow != null) {
                orderPopWindow.setNewPrice(isGetNewPrice, Double.parseDouble(price_buy));
            }

            if (guaDanPopWindow != null) {
                guaDanPopWindow.setNewPrice(isGetNewPrice, price_buy);
            }

            //时间
            Long unixTime = quotation.getUnixTime();
            long l = unixTime + 5 * 60 * 60 * 1000;
            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String time = TimeUtils.millis2String(l, simpleDateFormat);
//            time1 = quotation.getTime();
            time1 = time;
            //竖屏
            if (tv_latest != null) {
                //设置最新买入价
                tv_latest.setText(price_new_scale + "");
                //设置时间
                tv_time.setText(time1);
            }
            //横屏
            if (tv_latest_land != null) {
                //设置最新买入价
                tv_latest_land.setText(price_new_scale + "");
                //设置时间
                tv_time_land.setText(time1);
            }
            //比较新价格与昨收，
            double compare = numberUtils.compare(price_new, close);

            if (compare == 1) {//新价格 > 昨收   涨
                //计算涨跌值 新 - 旧
                subtract = numberUtils.subtract(price_new, close);
                String s = numberUtils.doubleToString(subtract);
                //字体颜色
                textColor = getResources().getColor(R.color.color_opt_gt);
                //计算涨跌幅
                range = numberUtils.divide(subtract, close, 4);
                //涨跌幅格式化
                String s2 = NumberUtils.setScale2(range);
                //竖屏时候设置
                if (tv_rate != null) {
                    tv_rate.setText("+" + s + "   +" + s2 + "%");
                }
                //横屏时候设置
                if (tv_rate_land != null) {
                    tv_rate_land.setText("+" + s + "   +" + s2 + "%");
                }

                //设置 涨跌值 涨跌幅
            } else if (compare == -1) {//新价格 < 昨收   跌
                //计算涨跌值  旧 - 新
                subtract = numberUtils.subtract(close, price_new);
                String s = numberUtils.doubleToString(subtract);
                //字体颜色
                textColor = getResources().getColor(R.color.trade_down);
                //计算涨跌幅
                range = numberUtils.divide(subtract, close, 4);
                //涨跌幅格式化
                String s2 = NumberUtils.setScale2(range);
                //设置 涨跌值 涨跌幅

                rate = "-" + s + "   -" + s2 + "%";
                //竖屏时候设置
                if (tv_rate != null) {
                    tv_rate.setText(rate);
                }
                //横屏时候设置
                if (tv_rate_land != null) {
                    tv_rate_land.setText(rate);
                }
            } else {////新价格 = 旧价格   平

            }
            //改变字体颜色
            tv_latest.setTextColor(textColor);
            //改变涨跌值和涨跌幅的颜色
            //竖屏
            if (tv_rate != null) {
                tv_rate.setTextColor(textColor);
            }
            //横屏
            if (tv_rate_land != null) {
                tv_rate_land.setTextColor(textColor);
            }
        }

    }


    /**
     * 是否显示标题栏
     *
     * @return
     */
    @Override
    protected boolean isshowToolbar() {
        return false;
    }

    /**
     * 是否为横屏状态
     *
     * @param isLand
     */
    protected void handeVisibleView(boolean isLand) {
        View layoutTop = rootView.findViewById(R.id.layoutTop);
        View layoutTop_land = rootView.findViewById(R.id.layoutTop_land);
        View layoutBottom = rootView.findViewById(R.id.layoutBottom);
        View layoutBottom_land = rootView.findViewById(R.id.layoutBottom_land);
        if (isLand) {
            //横屏状态
            if (layoutTop != null)
                layoutTop.setVisibility(View.GONE);
            if (layoutTop_land != null)
                layoutTop_land.setVisibility(View.VISIBLE);
            if (layoutBottom != null)
                layoutBottom.setVisibility(View.GONE);
//            if (layoutBottom_land != null && selectPos != 0)//默认分时第一位
            if (layoutBottom_land != null)//默认分时第一位
                layoutBottom_land.setVisibility(View.VISIBLE);


            if (landRecycleViewCycle != null)
                landRecycleViewCycle.getAdapter().notifyDataSetChanged();

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

            if (recyclerView != null)
                recyclerView.getAdapter().notifyDataSetChanged();

        }
    }


    @Override
    protected void updateOptionsMenu(Menu menu) {

    }

    public void initView() {
        List<KlineCycle> klineCycles = new ArrayList<>();

        klineCycles.add(new KlineCycle("1分钟", PARAM_KLINE_1M_WEIPAN_new));
        klineCycles.add(new KlineCycle("5分钟", PARAM_KLINE_5M_WEIPAN_new));
        klineCycles.add(new KlineCycle("15分钟", PARAM_KLINE_15M_WEIPAN_new));
        klineCycles.add(new KlineCycle("30分钟", PARAM_KLINE_30M_WEIPAN_new));
        klineCycles.add(new KlineCycle("1小时", PARAM_KLINE_60M_WEIPAN_new));
        klineCycles.add(new KlineCycle("4小时", PARAM_KLINE_4H_WEIPAN_new));
        klineCycles.add(new KlineCycle("日线", PARAM_KLINE_1D_WEIPAN_new));
        klineCycles.add(new KlineCycle("周线", PARAM_KLINE_WEEK_WEIPAN_new));

        tradeLayout = rootView.findViewById(R.id.tradeLayout);
        goWeipanListView = rootView.findViewById(R.id.goWeipanListView);
        goWeipanListView.setOnClickListener(this);
        TextView tv_buyUp = (TextView) rootView.findViewById(R.id.tv_buyUp);
        TextView tv_buyDown = (TextView) rootView.findViewById(R.id.tv_buyDown);
        tv_tradeclose = (TextView) rootView.findViewById(R.id.tv_tradeclose);
        tv_buyUp.setOnClickListener(this);
        tv_buyDown.setOnClickListener(this);
        tv_tradeclose.setOnClickListener(this);
        //返回按钮
        ImageView imageView = rootView.findViewById(R.id.gobackView);
        imageView.setOnClickListener(this);


        tradeLayout.setVisibility(View.VISIBLE);
        goWeipanListView.setVisibility(View.GONE);
        goWeipanListView.setVisibility(View.GONE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//linearLayoutManager is already attached recyclerView不能共用linearLayoutManager
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        //初始化 竖屏时候的 recycler_view
        if (recyclerView != null) {
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new MyAdapter(klineCycles, MyAdapter.ITEM_NORMAL));
            recyclerView.setItemViewCacheSize(klineCycles.size());
        }
        //初始化 横屏时候的 recycler_view
        landRecycleViewCycle = (RecyclerView) rootView.findViewById(R.id.landRecycleViewCycle);
        if (landRecycleViewCycle != null) {
            LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
            linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
            landRecycleViewCycle.setLayoutManager(linearLayoutManager3);
            landRecycleViewCycle.setAdapter(new MyAdapter(klineCycles, MyAdapter.ITEM_LAND));
            landRecycleViewCycle.setItemViewCacheSize(klineCycles.size());
        }

        kline_fullscreenView = rootView.findViewById(R.id.kline_fullscreenView);
        if (kline_fullscreenView != null)
            kline_fullscreenView.setOnClickListener(this);
        View iconback = rootView.findViewById(R.id.iconback);
        if (iconback != null)
            iconback.setOnClickListener(this);

        goLiveView = rootView.findViewById(R.id.goLiveView);
        if (goLiveView != null)
            goLiveView.setOnClickListener(this);
        refreshView_land = rootView.findViewById(R.id.refreshView_land);
        if (refreshView_land != null)
            refreshView_land.setOnClickListener(this);

        //common view
        tv_latest = rootView.findViewById(R.id.tv_latest);
        tv_rate = (TextView) rootView.findViewById(R.id.tv_rate);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_time = (TextView) rootView.findViewById(R.id.tv_time);
        tv_open = (TextView) rootView.findViewById(R.id.tv_open);
        tv_closed = (TextView) rootView.findViewById(R.id.tv_closed);
        tv_high = (TextView) rootView.findViewById(R.id.tv_high);
        tv_low = (TextView) rootView.findViewById(R.id.tv_low);
//        tv_buy = (TextView) rootView.findViewById(R.id.tv_buy);
//        tv_sell = (TextView) rootView.findViewById(R.id.tv_sell);
        fragment_container = rootView.findViewById(R.id.fragment_container);


        tv_latest_land = rootView.findViewById(R.id.tv_latest_land);
        tv_rate_land = (TextView) rootView.findViewById(R.id.tv_rate_land);
        tv_title_land = (TextView) rootView.findViewById(R.id.tv_title_land);
        tv_time_land = (TextView) rootView.findViewById(R.id.tv_time_land);
        tv_open_land = (TextView) rootView.findViewById(R.id.tv_open_land);
        tv_closed_land = (TextView) rootView.findViewById(R.id.tv_closed_land);
        tv_high_land = (TextView) rootView.findViewById(R.id.tv_high_land);
        tv_low_land = (TextView) rootView.findViewById(R.id.tv_low_land);
//        tv_buy_land = (TextView) rootView.findViewById(R.id.tv_buy_land);
//        tv_sell_land = (TextView) rootView.findViewById(R.id.tv_sell_land);

        line_trade_bcjg = rootView.findViewById(R.id.line_trade_bcjg);
        text_trade_bcjg = (TextView) rootView.findViewById(R.id.text_trade_bcjg);
        tvGuaDan = rootView.findViewById(R.id.tv_guadan);
        tvGuaDan.setOnClickListener(this);
        lineAnimate();

    }

    /**
     * 图片线条动画
     */
    void lineAnimate() {
        //使用Runnalbe()来加载,默认xml中不会播放
        final ImageView plineView = (ImageView) rootView.findViewById(R.id.plineView);
        plineView.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.p_head_line);
                plineView.startAnimation(animation);
            }
        });
    }

    /**
     * 当前品种的英文名称
     */
    private String symbol;
    //数据处理类
    private NumberUtils numberUtils;

    @Override
    protected void initIntentData(Intent intent) {
        //获取产品购买信息
        tradeVo = (TradeVo.Trade) getIntent().getSerializableExtra("trade");
        //品种中文名称
        symbol_cn = intent.getStringExtra(AppConstans.symbol_cn);
        //品种英文名称
        symbol = intent.getStringExtra(AppConstans.symbol);
        //excode
        excode = intent.getStringExtra("excode");
        String[] startDataAndEndData = getStartDateAndEndDate(PARAM_KLINE_1M_WEIPAN_new);
        //获取精度
        digit = intent.getIntExtra(AppConstans.digit, 5);
        //止损 止盈 点位
        stops_level = intent.getIntExtra(AppConstans.stops_level, 0);

        replaceFragment(getKlineFragment(PARAM_KLINE_1M_WEIPAN_new, startDataAndEndData[0], startDataAndEndData[1]));

        //设置买价
        price_buy = intent.getStringExtra(AppConstans.price_buy);
        //获取传递过来的最新卖出价
        price_sell = intent.getStringExtra(AppConstans.price_sell);
        tv_latest.setText(price_buy);
        //设置涨跌值 涨跌幅
        tv_rate.setText(intent.getStringExtra(AppConstans.value) + "   " + intent.getStringExtra(AppConstans.range));
        //设置时间
        this.time = intent.getStringExtra(AppConstans.time);
        tv_time.setText(this.time);
        //最高
        String high = intent.getStringExtra(AppConstans.high);
        //最低
        String low = intent.getStringExtra(AppConstans.low);
        //今开
        String open = intent.getStringExtra(AppConstans.open);
        //昨收
        close = intent.getDoubleExtra(AppConstans.close, 0);
        //字体颜色
        whatColor = intent.getBooleanExtra(AppConstans.whatColor, false);
        if (whatColor) {
            this.textColor = getResources().getColor(R.color.color_opt_gt);
        } else {
            this.textColor = getResources().getColor(R.color.trade_down);
        }
        //横屏
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //设置标题
            tv_title_land.setText(symbol_cn);
            //最高
            tv_high_land.setText(high);
            //最低
            tv_low_land.setText(low);
            //今开
            tv_open_land.setText(open);
            //昨收
            tv_closed_land.setText(NumberUtils.setScale(close, digit));
            //改变字体颜色
            tv_latest_land.setTextColor(this.textColor);
            tv_rate_land.setTextColor(this.textColor);
        } else {//竖屏
            //设置标题
            tv_title.setText(symbol_cn);
            //最高
            tv_high.setText(high);
            //最低
            tv_low.setText(low);
            //今开
            tv_open.setText(open);
            //昨收
            tv_closed.setText(NumberUtils.setScale(close, digit));
            //改变字体颜色
            tv_latest.setTextColor(this.textColor);
            tv_rate.setTextColor(this.textColor);
        }

        spUtils = SPUtils.getInstance();
        //获取最新买入价
        String price_sp = spUtils.getString(AppConstans.price_buy);
        //获取最新涨跌值和涨跌幅
        String rate_sp = spUtils.getString("rate", this.rate);
        //获取字体颜色
        int textColor_sp = spUtils.getInt("textColor");
        //获取时间
        String time_sp = spUtils.getString("time");
        if (time_sp.equals("")) {
            return;
        }
        //横屏
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tv_latest_land.setText(price_sp);
            tv_rate_land.setText(rate_sp);
            tv_latest_land.setTextColor(textColor_sp);
            tv_rate_land.setTextColor(textColor_sp);
            tv_time_land.setText(time_sp);
        } else {
            tv_latest.setText(price_sp);
            tv_rate.setText(rate_sp);
            tv_latest.setTextColor(textColor_sp);
            tv_rate.setTextColor(textColor_sp);
            tv_time.setText(time_sp);
        }
    }

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        rootView = getLayoutInflater().inflate(R.layout.activity_product, null, false);
        initView();
//        initData();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前为横屏
            handeVisibleView(true);
        } else {
            //切换到竖屏
            handeVisibleView(false);
        }
        return rootView;
    }


    /**
     * 数据请求完毕之后 ，设置数据
     *
     * @param optional
     */
    public void setTextValue(Optional optional) {
        if (optional == null)
            return;

        /**
         * 竖屏时候数据设置
         */

        //今开
        if (tv_open != null)
            tv_open.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getOpening(), "")));
        //昨收
        if (tv_closed != null)
            tv_closed.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getClosed(), "")));
        //最高
        if (tv_high != null)
            tv_high.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getHighest(), "")));
        //最低
        if (tv_low != null)
            tv_low.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getLowest(), "")));
//        //买价
//        if (tv_buy != null)
//            tv_buy.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getBuyone(), "")));
//        //卖价
//        if (tv_sell != null)
//            tv_sell.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getSellone(), "")));

        /**
         * 横屏时候数据设置
         */
        if (tv_title_land != null)
            tv_title_land.setText(NVL(optional.getTitle(), ""));
        if (tv_time_land != null)
            tv_time_land.setText(NVL(optional.getAdd_time(), ""));
        if (tv_open_land != null)
            tv_open_land.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getOpening(), "")));
        if (tv_closed_land != null)
            tv_closed_land.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getClosed(), "")));
        if (tv_high_land != null)
            tv_high_land.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getHighest(), "")));
        if (tv_low_land != null)
            tv_low_land.setText(ProFormatConfig.formatByCodes(optional.getCode(), NVL(optional.getLowest(), "")));

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.kline_fullscreenView) {//更多按钮
            showPopWindown();
        } else if (id == R.id.goLiveView) {


        } else if (id == R.id.gobackView) {//返回按钮
            finish();

        } else if (id == R.id.refreshView_land) {

        } else if (id == R.id.iconback) {//退出全屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (id == R.id.tv_buyUp) {//买涨
            if (orderPopWindow == null) {
                orderPopWindow = new OrderPopWindow(price_buy, symbol_cn, textColor, symbol, tradeVo, this);
            }
            orderPopWindow.showOrderPopwindow(v, true);
            /*dialogFragment_order = new DialogFragment_order();
            Bundle bundle = new Bundle();
            bundle.putString("price_buy",price_buy);
            bundle.putString("price_sell",price_sell);
            bundle.putString("symbol_cn",symbol_cn);
            bundle.putInt("textColor",textColor);
            bundle.putString("symbol",symbol);
            bundle.putInt("type_order",1);
            bundle.putInt("digit",digit);
            bundle.putInt("stops_level",stops_level);
            dialogFragment_order.setArguments(bundle);
            dialogFragment_order.show(getSupportFragmentManager(),"order");*/

        } else if (id == R.id.tv_buyDown) {//买跌
            if (orderPopWindow == null) {
                orderPopWindow = new OrderPopWindow(price_buy, symbol_cn, textColor, symbol, tradeVo, this);
            }
            orderPopWindow.showOrderPopwindow(v, false);

           /* dialogFragment_order = new DialogFragment_order();
            Bundle bundle = new Bundle();
            bundle.putString("price_buy", price_buy);
            bundle.putString("price_sell", price_sell);
            bundle.putString("symbol_cn", symbol_cn);
            bundle.putInt("textColor", textColor);
            bundle.putString("symbol", symbol);
            bundle.putInt("type_order", 2);
            bundle.putInt("digit", digit);
            bundle.putInt("stops_level", stops_level);
            dialogFragment_order.setArguments(bundle);
            dialogFragment_order.show(getSupportFragmentManager(), "order");*/

        } else if (id == R.id.tv_tradeclose) {// 查看持仓
            finish();
            MainActivity2.setViewPagerCurrentItme(2);

        } else if (id == R.id.line_productnotice) {//提醒


        } else if (id == R.id.line_product_fullscr) {//全屏

            //保存最新买入价
            spUtils.put(AppConstans.price_buy, price_buy);
            //保存最新涨跌值和涨跌幅
            spUtils.put("rate", rate);
            //保存字体颜色
            spUtils.put("textColor", textColor);
            spUtils.put("time", time);
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
                guaDanPopWindow = new GuaDanPopWindow(this, price_buy, symbol_cn, textColor, symbol, tradeVo);
            }
            guaDanPopWindow.showWindow(v);
        }

    }

    int selectPos = 0;
    View selectTextView = null, currentDKView = null;

    //时间格式化
    private DateFormat simpleDateFormat;


    /**
     * recyclerView 适配器
     */
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<KlineCycle> goodsList;
        int type;
        public static final int ITEM_NORMAL = 0;//正常状态下 竖屏
        public static final int ITEM_LAND = 1;//横屏状态下

        public MyAdapter(List<KlineCycle> goodsList, int type) {
            this.goodsList = goodsList;
            this.type = type;
        }

        public KlineCycle getItem(int position) {
            if (goodsList != null && position < goodsList.size())
                return goodsList.get(position);
            return null;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View rootView = null;
            if (type == ITEM_LAND) {
                rootView = View.inflate(viewGroup.getContext(), R.layout.product_land_cycle_item, null);
            } else {
                rootView = View.inflate(viewGroup.getContext(), R.layout.product_klinecycle_item, null);
            }
            return new MyViewHolder(rootView);
        }

        public void clear() {
            if (null != goodsList) {
                goodsList.clear();
                notifyDataSetChanged();
            }
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            if (myViewHolder.textView == null)
                return;
            if (i == selectPos) {
                selectTextView = myViewHolder.textView;
                selectTextView.setSelected(true);
            } else {
                myViewHolder.textView.setSelected(false);
            }
            final int position = i;
            myViewHolder.textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = goodsList.get(position).getCode();
                    String name = goodsList.get(position).getName();
                    String[] startDataAndEndData = getStartDateAndEndDate(code);
                    //TODO recyclerView 点击切换Fragment
                    Log.i(TAG, "onClick: 开始时间 ：" + startDataAndEndData[0] + "结束时间：" + startDataAndEndData[1]);
                    //周期K线
                    replaceFragment(getKlineFragment(code, startDataAndEndData[0], startDataAndEndData[1]));

                    if (selectTextView != null)
                        selectTextView.setSelected(false);
                    selectTextView = v;
                    selectPos = position;
                    if (selectTextView != null)
                        selectTextView.setSelected(true);
                    if (recyclerView != null)
                        recyclerView.scrollToPosition(position);
                    if (landRecycleViewCycle != null)
                        landRecycleViewCycle.scrollToPosition(position);
                }
            });

            myViewHolder.textView.setText(goodsList.get(i).getName());
        }

        @Override
        public int getItemCount() {
            return goodsList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.item_name);
            }
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
        long nowMills = TimeUtils.getNowMills();

        //当前的 code
        Integer code_int_custom = Integer.valueOf(code);
        //以分钟为单位
//        if (code_int_custom <= Integer.valueOf(PARAM_KLINE_30M_WEIPAN_new)) {
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            //以小时为单位
//        } else if (code_int_custom <= Integer.valueOf(PARAM_KLINE_4H_WEIPAN_new)) {
//            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            //以天为单位
//        } else {
//            simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        }
        //格式化之后的  结束时间
        String endDate = TimeUtils.millis2String(nowMills, simpleDateFormat);
        LogUtils.d("传递之前的  endDate    :  " + endDate);
        Log.i(TAG, "传递之前的  endDate   :  " + endDate);
        //计算往前推 50 个 code 的毫秒数 ,要先转 long 类型，否则计算失真
        long intervalMills = ((long) code_int_custom) * 60 * 1000 * 50;
        //计算开始时间的毫秒数
        long startMills = nowMills - intervalMills;
        //格式化之后的  开始时间
        String startDate = TimeUtils.millis2String(startMills, simpleDateFormat);
        LogUtils.d("传递之前的  startDate    :  " + startDate);
        Log.i(TAG, "传递之前的  startDate   :  " + startDate);
        arr[0] = startDate;
        arr[1] = endDate;
        return arr;
    }

    PopupWindow popWinForMoreOption;
    private LinearLayout line_productnotice;
    private TextView text_product_noticenum;
    private LinearLayout line_product_fullscr;
    private LinearLayout line_product_line;
    private ImageView img_product_oc;
    private TextView text_product_oc;
    private LinearLayout line_productqa;

    /**
     * 产品详情页更多弹窗
     */
    private void showPopWindown() {
        View view = View.inflate(ProductActivity.this, R.layout.layout_productmore, null);
        line_productnotice = (LinearLayout) view.findViewById(R.id.line_productnotice);
        text_product_noticenum = (TextView) view.findViewById(R.id.text_product_noticenum);
        line_product_fullscr = (LinearLayout) view.findViewById(R.id.line_product_fullscr);
        line_product_line = (LinearLayout) view.findViewById(R.id.line_product_line);
        img_product_oc = (ImageView) view.findViewById(R.id.img_product_oc);
        text_product_oc = (TextView) view.findViewById(R.id.text_product_oc);
        line_productqa = (LinearLayout) view.findViewById(R.id.line_productqa);

        text_product_noticenum.setText("(" + 0 + ")");

        line_productnotice.setOnClickListener(this);
        line_product_fullscr.setOnClickListener(this);
        line_product_line.setOnClickListener(this);
        line_productqa.setOnClickListener(this);

        img_product_oc.setImageResource(R.mipmap.img_product_helpline);
        text_product_oc.setText(R.string.product_closeline);

        int w = (int) getResources().getDimension(R.dimen.margin_143dp);
        int h = (int) getResources().getDimension(R.dimen.margin_175dp);
        popWinForMoreOption = new PopupWindow(view, w, h);
        popWinForMoreOption.setFocusable(true);//影响listView的item点击
        popWinForMoreOption.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWinForMoreOption.setBackgroundDrawable(new BitmapDrawable());

        popWinForMoreOption.showAsDropDown(kline_fullscreenView, -w + (int) getResources().getDimension(R.dimen.margin_40dp), 0);
    }

    Optional optional = null;

    /**
     * 刷新行情去数据 需要按照交易所 和code去取
     */
    protected Optional getData() {
        try {
            if (isFinishing())
                return null;
            Optional currentOptional = BakSourceService.getOptionals(context, getInitOptional());
            optional = currentOptional;
            return currentOptional;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 传入的excode 和 品种代码 转成optional
     *
     * @return
     */
    Optional getInitOptional() {
        Optional optional = new Optional();
        optional.setType(excode);
        optional.setCustomCode(symbol);
        optional.setProductCode(symbol);
        return optional;
    }

    /**
     * 替换 Fragment
     *
     * @param fragment
     */
    protected void replaceFragment(Fragment fragment) {
        if (isFinishing())
            return;
//        if (optional == null) {
//            initData();
//            return;
//        }
        if (fragment_container == null)
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
//        if (addToBackStack) {
//            transaction.addToBackStack(null);
//        }
        transaction.commitAllowingStateLoss();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //分时 用的，
////                notifyToDrawOrderHelpLine();
////                notifyToDrawPNHelpLine();
//            }
//        }, 500);

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

        //保证传递的数据不为 null
//        if (optional == null) {
//            initData();
//            return null;
//        }
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


        KLineFragment fragment = KLineFragment.newInstance(bundle);
        return fragment;

    }

    public String NVL(Object obj, String value) {
        try {
            if (obj == null) {
                return value;
            }
            if (String.valueOf(obj).trim().equals("")) {
                return value;
            }
            if (String.valueOf(obj).trim().equalsIgnoreCase("null")) {
                return value;
            }
            return String.valueOf(obj);
        } catch (Exception e) {
            return value;
        }
    }

}
