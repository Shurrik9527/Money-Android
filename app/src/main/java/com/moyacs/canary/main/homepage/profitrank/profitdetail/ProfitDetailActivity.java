package com.moyacs.canary.main.homepage.profitrank.profitdetail;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.ProfitDetailBean;
import com.moyacs.canary.moudle.recharge.RechargeActivity;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/28
 * @email 252774645@qq.com
 */
public class ProfitDetailActivity extends BaseActivity implements ProfitDetailContract.View {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.top_title_tv)
    TextView topTitleTv;
    @BindView(R.id.profit_detail_headbg_iv)
    ImageView profitDetailHeadbgIv;
    @BindView(R.id.profit_detail_head_iv)
    ImageView profitDetailHeadIv;
    @BindView(R.id.profit_detail_rang_tv)
    TextView profitDetailRangTv;
    @BindView(R.id.profit_detail_name_tv)
    TextView profitDetailNameTv;
    @BindView(R.id.profit_detail_head_rl)
    RelativeLayout profitDetailHeadRl;
    @BindView(R.id.profit_detail_time_tv)
    TextView profitDetailTimeTv;
    @BindView(R.id.profit_detail_red_tv)
    TextView profitDetailRedTv;
    @BindView(R.id.profit_detail_buy_tv)
    TextView profitDetailBuyTv;
    @BindView(R.id.profit_detail_title_tv)
    TextView profitDetailTitleTv;
    @BindView(R.id.profitdetail_rechange_btn)
    Button profitdetailRechangeBtn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_profitdetail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {
        topTitleTv.setText(getString(R.string.profit_detail_top_title));
    }

    @Override
    public void showProfitDetail(ProfitDetailBean mProfitDetailBean) {

    }

    @Override
    public void setPresenter(ProfitDetailContract.Presenter presenter) {

    }

    @Override
    public void showMessageTips(String msg) {

    }


    @OnClick({R.id.iv_back, R.id.profitdetail_rechange_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.profitdetail_rechange_btn:
                startActivity(new Intent(ProfitDetailActivity.this, RechargeActivity.class));
                break;
        }
    }



}
