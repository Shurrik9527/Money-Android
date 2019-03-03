package com.moyacs.canary.main.homepage.profitrank.profitorder;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.widget.XXRefreshView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 今日盈利单
 * @date 2019/3/1
 * @email 252774645@qq.com
 */
public class ProfitOrderActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.top_title_tv)
    TextView topTitleTv;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.profitorder_top_rl)
    RelativeLayout profitorderTopRl;
    @BindView(R.id.profitorder_hp_iv)
    ImageView profitorderHpIv;
    @BindView(R.id.profitorder_num_iv)
    TextView profitorderNumIv;
    @BindView(R.id.profitorder_auto_sw)
    SwitchCompat profitorderAutoSw;
    @BindView(R.id.profitorder_title_iv)
    TextView profitorderTitleIv;
    @BindView(R.id.profitorder_rv)
    RecyclerView profitorderRv;
    @BindView(R.id.profit_lines_xrefreshview)
    XXRefreshView profitLinesXrefreshview;
    @BindView(R.id.profitorder_all_btn)
    Button profitorderAllBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profitorder;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {


    }


    @OnClick(R.id.profitorder_all_btn)
    public void onClick() {


    }
}
