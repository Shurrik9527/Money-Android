package com.moyacs.canary.main.deal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.TimeUtils;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.main.deal.net_tab3.WithdrawalDataBean;
import com.moyacs.canary.util.ViewListenerAbs;

import java.text.SimpleDateFormat;
import java.util.List;

import www.moyacs.com.myapplication.R;

public class WithdrawalAdapter extends RecyclerView.Adapter<WithdrawalAdapter.ViewHolder> {
    private List<WithdrawalDataBean.ContentBean> withdrawalList;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ViewListenerAbs.ItemClickListener itemClickListener;

    public WithdrawalAdapter(List<WithdrawalDataBean.ContentBean> withdrawalList) {
        this.withdrawalList = withdrawalList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_fragment_deal_tab3_tab2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final WithdrawalDataBean.ContentBean contentBean = withdrawalList.get(position);
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
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener(v, position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (withdrawalList == null) {
            return 0;
        }
        return withdrawalList.size();
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

    public void setItemClickListener(ViewListenerAbs.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
