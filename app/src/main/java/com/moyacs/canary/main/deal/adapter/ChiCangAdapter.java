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
import com.moyacs.canary.util.ViewListenerAbs;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;

public class ChiCangAdapter extends RecyclerView.Adapter<ChiCangAdapter.ViewHolder> {
    private Context context;
    private List<TransactionRecordVo.Record> recordList;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ViewListenerAbs.ItemClickListener itemClickListener;

    public ChiCangAdapter(Context context, List<TransactionRecordVo.Record> recordList) {
        this.recordList = recordList;
        this.context = context;
    }

    @Override
    public ChiCangAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_feagmentdeal_tab2
                , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChiCangAdapter.ViewHolder holder, final int position) {
        final TransactionRecordVo.Record record = recordList.get(position);
        //品种名称
        String symbol_cn = SPUtils.getInstance(AppConstans.allMarket_en_cn).getString(record.getSymbolCode());
        if (symbol_cn == null || symbol_cn.equals("")) {
            return;
        }
        holder.tvSymbolCn.setText(symbol_cn);
        //买或者卖
        int type = record.getTransactionStatus();

        String type_cn = getType_cn(type);

        //手数
        String s1 = NumberUtils.setScale2(record.getLot());
        holder.tvShoushu.setText(s1);
        //订单号
        holder.tvOrder.setText(record.getId());
        //type
        holder.tvType.setText(type_cn);
        //收益
        holder.tvProfit.setText("(" + record.getProfit() + "$)");
        //涨 或者 跌 并且设置收益的颜色
        if (record.getProfit() > 0) {
            holder.tvShouyiType.setText("涨");
            holder.tvShouyiType.setBackground(context.getResources().getDrawable(R.drawable.zhang));

            holder.tvProfit.setTextColor(context.getResources().getColor(R.color.color_opt_gt));
        } else {
            holder.tvShouyiType.setText("跌");
            holder.tvShouyiType.setBackground(context.getResources().getDrawable(R.drawable.die));

            holder.tvProfit.setTextColor(context.getResources().getColor(R.color.color_opt_lt));
        }
        //建仓时间
        String openTime = TimeUtils.millis2String(record.getCreateTime(), simpleDateFormat);
        holder.tvJiancangTime.setText(openTime);
        //建仓价
        double open_price = record.getExponent();
        String s2 = NumberUtils.setScale(open_price, record.getDigit());
        holder.tvJiancangPrice.setText(s2);
        //止盈
        String s = NumberUtils.setScale(record.getStopProfitCount(), record.getDigit());
        holder.tvZhiyingValue.setText(s);
        //止损
        String s3 = NumberUtils.setScale(record.getStopLossCount(), record.getDigit());
        holder.tvZhisunValue.setText(s3);
        String orderType = getType_cn(type);
        String pingCangText;
        if (orderType.equals("挂单")) {
            pingCangText = "撤销";
            holder.tvSubmit.setText("撤销");
        } else {
            pingCangText = "平仓";
            holder.tvSubmit.setText("平仓");
        }
        //市价单或者挂单
        holder.tvShijiadan.setText(orderType);
        holder.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener(v, position);
                }
            }
        });
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        //表示 viewHolder 的整个条目所有数据都改变了！
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            //表示，viewHolder 的一部分数据改变
        } else {
            TransactionRecordVo.Record record = recordList.get(position);
            String s = NumberUtils.setScale(record.getPrice_buy(), record.getDigit());
            holder.tvRate.setText(s);
            holder.tvRate.setTextColor(context.getResources().getColor(R.color.color_opt_gt));
            String s1 = NumberUtils.setScale(record.getPrice_sell(), record.getDigit());
            holder.tvRateChange.setText(s1);
            holder.tvRateChange.setTextColor(context.getResources().getColor(R.color.color_opt_lt));
        }
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
        @BindView(R.id.tv_symbol_cn)
        TextView tvSymbolCn;
        @BindView(R.id.tv_rate)
        TextView tvRate;
        @BindView(R.id.tv_rateChange)
        TextView tvRateChange;
        @BindView(R.id.tv_shijiadan)
        TextView tvShijiadan;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_arrow)
        TextView tvArrow;
        //涨 还是 跌
        @BindView(R.id.tv_shouyi_type)
        TextView tvShouyiType;
        @BindView(R.id.tv_jiancang_time)
        TextView tvJiancangTime;

        @BindView(R.id.tv_jiancang_price)
        TextView tvJiancangPrice;
        @BindView(R.id.tv_profit)
        TextView tvProfit;
        @BindView(R.id.tv_submit)
        TextView tvSubmit;
        @BindView(R.id.tv_zhiyingValue)
        TextView tvZhiyingValue;
        @BindView(R.id.tv_zhisunValue)
        TextView tvZhisunValue;
        @BindView(R.id.tv_shoushu)
        TextView tvShoushu;
        //订单号
        @BindView(R.id.tv_order)
        TextView tvOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 根据type 内容显示不同的 中文类型
     *
     * @param type
     * @return
     */
    private String getType_cn(int type) {
        //根据type 内容显示不同的 中文类型
        String type_cn = "";
        switch (type) {
            case 1:
                type_cn = "建仓";
                break;
            case 2:
                type_cn = "平仓";
                break;
            case 3:
                type_cn = "挂单";
                break;
            case 4:
                type_cn = "取消";
                break;
            case 5:
                type_cn = "爆仓";
                break;
        }
        return type_cn;
    }

    public void setItemClickListener(ViewListenerAbs.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}