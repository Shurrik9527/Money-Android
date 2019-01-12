package com.moyacs.canary.main.deal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperTextView;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.main.deal.net_tab3.PaymentDateBean;
import com.moyacs.canary.util.DateUtil;

import java.util.Date;
import java.util.List;

import www.moyacs.com.myapplication.R;

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.ViewHolder> {
    private List<PaymentDateBean.ContentBean> paymentList;

    public RechargeAdapter(List<PaymentDateBean.ContentBean> paymentList) {
        this.paymentList = paymentList;
    }

    @Override
    public RechargeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_fragment_deal_tab3_tab3, parent, false);
        return new RechargeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RechargeAdapter.ViewHolder holder, final int position) {
        holder.superTextView.setLeftTopString("充值");
        if (paymentList == null) {
            return;
        }
        PaymentDateBean.ContentBean contentBean = paymentList.get(position);
        //时间
        long date = contentBean.getDate();
        String simpleDateFormat = "yyyy-MM-dd HH:mm:ss";
        String time = DateUtil.parseDateToStr(new Date(date), simpleDateFormat);
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
        if (paymentList == null) {
            return 0;
        }
        return paymentList.size();
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
