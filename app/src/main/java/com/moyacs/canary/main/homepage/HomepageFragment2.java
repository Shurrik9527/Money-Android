package com.moyacs.canary.main.homepage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.moyacs.canary.base.BaseDelegateAdapter;
import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.im.KefuActivity;
import com.moyacs.canary.main.deal.net_tab2.ChiCangDateBean;
import com.moyacs.canary.main.homepage.contract.MarketContract;
import com.moyacs.canary.main.homepage.contract.MarketPresenterImpl;
import com.moyacs.canary.main.homepage.net.BannerDate;
import com.moyacs.canary.main.homepage.net.DealChanceDate;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.product_fxbtg.ProductActivity;
import com.moyacs.canary.widget.pullrefreshlayout.ClassicsHeader;
import com.yan.pullrefreshlayout.PullRefreshLayout;
import com.youth.banner.Banner;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

import static com.moyacs.canary.common.AppConstans.marketDataBeanList;

/**
 * 作者：luoshen on 2018/3/2 0002 10:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：首页
 */

public class HomepageFragment2 extends BaseFragment2 implements MarketContract.MarketView {

    private static final String TAG = "HomepageFragment2";
    @BindView(R.id.recycler_homepage)
    RecyclerView recyclerHomepage;
    PullRefreshLayout pullrefreshLayout;
    Unbinder unbinder;
    private View rootView;
    private Context context;
    /**
     * adapter 数据源，vlayout 框架使用
     */
    private List<DelegateAdapter.Adapter> adapters;

    private MarketContract.MarketPresenter presenter;
    private int banner_viewtype = 1;
    private int banner_bottom1 = 2;
    private int horizontalRecyclerView = 3;
    private int jiaoYiJiHui = 4;
    private int verticalRecyclerView = 5;
    private MyHorizontalRecyclerAdapter myHorizontalRecyclerAdapter;
    /**
     * 记录 价格改变时间
     */
    private String time;
    private BaseDelegateAdapter bannerAdapter;
    //屏幕宽度 动态设置 recyclerView 的宽度为 屏幕的 三分之一
    private int screenWidth;
    /**
     * 交易机会数据源
     */
    private List<DealChanceDate> list_chance;
    /**
     * 交易机会条目对应的 adapter
     */
    private BaseDelegateAdapter baseDelegateAdapter_vertical_item;
    /**
     * 横向 recyclerview adapter
     */
    private BaseDelegateAdapter horizontalRecyclerAdapter;

    private List<TradeVo.Trade> tradeList; // 可交易外汇列表

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        Log.i(TAG, "onCreateView:   addChildInflaterView");
        presenter = new MarketPresenterImpl(this);
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_homepage, null, false);
        pullrefreshLayout = rootView.findViewById(R.id.pullrefreshLayout);
        unbinder = ButterKnife.bind(this, rootView);
        initRefreshLayout();
        initVLayout();
        //给宿主 Activity 的 toolbar 添加 item，为了确保 onCreatOptionMenu 方法被调用。
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    protected void initBundleData(Bundle bundle) {
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void loadData() {
        //防止重复加载数据
        if (list_banner == null) {
            presenter.getBannerList(4);
        }
        if (list_chance == null) {
            presenter.getDealChanceList(10, 0);
        }
        //防止重复加载数据
        if (tradeList == null) {
            presenter.getTradList();
        }
        SPUtils instance = SPUtils.getInstance(AppConstans.allMarket);
        String allMarket = instance.getString(AppConstans.allMarket, "");
        Gson gson = new Gson();
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        Log.i(TAG, "loadData: allMarket    :" + allMarket);
        if (allMarket.equals("") || allMarket.equals("null")) {
            return;
        }
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(allMarket).getAsJsonArray();
        list_recycler = new ArrayList<>();

        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            MarketDataBean marketDataBean = gson.fromJson(user, MarketDataBean.class);
            list_recycler.add(marketDataBean);
        }
        if (list_recycler != null && myHorizontalRecyclerAdapter != null) {
            Log.i(TAG, "loadData: myHorizontalRecyclerAdapter != null");
            myHorizontalRecyclerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化下拉刷新 上拉加载框架
     */
    private void initRefreshLayout() {
        //刷新监听
        pullrefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter == null) {
                    return;
                }
                if (tradeList == null && tradeList.size() <= 0) {
                    return;
                }
                presenter.getMarketList("demo", "");
                presenter.getBannerList(4);
                presenter.getDealChanceList(10, 0);
            }

            @Override
            public void onLoading() {
                LogUtils.d("pullrefreshLayout :    onLoading ");
            }
        });
    }

    /**
     * 阿里快速布局框架  Vlayout
     */
    private void initVLayout() {
        // 配置 Vlayout 相关
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(context);
        recyclerHomepage.setLayoutManager(virtualLayoutManager);
        //adapter 数据源
        adapters = new LinkedList<>();
        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）：
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerHomepage.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 3);
        //顶部 banner 图
        initBanner();
        //新手学堂布局
        initBannerBottom1();
        //横向滑动的 recyclerView 品种实时详情
        initHorizontalRecyclerView();
        //交易机会布局
        initSingleLayoutHelper();
        //交易机会下方条目布局
        initVertivalRecyclerView();

        //为 recyclerView 设置 adapter
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        delegateAdapter.setAdapters(adapters);
        recyclerHomepage.setAdapter(delegateAdapter);
    }

    /**
     * 交易机会下方条目布局
     */
    private void initVertivalRecyclerView() {

        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();

        baseDelegateAdapter_vertical_item = new BaseDelegateAdapter(context, linearLayoutHelper,
                R.layout.vlayout_verticalrecyclerview_item, 10, verticalRecyclerView) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (list_chance == null || list_chance.size() < 10) {
                    return;
                }
                DealChanceDate dealChanceDate = list_chance.get(position);

                //品种名称
                TextView tv_name = holder.getView(R.id.tv_homeinformation_product);
                tv_name.setText(dealChanceDate.getName());
                //趋势 看多
                TextView tv_trend = holder.getView(R.id.tv_homeinformation_abstract);
                tv_trend.setText(dealChanceDate.getTrend());
                //年月日日期
                TextView tv_data = holder.getView(R.id.tv_data);
                tv_data.setText(dealChanceDate.getDate());
                //类型：专家解读
                TextView tv_type = holder.getView(R.id.tv_type);
                tv_type.setText(dealChanceDate.getType());
                //标题
                TextView tv_title = holder.getView(R.id.tv_title);
                tv_title.setText(dealChanceDate.getTitle());
                //建议
                TextView tv_suggest = holder.getView(R.id.tv_suggest);
                tv_suggest.setText(dealChanceDate.getSuggest());
                //时分秒时间
                TextView tv_time = holder.getView(R.id.tv_time);
                tv_time.setText(dealChanceDate.getTime());
                //买涨比例
                TextView tv_tradeup = holder.getView(R.id.tv_homeinformation_tradeup);
                //买涨的数据 0-100
                int range = dealChanceDate.getRange();
                tv_tradeup.setText(range + "%用户买涨");
                //买涨 imageview
                ImageView iv_tradeup = holder.getView(R.id.iv_homeinformation_tradeup);
                LinearLayout.LayoutParams layoutParams_old =
                        (LinearLayout.LayoutParams) iv_tradeup.getLayoutParams();
                LinearLayout.LayoutParams layoutParams_new =
                        new LinearLayout.LayoutParams(0, layoutParams_old.height, range);
                iv_tradeup.setLayoutParams(layoutParams_new);
                //买跌比例
                TextView tv_tradeDown = holder.getView(R.id.tv_homeinformation_tradedown);
                tv_tradeDown.setText(100 - range + "%用户买跌");
                //买跌 imageView
                ImageView iv_tradeDown = holder.getView(R.id.iv_homeinformation_tradedown);
                LinearLayout.LayoutParams layoutParams_old1 =
                        (LinearLayout.LayoutParams) iv_tradeDown.getLayoutParams();
                LinearLayout.LayoutParams layoutParams_new1 =
                        new LinearLayout.LayoutParams(0, layoutParams_old1.height, 100 - range);
                iv_tradeDown.setLayoutParams(layoutParams_new1);
            }
        };
        adapters.add(baseDelegateAdapter_vertical_item);
    }

    /**
     * 交易机会布局
     */
    private void initSingleLayoutHelper() {
        //true 为吸顶，false 为吸底
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper(true);
        BaseDelegateAdapter baseDelegateAdapter = new BaseDelegateAdapter(context, stickyLayoutHelper,
                R.layout.vlayout_jiaoyijihui, 1, jiaoYiJiHui);
        adapters.add(baseDelegateAdapter);
    }

    /**
     * 横向滑动的 recyclerView 品种实时详情
     */
    private void initHorizontalRecyclerView() {
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        horizontalRecyclerAdapter = new BaseDelegateAdapter(context, singleLayoutHelper,
                R.layout.vlayout_horizontalrecyclerview, 1, horizontalRecyclerView) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                RecyclerView recyclerView = holder.getView(R.id.recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);

                myHorizontalRecyclerAdapter = new MyHorizontalRecyclerAdapter();
                recyclerView.setAdapter(myHorizontalRecyclerAdapter);
            }
        };
        adapters.add(horizontalRecyclerAdapter);
    }

    /**
     * banner 下方 新手学堂布局
     */
    private void initBannerBottom1() {
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        BaseDelegateAdapter baseDelegateAdapter = new BaseDelegateAdapter(context, singleLayoutHelper,
                R.layout.vlayout_bannerbottom1, 1, banner_bottom1) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                //新手学堂布局
                View view = holder.getView(R.id.rl_home_newuser_classroom);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.d("新手学堂");
                    }
                });
                //在线客服
                View view1 = holder.getView(R.id.rl_home_newuser_gold);
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.d("在线客服");
                        startActivity(new Intent(getContext(), KefuActivity.class));
                    }
                });
            }
        };
        adapters.add(baseDelegateAdapter);
    }

    /**
     * banner
     */
    private void initBanner() {

        LinearLayoutHelper bannerHelper = new LinearLayoutHelper();

        bannerAdapter = new BaseDelegateAdapter(context, bannerHelper,
                R.layout.vlayout_banner_homepage, 1, banner_viewtype) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                //设置banner 数据源及样式
                Banner banner = holder.getView(R.id.banner);
                ArrayList<String> imageUrls = new ArrayList<>();
                if (list_banner == null || list_banner.size() <= 0) {
                    return;
                }
                for (int i = 0; i < list_banner.size(); i++) {

                    imageUrls.add(list_banner.get(i).getImage());
                }
                //添加数据
                //设置图片加载器
                banner.setImageLoader(new BannerImageLoader());
                //设置图片集合
                banner.setImages(imageUrls);
                //设置轮播时间
                banner.setDelayTime(5000);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
            }
        };
        adapters.add(bannerAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyData(Quotation quotation) {
        //如果没有获取行情列表数据就返回
        if (list_recycler == null) {
            return;
        }
        //如果 fragment 不可见，不解析数据
        if (!isVisibleToUser) {
            return;
        }
        //遍历比对名称
        for (int i = 0; i < list_recycler.size(); i++) {
            MarketDataBean marketDataBean = list_recycler.get(i);
            String symbol = marketDataBean.getSymbol();
            //名称比对成功，就更改价格数据，并更新 对应条目
            if (symbol.equals(quotation.getSymbol())) {
                marketDataBean.setPrice_buy(quotation.getAsk());
                time = quotation.getTime();
                list_recycler.set(i, marketDataBean);
//                第二个参数不为 0 ，表示可以更新item 中的一部分 ui，对应 adapter 中的 三个参数的 onbindviewHolder
                myHorizontalRecyclerAdapter.notifyItemChanged(i, i);
            }
            marketDataBean = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        Log.i(TAG, "onDestroyView:   onDestroyView");
    }

    @Override
    public void unsubscribe() {
        presenter.unsubscribe();
    }

    @Override
    public void showLoadingDailog() {
        if (pullrefreshLayout.isRefreshing()) {
            return;
        }
        //自动刷新，但是不触发刷新回调
        pullrefreshLayout.autoRefresh(false);
    }

    @Override
    public void dismissLoadingDialog() {
//        stopLoading();
        pullrefreshLayout.refreshComplete();
    }

    /**
     * 横向 recyclerView 数据源
     */
    private List<MarketDataBean> list_recycler;

    @Override
    public void getMarketListSucessed(List<MarketDataBean> result) {
        LogUtils.d("获取所有品种详情成功");
        if (marketDataBeanList != null && marketDataBeanList.size() > 0) {
            Log.i(TAG, "marketDataBeanList.size()  :   " + marketDataBeanList.size());
            myHorizontalRecyclerAdapter.notifyDataSetChanged();
            return;
        }
        List<MarketDataBean> listData = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            MarketDataBean marketDataBean = result.get(i);
            for (TradeVo.Trade trade : tradeList) {
                if (TextUtils.equals(marketDataBean.getSymbol(), trade.getSymbolCode())) {
                    marketDataBean.setTrade(trade);
                    listData.add(marketDataBean);
                }
            }
        }
        //给全局对象赋值
        marketDataBeanList = new ArrayList<>(listData);
        list_recycler = new ArrayList<>(listData);
        myHorizontalRecyclerAdapter.notifyDataSetChanged();
        //将所有的品种 按照  （英文 - 中文）  这种格式存入 sp
        SPUtils spUtils = SPUtils.getInstance(AppConstans.allMarket_en_cn);
        for (int i = 0; i < listData.size(); i++) {
            MarketDataBean marketDataBean = listData.get(i);
            spUtils.put(marketDataBean.getSymbol(), marketDataBean.getSymbol_cn());
        }
    }

    @Override
    public void getMarketListFailed(String errormsg) {
        LogUtils.d("获取所有品种详情失败");
    }

    /**
     * 轮播图数据源
     */
    private List<BannerDate.Banner> list_banner;

    @Override
    public void getBannerListSucessed(List<BannerDate.Banner> result) {
        list_banner = new ArrayList<>(result);
        //更新 banner 数据源
        bannerAdapter.notifyDataSetChanged();
        LogUtils.d("获取 banner 列表成功");
    }

    @Override
    public void getBannerListFailed(String errormsg) {
        LogUtils.d("获取 banner 列表失败");
    }

    @Override
    public void getDealChanceListResponseSucessed(List<DealChanceDate> result) {
        list_chance = new ArrayList<>(result);
        baseDelegateAdapter_vertical_item.notifyDataSetChanged();
        Log.i(TAG, "getDealChanceListResponseSucessed: 交易机会获取成功： " + result.size());
    }

    @Override
    public void getDealChanceListResponseFailed(String errormsg) {
        Log.i(TAG, "getDealChanceListResponseFailed: 交易机会获取失败");
    }

    /**
     * 对所有行情列表进行二次处理
     *
     * @param result
     */
    @Override
    public void doOnNext(List<MarketDataBean> result) {
        //转化为 json 并存入 sp
        Gson gson = new Gson();
        String s = gson.toJson(result);
        SPUtils.getInstance(AppConstans.allMarket).put(AppConstans.allMarket, s);
        Log.i(TAG, "doOnNext:    " + s);
    }

    @Override
    public void getTradListSuccess(List<TradeVo.Trade> list) {
        if (tradeList == null) {
            tradeList = new ArrayList<>();
        }
        tradeList.clear();
        tradeList.addAll(list);
        presenter.getMarketList("live", "");
    }


    @Override
    public void getTradListFiled(String filedMsg) {

    }

    /**
     * 上一次的价格
     */
    private String oldPrice;
    //旧价格
    private double oldprice_d;
    //买入价 动画闪跳颜色
    private int Animatorcolor = 0;
    //新旧价格差
    private double subtract = 0;
    //买入价的字体颜色
    private int rangeColor = 0;
    //涨跌幅
    private double range;
    //涨跌幅：拼接好的字符串
    private String range_;
    //决定跳转到的页面字体显示什么颜色
    private boolean whatColor;
    //昨收
    private double close;
    //涨跌值
    private String value;

    /**
     * 横向  recyclerView 的adapter
     */
    public class MyHorizontalRecyclerAdapter extends RecyclerView.Adapter<MyHorizontalRecyclerAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vlayout_horizontalrecyclerview_item, parent, false);
            //设置 recyclerView 条目的宽度
            view.getLayoutParams().width = screenWidth / 3;
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (list_recycler == null) {
                return;
            }
            MarketDataBean marketDataBean = list_recycler.get(position);
            holder.tvName.setText(marketDataBean.getSymbol_cn());
            if (marketDataBean != null) {
//                    获取旧价格，String类型
                oldPrice = (String) holder.tvPrice.getText();
                //昨收
                close = marketDataBean.getClose();

//                    即将赋值的价格
//                    double newPrice_d = marketDataBean.getPrice_buy();
                double newPrice_d = marketDataBean.getPrice_buy() == 0 ? marketDataBean.getOpen() : marketDataBean.getPrice_buy();
                //根据保留的小数位截取数据
                String newPrice_d_scale = NumberUtils.setScale(newPrice_d, marketDataBean.getDigit());
                //设置最新价格
                holder.tvPrice.setText(newPrice_d_scale + "");

                //第一次进入的时候 oldPrice 没数据
                if (oldPrice != null && !oldPrice.equals("null") && !oldPrice.equals("")) {
                    oldprice_d = Double.valueOf(oldPrice);
                    //比较昨收价格  与 最新买入价 大小
                    int compare = NumberUtils.compare(newPrice_d, close);
                    //比较，新旧买入价
                    int compare2 = NumberUtils.compare(newPrice_d, oldprice_d);

                    //主要是为了保证 相减始终为正数，正负号手动添加
                    if (compare == -1) {// 新价格 < 昨收价格
                        whatColor = false;
                        //计算新旧价格差
                        subtract = NumberUtils.subtract(close, newPrice_d);
                        rangeColor = getResources().getColor(R.color.trade_down);
                        //计算涨跌值
                        value = "-" + NumberUtils.doubleToString(subtract);
                        //计算涨跌幅
                        range = NumberUtils.divide(subtract, close, 4);
                        //涨跌幅数据格式化
                        String s = NumberUtils.setScale2(range);
                        //跌 加 - 号
                        range_ = "-" + s + "%";
                        holder.tvvalueAndRate.setText(value + "   " + range_);

                    } else if (compare == 0) {

                    } else {//新价格 > 昨收价格
                        whatColor = true;
                        rangeColor = getResources().getColor(R.color.color_opt_gt);
                        subtract = NumberUtils.subtract(newPrice_d, close);
                        //计算涨跌值
                        value = "+" + NumberUtils.doubleToString(subtract);
                        //计算涨跌幅
                        range = NumberUtils.divide(subtract, close, 4);
                        //涨跌幅数据格式化
                        String s = NumberUtils.setScale2(range);
                        //涨 加 + 号
                        range_ = "+" + s + "%";
                        holder.tvvalueAndRate.setText(value + "   " + range_);
                    }
                    // 价格 执行动画的颜色， 靠 前后两次买入价对比决定
                    if (compare2 == -1) {//新价格 < 旧价格 ，跌
                        Animatorcolor = getResources().getColor(R.color.color_opt_lt_shan);
                    } else if (compare2 == 1) {// 新价格 > 旧价格 ， 涨
                        Animatorcolor = getResources().getColor(R.color.color_opt_gt_shan);
                    } else {

                    }
                    //价格  执行动画
                    getAnimator(holder.relativeRoot, Animatorcolor);
                    //买入价的字体颜色 ，
                    holder.tvPrice.setTextColor(rangeColor);
                    holder.tvvalueAndRate.setTextColor(rangeColor);
                }
            }
            //外汇点击进入详情
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentProductActivity(marketDataBeanList.get(position));
                }
            });
        }

     /*   @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            //表示 viewHolder 的整个条目所有数据都改变了！
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
            } else {
                final MarketDataBean marketDataBean = list_recycler.get(position);
                //数据不为空//表示，viewHolder 的一部分数据改变
                if (marketDataBean != null) {
//                    获取旧价格，String类型
                    oldPrice = (String) holder.tvPrice.getText();
                    //昨收
                    close = marketDataBean.getClose();

//                    即将赋值的价格
//                    double newPrice_d = marketDataBean.getPrice_buy();

                    double newPrice_d = marketDataBean.getPrice_buy() == 0 ? marketDataBean.getClose() : marketDataBean.getPrice_buy();
                    //根据保留的小数位截取数据
                    String newPrice_d_scale = NumberUtils.setScale(newPrice_d, marketDataBean.getDigit());
                    //设置最新价格
                    holder.tvPrice.setText(newPrice_d_scale + "");

                    //第一次进入的时候 oldPrice 没数据
                    if (oldPrice != null && !oldPrice.equals(null) && !oldPrice.equals("")) {
                        oldprice_d = Double.valueOf(oldPrice);
                        //比较昨收价格  与 最新买入价 大小
                        int compare = NumberUtils.compare(newPrice_d, close);
                        //比较，新旧买入价
                        int compare2 = NumberUtils.compare(newPrice_d, oldprice_d);

                        //主要是为了保证 相减始终为正数，正负号手动添加
                        if (compare == -1) {// 新价格 < 昨收价格
                            whatColor = false;
                            //计算新旧价格差
                            subtract = NumberUtils.subtract(close, newPrice_d);
                            rangeColor = getResources().getColor(R.color.trade_down);
                            //计算涨跌值
                            value = "-" + NumberUtils.doubleToString(subtract);
                            //计算涨跌幅
                            range = NumberUtils.divide(subtract, close, 4);
                            //涨跌幅数据格式化
                            String s = NumberUtils.setScale2(range);
                            //跌 加 - 号
                            range_ = "-" + s + "%";
                            holder.tvvalueAndRate.setText(value + "   " + range_);

                        } else if (compare == 0) {

                        } else {//新价格 > 昨收价格
                            whatColor = true;
                            rangeColor = getResources().getColor(R.color.color_opt_gt);
                            subtract = NumberUtils.subtract(newPrice_d, close);
                            //计算涨跌值
                            value = "+" + NumberUtils.doubleToString(subtract);
                            //计算涨跌幅
                            range = NumberUtils.divide(subtract, close, 4);
                            //涨跌幅数据格式化
                            String s = NumberUtils.setScale2(range);
                            //涨 加 + 号
                            range_ = "+" + s + "%";
                            holder.tvvalueAndRate.setText(value + "   " + range_);

                        }
                        // 价格 执行动画的颜色， 靠 前后两次买入价对比决定
                        if (compare2 == -1) {//新价格 < 旧价格 ，跌
                            Animatorcolor = getResources().getColor(R.color.color_opt_lt_shan);
                        } else if (compare2 == 1) {// 新价格 > 旧价格 ， 涨
                            Animatorcolor = getResources().getColor(R.color.color_opt_gt_shan);
                        } else {

                        }
                        //价格  执行动画
                        getAnimator(holder.relativeRoot, Animatorcolor);
                        //买入价的字体颜色 ，
                        holder.tvPrice.setTextColor(rangeColor);
                        holder.tvvalueAndRate.setTextColor(rangeColor);
                    }
                }
            }
        }*/

        @Override
        public int getItemCount() {
            if (list_recycler != null) {
                return list_recycler.size();
            }
            return 0;
        }

        /**
         * 为了防止 item 数据错乱，所以返回 position
         *
         * @param position
         * @return
         */
        @Override
        public int getItemViewType(int position) {
            return position;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_price)
            TextView tvPrice;
            @BindView(R.id.tv_valueAndRate)
            TextView tvvalueAndRate;

            @BindView(R.id.relative_root)
            RelativeLayout relativeRoot;

            public ViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }
        }
    }

    //属性动画
    private ValueAnimator colorAnim;

    /**
     * 渐变动画
     */
    private void getAnimator(final View view, final int resultColor) {
  /*      if (colorAnim != null && colorAnim.isRunning()) {
            colorAnim.cancel();
            colorAnim = null;
        }
        colorAnim = ValueAnimator.ofInt(100);
        colorAnim.setDuration(100);
        colorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                view.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setBackgroundColor(resultColor);
            }
        });
        colorAnim.start();*/

        colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", getResources().getColor(R.color.white), resultColor);
        colorAnim.setDuration(500); // 动画时间为2s
        colorAnim.setEvaluator(new ArgbEvaluator()); // 设置估值器
        //监听动画执行完毕
        colorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                colorAnim = null;
            }
        });
        colorAnim.setRepeatCount(1);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE); // 设置变化反转效果，即第一次动画执行完后再次执行时背景色时从后面的颜色值往前面的变化
        colorAnim.start();
    }

    /**
     * 跳转到购买详情
     *
     * @param marketDataBean
     */
    private void intentProductActivity(MarketDataBean marketDataBean) {
        //跳转详情页面
        Intent intent = new Intent(getContext(), ProductActivity.class);
        //中文名
        intent.putExtra(AppConstans.symbol_cn, marketDataBean.getSymbol_cn());
        //英文名
        intent.putExtra(AppConstans.symbol, marketDataBean.getSymbol());
        //时间
        intent.putExtra(AppConstans.time, time);
        //涨跌幅
        intent.putExtra(AppConstans.range, range_);
        //买入价
        intent.putExtra(AppConstans.price_buy, marketDataBean.getPrice_buy() + "");
        //卖出价
        intent.putExtra(AppConstans.price_sell, marketDataBean.getPrice_sale() + "");
        //涨跌值
        intent.putExtra(AppConstans.value, value);
        //今开
        intent.putExtra(AppConstans.open, NumberUtils.setScale(marketDataBean.getOpen(), marketDataBean.getDigit()));
        //昨收
        intent.putExtra(AppConstans.close, marketDataBean.getClose());
        //最高
        intent.putExtra(AppConstans.high, NumberUtils.setScale(marketDataBean.getHigh(), marketDataBean.getDigit()));
        //最低
        intent.putExtra(AppConstans.low, NumberUtils.setScale(marketDataBean.getLow(), marketDataBean.getDigit()));
        //买入价的小数点位数
        intent.putExtra(AppConstans.digit, marketDataBean.getDigit());
        //止损止盈点位
        intent.putExtra(AppConstans.stops_level, marketDataBean.getStops_level());
        // ture 为红色， false 为绿色
        intent.putExtra(AppConstans.whatColor, whatColor);
        //excode 字段，老版代码所需要
        intent.putExtra("excode", "FXBTG");
        intent.putExtra("trade", marketDataBean.getTrade());
        startActivity(intent);
    }
}
