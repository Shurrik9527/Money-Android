package com.moyacs.canary.main.deal;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.widget.pullrefreshlayout.ClassicsHeader;
import com.yan.pullrefreshlayout.PullRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/30 0030 18:07
 * 邮箱：rsf411613593@gmail.com
 * 说明：tab1 买卖页面 已废弃
 */

public class Deal_tab1_Fragment extends BaseFragment2 {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pullrefreshLayout)
    PullRefreshLayout pullrefreshLayout;
    Unbinder unbinder;

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_deal_tab2, null);
        unbinder = ButterKnife.bind(this, rootView);
        initrecyclerView();
        initRefreshLayout();
        return rootView;
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected void loadData() {

    }

    /**
     * 初始化 recyclerView
     */
    private void initrecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter();
        recyclerView.setAdapter(myRecyclerAdapter);
    }

    /**
     * 初始化下拉刷新 上拉加载框架
     */
    private void initRefreshLayout() {
        //自动刷新，进入界面之后先刷新
        pullrefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullrefreshLayout.autoRefresh();
            }
        }, 150);

        //刷新监听
        pullrefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.d("freshLayout :    onRefresh ");
                pullrefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pullrefreshLayout != null) {
                            ClassicsHeader classicsHeader = pullrefreshLayout.getHeaderView();
                            classicsHeader.setRefreshError();
                            pullrefreshLayout.refreshComplete();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onLoading() {
                LogUtils.d("freshLayout :    onLoading ");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 横向  recyclerView 的adapter
     */
    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_deal_tab2_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {

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

            @BindView(R.id.tv_title)
            TextView tvTitle;
            @BindView(R.id.tv_price)
            TextView tvPrice;
            @BindView(R.id.tv_rate)
            TextView tvRate;
            @BindView(R.id.tv_rateChange)
            TextView tvRateChange;
            @BindView(R.id.tv_arrow)
            TextView tvArrow;
            @BindView(R.id.titleView)
            LinearLayout titleView;

            @BindView(R.id.item01)
            RelativeLayout item01;

            @BindView(R.id.item02)
            RelativeLayout item02;

            @BindView(R.id.item03)
            RelativeLayout item03;
            @BindView(R.id.tv_upCount)
            TextView tvUpCount;
            @BindView(R.id.tv_downCount)
            TextView tvDownCount;
            @BindView(R.id.tv_buyUp)
            TextView tvBuyUp;
            @BindView(R.id.tv_buyDown)
            TextView tvBuyDown;

            public ViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }
        }
    }
}
