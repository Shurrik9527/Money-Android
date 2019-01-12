package com.moyacs.canary.main.market.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
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
import com.moyacs.canary.main.market.net.MarketDataBean;

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
    private List<MarketDataBean> marketList;
    private int animatorColor = 0;
    private boolean isUp;
    private Context context;
    private int rangeColor;
    private String rangeString;
    private String rangeValue;
    private boolean isShowDianCha = false; //是否显示点差
    private ObjectAnimator colorAnim;
    private AdapterClickListener clickListener;
    private boolean isShowFootView; // 是否显示底部View

    public MarketAdapter(List<MarketDataBean> marketList, Context context) {
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
            return new ViewHolder(view);
        }
    }

    /**
     * 重写的  onBindViewHolder ，为了局部刷新某个条目中的某个控件
     *
     * @param holder
     * @param position
     * @param payloads
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (holder instanceof ViewHolder) {
            //表示 viewHolder 的整个条目所有数据都改变了！
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
                //表示，viewHolder 的一部分数据改变
            } else {
                final MarketDataBean marketDataBean = marketList.get(position);
                //数据不为空
                if (marketDataBean != null) {
//                    获取旧价格，String类型
                    String oldPrice = (String) ((ViewHolder) holder).tvPrice.getText();
                    //昨收
                    double close = marketDataBean.getClose();
                    //即将赋值的价格  买入价
//                    double newPrice_d = marketDataBean.getPrice_buy();
                    double newPrice_d = marketDataBean.getPrice_buy() == 0 ? marketDataBean.getOpen() : marketDataBean.getPrice_buy();
                    //根据保留的小数位截取数据
                    String newPrice_d_scale = NumberUtils.setScale(newPrice_d, marketDataBean.getDigit());
                    //设置最新价格
                    ((ViewHolder) holder).tvPrice.setText(newPrice_d_scale);

                    //设置 卖出价
//                Double price_sale = marketDataBean.getPrice_sale();
                    Double price_sale = marketDataBean.getPrice_sale() == 0 ? marketDataBean.getClose() : marketDataBean.getPrice_sale();
                    //根据保留的小数位截取数据
                    String price_sale_scale = NumberUtils.setScale(price_sale, marketDataBean.getDigit());
                    ((ViewHolder) holder).tvSale.setText(price_sale_scale);
                    //第一次进入的时候 oldPrice 没数据
                    if (oldPrice != null && !oldPrice.equals("null") && !oldPrice.equals("")) {
                        double oldprice_d = Double.valueOf(oldPrice);
                        //比较昨收价格  与 最新买入价 大小
                        int compare = NumberUtils.compare(newPrice_d, close);
                        //比较，新旧买入价
                        int compare2 = NumberUtils.compare(newPrice_d, oldprice_d);

                        //主要是为了保证 相减始终为正数，正负号手动添加
                        if (compare == -1) {// 新价格 < 昨收价格
                            isUp = false;
                            //计算新旧价格差
                            double subtract = NumberUtils.subtract(close, newPrice_d);
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
                            rangeColor = context.getResources().getColor(R.color.color_opt_gt);
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
                        //缓存涨跌幅
                        marketDataBean.setRang_(rangeString);
                        //缓存买入价，卖出价，涨跌幅背景 的颜色
                        marketDataBean.setRangeColor(rangeColor);
                        if (compare2 == -1) {//新价格 < 旧价格 ，跌
                            animatorColor = MyApplication.instance.getResources().getColor(R.color.color_opt_lt_50);
                        } else if (compare2 == 1) {// 新价格 > 旧价格 ， 涨
                            animatorColor = MyApplication.instance.getResources().getColor(R.color.color_opt_gt_50);
                        }
                        //价格  执行动画
                        startAnimatorRGB(((ViewHolder) holder).tvPrice, animatorColor);
                        //买入价的字体颜色 ，与 涨跌幅的背景颜色一致
                        ((ViewHolder) holder).tvPrice.setTextColor(rangeColor);
                        ((ViewHolder) holder).tvSale.setTextColor(rangeColor);
                        //如果是显示点差状态
                        if (isShowDianCha) {
                            //卖出 - 买入
                            double subtract = NumberUtils.subtract(price_sale, newPrice_d);
                            double abs = Math.abs(subtract);
                            //格式化小数位
                            String s = NumberUtils.setScale(abs, marketDataBean.getDigit());
                            //设置数据
                            ((ViewHolder) holder).tvRange.setText(s);
                        } else {
                            //获取 shape 的背景色
                            GradientDrawable gradientDrawable = (GradientDrawable) ((ViewHolder) holder).tvRange.getBackground();
                            gradientDrawable.setColor(rangeColor);
                            ((ViewHolder) holder).tvRange.setText(rangeString);
                        }

                        //缓存涨跌幅
                       /* switch (type_showTab) {
                            case 0:
                                if (ziXuanList != null) {
                                    ziXuanList.set(position, marketDataBean);
                                }
                                break;
                            case 1:
                                if (waiHuiList != null) {
                                    waiHuiList.set(position, marketDataBean);
                                }
                                break;
                            case 2:
                                if (guiJinShuList != null) {
                                    guiJinShuList.set(position, marketDataBean);
                                }
                                break;
                            case 3:
                                if (yuanYouList != null) {
                                    yuanYouList.set(position, marketDataBean);
                                }
                                break;
                            case 4:
                                if (quanQiuZhiShuList != null) {
                                    quanQiuZhiShuList.set(position, marketDataBean);
                                }
                                break;*/
                    }
                }
            }
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvRange.setText("");
            if (isShowDianCha) {
                ((ViewHolder) holder).tvRange.setBackgroundResource(R.color.white);
                ((ViewHolder) holder).tvRange.setTextColor(context.getResources().getColor(R.color.app_bottom_text));
            } else {
                ((ViewHolder) holder).tvRange.setTextColor(context.getResources().getColor(R.color.white));
                ((ViewHolder) holder).tvRange.setBackground(context.getResources().getDrawable(R.drawable.bg_market_text_item));
            }
            final MarketDataBean marketDataBean = marketList.get(position);
            if (marketDataBean == null) {
                return;
            }
            //获取 shape 的背景色
            //初始化背景色为蓝色
            GradientDrawable gradientDrawable = (GradientDrawable) ((ViewHolder) holder).tvRange.getBackground();
            gradientDrawable.setColor(context.getResources().getColor(R.color.dialog_btn_blue));
            int rangeColor = marketDataBean.getRangeColor();

            //品种中文名称
            ((ViewHolder) holder).tvChinaname.setText(marketDataBean.getSymbol_cn());
            //品种英文名称
            ((ViewHolder) holder).tvEnglishname.setText(marketDataBean.getSymbol());
            //买入价
            double newPrice_d = marketDataBean.getPrice_buy() == 0 ? marketDataBean.getOpen() : marketDataBean.getPrice_buy();
            ((ViewHolder) holder).tvPrice.setText(newPrice_d + "");
            //卖出价
            Double price_sale = marketDataBean.getPrice_sale() == 0 ? marketDataBean.getClose() : marketDataBean.getPrice_sale();
            ((ViewHolder) holder).tvSale.setText(price_sale + "");
            //涨跌幅
            if (!isShowDianCha) {
                ((ViewHolder) holder).tvRange.setText(marketDataBean.getRang_());
            }
            //设置对应的颜色
            if (rangeColor != 0) {
                ((ViewHolder) holder).tvPrice.setTextColor(rangeColor);
                ((ViewHolder) holder).tvSale.setTextColor(rangeColor);
                gradientDrawable.setColor(rangeColor);
            }

            //条目点击事件
            holder.itemView.setOnClickListener(v -> {
                //跳转详情页面
                if (clickListener != null) {
                    clickListener.onItemClickListener(marketDataBean);
                }
            });
            //是否显示点差
            ((ViewHolder) holder).tvRange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShowDianCha = !isShowDianCha;
                    notifyDataSetChanged();
                }
            });
        } else if (holder instanceof FootViewHolder) {
            holder.itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onFootItemClickListener();
                }
            });
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_chinaname)
        TextView tvChinaname;
        @BindView(R.id.tv_englishname)
        TextView tvEnglishname;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_sale)
        TextView tvSale;
        @BindView(R.id.tv_range)
        TextView tvRange;
        @BindView(R.id.ll_price)
        LinearLayout llPrice;
        @BindView(R.id.ll_sale)
        LinearLayout llSale;
        @BindView(R.id.ll_range)
        LinearLayout llRange;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void startAnimatorRGB(View view, int resultColor) {
        colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", MyApplication.instance.getResources().getColor(R.color.white), resultColor);
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

    public interface AdapterClickListener {
        void onItemClickListener(MarketDataBean dataBean);

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
