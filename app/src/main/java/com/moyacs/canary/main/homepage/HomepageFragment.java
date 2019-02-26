package com.moyacs.canary.main.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
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
import com.moyacs.canary.base.BaseDelegateAdapter;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.bean.BannerBean;
import com.moyacs.canary.bean.TradeVo;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.bean.DealChanceBean;
import com.moyacs.canary.main.homepage.profitrank.ProfitRangActivity;
import com.moyacs.canary.product_fxbtg.ProductActivity;
import com.moyacs.canary.service.SocketQuotation;
import com.moyacs.canary.util.BannerImageLoaderUtil;
import com.moyacs.canary.web.WebActivity;
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
 * @author heguogui
 * @version v 1.0.0
 * @describe 首页 Fragment
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class HomepageFragment extends BaseFragment implements HomeContract.HomeView {

    @BindView(R.id.recycler_homepage)
    RecyclerView recyclerHomepage;
    @BindView(R.id.pullrefreshLayout)
    PullRefreshLayout pullrefreshLayout;

    private List<DelegateAdapter.Adapter> adapters;//adapter 数据源，vlayout 框架使用
    private HomeContract.HomePresenter mPresenter;
    private TradeHorizontalAdapter tradeHorizontalAdapter;
    private BaseDelegateAdapter bannerAdapter;
    private List<DealChanceBean> chanceList;//交易机会数据源
    private BaseDelegateAdapter dealChanceAdapter;//交易机会条目对应的 adapter
    private List<TradeVo.Trade> tradeList; // 可交易外汇列表
    private List<BannerBean> bannerList;//轮播图数据源

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HomePresenter(this);
    }

    @Override
    protected void initView() {
        tradeList = new ArrayList<>();
        initVLayout();
    }

    @Override
    protected void intListener() {
        //刷新监听
        pullrefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

//                if(mPresenter!=null){
//                    //获取banner数据
//                    mPresenter.getBannerList();
//                    //获取MOA交易机会
//                    mPresenter.getDealChanceList();
//                    //获取可交易列表
//                    mPresenter.getTradList();
//                }
                new Handler(Looper.getMainLooper()).postAtTime(() -> pullrefreshLayout.refreshComplete(), 2000);
            }

            @Override
            public void onLoading() {

            }
        });
    }

    @Override
    protected void initData() {
        //请求数据
        if(mPresenter!=null){
            //获取banner数据
            mPresenter.getBannerList();
            //获取MOA交易机会
            mPresenter.getDealChanceList();
            //获取可交易列表
            mPresenter.getTradList();
        }
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
        bannerAdapter = new BaseDelegateAdapter(mActivity, bannerHelper,
                R.layout.vlayout_banner_homepage, 1, AppConstans.HOME_ADAPTER_ITEM_BANNER_TYPE) {
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
                    imageUrls.add(bannerList.get(i).getImgAddress());
                }
                //设置图片加载器
                banner.setImageLoader(new BannerImageLoaderUtil());
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
        BaseDelegateAdapter baseDelegateAdapter = new BaseDelegateAdapter(mActivity, singleLayoutHelper,
                R.layout.vlayout_bannerbottom1, 1, AppConstans.HOME_ADAPTER_ITEM_FRESH_RANK_TYPE) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                //新手学堂布局
                View view = holder.getView(R.id.rl_home_newuser_classroom);
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(mActivity, WebActivity.class);
                    intent.putExtra("title", "一分钟了解汇大师");
                    intent.putExtra("loadUrl", AppConstans.KNOW_APP);
                    startActivity(intent);
                });
                //盈利榜
                View view1 = holder.getView(R.id.rl_home_newuser_gold);
                view1.setOnClickListener(v -> {
                    startActivity(new Intent(getContext(), ProfitRangActivity.class));
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
        //横向 recyclerview adapter
        BaseDelegateAdapter horizontalRecyclerAdapter = new BaseDelegateAdapter(mActivity, singleLayoutHelper,
                R.layout.vlayout_horizontalrecyclerview, 1, AppConstans.HOME_ADAPTER_ITEM_SPECIES_TYPE) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                RecyclerView recyclerView = holder.getView(R.id.recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                tradeHorizontalAdapter = new TradeHorizontalAdapter(tradeList);
                tradeHorizontalAdapter.setItemClickListener((view, pos) -> {
                    //外汇列表点击跳转详情
                    intentProductActivity(tradeList.get(pos));
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
        BaseDelegateAdapter baseDelegateAdapter = new BaseDelegateAdapter(mActivity, stickyLayoutHelper,
                R.layout.vlayout_jiaoyijihui, 1, AppConstans.HOME_ADAPTER_ITEM_CHANGE_TYPE);
        adapters.add(baseDelegateAdapter);
    }

    /**
     * 交易机会下方条目布局
     */
    private void initVertivalRecyclerView() {
        chanceList = new ArrayList<>();
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        dealChanceAdapter = new DealChanceAdapter(mActivity, linearLayoutHelper,
                R.layout.vlayout_verticalrecyclerview_item, chanceList.size(), AppConstans.HOME_ADAPTER_ITEM_DETAIL_TYPE, chanceList);
        adapters.add(dealChanceAdapter);
    }

    @Override
    public void unsubscribe() {
        if(mPresenter!=null){
            mPresenter.unsubscribe();
        }
    }

    @Override
    public void setPresenter(HomeContract.HomePresenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void setBannerList(List<BannerBean> result) {
        //更新 banner 数据源
        if(bannerList==null){
            bannerList =new ArrayList<>();
        }else {
            bannerList.clear();
        }
        bannerList.addAll(result);
        bannerAdapter.notifyDataSetChanged();
    }

    @Override
    public void setDealChanceList(List<DealChanceBean> result) {
        if(chanceList==null){
            chanceList = new ArrayList<>();
        }else {
            chanceList.clear();
        }
        chanceList.addAll(result);
        dealChanceAdapter.setmCount(chanceList.size());
        dealChanceAdapter.notifyDataSetChanged();
    }

    @Override
    public void setTradList(List<TradeVo.Trade> mlist) {
        if(tradeList==null){
            tradeList = new ArrayList<>();
        }else {
            tradeList.clear();
        }
        tradeList.addAll(mlist);
        if(tradeHorizontalAdapter!=null){
            tradeHorizontalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void refreshFinish() {
        if(pullrefreshLayout!=null){
            pullrefreshLayout.refreshComplete();
        }
    }

    /**
     * 跳转到购买详情
     *
     * @param marketDataBean 交易信息
     */
    private void intentProductActivity(TradeVo.Trade marketDataBean) {
        //跳转详情页面
        Intent intent = new Intent(getContext(), ProductActivity.class);
        //外汇信息
        intent.putExtra("marketData", marketDataBean);
        startActivity(intent);
    }

    /**
     * 刷新产品种类信息
     * @param evenVo
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebSocketData(EvenVo<SocketQuotation> evenVo) {
        if (evenVo.getCode() == EvenVo.SOCKET_QUOTATION && tradeList != null
                && tradeList.size() > 0 && isVisible()) {
            SocketQuotation sq = evenVo.getT();
            for (int i = 0; i < tradeList.size(); i++) {
                TradeVo.Trade marketDataBean = tradeList.get(i);
                if (TextUtils.equals(marketDataBean.getSymbolCode(), sq.getSymbolCode())) {
                    marketDataBean.setPriceBuy(sq.getPrice());
                    marketDataBean.setTime(sq.getMarketTime().getTime());
                    tradeList.set(i, marketDataBean);
                    tradeHorizontalAdapter.notifyItemChanged(i, "live");
                    break;
                }
            }
        }
    }
}
