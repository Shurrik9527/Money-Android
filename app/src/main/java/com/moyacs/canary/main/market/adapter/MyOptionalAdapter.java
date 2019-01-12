package com.moyacs.canary.main.market.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyacs.canary.util.ViewListenerAbs;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.POST;
import www.moyacs.com.myapplication.R;

/**
 * @Author: Akame
 * @Date: 2019/1/11
 * @Description:
 */
public class MyOptionalAdapter extends RecyclerView.Adapter<MyOptionalAdapter.ViewHolder> {
    private boolean isSelect;
    private ViewListenerAbs.ItemClickListener itemClickListener;

    public MyOptionalAdapter(boolean isSelect) {
        this.isSelect = isSelect;
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
    }

    @Override
    public int getItemCount() {
        return 5;
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
