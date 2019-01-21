package com.moyacs.canary.main.me.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import www.moyacs.com.myapplication.R;

/**
 * @Author: Akame
 * @Date: 2019/1/21
 * @Description:
 */
public class UserRechargeAdapter extends RecyclerView.Adapter<UserRechargeAdapter.ViewHolder> {
    private View oldSelectView;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
