package com.moyacs.canary.main.deal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<TransactionRecordVo.Record> recordList;
    private Context context;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TransactionAdapter(Context context, List<TransactionRecordVo.Record> recordList) {
        this.recordList = recordList;
        this.context = context;
    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_fragment_deal_tab3_tab1, parent, false);
        return new TransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TransactionAdapter.ViewHolder holder, final int position) {

        TransactionRecordVo.Record chiCangDateBean = recordList.get(position);
        //品种名称
        String symbol_cn = SPUtils.getInstance(AppConstans.allMarket_en_cn).getString(chiCangDateBean.getSymbolCode());
        if (symbol_cn == null || symbol_cn.equals("")) {
            return;
        }
        holder.tvSymbolCn.setText(symbol_cn);
        //买或者卖
        String type = chiCangDateBean.getRansactionType();
        int color = 0;
        //根据 type 类型设置字体颜色
        if (type != null) {
            if (type.startsWith("1")) {
                color = context.getResources().getColor(R.color.color_opt_gt);
            } else {
                color = context.getResources().getColor(R.color.color_opt_lt);
            }
        }
        //买涨 / 买跌
        String type_cn = getType_cn(type);
        holder.tvType.setText(type_cn);
        holder.tvType.setTextColor(color);

        //手数
        String s1 = NumberUtils.setScale2(chiCangDateBean.getLot());
        holder.tvShoushu.setText(s1 + "手");
        holder.tvShoushu.setTextColor(color);
        //收益
        double profit = chiCangDateBean.getProfit();
        double abs = Math.abs(profit);
        String profit_s;
        if (profit > 0) {
            profit_s = "+$" + abs;
            color = context.getResources().getColor(R.color.color_opt_gt);
        } else {
            profit_s = "-$" + abs;
            color = context.getResources().getColor(R.color.color_opt_lt);
        }
        holder.tvProfit.setText(profit_s);
        holder.tvProfit.setTextColor(color);
        //建仓时间
        String openTime = TimeUtils.millis2String(chiCangDateBean.getCreateTime(), simpleDateFormat);
        holder.tvTime.setText(openTime);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
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
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_shoushu)
        TextView tvShoushu;
        @BindView(R.id.tv_symbol_cn)
        TextView tvSymbolCn;
        @BindView(R.id.tv_rofit)
        TextView tvProfit;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String getType_cn(String type) {
        //根据type 内容显示不同的 中文类型
        String type_cn = "";
        switch (type) {
            case AppConstans.type_BUY:
                type_cn = "买涨";
                break;
            case AppConstans.type_BUY_LIMIT:
                type_cn = "买涨限价";
                break;
            case AppConstans.type_BUY_STOP:
                type_cn = "买涨止损";
                break;
            case AppConstans.type_SELL_:
                type_cn = "买跌";
                break;
            case AppConstans.type_SELL_LIMIT:
                type_cn = "买跌限价";
                break;
            case AppConstans.type_SELL_STOP:
                type_cn = "买跌止损";
                break;
        }
        return type_cn;
    }
}
