package com.moyacs.canary.main.deal.capital;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyacs.canary.bean.TransactionRecordVo;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.util.ForeignUtil;
import com.moyacs.canary.util.MoneyUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<TransactionRecordVo.Record> recordList;
    private Context context;

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
        /*String symbol_cn = SPUtils.getInstance(AppConstans.allMarket_en_cn).getString(chiCangDateBean.getSymbolCode());
        if (symbol_cn == null || symbol_cn.equals("")) {
            return;
        }*/

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
        String type_cn;
        if (TextUtils.equals("1", chiCangDateBean.getRansactionType())) {
            type_cn = "买涨";
        } else {
            type_cn = "买跌";
        }
        holder.tvType.setText(type_cn + "\t\t" + chiCangDateBean.getLot() + "手");
        holder.tvType.setTextColor(color);
        holder.vHead.setBackgroundColor(color);
        holder.tvSymbolCn.setText(ForeignUtil.codeFormatCN(chiCangDateBean.getSymbolCode()) + "\t\t" + chiCangDateBean.getMoney() + ForeignUtil.formatForeignUtil(chiCangDateBean.getSymbolCode()));
        //收益
        String p_unit;
        if (Math.abs(chiCangDateBean.getProfit()) > 0) {
            color = context.getResources().getColor(R.color.color_opt_gt);
            p_unit = "+";
        } else {
            color = context.getResources().getColor(R.color.color_opt_lt);
            p_unit = "-";
        }
        holder.tvProfit.setText(p_unit + MoneyUtil.moneyAdd(String.valueOf(chiCangDateBean.getMoney()), String.valueOf(chiCangDateBean.getCommissionCharges())) + "美元");
        holder.tvProfit.setTextColor(color);
        //建仓时间
        holder.tvTime.setText(chiCangDateBean.getCreateTime());
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
        @BindView(R.id.v_head)
        View vHead;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_symbol_cn)
        TextView tvSymbolCn;
        @BindView(R.id.tv_profit)
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
