package com.moyacs.canary.main.deal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.moyacs.canary.base.BaseFragment3;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.main.deal.contract_tab3.FundCountract;
import com.moyacs.canary.main.deal.contract_tab3.FundPresenterImpl;
import com.moyacs.canary.main.deal.net_tab3.PaymentDateBean;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.deal.net_tab3.UserAmountVo;
import com.moyacs.canary.main.deal.net_tab3.WithdrawalDataBean;
import com.moyacs.canary.pay.PayActivity;
import com.moyacs.canary.pay.WithdrawActivity;
import com.moyacs.canary.widget.UnderLineTextView;
import com.yan.pullrefreshlayout.PullRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;


/**
 * 作者：luoshen on 2018/3/30 0030 18:07
 * 邮箱：rsf411613593@gmail.com
 * 说明：tab3 资金页面
 */

public class Deal_tab3_Fragment extends BaseFragment3 implements FundCountract.FundView {

    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.tv_baozhengjin)
    TextView tvBaoZhengJin;
    @BindView(R.id.btn_chongzhi)
    Button btnChongzhi;
    @BindView(R.id.btn_tixian)
    Button btnTixian;
    @BindView(R.id.tv_1)
    UnderLineTextView tv1;
    @BindView(R.id.tv_2)
    UnderLineTextView tv2;
    @BindView(R.id.tv_3)
    UnderLineTextView tv3;
    @BindView(R.id.tv_4)
    UnderLineTextView tv4;
    @BindView(R.id.typeView)
    LinearLayout typeView;
    @BindView(R.id.recycler_fragment_deal_tab3)
    RecyclerView recyclerFragmentDealTab3;
    @BindView(R.id.pullrefreshLayout)
    PullRefreshLayout pullrefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.tv_requestFailed)
    TextView tvRequestFailed;
    @BindView(R.id.rl_requestFailed)
    RelativeLayout rlRequestFailed;
    Unbinder unbinder1;
    private View rootView;

    /**
     * 顶部 tab 集合
     */
    private ArrayList<UnderLineTextView> tabList;
    private MyRecyclerAdapter_tab1 adapter_tab1;
    private MyRecyclerAdapter_tab2 adapter_tab2;
    private MyRecyclerAdapter_tab3 adapter_tab3;
    /**
     * 记录当前被点击的 tab 页
     */
    private int tab_type = 1;
    private FundCountract.FundPresenter fundPresenter;
    /**
     * 开始时间 获取各种记录时所用
     */
    private String startDate = "01-01-2000 00:00:00";
    /**
     * 结束时间获 取各种记录时所用
     */
    private String endDate = "01-01-2030 00:00:00";
    /**
     * 展示时候的格式
     */
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 请求数据时的格式，
     */
    private SimpleDateFormat simpleDateFormat_request = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private int mt4_id;
    /**
     * 出金数据
     */
    private List<WithdrawalDataBean.ContentBean> list_withdrawal;
    /**
     * 入金数据
     */
    private List<PaymentDateBean.ContentBean> list_payment;
    /**
     * 交易记录
     */
    private List<TransactionRecordVo.Record> list_deal;
    /**
     * 余额
     */
    private String balance;
    /**
     * 保证金
     */
    private String marginFree;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        rootView = inflater.inflate(R.layout.fragment_deal_tab3, null, false);
        unbinder = ButterKnife.bind(this, rootView);
        initTabList();
        setTabSelect(0);
        initRecyclerView();
        initPullRefreshLayout();
        //默认显示 交易记录
        if (adapter_tab1 == null) {
            adapter_tab1 = new MyRecyclerAdapter_tab1();
        }
        recyclerFragmentDealTab3.setAdapter(adapter_tab1);
        return rootView;
    }

    /**
     * 初始化下拉刷新
     */
    private void initPullRefreshLayout() {
        //刷新监听
        pullrefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //根据显示的不同的 tab 加载不同数据
                getSelectData();
            }

            @Override
            public void onLoading() {

            }
        });
    }

    /**
     * 初始化 recyclerView 配置
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFragmentDealTab3.setLayoutManager(linearLayoutManager);
        recyclerFragmentDealTab3.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    /**
     * 将所有 tab 放入集合，方便以后状态改变
     */
    private void initTabList() {
        tabList = new ArrayList<>();
        tabList.add(tv1);
        tabList.add(tv2);
        tabList.add(tv3);
        tabList.add(tv4);
    }

    /**
     * 抽取方法，便于设置 对应tab 的选中状态
     */
    private void setTabSelect(int tabSelect) {
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
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected void loadData() {
        if (fundPresenter == null) {
            fundPresenter = new FundPresenterImpl(this);
        }
        //获取交易记录
        getSelectData();
        //防止重复加载数据
        if (balance != null && marginFree != null) {
            return;
        }
        fundPresenter.getFund(mt4_id);
    }


    private void getSelectData() {
        //根据显示的不同的 tab 加载不同数据
        switch (tab_type) {
            case 1://交易记录
//                fundPresenter.getTradingRecords(mt4_id, server, startDate, endDate);
                fundPresenter.getTransactionRecordList("0");
                break;
            case 2://出金记录
                //防止重复加载数据
                if (list_withdrawal != null && list_withdrawal.size() >= 0) {
                    break;
                }
                fundPresenter.getWithdrawal(mt4_id, startDate, endDate, 1, 999);
                break;
            case 3://入金记录
                //防止重复加载数据
                if (list_payment != null && list_payment.size() >= 0) {
                    break;
                }
                fundPresenter.getPayment(mt4_id, startDate, endDate, 1, 999);
                break;
            case 4:
                //挂单
                fundPresenter.getTransactionRecordList("3");
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_chongzhi, R.id.btn_tixian, R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chongzhi:
//                http://uc.moyacs.com/my.account-deposit.funds_app_v2.html?v=时间戳&mt4id=812999&token=xxxxxxx
                long nowMills = TimeUtils.getNowMills();
                String url = "http://uc.moyacs.com/my.account-deposit.funds_app_v2.html?v=" + nowMills + "&mt4id=812999&token=xxxxxxx";
                Intent intent = new Intent(getContext(), PayActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.btn_tixian:
                //提现
                Intent intent2 = new Intent(getContext(), WithdrawActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_1:
                setTabSelect(0);
                //默认显示 交易记录
                tab_type = 1;
                //防止重复加载数据
                rlRequestFailed.setVisibility(View.GONE);
                if (adapter_tab1 == null) {
                    adapter_tab1 = new MyRecyclerAdapter_tab1();
                }
                recyclerFragmentDealTab3.setAdapter(adapter_tab1);
                fundPresenter.getTransactionRecordList("0");
//                fundPresenter.getTradingRecords(mt4_id, server, startDate, endDate);
                break;
            case R.id.tv_2:
                setTabSelect(1);
                tab_type = 2;
                //防止重复加载数据
                if (list_withdrawal != null && list_withdrawal.size() >= 0) {
                    if (adapter_tab2 == null) {
                        adapter_tab2 = new MyRecyclerAdapter_tab2();
                    }
                    rlRequestFailed.setVisibility(View.GONE);
                    recyclerFragmentDealTab3.setAdapter(adapter_tab2);
                    break;
                }
                fundPresenter.getWithdrawal(mt4_id, startDate, endDate, 999, 1);
                break;
            case R.id.tv_3:
                setTabSelect(2);
                tab_type = 3;
                //防止重复加载数据
                if (list_payment != null && list_payment.size() >= 0) {
                    if (adapter_tab3 == null) {
                        adapter_tab3 = new MyRecyclerAdapter_tab3();
                    }
                    rlRequestFailed.setVisibility(View.GONE);
                    recyclerFragmentDealTab3.setAdapter(adapter_tab3);
                    break;
                }
                fundPresenter.getPayment(mt4_id, startDate, endDate, 999, 1);
                break;
            case R.id.tv_4:
                setTabSelect(3);
                tab_type = 4;
              /*  if (list_payment != null) {
                    if (adapter_tab3 == null) {
                        adapter_tab3 = new MyRecyclerAdapter_tab3();
                    }
                    rlRequestFailed.setVisibility(View.GONE);
                    recyclerFragmentDealTab3.setAdapter(adapter_tab3);
                    break;
                }*/
                fundPresenter.getTransactionRecordList("3");
                break;
        }
    }

    @Override
    public void unsubscribe() {
        fundPresenter.unsubscribe();
    }

    @Override
    public void showLoadingDailog() {
        rlRequestFailed.setVisibility(View.GONE);
//        startLoading();
        if (pullrefreshLayout.isRefreshing()) {
            return;
        }
        //自动刷新，但是不触发刷新回调
        pullrefreshLayout.autoRefresh(false);
    }

    @Override
    public void dismissLoadingDialog() {
        pullrefreshLayout.refreshComplete();
//        stopLoading();
    }

    @Override
    public void getFundSucess(UserAmountVo result) {
        Log.i("Deal_tab3_Fragment", "getFundSucess:获取资金数据成功 ");
        balance = result.getBalance();
        //设置余额和保证金
        tvTotalMoney.setText(balance);
        Double margin_free = Double.parseDouble(result.getCashDeposit());
        marginFree = NumberUtils.setScale_down(margin_free);
        tvBaoZhengJin.setText(marginFree);
    }

    @Override
    public void getFundFailed(String errormsg) {
        Log.i("Deal_tab3_Fragment", "getFundFailed: 获取资金数据失败");
    }

    @Override
    public void getWithdrawalSucess(WithdrawalDataBean result) {
        list_withdrawal = result.getContent();
        if (adapter_tab2 == null) {
            adapter_tab2 = new MyRecyclerAdapter_tab2();
        }
        recyclerFragmentDealTab3.setAdapter(adapter_tab2);
        rlRequestFailed.setVisibility(View.GONE);
    }

    @Override
    public void getWithdrawalFailed(String errormsg) {
        rlRequestFailed.setVisibility(View.VISIBLE);
    }

    @Override
    public void getPaymentSucess(PaymentDateBean result) {
        list_payment = result.getContent();
        if (adapter_tab3 == null) {
            adapter_tab3 = new MyRecyclerAdapter_tab3();
        }
        recyclerFragmentDealTab3.setAdapter(adapter_tab3);
        rlRequestFailed.setVisibility(View.GONE);
    }

    @Override
    public void getPaymentFailed(String errormsg) {
        rlRequestFailed.setVisibility(View.VISIBLE);
    }

    @Override
    public void getTradingRecordsSucessed(List<TransactionRecordVo.Record> result) {
        rlRequestFailed.setVisibility(View.GONE);
        list_deal = new ArrayList<>(result);
        if (adapter_tab1 == null) {
            adapter_tab1 = new MyRecyclerAdapter_tab1();
        }
        recyclerFragmentDealTab3.setAdapter(adapter_tab1);
    }

    @Override
    public void getTradingRecordsFailed(String errormsg) {
        rlRequestFailed.setVisibility(View.VISIBLE);
    }

    /**
     * 设置交易记录
     *
     * @param list 记录数据
     * @param type 记录类型
     */
    @Override
    public void setTransactionRecordList(List<TransactionRecordVo.Record> list, String type) {
        if (list == null || list.size() <= 0) {
            rlRequestFailed.setVisibility(View.VISIBLE);
            tvRequestFailed.setText("当前记录为空");
        } else {
            if (TextUtils.equals(type, "0")) {
                //交易记录
                list_deal.clear();
                list_deal.addAll(list);
                adapter_tab1.notifyDataSetChanged();
            } else if (TextUtils.equals(type, "1")) {
                //持仓

            } else if (TextUtils.equals(type, "3")) {
                //挂单
            }
        }
    }

    /**
     * 交易记录获取失败
     *
     * @param errormsg 失败原因
     * @param type     记录类型
     */
    @Override
    public void setTransactionRecordsListFailed(String errormsg, String type) {
        rlRequestFailed.setVisibility(View.VISIBLE);
        if (TextUtils.equals(type, "0")) {
            tvRequestFailed.setText("交易记录获取失败");
        } else if (TextUtils.equals(type, "1")) {
            //持仓
            tvRequestFailed.setText("持仓记录获取失败");
        } else if (TextUtils.equals(type, "3")) {
            //挂单
            tvRequestFailed.setText("挂单记录获取失败");
        }
    }


    /**
     * 交易记录对应 的 adapter
     */
    public class MyRecyclerAdapter_tab1 extends RecyclerView.Adapter<MyRecyclerAdapter_tab1.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_fragment_deal_tab3_tab1, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (list_deal == null) {
                return;
            }
//            ChiCangDateBean chiCangDateBean = list_deal.get(position);
            TransactionRecordVo.Record chiCangDateBean = list_deal.get(position);
            //品种名称
            String symbol_cn = SPUtils.getInstance(AppConstans.allMarket_en_cn).getString(chiCangDateBean.getSymbolCode());
            if (symbol_cn == null || symbol_cn.equals("")) {
                return;
            }
            holder.tvSymbolCn.setText(symbol_cn);
            //买或者卖
            String type = chiCangDateBean.getRansactionType();
            int color = 0;
            //根据 type 类型设置字体颜色
            if (type != null) {
                if (type.startsWith("1")) {
                    color = getResources().getColor(R.color.color_opt_gt);
                } else {
                    color = getResources().getColor(R.color.color_opt_lt);
                }
            }
            //买涨 / 买跌
            String type_cn = getType_cn(type);
            holder.tvType.setText(type_cn);
            holder.tvType.setTextColor(color);

            //手数
            String s1 = NumberUtils.setScale2(chiCangDateBean.getLot());
            holder.tvShoushu.setText(s1 + "手");
            holder.tvShoushu.setTextColor(color);
            //收益
            double profit = chiCangDateBean.getProfit();
            double abs = Math.abs(profit);
            String profit_s;
            if (profit > 0) {
                profit_s = "+$" + abs;
                color = getResources().getColor(R.color.color_opt_gt);
            } else {
                profit_s = "-$" + abs;
                color = getResources().getColor(R.color.color_opt_lt);
            }
            holder.tvProfit.setText(profit_s);
            holder.tvProfit.setTextColor(color);
            //建仓时间
            String openTime = TimeUtils.millis2String(chiCangDateBean.getCreateTime(), simpleDateFormat);
            holder.tvTime.setText(openTime);
        }

        @Override
        public int getItemCount() {
            if (list_deal == null) {
                return 0;
            }
            return list_deal.size();
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
            @BindView(R.id.tv_type)
            TextView tvType;
            @BindView(R.id.tv_shoushu)
            TextView tvShoushu;
            @BindView(R.id.tv_symbol_cn)
            TextView tvSymbolCn;
            @BindView(R.id.tv_rofit)
            TextView tvProfit;
            @BindView(R.id.tv_time)
            TextView tvTime;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    /**
     * @param type
     * @return
     */
    private String getType_cn(String type) {
        //根据type 内容显示不同的 中文类型
        String type_cn = "";
        switch (type) {
            case AppConstans.type_BUY:
                type_cn = "买涨";
                break;
            case AppConstans.type_BUY_LIMIT:
                type_cn = "买涨限价";
                break;
            case AppConstans.type_BUY_STOP:
                type_cn = "买涨止损";
                break;
            case AppConstans.type_SELL_:
                type_cn = "买跌";
                break;
            case AppConstans.type_SELL_LIMIT:
                type_cn = "买跌限价";
                break;
            case AppConstans.type_SELL_STOP:
                type_cn = "买跌止损";
                break;
        }
        return type_cn;
    }

    /**
     * 出金记录 对应的  adapter
     */
    public class MyRecyclerAdapter_tab2 extends RecyclerView.Adapter<MyRecyclerAdapter_tab2.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_fragment_deal_tab3_tab2, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (list_withdrawal == null) {
                return;
            }
            final WithdrawalDataBean.ContentBean contentBean = list_withdrawal.get(position);
            //时间
            long date = contentBean.getDate();
            String time = TimeUtils.millis2String(date, simpleDateFormat);
            holder.superTextView.setLeftBottomString(time);

            //金额
            double amount = contentBean.getAmount();
            String s = NumberUtils.setScale(amount, 2);
            holder.superTextView.setRightTopString("$" + s);

            //状态
            String status = contentBean.getStatus();
            String rightBottomString;
            if (status.equals("DONE")) {
                rightBottomString = "已转账";
            } else if (status.equals("WAIT")) {
                rightBottomString = "等待处理";
            } else {
                rightBottomString = contentBean.getCommit();
                if (rightBottomString == null || rightBottomString.equals("") || rightBottomString.equals("null")) {
                    rightBottomString = "提现失败";
                }
            }
            holder.superTextView.setRightBottomString(rightBottomString);
            holder.superTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WithdrawalDealActivity.class);
                    intent.putExtra("contentBean", contentBean);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (list_withdrawal == null) {
                return 0;
            }
            return list_withdrawal.size();
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

            private SuperTextView superTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                superTextView = itemView.findViewById(R.id.supertextView);
            }
        }
    }

    /**
     * 入金记录 对应的  adapter
     */
    public class MyRecyclerAdapter_tab3 extends RecyclerView.Adapter<MyRecyclerAdapter_tab3.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_fragment_deal_tab3_tab3, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.superTextView.setLeftTopString("充值");
            if (list_payment == null) {
                return;
            }
            PaymentDateBean.ContentBean contentBean = list_payment.get(position);
            //时间
            long date = contentBean.getDate();
            String time = TimeUtils.millis2String(date, simpleDateFormat);
            holder.superTextView.setLeftBottomString(time);
            //金额
            double amount = contentBean.getAmount();
            String s = NumberUtils.setScale(amount, 2);
            holder.superTextView.setRightTopString("$" + s);

            //状态
            String status = contentBean.getStatus();
            String rightBottomString;
            if (status.equals("SUCCESS")) {
                rightBottomString = "支付成功";
            } else {
                rightBottomString = "等待支付";
            }
            holder.superTextView.setRightBottomString(rightBottomString);
        }

        @Override
        public int getItemCount() {
            if (list_payment == null) {
                return 0;
            }
            return list_payment.size();
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

            private SuperTextView superTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                superTextView = itemView.findViewById(R.id.supertextView);
            }
        }
    }
}
