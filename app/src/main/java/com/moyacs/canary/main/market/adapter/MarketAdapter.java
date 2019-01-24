package com.moyacs.canary.main.market.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyacs.canary.MyApplication;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.main.market.net.TradeVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;

/**
 * @Author: Akame
 * @Date: 2019/1/9
 * @Description:
 */
public class MarketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private List<TradeVo.Trade> marketList;
    private int animatorColor = 0;
    private boolean isUp;
    private Context context;
    private String rangeString;
    private String rangeValue;
    private boolean isShowDianCha = false; //是否显示点差
    private ObjectAnimator colorAnim;
    private AdapterClickListener clickListener;
    private boolean isShowFootView; // 是否显示底部View

    public MarketAdapter(List<TradeVo.Trade> marketList, Context context) {
        this.marketList = marketList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //如果是FooterView，直接在Holder中返回
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_market_footer, parent, false);
            return new FootViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_market, parent, false);
            return new ContentViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {
            final TradeVo.Trade marketDataBean = marketList.get(position);
            if (marketDataBean == null) {
                return;
            }
            ((ContentViewHolder) holder).tvRange.setText("0.00");//初始化涨跌幅
            ((ContentViewHolder) holder).tvPrice.setText("0.00"); // 初始化购买价格
            //品种中文名称
            ((ContentViewHolder) holder).tvChinaName.setText(marketDataBean.getSymbolName());
            //品种英文名称
            ((ContentViewHolder) holder).tvEnglishName.setText(marketDataBean.getSymbolCode());
            setViewContent(marketDataBean, (ContentViewHolder) holder);
            //条目点击事件
            holder.itemView.setOnClickListener(v -> {
                //跳转详情页面
                if (clickListener != null) {
                    clickListener.onItemClickListener(marketDataBean);
                }
            });
            //是否显示点差
            ((ContentViewHolder) holder).tvRange.setOnClickListener(v -> {
                isShowDianCha = !isShowDianCha;
                notifyDataSetChanged();
            });
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (holder instanceof ContentViewHolder) {
                setViewContent(marketList.get(position), (ContentViewHolder) holder);
            }
        }
    }

    private void setViewContent(TradeVo.Trade marketDataBean, ContentViewHolder holder) {
        // 获取旧价格，String类型
        String oldPrice = (String) holder.tvPrice.getText();
        //昨收
        double close = marketDataBean.getClose();
        //即将赋值的价格  买入价
        double newPrice_d = marketDataBean.getPriceBuy() == 0 ? marketDataBean.getClose() : marketDataBean.getPriceBuy();
        //根据保留的小数位截取数据
        String newPrice_d_scale = NumberUtils.setScale(newPrice_d, marketDataBean.getDigit());
        //设置最新价格
        holder.tvPrice.setText(newPrice_d_scale);
        int rangeColor = marketDataBean.getRangeColor();
        GradientDrawable rangeDrawableBg = (GradientDrawable) holder.tvRange.getBackground();
        if (rangeColor == 0) {
            rangeDrawableBg.setColor(context.getResources().getColor(R.color.sub_blue));
        } else {
            rangeDrawableBg.setColor(rangeColor);
        }
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
                //设置跌幅颜色
                rangeColor = context.getResources().getColor(R.color.trade_down);
                //计算涨跌值
                rangeValue = "-" + NumberUtils.doubleToString(subtract);
                //计算涨跌幅
                double range = NumberUtils.divide(subtract, close, 4);
                //涨跌幅数据格式化
                String s = NumberUtils.setScale2(range);
                //跌 加 - 号
                rangeString = "- " + s + "%";
            } else if (compare == 0) {

            } else {//新价格 > 昨收价格
                isUp = true;
                //设置涨幅颜色
                rangeColor = context.getResources().getColor(R.color.color_opt_gt);
                //计算价格差
                double subtract = NumberUtils.subtract(newPrice_d, close);
                //计算涨跌值
                rangeValue = "+" + NumberUtils.doubleToString(subtract);
                //计算涨跌幅
                double range = NumberUtils.divide(subtract, close, 4);
                //涨跌幅数据格式化
                String s = NumberUtils.setScale2(range);
                //涨 加 + 号
                rangeString = "+ " + s + "%";
            }
            marketDataBean.setRangValue(rangeValue);
            marketDataBean.setRangString(rangeString);
            marketDataBean.setUp(isUp);
            //缓存买入价，卖出价，涨跌幅背景 的颜色
            marketDataBean.setRangeColor(rangeColor);
            if (compare2 == -1) {//新价格 < 旧价格 ，跌
                animatorColor = MyApplication.instance.getResources().getColor(R.color.color_opt_lt_shan);
            } else if (compare2 == 1) {// 新价格 > 旧价格 ， 涨
                animatorColor = MyApplication.instance.getResources().getColor(R.color.color_opt_gt_shan);
            }
            GradientDrawable priceDrawableBg = (GradientDrawable) holder.tvPrice.getBackground();
            //如果是显示点差状态
            if (isShowDianCha) {
                //设置数据
                holder.tvRange.setText(rangeValue);
            } else {
                holder.tvRange.setText(rangeString);
            }
            if(rangeColor!=0){
                //买入价的字体颜色 ，与 涨跌幅的背景颜色一致
                holder.tvPrice.setTextColor(rangeColor);
                rangeDrawableBg.setColor(rangeColor);
            }
            //价格  执行动画
            startAnimatorRGB(priceDrawableBg, animatorColor);
        }
    }

    @Override
    public int getItemCount() {
        if (isShowFootView) {
            return marketList.size() + 1;
        } else {
            return marketList.size();
        }
    }

    /**
     * 为了防止 item 数据错乱，所以返回 position
     */
    @Override
    public int getItemViewType(int position) {
        //最后一个,应该加载Footer
        if (isShowFootView) {
            if ((position + 1) == getItemCount()) {
                return TYPE_FOOTER;
            }
        }
        return TYPE_NORMAL;
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_chinaname)
        TextView tvChinaName;
        @BindView(R.id.tv_englishname)
        TextView tvEnglishName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_range)
        TextView tvRange;
        @BindView(R.id.ll_price)
        LinearLayout llPrice;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_footer)
        LinearLayout llFooter;

        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llFooter.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onFootItemClickListener();
                }
            });
        }
    }

    private void startAnimatorRGB(GradientDrawable view, int resultColor) {
        ObjectAnimator colorAnim = ObjectAnimator.ofInt(view, "color", Color.TRANSPARENT, resultColor);
        colorAnim.setDuration(500); // 动画时间为2s
        colorAnim.setEvaluator(new ArgbEvaluator()); // 设置估值器
        colorAnim.setRepeatCount(1);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE); // 设置变化反转效果，即第一次动画执行完后再次执行时背景色时从后面的颜色值往前面的变化
        colorAnim.start();
    }

    public interface AdapterClickListener {
        void onItemClickListener(TradeVo.Trade dataBean);

        void onFootItemClickListener();
    }

    public void setAdapterClickListener(AdapterClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /***
     * 设置显示底部View
     * @param isShowFootView 是否显示底部View
     */
    public void setIsShowFootView(boolean isShowFootView) {
        this.isShowFootView = isShowFootView;
    }
}
