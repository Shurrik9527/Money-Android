
package com.moyacs.canary.main.market;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.main.market.adapter.MarketAdapter;
import com.moyacs.canary.main.market.contract.MarketContract;
import com.moyacs.canary.main.market.contract.MarketPresenterImpl;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.main.market.net.TradeVo;
import com.moyacs.canary.main.me.EvenVo;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.product_fxbtg.ProductActivity;
import com.moyacs.canary.util.LogUtils;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.widget.UnderLineTextView;
import com.yan.pullrefreshlayout.PullRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;


/**
 * 作者：luoshen on 2018/3/2 0002 10:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：行情页面
 */

public class MarketFragment extends BaseFragment implements MarketContract.MarketView {
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
    private MarketAdapter marketAdapter;
    private MarketContract.MarketPresenter presenter;
    // 记录 价格改变时间
    private String time;
    //1:外汇 2:贵金属 3:原油  4:全球指数 “” 代表所有 ，是为了请求对应数据
    private String type;
    //为了确定当前页面显示的是哪个tab * 0 自选 1:外汇 2:贵金属 3:原油  4:全球指数
    private int showTabType = 1;
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
    //可交易品种列表MOA
    private List<MarketDataBean> marketAllList;

    private boolean isLoaderData = false;

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
        marketAdapter.setAdapterClickListener(new MarketAdapter.AdapterClickListener() {
            @Override
            public void onItemClickListener(MarketDataBean dataBean) {
                //跳转详情页面
                Intent intent = new Intent(mActivity, ProductActivity.class);
                //时间
                intent.putExtra(AppConstans.time, time);
                //外汇信息
                intent.putExtra("marketData", dataBean);
                startActivity(intent);
            }

            @Override
            public void onFootItemClickListener() {
                Intent intent = new Intent(mActivity, OptionalActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        registerEventBus();
        presenter = new MarketPresenterImpl(this);
        initList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoaderData) {
            //获取可以交类型列表
            presenter.getTradeList();
            isLoaderData = true;
        }
    }

    /**
     * 初始化 recyclerView
     */
    private void initRecyclerView() {
        rvMarket.setLayoutManager(new LinearLayoutManager(mActivity));
        marketList = new ArrayList<>();
        marketAdapter = new MarketAdapter(marketList, mActivity);
        rvMarket.setAdapter(marketAdapter);
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
                    presenter.getMyChoiceList();
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

    private void initList() {
        waiHuiList = new ArrayList<>();
        guiJinShuList = new ArrayList<>();
        yuanYouList = new ArrayList<>();
        quanQiuZhiShuList = new ArrayList<>();
    }

    /**
     * 抽取方法，便于设置 对应tab 的选中状态
     */
    private void setTabSelect(int tabSelect) {
        tabList.get(showTabType).setSelected(false);
        showTabType = tabSelect;
        tabList.get(tabSelect).setSelected(true);
    }

    @OnClick({R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4, R.id.tab5, R.id.ll_zhangdiefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                if (TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
                    dismissLoadingDialog();
                    DialogUtils.login_please("请先登录", getContext());
                    return;
                }
                setTabSelect(0);
                //防止重复加载数据
                marketAdapter.setIsShowFootView(true);
                if (ziXuanList == null) {
                    presenter.getMyChoiceList();
                } else {
                    replaceMarketList(ziXuanList);
                }
                break;
            case R.id.tab2:
                setTabSelect(1);
                marketAdapter.setIsShowFootView(false);
                replaceMarketList(waiHuiList);
                break;
            case R.id.tab3:
                setTabSelect(2);
                marketAdapter.setIsShowFootView(false);
                replaceMarketList(guiJinShuList);
                break;
            case R.id.tab4:
                setTabSelect(3);
                marketAdapter.setIsShowFootView(false);
                replaceMarketList(yuanYouList);
                break;
            case R.id.tab5:
                setTabSelect(4);
                replaceMarketList(quanQiuZhiShuList);
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
            rvMarket.setVisibility(View.GONE);
            tvFailedView.setVisibility(View.VISIBLE);
            tvFailedView.setText("当前数据为空");
        } else {
            marketList.clear();
            marketList.addAll(list);
            marketAdapter.notifyDataSetChanged();
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
    public void setMyChoiceList(List<TradeVo.Trade> result) {
        ziXuanList = new ArrayList<>();
        if (result.size() > 0 && marketAllList != null && marketAllList.size() > 0) {
            for (TradeVo.Trade t : result) {
                for (MarketDataBean mb : marketAllList) {
                    if (TextUtils.equals(t.getSymbolCode(), mb.getSymbol())) {
                        mb.setTrade(t);
                        ziXuanList.add(mb);
                    }
                }
            }
        }
        replaceMarketList(ziXuanList);
    }

    @Override
    public void getMyChoiceListFiled(String msg) {
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
    public void setMarketList(List<MarketDataBean> result) {
        marketAllList = new ArrayList<>(result);
        //过滤数据
        if (tradeList != null) {
            for (MarketDataBean mb : marketAllList) {
                for (TradeVo.Trade t : tradeList) {
                    if (TextUtils.equals(mb.getSymbol(), t.getSymbolCode())) {
                        mb.setTrade(t);
                        if (TextUtils.equals("1", mb.getType())) {
                            waiHuiList.add(mb);
                        } else if (TextUtils.equals("2", mb.getType())) {
                            guiJinShuList.add(mb);
                        } else if (TextUtils.equals("3", mb.getType())) {
                            yuanYouList.add(mb);
                        } else {
                            quanQiuZhiShuList.add(mb);
                        }
                    }
                }
            }
        }
        marketList.clear();
        switch (showTabType) {
            case 1:
                marketList.addAll(waiHuiList);
                break;
            case 2:
                marketList.addAll(guiJinShuList);
                break;
            case 3:
                marketList.addAll(yuanYouList);
                break;
            case 4:
                marketList.addAll(quanQiuZhiShuList);
                break;
        }
        rvMarket.setVisibility(View.VISIBLE);
        marketAdapter.setIsShowFootView(false);
        marketAdapter.notifyDataSetChanged();
        tvFailedView.setVisibility(View.GONE);
    }

    @Override
    public void getMarkTypeListFiled(String msg) {
        switch (showTabType) {
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
    public void setTradList(List<TradeVo.Trade> list) {
        tradeList = new ArrayList<>();
        tradeList.addAll(list);
        presenter.getMarketList();
    }

    @Override
    public void getTradListFiled(String msg) {
        getMarkTypeListFiled(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyData(Quotation quotation) {
        // 如果没有获取行情列表数据就返回 或者 fragment 不可见，不解析数据
        if (marketList == null || isHidden()) {
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
                switch (showTabType) {
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
                marketAdapter.notifyItemChanged(i, i);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventData(EvenVo evenVo) {
        if (evenVo.getCode() == EvenVo.UPDATE_MY_CHOICE) {
            presenter.getMyChoiceList();
        }
    }
}
