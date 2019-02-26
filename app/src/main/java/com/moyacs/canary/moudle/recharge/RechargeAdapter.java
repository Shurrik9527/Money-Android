package com.moyacs.canary.moudle.recharge;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import www.moyacs.com.myapplication.R;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.ViewHolder>{
    private View oldSelectView;

    @NonNull
    @Override
    public RechargeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recharge, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i == 0) {
            viewHolder.tvPrice.setSelected(true);
            oldSelectView = viewHolder.tvPrice;
        }
        viewHolder.itemView.setOnClickListener(v -> {
            if (oldSelectView != null) {
                oldSelectView.setSelected(false);
            }
            v.setSelected(true);
            oldSelectView = v;
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}
