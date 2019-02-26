
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
import com.moyacs.canary.bean.TradeVo;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.main.market.optional.OptionalActivity;
import com.moyacs.canary.product_fxbtg.ProductActivity;
import com.moyacs.canary.service.SocketQuotation;
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
    private MarketAdapter marketAdapter;

    //1:外汇 2:贵金属 3:原油  4:全球指数 “” 代表所有 ，是为了请求对应数据
    private String type;
    //为了确定当前页面显示的是哪个tab * 0 自选 1:外汇 2:贵金属 3:原油  4:全球指数
    private int showTabType = 1;
    //外汇 数据源
    private ArrayList<TradeVo.Trade> waiHuiList;
    //贵金属 数据源
    private ArrayList<TradeVo.Trade> guiJinShuList;
    ///原油 数据源
    private ArrayList<TradeVo.Trade> yuanYouList;
    //全球指数  数据源
    private ArrayList<TradeVo.Trade> quanQiuZhiShuList;
    //五个 tab 公用的 数据源
    private List<TradeVo.Trade> marketList;
    //recycler 数据源 自选
    private ArrayList<TradeVo.Trade> ziXuanList;
    private boolean isLoaderData = false;

    private MarketContract.MarketPresenter mPresenter;

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
            public void onItemClickListener(TradeVo.Trade dataBean) {
                //跳转详情页面
                Intent intent = new Intent(mActivity, ProductActivity.class);
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
        new MarketPresenter(this);
        initList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoaderData) {
            //获取可以交类型列表
            if(mPresenter!=null){
                mPresenter.getTradeList();
            }
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
                    if(mPresenter!=null){
                        mPresenter.getMyChoiceList();
                    }
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
                    if(mPresenter!=null){
                        mPresenter.getMyChoiceList();
                    }
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
    private void replaceMarketList(List<TradeVo.Trade> list) {
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
        if(mPresenter!=null){
            mPresenter.unsubscribe();
        }
    }

    @Override
    public void setPresenter(MarketContract.MarketPresenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void showMessageTips(String msg) {

    }

    /**
     * 请求自选列表成功
     */
    @Override
    public void setMyChoiceList(List<TradeVo.Trade> result) {
        ziXuanList = new ArrayList<>();
        ziXuanList.addAll(result);
        replaceMarketList(ziXuanList);
    }

    @Override
    public void getMyChoiceListFiled(String msg) {
        rvMarket.setVisibility(View.GONE);
        tvFailedView.setVisibility(View.VISIBLE);
        tvFailedView.setText("自选列表获取失败");
    }

    @Override
    public void setTradList(List<TradeVo.Trade> list) {
        for (TradeVo.Trade t : list) {
            if (1 == t.getSymbolType()) {
                waiHuiList.add(t);
            } else if (2 == t.getSymbolType()) {
                guiJinShuList.add(t);
            } else if (3 == t.getSymbolType()) {
                yuanYouList.add(t);
            } else {
                quanQiuZhiShuList.add(t);
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
    public void getTradListFiled(String msg) {
        getMarkTypeListFiled(msg);
    }

    private void getMarkTypeListFiled(String msg) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventData(EvenVo<SocketQuotation> evenVo) {
        if (evenVo.getCode() == EvenVo.UPDATE_MY_CHOICE) {
            if(mPresenter!=null){
                mPresenter.getMyChoiceList();
            }
        } else if (evenVo.getCode() == EvenVo.SOCKET_QUOTATION) {
            if (marketList != null && marketList.size() > 0 && isVisible()) {
                SocketQuotation sq = evenVo.getT();
                for (int i = 0; i < marketList.size(); i++) {
                    TradeVo.Trade dataBean = marketList.get(i);
                    if (TextUtils.equals(dataBean.getSymbolCode(), sq.getSymbolCode())) {
                        //设置买入价
                        dataBean.setPriceBuy(sq.getPrice());
                        dataBean.setTime(sq.getMarketTime().getTime());
                        marketList.set(i, dataBean);
                        //第二个参数不为 0 ，表示可以更新item 中的一部分 ui，对应 adapter 中的 三个参数的 onbindviewHolder
                        marketAdapter.notifyItemChanged(i,"live");
                        break;
                    }
                }
            }
        }
    }
}
