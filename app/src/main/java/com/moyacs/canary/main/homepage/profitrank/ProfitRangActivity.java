package com.moyacs.canary.main.homepage.profitrank;

import android.view.View;
import android.widget.ImageView;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.widget.UnderLineTextView;
import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 盈利榜
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class ProfitRangActivity extends BaseActivity {

    @BindView(R.id.tv_1)
    UnderLineTextView tv1;
    @BindView(R.id.tv_2)
    UnderLineTextView tv2;
    @BindView(R.id.iv_profit)
    ImageView ivProfit;
    private UnderLineTextView tvSelectView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profit;
    }

    @Override
    protected void initView() {
        onViewClicked(tv1);
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.tv_1, R.id.tv_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_1:
                setSelectView(tv1);
                ivProfit.setImageResource(R.mipmap.ic_yinli01);
                break;
            case R.id.tv_2:
                setSelectView(tv2);
                ivProfit.setImageResource(R.mipmap.ic_yinli02);
                break;
        }
    }

    private void setSelectView(UnderLineTextView selectView) {
        if (tvSelectView != null) {
            tvSelectView.setSelected(false);
        }
        selectView.setSelected(true);
        tvSelectView = selectView;
    }
}
