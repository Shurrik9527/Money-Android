package com.moyacs.canary.main.deal;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.MyDecoration;
import com.moyacs.canary.common.RSAKeyManger;
import com.moyacs.canary.main.deal.adapter.ChiCangAdapter;
import com.moyacs.canary.main.deal.contract_tab2.ChiCangCountract;
import com.moyacs.canary.main.deal.contract_tab2.ChiCangPresenterImpl;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.pay.contract.PayContract;
import com.moyacs.canary.pay.contract.PayPresenterImpl;
import com.yan.pullrefreshlayout.PullRefreshLayout;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import www.moyacs.com.myapplication.R;

import static com.moyacs.canary.common.AppConstans.marketDataBeanList;

/**
 * 作者：luoshen on 2018/3/30 0030 18:07
 * 邮箱：rsf411613593@gmail.com
 * 说明：tab2 持仓页面
 */

public class DealTab2Fragment extends BaseFragment implements ChiCangCountract.ChiCangView, PayContract.PayView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pullrefreshLayout)
    PullRefreshLayout pullrefreshLayout;
    @BindView(R.id.tv_error)
    TextView tvError;

    private ChiCangCountract.ChiCangPresenter chiCangPresenter;
    private PayContract.PayPresenter payPresenter;
    private ChiCangAdapter chiCangAdapter;
    private int selectPos;//记录当前点击的条目
    private List<TransactionRecordVo.Record> recordList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_deal_tab2;
    }

    @Override
    protected void initView() {
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    protected void intListener() {
        chiCangAdapter.setItemClickListener((view, pos) -> {
            selectPos = pos;
            LemonHello.getWarningHello("您确认平仓吗？", "")
                    .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                        @Override
                        public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                            helloView.hide();
                        }
                    }))
                    .addAction(new LemonHelloAction("确定", Color.RED, new LemonHelloActionDelegate() {
                        @Override
                        public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                            CheXiaoAndPingCang();
                            lemonHelloView.hide();
                        }
                    })).show(getActivity());
        });
    }

    @Override
    protected void initData() {
        registerEventBus();
        chiCangPresenter = new ChiCangPresenterImpl(this);
        //所有品种行情尚未获取成功
        if (AppConstans.marketDataBeanList == null || AppConstans.marketDataBeanList.size() <= 0) {
            return;
        }
        chiCangPresenter.getRecordList();
    }

    /**
     * 初始化 recyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new MyDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(manager);
        recordList = new ArrayList<>();
        chiCangAdapter = new ChiCangAdapter(mActivity, recordList);
        recyclerView.setAdapter(chiCangAdapter);
    }

    /**
     * 初始化下拉刷新 上拉加载框架
     */
    private void initRefreshLayout() {
        //刷新监听
        pullrefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("DealTab3Fragment", "onRefresh: ");
                chiCangPresenter.getRecordList();
            }

            @Override
            public void onLoading() {
                LogUtils.d("pullrefreshLayout :    onLoading ");
            }
        });
    }

    @Override
    public void unsubscribe() {
        chiCangPresenter.unsubscribe();
    }


    @Override
    public void setRecordList(List<TransactionRecordVo.Record> result) {
        if (result == null || result.size() <= 0) {
            recyclerView.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("持仓列表为空");
        } else {
            tvError.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (marketDataBeanList == null) {
                marketDataBeanList = new ArrayList<>();
            }
            recordList.clear();
            recordList.addAll(result);
            for (int i = 0; i < marketDataBeanList.size(); i++) {
                MarketDataBean marketDataBean = marketDataBeanList.get(i);
                for (int i1 = 0; i1 < result.size(); i1++) {
                    TransactionRecordVo.Record record = result.get(i1);
                    if (marketDataBean.getSymbol().equals(record.getSymbolCode())) {
                        //设置小数点位数
                        record.setDigit(marketDataBean.getDigit());
                        recordList.set(i1, record);
                    }
                }
            }
            chiCangAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getRecordListFailed(String errormsg) {
        recyclerView.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(errormsg);
    }

    /**
     * 撤销挂单 和 平仓市价单 成功
     */
    @Override
    public void closeOrderSuccess() {
        recordList.remove(selectPos);
        chiCangAdapter.notifyItemRangeChanged(selectPos, chiCangAdapter.getItemCount()); //刷新被删除数据，以及其后面的数据
    }

    /**
     * 撤销挂单 和 平仓市价单 失败
     */
    @Override
    public void closeOrderFailed(String msg) {
        ToastUtils.showShort(msg);
    }

    /**
     * 撤销和平仓 通用的方法
     */
    private void CheXiaoAndPingCang() {
        TransactionRecordVo.Record record = recordList.get(selectPos);
        if (payPresenter == null) {
            payPresenter = new PayPresenterImpl(this);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", SPUtils.getInstance().getString(AppConstans.USER_PHONE));
        map.put("transactionStatus", "2");
        map.put("symbolCode", record.getSymbolCode());
        map.put("ransactionType", record.getRansactionType());
        map.put("unitPrice", record.getUnitPrice());
        map.put("lot", record.getLot());
        map.put("id", record.getId());
        map.put("sign", RSAKeyManger.getFormatParams(map));
        payPresenter.closeOrder(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyData(Quotation quotation) {
        //如果没有获取行情列表数据就返回
        if (recordList == null || isHidden()) {
            return;
        }
        //遍历比对名称
        for (int i = 0; i < recordList.size(); i++) {
            TransactionRecordVo.Record record = recordList.get(i);
            String symbol = record.getCloseOutPrice();
            //名称比对成功，就更改价格数据，并更新 对应条目
            if (symbol.equals(quotation.getSymbol())) {
                record.setPrice_buy(quotation.getAsk());
                record.setPrice_sell(quotation.getBid());
                recordList.set(i, record);
//                第二个参数不为 0 ，表示可以更新item 中的一部分 ui，对应 adapter 中的 三个参数的 onbindviewHolder
                if (chiCangAdapter != null) {
                    chiCangAdapter.notifyItemChanged(i, i);
                }
            }
        }
    }
}
