package com.moyacs.canary.main.deal;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.main.deal.adapter.RechargeAdapter;
import com.moyacs.canary.main.deal.adapter.TransactionAdapter;
import com.moyacs.canary.main.deal.adapter.WithdrawalAdapter;
import com.moyacs.canary.main.deal.contract_tab3.FundContract;
import com.moyacs.canary.main.deal.contract_tab3.FundPresenterImpl;
import com.moyacs.canary.main.deal.net_tab3.PaymentDateBean;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;
import com.moyacs.canary.main.deal.net_tab3.UserAmountVo;
import com.moyacs.canary.main.deal.net_tab3.WithdrawalDataBean;
import com.moyacs.canary.main.me.EvenVo;
import com.moyacs.canary.main.me.RechargeActivity;
import com.moyacs.canary.pay.WithdrawActivity;
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
 * 作者：luoshen on 2018/3/30 0030 18:07
 * 邮箱：rsf411613593@gmail.com
 * 说明：tab3 资金页面
 */

public class CapitalFragment extends BaseFragment implements FundContract.FundView {
    @BindView(R.id.tv_totalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.tv_baozhengjin)
    TextView tvBaoZhengJin;
    @BindView(R.id.tv_1)
    UnderLineTextView tv1;
    @BindView(R.id.tv_2)
    UnderLineTextView tv2;
    @BindView(R.id.tv_3)
    UnderLineTextView tv3;
    @BindView(R.id.tv_4)
    UnderLineTextView tv4;
    @BindView(R.id.recycler_fragment_deal_tab3)
    RecyclerView rvContent;
    @BindView(R.id.pullrefreshLayout)
    PullRefreshLayout freshLayout;
    @BindView(R.id.tv_requestFailed)
    TextView tvRequestFailed;
    @BindView(R.id.rl_requestFailed)
    RelativeLayout rlRequestFailed;
    //顶部 tab 集合
    private ArrayList<UnderLineTextView> tabList;
    private TransactionAdapter tradeAdapter;
    private WithdrawalAdapter withdrawalAdapter;
    private RechargeAdapter rechargeAdapter;
    //记录当前被点击的 tab 页
    private int selectTabType = 0;
    private FundContract.FundPresenter fundPresenter;
    //提现数据
    private List<WithdrawalDataBean.ContentBean> withdrawalList;
    //充值数据
    private List<PaymentDateBean.ContentBean> paymentList;
    //交易记录
    private List<TransactionRecordVo.Record> dealList;
    private boolean isLoadData = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_deal_tab3;
    }

    @Override
    protected void initView() {
        initTabList();
        initRecyclerView();
        initPullRefreshLayout();
    }

    @Override
    protected void intListener() {
        withdrawalAdapter.setItemClickListener((view, pos) -> {
            //跳转提现记录详情
            Intent intent = new Intent(getContext(), WithdrawalDealActivity.class);
            intent.putExtra("contentBean", withdrawalList.get(pos));
            startActivity(intent);
        });
    }

    @Override
    protected void initData() {
        registerEventBus();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoadData) {
            fundPresenter = new FundPresenterImpl(this);
            //获取交易记录
            getSelectData(selectTabType);
            //防止重复加载数据
            fundPresenter.getAccountInfo();
            isLoadData = true;
        }
    }

    /**
     * 初始化下拉刷新
     */
    private void initPullRefreshLayout() {
        //刷新监听
        freshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //根据显示的不同的 tab 加载不同数据
                getSelectData(selectTabType);
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
        rvContent.setLayoutManager(linearLayoutManager);
        rvContent.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        dealList = new ArrayList<>();
        tradeAdapter = new TransactionAdapter(mActivity, dealList);
        rvContent.setAdapter(tradeAdapter);

        withdrawalList = new ArrayList<>();
        withdrawalAdapter = new WithdrawalAdapter(withdrawalList);

        paymentList = new ArrayList<>();
        rechargeAdapter = new RechargeAdapter(paymentList);
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
     * 设置Tab选择
     *
     * @param tabSelect 选择的位置
     */
    private void setTabSelect(int tabSelect) {
        tabList.get(selectTabType).setSelected(false);
        tabList.get(tabSelect).setSelected(true);
        selectTabType = tabSelect;
        getSelectData(selectTabType);
    }

    private void getSelectData(int selectTabType) {
        rlRequestFailed.setVisibility(View.GONE);
        switch (selectTabType) {
            case 0://交易记录
                fundPresenter.getTransactionRecordList("0");
                break;
            case 1://挂单
                fundPresenter.getTransactionRecordList("3");
                break;
            case 2://提现
                fundPresenter.getWithdrawal();
                break;
            case 3://充值
                fundPresenter.getPayment();
                break;
        }
    }

    @OnClick({R.id.btn_chongzhi, R.id.btn_tixian, R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chongzhi:
              /*  long nowMills = System.currentTimeMillis();
                String url = "http://uc.moyacs.com/my.account-deposit.funds_app_v2.html?v=" + nowMills + "&mt4id=812999&token=xxxxxxx";
                Intent intent = new Intent(getContext(), PayActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);*/
                startActivity(new Intent(getActivity(), RechargeActivity.class));
                break;
            case R.id.btn_tixian:
                //提现
                Intent intent2 = new Intent(getContext(), WithdrawActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_1:
                setTabSelect(0);
                break;
            case R.id.tv_2:
                setTabSelect(1);
                break;
            case R.id.tv_3:
                setTabSelect(2);
                break;
            case R.id.tv_4:
                setTabSelect(3);
                break;
        }
    }

    @Override
    public void unsubscribe() {
        if (fundPresenter != null) {
            fundPresenter.unsubscribe();
        }
    }

    @Override
    public void setAccountInfo(UserAmountVo result) {
        if (result == null) {
            return;
        }
        //余额
        String balance = result.getBalance();
        //设置余额和保证金
        tvTotalMoney.setText(balance);
        if (result.getCashDeposit() != null && TextUtils.equals("null", result.getCashDeposit())) {
            //保证金
            String marginFree = NumberUtils.setScale_down(Double.parseDouble(result.getCashDeposit()));
            tvBaoZhengJin.setText(marginFree);
        }
    }

    /**
     * 设置交易记录
     */
    @Override
    public void setTradingRecordList(List<TransactionRecordVo.Record> list, String type) {
        freshLayout.refreshComplete();
        if (list == null || list.size() <= 0) {
            rlRequestFailed.setVisibility(View.VISIBLE);
            tvRequestFailed.setText("当前记录为空");
        } else {
            if (TextUtils.equals(type, "0")) {
                //交易记录
                dealList.clear();
                dealList.addAll(list);
                tradeAdapter.notifyDataSetChanged();
            } else if (TextUtils.equals(type, "1")) {
                //持仓

            } else if (TextUtils.equals(type, "3")) {
                //挂单
                dealList.clear();
                dealList.addAll(list);
                tradeAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 交易记录获取失败
     */
    @Override
    public void getTradingRecordsFailed(String msg, String type) {
        freshLayout.refreshComplete();
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

    @Override
    public void setWithdrawalList() {
        freshLayout.refreshComplete();
//        withdrawalList = result.getContent();
        rvContent.setAdapter(withdrawalAdapter);
    }

    @Override
    public void getWithdrawalFailed(String msg) {
        freshLayout.refreshComplete();
        tvRequestFailed.setText("获取提现记录失败");
        rlRequestFailed.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPaymentList() {
        freshLayout.refreshComplete();
//        paymentList = result.getContent();
        rvContent.setAdapter(rechargeAdapter);
    }

    @Override
    public void getPaymentListFailed(String msg) {
        freshLayout.refreshComplete();
        tvRequestFailed.setText("获取充值记录失败");
        rlRequestFailed.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(EvenVo evenVo) {
        if (evenVo.getCode() == EvenVo.CHANGE_ORDER_SUCCESS && fundPresenter != null) {
            fundPresenter.getAccountInfo();
            if (tv1.isSelected()) {
                fundPresenter.getTransactionRecordList("0");
            }
        }
    }
}
