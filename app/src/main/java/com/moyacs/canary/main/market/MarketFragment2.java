
package com.moyacs.canary.main.market;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.login.LoginActivity2;
import com.moyacs.canary.main.MainActivity2;
import com.moyacs.canary.main.market.contract.MarketContract;
import com.moyacs.canary.main.market.contract.MarketPresenterImpl;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.product_fxbtg.Optional;
import com.moyacs.canary.product_fxbtg.ProductActivity;
import com.moyacs.canary.service.SocketService;
import com.moyacs.canary.widget.UnderLineTextView;
import com.moyacs.canary.widget.pullrefreshlayout.ClassicsHeader;
import com.yan.pullrefreshlayout.PullRefreshLayout;


import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import www.moyacs.com.myapplication.R;


/**
 * 作者：luoshen on 2018/3/2 0002 10:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：行情页面
 */

public class MarketFragment2 extends BaseFragment2 implements MarketContract.MarketView {

    private static final String TAG = "MarketFragment2";
    @BindView(R.id.tab1)
    UnderLineTextView tab1;
    @BindView(R.id.tab2)
    UnderLineTextView tab2;
    @BindView(R.id.tab3)
    UnderLineTextView tab3;
    @BindView(R.id.tab4)
    UnderLineTextView tab4;
    @BindView(R.id.tab5)
    UnderLineTextView tab5;
    @BindView(R.id.textView1)
    TextView textView1;

    RecyclerView recyclerMarket;
    @BindView(R.id.pullrefreshLayout)
    PullRefreshLayout pullrefreshLayout;

    @BindView(R.id.zhang_die_fu)
    TextView tvZhangDieFu;

    Unbinder unbinder;
    private View rootView;
    /**
     * 顶部 tab 集合
     */
    private ArrayList<UnderLineTextView> tabList;
    /**
     * 五个 tab 公用的 数据源
     */
    private ArrayList<MarketDataBean> recyclerListData;
    /**
     * recycler 数据源 自选
     */
    private ArrayList<MarketDataBean> list_zixuan;

    private MyRecyclerAdapter myRecyclerAdapter;

    private MarketContract.MarketPresenter presenter;

    //属性动画
    private ValueAnimator colorAnim;
    private Intent intent;
    private NumberUtils numberUtils;
    // 记录 价格改变时间
    private String time;

    //涨跌值
    private String value;
    private View footerView;
    /**
     * 1:外汇 2:贵金属 3:原油  4:全球指数 “” 代表所有 ，是为了请求对应数据
     */
    private String type;
    /**
     * 为了确定当前页面显示的是哪个tab
     * 0 自选 1:外汇 2:贵金属 3:原油  4:全球指数
     */
    private int type_showTab = 1;
    /**
     * 外汇 数据源
     */
    private ArrayList<MarketDataBean> list_waihui;
    /**
     * 贵金属 数据源
     */
    private ArrayList<MarketDataBean> list_guijinshu;
    /**
     * 原油 数据源
     */
    private ArrayList<MarketDataBean> list_yuanyou;
    /**
     * 全球指数  数据源
     */
    private ArrayList<MarketDataBean> list_quanqiuzhishu;
    /**
     * 加载数据失败的时候显示的提示信息
     */
    private TextView textView_failed;

    //可交易品种列表
    private List<TradeVo.Trade> tradeList;

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        EventBus.getDefault().register(this);
        rootView = inflater.inflate(R.layout.fragment_market, null, false);
        unbinder = ButterKnife.bind(this, rootView);
        recyclerMarket = rootView.findViewById(R.id.recycler_market);
        textView_failed = rootView.findViewById(R.id.tv_failed);
        initTabList();
        //初始化 tab 选中外汇
        setTabSelect(1);
        //初始化 recyclerView
        initrecyclerView();
        //初始化下拉刷新框架
        initRefreshLayout();
        if (myRecyclerAdapter == null) {
            myRecyclerAdapter = new MyRecyclerAdapter();
        }
        recyclerMarket.setAdapter(myRecyclerAdapter);
        return rootView;
    }

    @Override
    protected void initBundleData(Bundle bundle) {
    }

    private int tag = 0;

    @Override
    protected void loadData() {
        if (presenter == null) {
            presenter = new MarketPresenterImpl(this);
        }
        //获取可以交类型列表
        presenter.getTradeList();
        //防止重复加载数据
        if (list_zixuan != null && list_zixuan.size() >= 0) {
            return;
        }
        /**
         * 获取自选列表
         */
//        presenter.getMarketList("13232323636", "DEMO");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (recyclerListData != null && recyclerListData.size() > 0) {
//            myRecyclerAdapter = new MyRecyclerAdapter();
//            recyclerMarket.setAdapter(myRecyclerAdapter);
//            recyclerView 底部添加自选布局
//            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_recycler_market_footer, null, false);
//            myRecyclerAdapter.notifyItemInserted(myRecyclerAdapter.getItemCount() - 1);
//        }
    }

    /**
     * 初始化 recyclerView
     */
    private void initrecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerMarket.setLayoutManager(manager);
    }

    /**
     * 初始化下拉刷新 上拉加载框架
     */
    private void initRefreshLayout() {
        //刷新监听
        pullrefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (tab1.isSelected())
                    presenter.getMarketList("13232323636", "DEMO");
                else {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pullrefreshLayout.refreshComplete();
                        }
                    }, 600);
                }
            }

            @Override
            public void onLoading() {
                LogUtils.d("pullrefreshLayout :    onLoading ");
            }
        });
    }

    /**
     * 将所有 tab 放入集合，方便以后状态改变
     */
    private void initTabList() {
        tabList = new ArrayList<>();
        tabList.add(tab1);
        tabList.add(tab2);
        tabList.add(tab3);
        tabList.add(tab4);
        tabList.add(tab5);
    }

    /**
     * 抽取方法，便于设置 对应tab 的选中状态
     */
    private void setTabSelect(int tabSelect) {
        type_showTab = tabSelect;
        int size = tabList.size();
        if (tabSelect > size || tabSelect < 0) {
            return;
        }
        for (int i = 0; i < tabList.size(); i++) {
            UnderLineTextView underLineTextView = tabList.get(i);
            if (i == tabSelect) {
                underLineTextView.setSelected(true);
            } else {
                underLineTextView.setSelected(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        tag = 0;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyData(Quotation quotation) {
        //如果没有获取行情列表数据就返回
        if (recyclerListData == null) {
            return;
        }
        //如果 fragment 不可见，不解析数据
        if (!isVisibleToUser) {
            return;
        }
        //遍历比对名称
        for (int i = 0; i < recyclerListData.size(); i++) {
            MarketDataBean marketDataBean = recyclerListData.get(i);
            String symbol = marketDataBean.getSymbol();
            //名称比对成功，就更改价格数据，并更新 对应条目
            if (symbol.equals(quotation.getSymbol())) {
                //设置买入价
                marketDataBean.setPrice_buy(quotation.getAsk());
                //设置卖出价
                marketDataBean.setPrice_sale(quotation.getBid());
                time = quotation.getTime();
                //主要是为了缓存 对应 tab 的买入价 和 卖出价
                switch (type_showTab) {
                    case 0:
                        if (list_zixuan != null) {
                            list_zixuan.set(i, marketDataBean);
                        }
                        break;
                    case 1:
                        if (list_waihui != null) {
                            list_waihui.set(i, marketDataBean);
                        }
                        break;
                    case 2:
                        if (list_guijinshu != null) {
                            list_guijinshu.set(i, marketDataBean);
                        }
                        break;
                    case 3:
                        if (list_yuanyou != null) {
                            list_yuanyou.set(i, marketDataBean);
                        }
                        break;
                    case 4:
                        if (list_quanqiuzhishu != null) {
                            list_quanqiuzhishu.set(i, marketDataBean);
                        }
                        break;
                }
                recyclerListData.set(i, marketDataBean);
                //第二个参数不为 0 ，表示可以更新item 中的一部分 ui，对应 adapter 中的 三个参数的 onbindviewHolder
                myRecyclerAdapter.notifyItemChanged(i, i);
            }
            marketDataBean = null;
        }
    }

    /**
     * 是否显示点差
     */
    private boolean isShowDianCha;

    @OnClick({R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4, R.id.tab5, R.id.ll_zhangdiefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                String userPhone = SPUtils.getInstance().getString(AppConstans.USER_PHONE, "");
                if (TextUtils.isEmpty(userPhone)) {
                    DialogUtils.login_please("请先登录", getContext());
                    return;
                }
                setTabSelect(0);
                //防止重复加载数据
                if (list_zixuan == null) {
                    presenter.getMarketList("13232323636", "DEMO");
                } else {
                    recyclerListData = new ArrayList<>(list_zixuan);
                    myRecyclerAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.tab2:
                type = "1";
                setTabSelect(1);
                requestDate(list_waihui);
                break;
            case R.id.tab3:
                type = "2";
                setTabSelect(2);
                requestDate(list_guijinshu);
                break;
            case R.id.tab4:
                type = "3";
                setTabSelect(3);
                requestDate(list_yuanyou);
                break;
            case R.id.tab5:
                type = "4";
                setTabSelect(4);
                requestDate(list_quanqiuzhishu);
                break;
            case R.id.ll_zhangdiefu:
                /*isShowDianCha = !isShowDianCha;
                if (isShowDianCha) {
                    tvZhangDieFu.setText("点差");
                } else {
                    tvZhangDieFu.setText("涨跌幅");
                }
                myRecyclerAdapter.notifyDataSetChanged();*/
                break;
        }
    }

    /**
     * 防止重复加载数据
     *
     * @param list
     */
    private void requestDate(List<MarketDataBean> list) {
        if (tradeList == null) {
            return;
        }
        textView_failed.setVisibility(View.GONE);//加载数据隐藏异常状态提醒
        if (list == null) {
            presenter.getMarketList_type("DEMO", type);
        } else {
            recyclerListData = new ArrayList<>(list);
            myRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void unsubscribe() {
        presenter.unsubscribe();
    }

    @Override
    public void showLoadingDailog() {
        textView_failed.setVisibility(View.GONE);
        if (pullrefreshLayout.isRefreshing()) {
            return;
        }
        //自动刷新，但是不触发刷新回调
        pullrefreshLayout.autoRefresh(false);
    }

    @Override
    public void dismissLoadingDialog() {
        if (pullrefreshLayout != null) {
            pullrefreshLayout.refreshComplete();
        }
    }

    /**
     * 请求自选列表成功
     *
     * @param result
     */
    @Override
    public void getMarketListSucessed(List<MarketDataBean> result) {
        //隐藏自选按钮
        if (footerView != null) {
            footerView.setVisibility(View.VISIBLE);
        }
        textView_failed.setVisibility(View.GONE);
        LogUtils.d("获取自选列表成功");
        for (int i = 0; i < result.size(); i++) {
            int stops_level = result.get(i).getStops_level();
            Log.i("stops_level", "stops_level:   " + stops_level);

            double close = result.get(i).getClose();
            LogUtils.d(close);
            LogUtils.d("保留小数点位数  ：  " + result.get(i).getDigit());
        }
        list_zixuan = new ArrayList<>(result);
        recyclerListData = new ArrayList<>(result);
        myRecyclerAdapter = new MyRecyclerAdapter();
        recyclerMarket.setAdapter(myRecyclerAdapter);
        //recyclerview 底部添加自选布局
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_recycler_market_footer, recyclerMarket, false);
        myRecyclerAdapter.notifyItemInserted(myRecyclerAdapter.getItemCount() - 1);
        //底部添加自选布局 点击事件
        footerView.findViewById(R.id.ll_footer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OptionalActivity.class);
                startActivity(intent);
            }
        });
        numberUtils = new NumberUtils();
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
    //买入价 卖出价 涨跌幅背景的字体颜色
    private int rangeColor = 0;
    //涨跌幅
    private double range;
    //涨跌幅：拼接好的字符串
    private String range_;
    //决定跳转到的页面字体显示什么颜色
    private boolean whatColor;
    //昨收
    private double close;

    /**
     * 请求自选列表失败
     *
     * @param errormsg
     */
    @Override
    public void getMarketListFailed(String errormsg) {
        if (recyclerListData != null) {
            recyclerListData.clear();
        }
        myRecyclerAdapter.notifyDataSetChanged();
        textView_failed.setVisibility(View.VISIBLE);
        textView_failed.setText("自选列表获取失败");
    }

    /**
     * 获取行情列表成功
     *
     * @param result
     */
    @Override
    public void getMarketListSucessed_type(List<MarketDataBean> result) {
        List<MarketDataBean> listData = new ArrayList<>();
        //过滤数据
        if (tradeList != null) {
            for (MarketDataBean mb : result) {
                for (TradeVo.Trade t : tradeList) {
                    if (TextUtils.equals(mb.getSymbol(), t.getSymbolCode())) {
                        mb.setTrade(t);
                        listData.add(mb);
                    }
                }
            }
        }
        switch (type_showTab) {
            case 1:
                list_waihui = new ArrayList<>(listData);
                break;
            case 2:
                list_guijinshu = new ArrayList<>(listData);
                break;
            case 3:
                list_yuanyou = new ArrayList<>(listData);
                break;
            case 4:
                list_quanqiuzhishu = new ArrayList<>(listData);
                break;
        }
        Log.i(TAG, "getMarketListSucessed_type: 获取行情列表成功");
        recyclerListData = new ArrayList<>(listData);
        myRecyclerAdapter.notifyDataSetChanged();
        //隐藏自选按钮
        if (footerView != null) {
            footerView.setVisibility(View.GONE);
        }
        textView_failed.setVisibility(View.GONE);
    }

    /**
     * 获取行情列表失败
     *
     * @param errormsg
     */
    @Override
    public void getMarketListFailed_type(String errormsg) {
        textView_failed.setVisibility(View.VISIBLE);
        switch (type_showTab) {
            case 1:
                textView_failed.setText("外汇列表获取失败");
                break;
            case 2:
                textView_failed.setText("贵金属列表获取失败");
                break;
            case 3:
                textView_failed.setText("原油列表获取失败");
                break;
            case 4:
                textView_failed.setText("全球指数列表获取失败");
                break;
        }
        Log.i(TAG, "getMarketListFailed_type: 获取行情列表失败");
    }


    @Override
    public void getTradList(List<TradeVo.Trade> list) {
        tradeList = new ArrayList<>();
        tradeList.addAll(list);
    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
        public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
        public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的


        public void setFooterView(View footerView) {
            notifyItemInserted(getItemCount() - 1);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //如果是FooterView，直接在Holder中返回
            if (footerView != null && viewType == TYPE_FOOTER) {
                return new ViewHolder(footerView);
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_market, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }


        /**
         * 重写的  onBindViewHolder ，为了局部刷新某个条目中的某个控件
         *
         * @param holder
         * @param position
         * @param payloads
         */
        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            if (getItemViewType(position) == TYPE_FOOTER) {
                return;
            }
            //表示 viewHolder 的整个条目所有数据都改变了！
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
                //表示，viewHolder 的一部分数据改变
            } else {
                final MarketDataBean marketDataBean = recyclerListData.get(position);
                //数据不为空
                if (marketDataBean != null) {
//                    获取旧价格，String类型
                    oldPrice = (String) holder.tvPrice.getText();
                    //昨收
                    close = marketDataBean.getClose();

                    //即将赋值的价格  买入价
//                    double newPrice_d = marketDataBean.getPrice_buy();
                    double newPrice_d = marketDataBean.getPrice_buy() == 0 ? marketDataBean.getOpen() : marketDataBean.getPrice_buy();
                    //根据保留的小数位截取数据
                    String newPrice_d_scale = NumberUtils.setScale(newPrice_d, marketDataBean.getDigit());
                    //设置最新价格
                    holder.tvPrice.setText(newPrice_d_scale);

                    //设置 卖出价
//                Double price_sale = marketDataBean.getPrice_sale();
                    Double price_sale = marketDataBean.getPrice_sale() == 0 ? marketDataBean.getClose() : marketDataBean.getPrice_sale();
                    //根据保留的小数位截取数据
                    String price_sale_scale = NumberUtils.setScale(price_sale, marketDataBean.getDigit());
                    holder.tvSale.setText(price_sale_scale);

                    //第一次进入的时候 oldPrice 没数据
                    if (oldPrice != null && !oldPrice.equals("null") && !oldPrice.equals("")) {
                        oldprice_d = Double.valueOf(oldPrice);
                        //比较昨收价格  与 最新买入价 大小
                        int compare = numberUtils.compare(newPrice_d, close);
                        //比较，新旧买入价
                        int compare2 = numberUtils.compare(newPrice_d, oldprice_d);

                        //主要是为了保证 相减始终为正数，正负号手动添加
                        if (compare == -1) {// 新价格 < 昨收价格
                            whatColor = false;
                            //计算新旧价格差
                            subtract = numberUtils.subtract(close, newPrice_d);
                            rangeColor = getResources().getColor(R.color.trade_down);
                            //计算涨跌值
                            value = "-" + numberUtils.doubleToString(subtract);
                            //计算涨跌幅
                            range = numberUtils.divide(subtract, close, 4);
                            //涨跌幅数据格式化
                            String s = NumberUtils.setScale2(range);
                            //跌 加 - 号
                            range_ = "- " + s + "%";
                        } else if (compare == 0) {

                        } else {//新价格 > 昨收价格
                            whatColor = true;
                            rangeColor = getResources().getColor(R.color.color_opt_gt);
                            subtract = numberUtils.subtract(newPrice_d, close);
                            //计算涨跌值
                            value = "+" + numberUtils.doubleToString(subtract);
                            //计算涨跌幅
                            range = numberUtils.divide(subtract, close, 4);
                            //涨跌幅数据格式化
                            String s = NumberUtils.setScale2(range);
                            //涨 加 + 号
                            range_ = "+ " + s + "%";
                        }
                        //缓存涨跌幅
                        marketDataBean.setRang_(range_);
                        //缓存买入价，卖出价，涨跌幅背景 的颜色
                        marketDataBean.setRangeColor(rangeColor);
                        // 价格 执行动画的颜色， 靠 前后两次买入价对比决定
                        if (compare2 == -1) {//新价格 < 旧价格 ，跌
                            Animatorcolor = getResources().getColor(R.color.color_opt_lt_50);
                        } else if (compare2 == 1) {// 新价格 > 旧价格 ， 涨
                            Animatorcolor = getResources().getColor(R.color.color_opt_gt_50);
                        } else {

                        }

                        //价格  执行动画
//                    getAnimator(holder.tvPrice, Animatorcolor);
                        //买入价的字体颜色 ，与 涨跌幅的背景颜色一致
                        holder.tvPrice.setTextColor(rangeColor);
                        holder.tvSale.setTextColor(rangeColor);
                        //如果是显示点差状态
                        if (isShowDianCha) {
                            //卖出 - 买入
                            double subtract = NumberUtils.subtract(price_sale, newPrice_d);
                            double abs = Math.abs(subtract);
                            //格式化小数位
                            String s = NumberUtils.setScale(abs, marketDataBean.getDigit());
                            //设置数据
                            holder.tvRange.setText(s);
                        } else {
                            //获取 shape 的背景色
                            GradientDrawable gradientDrawable = (GradientDrawable) holder.tvRange.getBackground();
                            gradientDrawable.setColor(rangeColor);
                            holder.tvRange.setText(range_);
                        }
                        //缓存涨跌幅
                        switch (type_showTab) {
                            case 0:
                                if (list_zixuan != null) {
                                    list_zixuan.set(position, marketDataBean);
                                }
                                break;
                            case 1:
                                if (list_waihui != null) {
                                    list_waihui.set(position, marketDataBean);
                                }
                                break;
                            case 2:
                                if (list_guijinshu != null) {
                                    list_guijinshu.set(position, marketDataBean);
                                }
                                break;
                            case 3:
                                if (list_yuanyou != null) {
                                    list_yuanyou.set(position, marketDataBean);
                                }
                                break;
                            case 4:
                                if (list_quanqiuzhishu != null) {
                                    list_quanqiuzhishu.set(position, marketDataBean);
                                }
                                break;
                        }
                    }
                }
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_NORMAL) {
                if (holder instanceof ViewHolder) {
                    holder.tvRange.setText("");
                    if (isShowDianCha) {
                        holder.tvRange.setBackgroundResource(R.color.white);
                        holder.tvRange.setTextColor(getResources().getColor(R.color.app_bottom_text));
                    } else {
                        holder.tvRange.setTextColor(getResources().getColor(R.color.white));
                        holder.tvRange.setBackground(getResources().getDrawable(R.drawable.bg_market_text_item));
                    }
                    final MarketDataBean marketDataBean = recyclerListData.get(position);
                    if (marketDataBean == null) {
                        return;
                    }
                    //获取 shape 的背景色
                    //初始化背景色为蓝色
                    GradientDrawable gradientDrawable = (GradientDrawable) holder.tvRange.getBackground();
                    gradientDrawable.setColor(getResources().getColor(R.color.dialog_btn_blue));
                    int rangeColor = marketDataBean.getRangeColor();

                    //品种中文名称
                    holder.tvChinaname.setText(marketDataBean.getSymbol_cn());
                    //品种英文名称
                    holder.tvEnglishname.setText(marketDataBean.getSymbol());
                    //买入价
                    double newPrice_d = marketDataBean.getPrice_buy() == 0 ? marketDataBean.getOpen() : marketDataBean.getPrice_buy();
                    holder.tvPrice.setText(newPrice_d + "");
                    //卖出价
                    Double price_sale = marketDataBean.getPrice_sale() == 0 ? marketDataBean.getClose() : marketDataBean.getPrice_sale();
                    holder.tvSale.setText(price_sale + "");
                    //涨跌幅
                    if (!isShowDianCha) {
                        holder.tvRange.setText(marketDataBean.getRang_());
                    }

                    //设置对应的颜色
                    if (rangeColor != 0) {
                        holder.tvPrice.setTextColor(rangeColor);
                        holder.tvSale.setTextColor(rangeColor);

                        gradientDrawable.setColor(rangeColor);
                    }

                    //条目点击事件
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*int mt4id = SPUtils.getInstance().getInt(AppConstans.mt4id, -1);
                            if (mt4id == -1) {
4
                                return;
                            }*/
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
                    });
                }
            } else {
            }
        }

        @Override
        public int getItemCount() {
            if (recyclerListData == null || recyclerListData.size() <= 0) {
                return 0;
            } else if (footerView != null) {
                return recyclerListData.size() + 1;
            } else {
                return recyclerListData.size();
            }
        }

        /**
         * 为了防止 item 数据错乱，所以返回 position
         *
         * @param position
         * @return
         */
        @Override
        public int getItemViewType(int position) {
            if (footerView == null) {
                return TYPE_NORMAL;
            }
            //如果是最后一个条目，应该架子啊脚布局
            if (position == getItemCount() - 1) {
                //最后一个,应该加载Footer
                return TYPE_FOOTER;
            }
            return TYPE_NORMAL;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_chinaname)
            TextView tvChinaname;
            @BindView(R.id.tv_englishname)
            TextView tvEnglishname;
            @BindView(R.id.tv_price)
            TextView tvPrice;
            @BindView(R.id.tv_sale)
            TextView tvSale;
            @BindView(R.id.tv_range)
            TextView tvRange;
            @BindView(R.id.ll_price)
            LinearLayout llPrice;
            @BindView(R.id.ll_sale)
            LinearLayout llSale;
            @BindView(R.id.ll_range)
            LinearLayout llRange;

            public ViewHolder(View itemView) {
                super(itemView);
                if (itemView == footerView) {
                    return;
                }
                ButterKnife.bind(this, itemView);
            }
        }
    }

    /**
     * 渐变动画
     */
    private void getAnimator(View view, int resultColor) {
        colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", getResources().getColor(R.color.white), resultColor);
        colorAnim.setDuration(80); // 动画时间为2s
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

    public int getType_showTab() {
        return type_showTab;
    }
}
