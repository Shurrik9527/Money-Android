package com.moyacs.canary.main.deal.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moyacs.canary.bean.TransactionRecordVo;
import com.moyacs.canary.util.ForeignUtil;
import com.moyacs.canary.util.MoneyUtil;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 持仓适配器
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class HoldOrderAdapter extends RecyclerView.Adapter<HoldOrderAdapter.ViewHolder> {
    private List<TransactionRecordVo.Record> recordList;
    private ItemClickListener itemClickListener;
    private int upColor, downColor;

    public HoldOrderAdapter(Context context, List<TransactionRecordVo.Record> recordList) {
        this.recordList = recordList;
        upColor = context.getResources().getColor(R.color.trade_up);
        downColor = context.getResources().getColor(R.color.trade_down);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_chichang
                , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TransactionRecordVo.Record record = recordList.get(position);
        holder.tvSymbol.setText(ForeignUtil.codeFormatCN(record.getSymbolCode()));
        if (TextUtils.equals("1", record.getRansactionType())) {
            //买涨
            holder.tvType.setText("买涨\t" + record.getLot() + "手");
            holder.tvType.setTextColor(upColor);
            holder.vHead.setBackgroundColor(upColor);
        } else {
            //买涨
            holder.tvType.setText("买跌\t" + record.getLot() + "手");
            holder.tvType.setTextColor(downColor);
            holder.vHead.setBackgroundColor(downColor);
        }
        holder.tvUnit.setText(ForeignUtil.codeFormatCN(record.getSymbolCode()) + "\t\t" + record.getMoney() + ForeignUtil.formatForeignUtil(record.getSymbolCode()));
        holder.tvExponent.setText(record.getExponent() + "");
        holder.tvUnitPrice.setText(record.getUnitPrice() + "美元");
        holder.tvCommission.setText(record.getCommissionCharges() + "美元");
        holder.tvOverNight.setText(record.getOvernightFee() + "美元");
        holder.tvCreateTime.setText(record.getCreateTime());
        holder.tvStopLoss.setText(record.getStopLossCount() == 0 ? "不限" : String.valueOf(record.getStopLossCount()));
        holder.tvStopProfit.setText(record.getStopProfitCount() == 0 ? "不限" : String.valueOf(record.getStopProfitCount()));
        holder.sw.setImageResource(TextUtils.equals("1", record.getIsOvernight()) ? R.mipmap.ic_on : R.mipmap.ic_off);
        holder.tvClose.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.itemCloseClickListener(position);
            }
        });
        holder.sw.setOnClickListener(view -> {
            if (itemClickListener != null) {
                itemClickListener.itemSwitchClickListener(position);
            }
        });
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        //表示 viewHolder 的整个条目所有数据都改变了！
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //表示，viewHolder 的一部分数据改变
            String oldValue = holder.tvProfit01.getText().toString();
            TransactionRecordVo.Record record = recordList.get(position);
            String priceBuy = String.valueOf(record.getPrice_buy());
            if (!TextUtils.isEmpty(oldValue) && !TextUtils.equals("null", oldValue)) {
                if (MoneyUtil.moneyComp(priceBuy, oldValue)) {
                    holder.tvProfit01.setTextColor(upColor);
                } else {
                    holder.tvProfit01.setTextColor(downColor);
                }
            }
            holder.tvProfit01.setText(priceBuy);
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

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemCloseClickListener(int pos);

        void itemSwitchClickListener(int pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_symbol)
        TextView tvSymbol;
        @BindView(R.id.tv_profit01)
        TextView tvProfit01;
        @BindView(R.id.v_head)
        View vHead;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_unit)
        TextView tvUnit;
        @BindView(R.id.tv_profit02)
        TextView tvProfit02;
        @BindView(R.id.tv_exponent)
        TextView tvExponent;
        @BindView(R.id.tv_unitPrice)
        TextView tvUnitPrice;
        @BindView(R.id.tv_commission)
        TextView tvCommission;
        @BindView(R.id.tv_overNight)
        TextView tvOverNight;
        @BindView(R.id.tv_createTime)
        TextView tvCreateTime;
        @BindView(R.id.tv_stopLoss)
        TextView tvStopLoss;
        @BindView(R.id.tv_stopProfit)
        TextView tvStopProfit;
        @BindView(R.id.iv_sw)
        ImageView sw;
        @BindView(R.id.tv_close)
        TextView tvClose;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
