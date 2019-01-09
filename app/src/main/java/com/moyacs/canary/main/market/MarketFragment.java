
package com.moyacs.canary.main.market;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.main.market.contract.MarketContract;
import com.moyacs.canary.main.market.contract.MarketPresenterImpl;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.product_fxbtg.ProductActivity;
import com.moyacs.canary.util.AnimatorUtil;
import com.moyacs.canary.widget.UnderLineTextView;
import com.yan.pullrefreshlayout.PullRefreshLayout;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;


/**
 * 作者：luoshen on 2018/3/2 0002 10:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：行情页面
 */

public class MarketFragment extends BaseFragment implements MarketContract.MarketView {

    private static final String TAG = "MarketFragment";
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
    @BindView(R.id.recycler_market)
    RecyclerView rvMarket;
    @BindView(R.id.pullrefreshLayout)
    PullRefreshLayout freshLayout;
    @BindView(R.id.tv_failed)
    TextView tvFailedView; //加载数据失败的时候显示的提示信息
    @BindView(R.id.zhang_die_fu)
    TextView tvZhangDieFu;

    //顶部 tab 集合
    private ArrayList<UnderLineTextView> tabList;
    //五个 tab 公用的 数据源
    private ArrayList<MarketDataBean> marketList;
    //recycler 数据源 自选
    private ArrayList<MarketDataBean> ziXuanList;
    private MyRecyclerAdapter myRecyclerAdapter;
    private MarketContract.MarketPresenter presenter;
    //属性动画
    private ValueAnimator colorAnim;
    private Intent intent;
    // 记录 价格改变时间
    private String time;
    //涨跌值
    private String value;
    private View footerView;
    //1:外汇 2:贵金属 3:原油  4:全球指数 “” 代表所有 ，是为了请求对应数据
    private String type;
    //为了确定当前页面显示的是哪个tab * 0 自选 1:外汇 2:贵金属 3:原油  4:全球指数
    private int type_showTab = 1;
    //外汇 数据源
    private ArrayList<MarketDataBean> waiHuiList;
    //贵金属 数据源
    private ArrayList<MarketDataBean> guiJinShuList;
    ///原油 数据源
    private ArrayList<MarketDataBean> yuanYouList;
    //全球指数  数据源
    private ArrayList<MarketDataBean> quanQiuZhiShuList;
    //可交易品种列表
    private List<TradeVo.Trade> tradeList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void initView() {
        initTabList();
        //初始化 tab 选中外汇
        setTabSelect(1);
        //初始化 recyclerView
        initRecyclerView();
        //初始化下拉刷新框架
        initRefreshLayout();
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {
        registerEventBus();
        presenter = new MarketPresenterImpl(this);
        //获取可以交类型列表
        presenter.getTradeList();
    }

    /**
     * 初始化 recyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMarket.setLayoutManager(manager);
        myRecyclerAdapter = new MyRecyclerAdapter();
        rvMarket.setAdapter(myRecyclerAdapter);
    }

    /**
     * 初始化下拉刷新 上拉加载框架
     */
    private void initRefreshLayout() {
        //刷新监听
        freshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (tab1.isSelected())
                    presenter.getMarketList("13232323636", "DEMO");
                else {
                    // 模拟刷新  实际上什么都没做
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            freshLayout.refreshComplete();
                        }
                    }, 600);
                }
            }

            @Override
            public void onLoading() {
                LogUtils.d("freshLayout :    onLoading ");
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
        tabList.get(tabSelect).setSelected(true);
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
                if (ziXuanList == null) {
                    presenter.getMarketList("13232323636", "DEMO");
                } else {
                    replaceMarketList(ziXuanList);
                }
                break;
            case R.id.tab2:
                type = "1";
                setTabSelect(1);
                replaceMarketList(waiHuiList);
                break;
            case R.id.tab3:
                type = "2";
                setTabSelect(2);
                replaceMarketList(guiJinShuList);
                break;
            case R.id.tab4:
                type = "3";
                setTabSelect(3);
                replaceMarketList(yuanYouList);
                break;
            case R.id.tab5:
                type = "4";
                setTabSelect(4);
                replaceMarketList(quanQiuZhiShuList);
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
     * 替换数据源
     */
    private void replaceMarketList(List<MarketDataBean> list) {
        tvFailedView.setVisibility(View.GONE);//加载数据隐藏异常状态提醒
        rvMarket.setVisibility(View.VISIBLE);
        if (list == null) {
            presenter.getMarketList_type("DEMO", type);
        } else {
            marketList = new ArrayList<>(list);
            myRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void unsubscribe() {
        presenter.unsubscribe();
    }

    /**
     * 请求自选列表成功
     */
    @Override
    public void getMarketListSucessed(List<MarketDataBean> result) {
        //隐藏自选按钮
        if (footerView == null) {
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_recycler_market_footer, rvMarket, false);
            //底部添加自选布局 点击事件
            footerView.findViewById(R.id.ll_footer).setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), OptionalActivity.class);
                startActivity(intent);
            });
        } else {
            footerView.setVisibility(View.VISIBLE);
        }
        ziXuanList = new ArrayList<>(result);
        replaceMarketList(ziXuanList);
        //recyclerView 底部添加自选布局
        myRecyclerAdapter.notifyItemInserted(myRecyclerAdapter.getItemCount() - 1);
    }


    /**
     * 请求自选列表失败
     *
     * @param errormsg
     */
    @Override
    public void getMarketListFailed(String errormsg) {
        rvMarket.setVisibility(View.GONE);
        tvFailedView.setVisibility(View.VISIBLE);
        tvFailedView.setText("自选列表获取失败");
    }

    /**
     * 获取行情列表成功
     *
     * @param result
     */
    @Override
    public void getMarketListSucessed_type(List<MarketDataBean> result) {
        Log.i(TAG, "getMarketListSucessed_type: 获取行情列表成功");
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
                waiHuiList = new ArrayList<>(listData);
                break;
            case 2:
                guiJinShuList = new ArrayList<>(listData);
                break;
            case 3:
                yuanYouList = new ArrayList<>(listData);
                break;
            case 4:
                quanQiuZhiShuList = new ArrayList<>(listData);
                break;
        }
        rvMarket.setVisibility(View.VISIBLE);
        marketList = new ArrayList<>(listData);
        myRecyclerAdapter.notifyDataSetChanged();
        //隐藏自选按钮
        if (footerView != null) {
            footerView.setVisibility(View.GONE);
        }
        tvFailedView.setVisibility(View.GONE);
    }

    /**
     * 获取行情列表失败
     *
     * @param errormsg
     */
    @Override
    public void getMarketListFailed_type(String errormsg) {
        switch (type_showTab) {
            case 1:
                tvFailedView.setText("外汇列表获取失败");
                break;
            case 2:
                tvFailedView.setText("贵金属列表获取失败");
                break;
            case 3:
                tvFailedView.setText("原油列表获取失败");
                break;
            case 4:
                tvFailedView.setText("全球指数列表获取失败");
                break;
        }
        rvMarket.setVisibility(View.GONE);
        tvFailedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void getTradList(List<TradeVo.Trade> list) {
        tradeList = new ArrayList<>();
        tradeList.addAll(list);
        presenter.getMarketList_type("live", String.valueOf(type_showTab));
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
                final MarketDataBean marketDataBean = marketList.get(position);
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
                            range_ = "- " + s + "%";
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
                        AnimatorUtil.startAnimatorRGB(holder.tvPrice, Animatorcolor);
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
                                if (ziXuanList != null) {
                                    ziXuanList.set(position, marketDataBean);
                                }
                                break;
                            case 1:
                                if (waiHuiList != null) {
                                    waiHuiList.set(position, marketDataBean);
                                }
                                break;
                            case 2:
                                if (guiJinShuList != null) {
                                    guiJinShuList.set(position, marketDataBean);
                                }
                                break;
                            case 3:
                                if (yuanYouList != null) {
                                    yuanYouList.set(position, marketDataBean);
                                }
                                break;
                            case 4:
                                if (quanQiuZhiShuList != null) {
                                    quanQiuZhiShuList.set(position, marketDataBean);
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
                    final MarketDataBean marketDataBean = marketList.get(position);
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
                            //跳转详情页面
                            Intent intent = new Intent(getContext(), ProductActivity.class);
                            //时间
                            intent.putExtra(AppConstans.time, time);
                            intent.putExtra("marketData", marketDataBean);
                            startActivity(intent);
                        }
                    });
                }
            } else {
            }
        }

        @Override
        public int getItemCount() {
            if (marketList == null || marketList.size() <= 0) {
                return 0;
            } else if (footerView != null) {
                return marketList.size() + 1;
            } else {
                return marketList.size();
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
  /*  private void getAnimator(final View view, final int resultColor) {
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
    }*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyData(Quotation quotation) {
        //如果没有获取行情列表数据就返回
        if (marketList == null) {
            return;
        }
        //如果 fragment 不可见，不解析数据
        if (isHidden()) {
            return;
        }
        //遍历比对名称
        for (int i = 0; i < marketList.size(); i++) {
            MarketDataBean marketDataBean = marketList.get(i);
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
                        if (ziXuanList != null) {
                            ziXuanList.set(i, marketDataBean);
                        }
                        break;
                    case 1:
                        if (waiHuiList != null) {
                            waiHuiList.set(i, marketDataBean);
                        }
                        break;
                    case 2:
                        if (guiJinShuList != null) {
                            guiJinShuList.set(i, marketDataBean);
                        }
                        break;
                    case 3:
                        if (yuanYouList != null) {
                            yuanYouList.set(i, marketDataBean);
                        }
                        break;
                    case 4:
                        if (quanQiuZhiShuList != null) {
                            quanQiuZhiShuList.set(i, marketDataBean);
                        }
                        break;
                }
                marketList.set(i, marketDataBean);
                //第二个参数不为 0 ，表示可以更新item 中的一部分 ui，对应 adapter 中的 三个参数的 onbindviewHolder
                myRecyclerAdapter.notifyItemChanged(i, i);
            }
            marketDataBean = null;
        }
    }
}
