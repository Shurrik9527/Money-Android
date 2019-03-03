package com.moyacs.canary.main.homepage.profitrank.profitlines;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.andview.refreshview.XRefreshView;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.bean.ProfitLinesBean;
import com.moyacs.canary.bean.ProfitLinesVo;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.widget.XXRefreshView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ProfitLinesFragment extends BaseFragment implements ProfitLinesContract.ProfitLinesView {


    @BindView(R.id.profitlines_time_tv)
    TextView profitlinesTimeTv;
    @BindView(R.id.lines_rank_one_iv)
    ImageView linesRankOneIv;
    @BindView(R.id.lines_rank_one_hp_iv)
    ImageView linesRankOneHpIv;
    @BindView(R.id.lines_rank_one_name_tv)
    TextView linesRankOneNameTv;
    @BindView(R.id.lines_rank_one_rate_tv)
    TextView linesRankOneRateTv;
    @BindView(R.id.lines_rank_one_jifen_tv)
    TextView linesRankOneJifenTv;
    @BindView(R.id.lines_one_rl)
    RelativeLayout linesOneRl;
    @BindView(R.id.lines_rank_two_iv)
    ImageView linesRankTwoIv;
    @BindView(R.id.lines_rank_two_hp_iv)
    ImageView linesRankTwoHpIv;
    @BindView(R.id.lines_rank_two_name_tv)
    TextView linesRankTwoNameTv;
    @BindView(R.id.lines_rank_two_rate_tv)
    TextView linesRankTwoRateTv;
    @BindView(R.id.lines_rank_two_jifen_tv)
    TextView linesRankTwoJifenTv;
    @BindView(R.id.lines_two_rl)
    RelativeLayout linesTwoRl;
    @BindView(R.id.lines_rank_three_iv)
    ImageView linesRankThreeIv;
    @BindView(R.id.lines_rank_three_hp_iv)
    ImageView linesRankThreeHpIv;
    @BindView(R.id.lines_rank_three_name_tv)
    TextView linesRankThreeNameTv;
    @BindView(R.id.lines_rank_three_rate_tv)
    TextView linesRankThreeRateTv;
    @BindView(R.id.lines_rank_three_jifen_tv)
    TextView linesRankThreeJifenTv;
    @BindView(R.id.lines_three_rl)
    RelativeLayout linesThreeRl;
    @BindView(R.id.lines_user_name_tv)
    TextView linesUserNameTv;
    @BindView(R.id.lines_user_profit_tv)
    TextView linesUserProfitTv;
    @BindView(R.id.lines_user_reward_tv)
    TextView linesUserRewardTv;
    @BindView(R.id.profitlines_title_ll)
    LinearLayout profitlinesTitleLl;
    @BindView(R.id.profitlines_rv)
    RecyclerView profitlinesRv;
    @BindView(R.id.profit_lines_xrefreshview)
    XXRefreshView mXXRefreshView;


    private ProfitLinesContract.Presenter mPresenter;
    private List<ProfitLinesBean> mLists;
    private ProfitLinesAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profitlines;
    }

    @Override
    protected void initView() {
        if (mPresenter == null) {
            new ProfitLinesPresenter(this);
        }


        mXXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onRefresh(boolean isPullDown) {
                if(mPresenter!=null){
                    mPresenter.getProfitLinesList(true);
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                //获取更多
                if(mPresenter!=null){
                    mPresenter.getProfitLinesList(false);
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
        profitlinesRv.setLayoutManager(layoutManager);
        mAdapter = new ProfitLinesAdapter(getContext());
        profitlinesRv.setAdapter(mAdapter);

        if(mPresenter!=null){
            mPresenter.getProfitLinesList(true);
        }
    }


    @Override
    public void setPresenter(ProfitLinesContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtils.showShort(msg + "");
    }


    @Override
    public void showProfitLines(ProfitLinesVo mProfitLinesVo, boolean isRefresh) {

        if(isRefresh){
            refreshFinish();
            if(mLists!=null&&mLists.size()>0){
                mLists.clear();
            }
        }else {
            loadMoreFinish();
        }

        mLists.addAll(mProfitLinesVo.getList());
        if(mAdapter!=null){
            mAdapter.setNewData(mLists);
        }
    }

    @Override
    public void refreshFinish() {
        if(mXXRefreshView!=null){
            mXXRefreshView.stopRefresh();
        }
    }

    @Override
    public void loadMoreFinish() {
        if(mXXRefreshView!=null){
            mXXRefreshView.stopLoadMore();
        }
    }

}
