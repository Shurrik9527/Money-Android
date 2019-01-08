package com.moyacs.canary.product_fxbtg.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyacs.canary.product_fxbtg.KlineCycle;
import com.moyacs.canary.util.ViewListenerAbs;

import java.util.List;

import www.moyacs.com.myapplication.R;

/**
 * @Author: Akame
 * @Date: 2019/1/8
 * @Description:
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<KlineCycle> goodsList;
    private int type;
    public static final int ITEM_NORMAL = 0;//正常状态下 竖屏
    public static final int ITEM_LAND = 1;//横屏状态下
    private int selectPos;
    private View selectTextView;
    private ViewListenerAbs.ItemClickListener itemClickListener;

    public ProductAdapter(List<KlineCycle> goodsList, int type) {
        this.goodsList = goodsList;
        this.type = type;
    }

    public KlineCycle getItem(int position) {
        if (goodsList != null && position < goodsList.size())
            return goodsList.get(position);
        return null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View rootView = null;
        if (type == ITEM_LAND) {
            rootView = View.inflate(viewGroup.getContext(), R.layout.product_land_cycle_item, null);
        } else {
            rootView = View.inflate(viewGroup.getContext(), R.layout.product_klinecycle_item, null);
        }
        return new MyViewHolder(rootView);
    }

    public void clear() {
        if (null != goodsList) {
            goodsList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        if (myViewHolder.textView == null)
            return;
        if (i == selectPos) {
            selectTextView = myViewHolder.textView;
            selectTextView.setSelected(true);
        } else {
            myViewHolder.textView.setSelected(false);
        }
        final int position = i;
        myViewHolder.textView.setOnClickListener(v -> {
            if (selectTextView != null) {
                selectTextView.setSelected(false);
            }
            v.setSelected(true);
            selectTextView = v;
            selectPos = position;
            if (itemClickListener != null) {
                itemClickListener.onItemClickListener(v, position);
            }
            String name = goodsList.get(position).getName();

        });

        myViewHolder.textView.setText(goodsList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
        }
    }

    public void setItemClickListener(ViewListenerAbs.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
