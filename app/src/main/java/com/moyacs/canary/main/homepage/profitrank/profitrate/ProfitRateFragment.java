package com.moyacs.canary.main.homepage.profitrank.profitrate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.andview.refreshview.XRefreshView;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.bean.ProfitRateBean;
import com.moyacs.canary.bean.ProfitRateVo;
import com.moyacs.canary.main.homepage.profitrank.profitdetail.ProfitDetailActivity;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.widget.XXRefreshView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ProfitRateFragment extends BaseFragment implements ProfitRateContract.ProfitRateView {


    @BindView(R.id.profit_time_tv)
    TextView profitTimeTv;
    @BindView(R.id.rank_one_iv)
    ImageView rankOneIv;
    @BindView(R.id.rank_one_hp_iv)
    ImageView rankOneHpIv;
    @BindView(R.id.rank_one_name_tv)
    TextView rankOneNameTv;
    @BindView(R.id.rank_one_rate_tv)
    TextView rankOneRateTv;
    @BindView(R.id.rank_one_jifen_tv)
    TextView rankOneJifenTv;
    @BindView(R.id.one_rl)
    RelativeLayout oneRl;
    @BindView(R.id.rank_two_iv)
    ImageView rankTwoIv;
    @BindView(R.id.rank_two_hp_iv)
    ImageView rankTwoHpIv;
    @BindView(R.id.rank_two_name_tv)
    TextView rankTwoNameTv;
    @BindView(R.id.rank_two_rate_tv)
    TextView rankTwoRateTv;
    @BindView(R.id.rank_two_jifen_tv)
    TextView rankTwoJifenTv;
    @BindView(R.id.two_rl)
    RelativeLayout twoRl;
    @BindView(R.id.rank_three_iv)
    ImageView rankThreeIv;
    @BindView(R.id.rank_three_hp_iv)
    ImageView rankThreeHpIv;
    @BindView(R.id.rank_three_name_tv)
    TextView rankThreeNameTv;
    @BindView(R.id.rank_three_rate_tv)
    TextView rankThreeRateTv;
    @BindView(R.id.rank_three_jifen_tv)
    TextView rankThreeJifenTv;
    @BindView(R.id.three_rl)
    RelativeLayout threeRl;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.user_profit_tv)
    TextView userProfitTv;
    @BindView(R.id.user_reward_tv)
    TextView userRewardTv;
    @BindView(R.id.profitrate_title_ll)
    LinearLayout profitrateTitleLl;
    @BindView(R.id.profitrate_rv)
    RecyclerView profitrateRv;
    @BindView(R.id.profit_rate_xrefreshview)
    XXRefreshView mXXRefreshView;


    private ProfitRateContract.Presenter mPresenter;
    private List<ProfitRateBean> mLists;
    private ProfitRateAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profitrate;
    }

    @Override
    protected void initView() {
        if (mPresenter == null) {
            new ProfitRatePresenter(this);
        }

        mXXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                if (mPresenter != null) {
                    mPresenter.getProfitRateList(true);
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                //获取更多
                if (mPresenter != null) {
                    mPresenter.getProfitRateList(false);
                }
            }
        });


    }

    @Override
    protected void intListener() {

    }


    @Override
    protected void initData() {
        mLists = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        profitrateRv.setLayoutManager(layoutManager);
        mAdapter = new ProfitRateAdapter(getContext());
        profitrateRv.setAdapter(mAdapter);
        //设置悬浮

        if (mPresenter != null) {
            mPresenter.getProfitRateList(true);
        }
    }

    @Override
    public void showProfitRate(ProfitRateVo mProfitRateVo, boolean isRefresh) {
        if (isRefresh) {
            refreshFinish();
            if (mLists != null && mLists.size() > 0) {
                mLists.clear();
            }
        } else {
            loadMoreFinish();
        }

        mLists.addAll(mProfitRateVo.getList());

        if (mAdapter != null) {
            mAdapter.setNewData(mLists);
        }
    }

    @Override
    public void refreshFinish() {
        if (mXXRefreshView != null) {
            mXXRefreshView.stopRefresh();
        }
    }

    @Override
    public void loadMoreFinish() {
        if (mXXRefreshView != null) {
            mXXRefreshView.stopLoadMore();
        }
    }

    @Override
    public void setPresenter(ProfitRateContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtils.showShort(msg + "");
    }



    @OnClick({R.id.rank_one_hp_iv, R.id.rank_two_hp_iv, R.id.rank_three_hp_iv})
    public void onClick(View view) {
        Intent mIntent = new Intent(getContext(), ProfitDetailActivity.class);
        switch (view.getId()) {
            case R.id.rank_one_hp_iv:

                break;
            case R.id.rank_two_hp_iv:

                break;
            case R.id.rank_three_hp_iv:

                break;
        }
        mIntent.putExtra("M_ID","");
        startActivity(mIntent);
    }
}
