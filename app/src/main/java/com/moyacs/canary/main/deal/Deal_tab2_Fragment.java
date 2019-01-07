package com.moyacs.canary.main.deal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.moyacs.canary.base.BaseFragment3;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.MyDecoration;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.common.RSAKeyManger;
import com.moyacs.canary.main.deal.contract_tab2.ChiCangCountract;
import com.moyacs.canary.main.deal.contract_tab2.ChiCangPresenterImpl;
import com.moyacs.canary.main.deal.net_tab2.ChiCangDateBean;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.netty.codec.Quotation;
import com.moyacs.canary.pay.contract.PayCountract;
import com.moyacs.canary.pay.contract.PayPresenterImpl;
import com.yan.pullrefreshlayout.PullRefreshLayout;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;

import static com.moyacs.canary.common.AppConstans.marketDataBeanList;

/**
 * 作者：luoshen on 2018/3/30 0030 18:07
 * 邮箱：rsf411613593@gmail.com
 * 说明：tab2 持仓页面
 */

public class Deal_tab2_Fragment extends BaseFragment3 implements ChiCangCountract.ChiCangView, PayCountract.PayView {
    private static final String TAG = "Deal_tab2_Fragment";
    RecyclerView recyclerView;
    PullRefreshLayout pullrefreshLayout;
    private TextView tvError;

    private ChiCangCountract.ChiCangPresenter presenter;
    private PayCountract.PayPresenter presenter_pay;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private MyRecyclerAdapter myRecyclerAdapter;
    /**
     * 记录 是 市价单 或者挂单
     */
    private String orderType;
    /**
     * 是否是撤销和平仓，主要是为了判断是否下拉刷新
     */
    private boolean isCheXiaoAndPingCang;


    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        presenter = new ChiCangPresenterImpl(this);
        View rootView = inflater.inflate(R.layout.fragment_deal_tab2, null);
        pullrefreshLayout = rootView.findViewById(R.id.pullrefreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        tvError = rootView.findViewById(R.id.tv_error);
        initrecyclerView();
        initRefreshLayout();
        return rootView;
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
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected void loadData() {
        //所有品种行情尚未获取成功
        if (AppConstans.marketDataBeanList == null || AppConstans.marketDataBeanList.size() <= 0) {
            return;
        }
        //防止重复加载数据
        if (recyclerList != null && recyclerList.size() >= 0) {
            return;
        }
        Log.i(TAG, "loadData: ");
        if (presenter == null) {
            return;
        }
//        presenter.getChiCangList();
    }

    /**
     * 初始化 recyclerView
     */
    private void initrecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new MyDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(manager);
        myRecyclerAdapter = new MyRecyclerAdapter();
        recyclerView.setAdapter(myRecyclerAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 初始化下拉刷新 上拉加载框架
     */
    private void initRefreshLayout() {
        //刷新监听
        pullrefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("Deal_tab3_Fragment", "onRefresh: ");
//                presenter.getChiCangList();
            }

            @Override
            public void onLoading() {
                LogUtils.d("pullrefreshLayout :    onLoading ");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void unsubscribe() {
        presenter.unsubscribe();
    }

    @Override
    public void showLoadingDailog() {
        if (isCheXiaoAndPingCang) {
            return;
        }
        if (pullrefreshLayout.isRefreshing()) {
            return;
        }
        //参数为  false 不触发刷新监听回调
        pullrefreshLayout.autoRefresh(false);
//        startLoading();
    }

    @Override
    public void dismissLoadingDialog() {
        if (isCheXiaoAndPingCang) {
            return;
        }
        pullrefreshLayout.refreshComplete();
//        stopLoading();
    }

    private List<TransactionRecordVo.Record> recyclerList;

    @Override
    public void getChiCangListSucess(List<TransactionRecordVo.Record> result) {
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
            recyclerList = new ArrayList<>(result);
            for (int i = 0; i < marketDataBeanList.size(); i++) {
                MarketDataBean marketDataBean = marketDataBeanList.get(i);
                for (int i1 = 0; i1 < result.size(); i1++) {
                    TransactionRecordVo.Record record = result.get(i1);
                    if (marketDataBean.getSymbol().equals(record.getSymbolCode())) {
                        //设置小数点位数
                        record.setDigit(marketDataBean.getDigit());
                        recyclerList.set(i1, record);
                    }
                }
            }
            myRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getChiCangListFailed(String errormsg) {
        recyclerView.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(errormsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNettyData(Quotation quotation) {
        //如果没有获取行情列表数据就返回
        if (recyclerList == null) {
            return;
        }
        //如果 fragment 不可见，不解析数据
        if (!isVisibleToUser) {
            return;
        }
        //遍历比对名称
        for (int i = 0; i < recyclerList.size(); i++) {
            TransactionRecordVo.Record record = recyclerList.get(i);
            String symbol = record.getCloseOutPrice();
            //名称比对成功，就更改价格数据，并更新 对应条目
            if (symbol.equals(quotation.getSymbol())) {
                record.setPrice_buy(quotation.getAsk());
                record.setPrice_sell(quotation.getBid());
                recyclerList.set(i, record);
//                第二个参数不为 0 ，表示可以更新item 中的一部分 ui，对应 adapter 中的 三个参数的 onbindviewHolder
                if (myRecyclerAdapter != null) {
                    myRecyclerAdapter.notifyItemChanged(i, i);
                }
            }
        }
    }

    /**
     * 撤销挂单 和 平仓市价单 成功
     *
     * @param result
     */
    @Override
    public void submitOrderSucess(Object result) {
        myRecyclerAdapter.notifyItemRemoved(position_item);//删除某个条目
        recyclerList.remove(position_item);
        myRecyclerAdapter.notifyItemRangeChanged(position_item, myRecyclerAdapter.getItemCount()); //刷新被删除数据，以及其后面的数据
    }

    /**
     * 撤销挂单 和 平仓市价单 失败
     *
     * @param errormsg
     */
    @Override
    public void submitOrderFailed(String errormsg) {
        ToastUtils.showShort(errormsg);
    }


    /**
     * 记录当前点击的条目
     */
    private int position_item;
    /**
     * 记录 是平仓 还是 撤销
     */
    private String pingCangText;

    /**
     * 持仓列表
     */
    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_recycler_feagmentdeal_tab2, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        }

        /*    @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final TransactionRecordVo.Record record = recyclerList.get(position);
            //品种名称
            String symbol_cn = SPUtils.getInstance(AppConstans.allMarket_en_cn).getString(record.getSymbolCode());
            if (symbol_cn == null || symbol_cn.equals("")) {
                return;
            }
            holder.tvSymbolCn.setText(symbol_cn);
            //买或者卖
            int type = record.getTransactionStatus();

            String type_cn = getType_cn(type);

            //手数
            String s1 = NumberUtils.setScale2(record.getLot());
            holder.tvShoushu.setText(s1);
            //订单号
            holder.tvOrder.setText(record.getId());
            //type
            holder.tvType.setText(type_cn);
            //收益
            holder.tvProfit.setText("(" + record.getProfit() + "$)");
            //涨 或者 跌 并且设置收益的颜色
            if (record.getProfit() > 0) {
                holder.tvShouyiType.setText("涨");
                holder.tvShouyiType.setBackground(getResources().getDrawable(R.drawable.zhang));

                holder.tvProfit.setTextColor(getResources().getColor(R.color.color_opt_gt));
            } else {
                holder.tvShouyiType.setText("跌");
                holder.tvShouyiType.setBackground(getResources().getDrawable(R.drawable.die));

                holder.tvProfit.setTextColor(getResources().getColor(R.color.color_opt_lt));
            }
            //建仓时间
            String openTime = TimeUtils.millis2String(record.getCreateTime(), simpleDateFormat);
            holder.tvJiancangTime.setText(openTime);
            //建仓价
            double open_price = record.getExponent();
            String s2 = NumberUtils.setScale(open_price, record.getDigit());
            holder.tvJiancangPrice.setText(s2);
            //止盈
            String s = NumberUtils.setScale(record.getStopProfitCount(), record.getDigit());
            holder.tvZhiyingValue.setText(s);
            //止损
            String s3 = NumberUtils.setScale(record.getStopLossCount(), record.getDigit());
            holder.tvZhisunValue.setText(s3);
            orderType = getType_cn(type);
            if (orderType.equals("挂单")) {
                pingCangText = "撤销";
                holder.tvSubmit.setText("撤销");
            } else {
                pingCangText = "平仓";
                holder.tvSubmit.setText("平仓");
            }
            //市价单或者挂单
            holder.tvShijiadan.setText(orderType);
            holder.tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position_item = position;
                    LemonHello.getWarningHello("您确认" + pingCangText + "吗？", "")
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
                            }))
                            .show(getActivity());
                }
            });
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            //表示 viewHolder 的整个条目所有数据都改变了！
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
                //表示，viewHolder 的一部分数据改变
            } else {
                TransactionRecordVo.Record record = recyclerList.get(position);
                String s = NumberUtils.setScale(record.getPrice_buy(), record.getDigit());
                holder.tvRate.setText(s);
                holder.tvRate.setTextColor(getResources().getColor(R.color.color_opt_gt));
                String s1 = NumberUtils.setScale(record.getPrice_sell(), record.getDigit());
                holder.tvRateChange.setText(s1);
                holder.tvRateChange.setTextColor(getResources().getColor(R.color.color_opt_lt));
            }
        }*/

        @Override
        public int getItemCount() {
           /* if (recyclerList == null) {
                return 0;
            }
            return recyclerList.size();*/

            return 10;
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
            @BindView(R.id.tv_symbol_cn)
            TextView tvSymbolCn;
            @BindView(R.id.tv_rate)
            TextView tvRate;
            @BindView(R.id.tv_rateChange)
            TextView tvRateChange;
            @BindView(R.id.tv_shijiadan)
            TextView tvShijiadan;
            @BindView(R.id.tv_type)
            TextView tvType;
            @BindView(R.id.tv_arrow)
            TextView tvArrow;
            //涨 还是 跌
            @BindView(R.id.tv_shouyi_type)
            TextView tvShouyiType;
            @BindView(R.id.tv_jiancang_time)
            TextView tvJiancangTime;

            @BindView(R.id.tv_jiancang_price)
            TextView tvJiancangPrice;
            @BindView(R.id.tv_profit)
            TextView tvProfit;
            @BindView(R.id.tv_submit)
            TextView tvSubmit;
            @BindView(R.id.tv_zhiyingValue)
            TextView tvZhiyingValue;
            @BindView(R.id.tv_zhisunValue)
            TextView tvZhisunValue;
            @BindView(R.id.tv_shoushu)
            TextView tvShoushu;
            //订单号
            @BindView(R.id.tv_order)
            TextView tvOrder;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    /**
     * 撤销和平仓 通用的方法
     */
    private void CheXiaoAndPingCang() {
        isCheXiaoAndPingCang = true;
        TransactionRecordVo.Record record = recyclerList.get(position_item);
        if (presenter_pay == null) {
            presenter_pay = new PayPresenterImpl(this);
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
        presenter_pay.submitOrder(map);
        isCheXiaoAndPingCang = false;
    }

    /**
     * 根据type 内容显示不同的 中文类型
     *
     * @param type
     * @return
     */
    private String getType_cn(int type) {
        //根据type 内容显示不同的 中文类型
        String type_cn = "";
        switch (type) {
            case 1:
                type_cn = "建仓";
                break;
            case 2:
                type_cn = "平仓";
                break;
            case 3:
                type_cn = "挂单";
                break;
            case 4:
                type_cn = "取消";
                break;
            case 5:
                type_cn = "爆仓";
                break;
        }
        return type_cn;
    }

    /**
     * 根据 type 类型确定 是 市价单还是挂单
     *
     * @param type
     * @return
     */
    private String getOrderType(String type) {
        String orderType = "";
        if (type != null) {
            if (type.equals(AppConstans.type_BUY) || type.equals(AppConstans.type_SELL_)) {
                orderType = "市价单";
            } else {
                orderType = "挂单";
            }
        }
        return orderType;
    }
}
