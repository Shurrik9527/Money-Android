package com.moyacs.canary.main.homepage.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.main.market.net.MarketDataBean;
import com.moyacs.canary.util.ScreenUtil;
import com.moyacs.canary.util.ViewListenerAbs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;

/**
 * @Author: Akame
 * @Date: 2019/1/8
 * @Description:
 */
public class TradeHorizontalAdapter extends RecyclerView.Adapter<TradeHorizontalAdapter.ViewHolder> {
    private List<MarketDataBean> marketDataBeanList;

    //买入价 动画闪跳颜色
    private int animatorColor = 0;
    //买入价 字体颜色
    private int rangeColor = 0;
    //决定跳转到的页面字体显示什么颜色
    private boolean isUp;
    //涨跌幅
    private String rangeString;
    //涨跌值
    private String rangeValue;
    private ViewListenerAbs.ItemClickListener itemClickListener;

    public TradeHorizontalAdapter(List<MarketDataBean> marketDataBeanList) {
        this.marketDataBeanList = marketDataBeanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vlayout_horizontalrecyclerview_item, parent, false);
        //设置 recyclerView 条目的宽度
        view.getLayoutParams().width = ScreenUtil.getScreenWidth(parent.getContext()) / 3;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MarketDataBean marketDataBean = marketDataBeanList.get(position);
        if (marketDataBean != null) {
            holder.tvName.setText(marketDataBean.getSymbol_cn());
            //获取旧价格
            String oldPrice = (String) holder.tvPrice.getText();
            //昨收
            double close = marketDataBean.getClose();
            //即将赋值的价格  最新的买入价
            double newPrice_d = marketDataBean.getPrice_buy();
            //根据保留的小数位截取数据
            String newPrice_d_scale = NumberUtils.setScale(newPrice_d, marketDataBean.getDigit());
            //设置最新价格
            holder.tvPrice.setText(newPrice_d_scale);

            //第一次进入的时候 oldPrice 没数据
            if (oldPrice != null && !oldPrice.equals("null") && !oldPrice.equals("")) {
                double oldPrice_d = Double.valueOf(oldPrice);
                //比较昨收价格  与 最新买入价 大小
                int compare = NumberUtils.compare(newPrice_d, close);
                //比较，新旧买入价
                int compare2 = NumberUtils.compare(newPrice_d, oldPrice_d);

                //主要是为了保证 相减始终为正数，正负号手动添加
                if (compare == -1) {// 新价格 < 昨收价格
                    isUp = false;
                    //计算新旧价格差
                    double subtract = NumberUtils.subtract(close, newPrice_d);
                    //买入价的字体颜色
                    rangeColor = holder.itemView.getResources().getColor(R.color.trade_down);
                    //计算涨跌值
                    rangeValue = "-" + NumberUtils.doubleToString(subtract);
                    //计算涨跌幅
                    double range = NumberUtils.divide(subtract, close, 4);
                    //涨跌幅数据格式化
                    String s = NumberUtils.setScale2(range);
                    //跌 加 - 号
                    rangeString = "-" + s + "%";
                } else if (compare != 0) {//新价格 > 昨收价格
                    isUp = true;
                    rangeColor = holder.itemView.getResources().getColor(R.color.color_opt_gt);
                    double subtract = NumberUtils.subtract(newPrice_d, close);
                    //计算涨跌值
                    rangeValue = "+" + NumberUtils.doubleToString(subtract);
                    //计算涨跌幅
                    double range = NumberUtils.divide(subtract, close, 4);
                    //涨跌幅数据格式化
                    String s = NumberUtils.setScale2(range);
                    //涨 加 + 号
                    rangeString = "+" + s + "%";
                }
                holder.tvvalueAndRate.setText(rangeValue + "   " + rangeString);
                //这里的背景颜色表示实时的涨跌 所有由前后两次买入价格决定
                // 价格 执行动画的颜色， 靠 前后两次买入价对比决定
                if (compare2 == -1) {//新价格 < 旧价格 ，跌
                    animatorColor = holder.itemView.getResources().getColor(R.color.color_opt_lt_shan);
                } else if (compare2 == 1) {// 新价格 > 旧价格 ， 涨
                    animatorColor = holder.itemView.getResources().getColor(R.color.color_opt_gt_shan);
                }
                //价格  执行动画
                getAnimator(holder.relativeRoot, animatorColor);
                //买入价的字体颜色 ，
                holder.tvPrice.setTextColor(rangeColor);
                holder.tvvalueAndRate.setTextColor(rangeColor);
                marketDataBean.setRangString(rangeString);
                marketDataBean.setRangValue(rangeValue);
                marketDataBean.setUp(isUp);
            }
        }
        //外汇点击进入详情
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClickListener(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return marketDataBeanList.size();
    }

    /**
     * 为了防止 item 数据错乱，所以返回 position
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_valueAndRate)
        TextView tvvalueAndRate;
        @BindView(R.id.relative_root)
        RelativeLayout relativeRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setItemClickListener(ViewListenerAbs.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    //属性动画
    private ValueAnimator colorAnim;

    /**
     * 渐变动画
     */
    private void getAnimator(final View view, final int resultColor) {
        colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", view.getContext().getResources().getColor(R.color.white), resultColor);
        colorAnim.setDuration(500); // 动画时间为2s
        colorAnim.setEvaluator(new ArgbEvaluator()); // 设置估值器
        //监听动画执行完毕
        colorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                colorAnim = null;
            }
        });
        colorAnim.setRepeatCount(1);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE); // 设置变化反转效果，即第一次动画执行完后再次执行时背景色时从后面的颜色值往前面的变化
        colorAnim.start();
    }
}
