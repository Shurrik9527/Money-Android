package com.moyacs.canary.moudle.recharge;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
    private List<String> mLists;
    private OnViewLister mOnViewLister;
    public RechargeAdapter(List<String> mLists) {
        this.mLists = mLists;
    }

    @NonNull
    @Override
    public RechargeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recharge, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        if (pos == 0) {
            viewHolder.tvPrice.setSelected(true);
            oldSelectView = viewHolder.tvPrice;
            mOnViewLister.onItemClickListener(0);
        }
        viewHolder.itemView.setOnClickListener(v -> {
            if (oldSelectView != null) {
                oldSelectView.setSelected(false);
            }
            v.setSelected(true);
            oldSelectView = v;
            mOnViewLister.onItemClickListener(pos);
        });

        if(mLists!=null){
           String price = mLists.get(pos);
            viewHolder.tvPrice.setText("$"+price);
        }



    }

    @Override
    public int getItemCount() {
        return mLists==null?0:mLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }

    public interface   OnViewLister{
        void onItemClickListener(int pos);
    }
    public void setItemClickListener(OnViewLister onViewLister) {
        this.mOnViewLister = onViewLister;
    }
}
