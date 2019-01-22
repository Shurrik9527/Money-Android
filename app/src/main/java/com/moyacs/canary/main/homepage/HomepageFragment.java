package com.moyacs.canary.main.homepage;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.chad.library.adapter.base.BaseViewHolder;
import com.just.library.LogUtils;
import com.moyacs.canary.base.BaseDelegateAdapter;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.main.homepage.adapter.DealChanceAdapter;
import com.moyacs.canary.main.homepage.adapter.TradeHorizontalAdapter;
import com.moyacs.canary.main.homepage.contract.MarketContract;
import com.moyacs.canary.main.homepage.contract.MarketPresenterImpl;
import com.moyacs.canary.main.homepage.net.BannerDate;
import com.moyacs.canary.main.homepage.net.DealChanceDate;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.main.me.EvenVo;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.product_fxbtg.ProductActivity;
import com.moyacs.canary.service.SocketQuotation;
import com.yan.pullrefreshlayout.PullRefreshLayout;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/2 0002 10:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：首页
 */

public class HomepageFragment extends BaseFragment implements MarketContract.MarketView {

    @BindView(R.id.recycler_homepage)
    RecyclerView recyclerHomepage;
    @BindView(R.id.pullrefreshLayout)
    PullRefreshLayout pullrefreshLayout;

    private List<DelegateAdapter.Adapter> adapters;//adapter 数据源，vlayout 框架使用
    private MarketContract.MarketPresenter presenter;
    private TradeHorizontalAdapter tradeHorizontalAdapter;

    private String time;//记录 价格改变时间
    private BaseDelegateAdapter bannerAdapter;
    private List<DealChanceDate> chanceList;//交易机会数据源
    private BaseDelegateAdapter dealChanceAdapter;//交易机会条目对应的 adapter
    private List<TradeVo.Trade> tradeList; // 可交易外汇列表
    private List<BannerDate.Banner> bannerList;//轮播图数据源
    private List<MarketDataBean> marketDataBeans;//横向 recyclerView 数据源

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initView() {
        initVLayout();
    }

    @Override
    protected void intListener() {
        //刷新监听
        pullrefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新交易机会
                presenter.getDealChanceList();
            }

            @Override
            public void onLoading() {

            }
        });
    }

    @Override
    protected void initData() {
        presenter = new MarketPresenterImpl(this);
        //获取banner数据
        presenter.getBannerList();
        //获取MOA交易机会
        presenter.getDealChanceList();
        //获取可交易列表
        presenter.getTradList();
        registerEventBus();
    }

    /*
     * 阿里快速布局框架  Vlayout
     */
    private void initVLayout() {
        // 配置 Vlayout 相关
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(mActivity);
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
        initBannerBottom();
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
     * banner
     */
    private void initBanner() {
        bannerList = new ArrayList<>();
        LinearLayoutHelper bannerHelper = new LinearLayoutHelper();
        int banner_viewtype = 1;
        bannerAdapter = new BaseDelegateAdapter(mActivity, bannerHelper,
                R.layout.vlayout_banner_homepage, 1, banner_viewtype) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                //设置banner 数据源及样式
                Banner banner = holder.getView(R.id.banner);
                ArrayList<String> imageUrls = new ArrayList<>();
                if (bannerList == null || bannerList.size() <= 0) {
                    return;
                }
                //添加数据
                for (int i = 0; i < bannerList.size(); i++) {
                    imageUrls.add(bannerList.get(i).getImage());
                }
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

    /**
     * banner 下方 新手学堂布局
     */
    private void initBannerBottom() {
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        int banner_bottom1 = 2;
        BaseDelegateAdapter baseDelegateAdapter = new BaseDelegateAdapter(mActivity, singleLayoutHelper,
                R.layout.vlayout_bannerbottom1, 1, banner_bottom1) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                //新手学堂布局
                View view = holder.getView(R.id.rl_home_newuser_classroom);
                view.setOnClickListener(v -> LogUtils.e("", "新手学堂"));
                //盈利榜
                View view1 = holder.getView(R.id.rl_home_newuser_gold);
                view1.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), ProfitActivity.class));
                });
            }
        };
        adapters.add(baseDelegateAdapter);
    }

    /**
     * 横向滑动的 recyclerView 品种实时详情
     */
    private void initHorizontalRecyclerView() {
        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        int horizontalRecyclerView = 3;
        //横向 recyclerview adapter
        BaseDelegateAdapter horizontalRecyclerAdapter = new BaseDelegateAdapter(mActivity, singleLayoutHelper,
                R.layout.vlayout_horizontalrecyclerview, 1, horizontalRecyclerView) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                RecyclerView recyclerView = holder.getView(R.id.recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                marketDataBeans = new ArrayList<>();
                tradeHorizontalAdapter = new TradeHorizontalAdapter(marketDataBeans);
                tradeHorizontalAdapter.setItemClickListener((view, pos) -> {
                    //外汇列表点击跳转详情
                    intentProductActivity(marketDataBeans.get(pos));
                });
                recyclerView.setAdapter(tradeHorizontalAdapter);
            }
        };
        adapters.add(horizontalRecyclerAdapter);
    }

    /**
     * 交易机会布局
     */
    private void initSingleLayoutHelper() {
        //true 为吸顶，false 为吸底
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper(true);
        int jiaoYiJiHui = 4;
        BaseDelegateAdapter baseDelegateAdapter = new BaseDelegateAdapter(mActivity, stickyLayoutHelper,
                R.layout.vlayout_jiaoyijihui, 1, jiaoYiJiHui);
        adapters.add(baseDelegateAdapter);
    }

    /**
     * 交易机会下方条目布局
     */
    private void initVertivalRecyclerView() {
        chanceList = new ArrayList<>();
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        int verticalRecyclerView = 5;
        dealChanceAdapter = new DealChanceAdapter(mActivity, linearLayoutHelper,
                R.layout.vlayout_verticalrecyclerview_item, chanceList.size(), verticalRecyclerView, chanceList);
        adapters.add(dealChanceAdapter);
    }

    //socket 数据刷新 每一条外汇的值
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyData(Quotation quotation) {
        //如果没有获取行情列表数据就返回 如果 fragment 不可见，不解析数据
        if (marketDataBeans == null || isHidden()) {
            return;
        }
        //遍历比对名称
        for (int i = 0; i < marketDataBeans.size(); i++) {
            MarketDataBean marketDataBean = marketDataBeans.get(i);
            String symbol = marketDataBean.getSymbol();
            //名称比对成功，就更改价格数据，并更新 对应条目
            if (symbol.equals(quotation.getSymbol())) {
                marketDataBean.setPrice_buy(quotation.getAsk());
                time = quotation.getTime();
                marketDataBeans.set(i, marketDataBean);
//                第二个参数不为 0 ，表示可以更新item 中的一部分 ui，对应 adapter 中的 三个参数的 onbindviewHolder
                tradeHorizontalAdapter.notifyItemChanged(i, i);
                break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketData(EvenVo<SocketQuotation> evenVo) {
        if (evenVo.getCode() == EvenVo.SOCKET_QUOTATION && marketDataBeans != null
                && marketDataBeans.size() > 0 && isVisible()) {
            SocketQuotation sq = evenVo.getT();
            for (int i = 0; i < marketDataBeans.size(); i++) {
                MarketDataBean marketDataBean = marketDataBeans.get(i);
                if (TextUtils.equals(marketDataBean.getSymbol(), sq.getSymbolCode())) {
                    marketDataBean.setPrice_buy(sq.getPrice());
                    marketDataBean.setTime(sq.getMarketTime().getTime());
                    marketDataBeans.set(i, marketDataBean);
                    tradeHorizontalAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    @Override
    public void unsubscribe() {
        presenter.unsubscribe();
    }

    @Override
    public void showLoadingDialog() {
        if (pullrefreshLayout.isRefreshing()) {
            return;
        }
        //自动刷新，但是不触发刷新回调
        pullrefreshLayout.autoRefresh(false);
    }

    @Override
    public void dismissLoadingDialog() {
        pullrefreshLayout.refreshComplete();
    }


    @Override
    public void setMarketList(List<MarketDataBean> result) {
        if (marketDataBeans != null && marketDataBeans.size() > 0) {
            tradeHorizontalAdapter.notifyDataSetChanged();
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
//        marketDataBeanList = new ArrayList<>(listData);
        marketDataBeans.clear();
        marketDataBeans.addAll(listData);
        tradeHorizontalAdapter.notifyDataSetChanged();
        //将所有的品种 按照  （英文 - 中文）  这种格式存入 sp
        /*SPUtils spUtils = SPUtils.getInstance(AppConstans.allMarket_en_cn);
        for (int i = 0; i < listData.size(); i++) {
            MarketDataBean marketDataBean = listData.get(i);
            spUtils.put(marketDataBean.getSymbol(), marketDataBean.getSymbol_cn());
        }*/
    }

    @Override
    public void setBannerList(List<BannerDate.Banner> result) {
        //更新 banner 数据源
        bannerList.addAll(result);
        bannerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDealChanceList(List<DealChanceDate> result) {
        chanceList.addAll(result);
        dealChanceAdapter.setmCount(chanceList.size());
        dealChanceAdapter.notifyDataSetChanged();
    }

    @Override
    public void setTradList(List<TradeVo.Trade> list) {
        if (tradeList == null) {
            tradeList = new ArrayList<>(list);
        }
        presenter.getMarketList("live", "");
    }

    @Override
    public void refreshFinish() {
        pullrefreshLayout.refreshComplete();
    }

    /**
     * 跳转到购买详情
     *
     * @param marketDataBean
     */
    private void intentProductActivity(MarketDataBean marketDataBean) {
        //跳转详情页面
        Intent intent = new Intent(getContext(), ProductActivity.class);
        //时间
        intent.putExtra(AppConstans.time, time);
        //外汇信息
        intent.putExtra("marketData", marketDataBean);
        startActivity(intent);
    }
}
