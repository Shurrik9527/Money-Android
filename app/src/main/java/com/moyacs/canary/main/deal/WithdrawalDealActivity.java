package com.moyacs.canary.main.deal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.moyacs.canary.base.BaseActivity2;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.main.deal.net_tab3.WithdrawalDataBean;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 提现详情页面
 */

public class WithdrawalDealActivity extends BaseActivity2 {

    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.tv_6)
    TextView tv6;
    @BindView(R.id.tv_7)
    TextView tv7;
    @BindView(R.id.tv_8)
    TextView tv8;
    private Unbinder unbinder;
    /**
     * 展示时候的格式
     */
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_msg).setVisible(false);
    }

    @Override
    protected void initIntentData(Intent intent) {
        WithdrawalDataBean.ContentBean contentBean = intent.getParcelableExtra("contentBean");
        //转出金额
        double paidAmount = contentBean.getPaidAmount();
        String s = NumberUtils.setScale(paidAmount, 2);
        tv1.setText(s);
        //提现时间
        long date = contentBean.getDate();
        String s1 = TimeUtils.millis2String(date, simpleDateFormat);
        tv2.setText(s1);
        //处理状态
        String status = contentBean.getStatus();
        String rightBottomString;
        if (status.equals("DONE")) {
            rightBottomString = "已转账";
        }else if(status.equals("WAIT")){
            rightBottomString = "等待处理";
        }else {
            rightBottomString = contentBean.getCommit();
            if (rightBottomString == null || rightBottomString.equals("") || rightBottomString.equals("null")) {
                rightBottomString = "提现失败";
            }

        }
        tv3.setText(rightBottomString);
        //备注
        String commit = contentBean.getCommit();
        if (commit == null || commit.equals("") || commit.equals("null")) {
            commit = "无";
        }
        tv4.setText(commit);
        //卡号
        String accountNumber = contentBean.getAccountNumber();
        tv5.setText(accountNumber);
        //户名
        tv6.setText(contentBean.getAccountName());
        //银行名称
        tv7.setText(contentBean.getBankName());
        //银行支行
        tv8.setText(contentBean.getBankAddress());

    }

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_withdrawal_deal, null, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 是否显示左侧返回键
     *
     * @return
     */
    @Override
    protected Boolean isShowBack() {
        return true;
    }

    @Override
    protected String setToolbarTitle(String title) {
        return "提现详情";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
