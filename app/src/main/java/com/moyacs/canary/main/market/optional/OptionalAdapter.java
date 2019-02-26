package com.moyacs.canary.main.market.optional;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.moyacs.canary.bean.TradeVo;
import com.moyacs.canary.util.ViewListenerAbs;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 自选适配器
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class OptionalAdapter extends RecyclerView.Adapter<OptionalAdapter.ViewHolder>{

    private boolean isSelect;
    private ViewListenerAbs.ItemClickListener itemClickListener;
    private List<TradeVo.Trade> tradeList;

    public OptionalAdapter(boolean isSelect, List<TradeVo.Trade> tradeList) {
        this.isSelect = isSelect;
        this.tradeList = tradeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_optional, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvOptional.setSelected(isSelect);
        viewHolder.tvOptional.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClickListener(v, i);
            }
        });
        viewHolder.tvOptional.setText(tradeList.get(i).getSymbolName());
    }

    @Override
    public int getItemCount() {
        return tradeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_optional)
        TextView tvOptional;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setItemClickListener(ViewListenerAbs.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
