package com.moyacs.canary.product_fxbtg;

import android.widget.SeekBar;
import android.widget.TextView;

import com.moyacs.canary.common.NumberUtils;


/**
 * Created by fangzhu on 16/11/15.
 * 对SeekBar设置止盈止损
 */
public class SeekBarUtil {
    //显示百分比
    public static final boolean showPercent = false;

    //按照点滑动
    public static final int TYPE_POINT = 0;
    //按照百分比滑动
    public static final int TYPE_PERCENT = 1;
    /*买入多少手*/
    private int buyCount = 1;
    /*是否是代金券*/
    private boolean isQuan = false;
    SeekBar skbZhiying, skbZhisun;
    TextView tv_zhisunValue, tv_zhiyingValue, tv_maxZhisun, tv_maxZhiying,
            tv_zhisunMoney, tv_zhiyingMoney;

    public SeekBarUtil(SeekBar skbZhisun, SeekBar skbZhiying,
                       TextView tv_zhisunValue, TextView tv_zhiyingValue,
                       TextView tv_zhisunMoney, TextView tv_zhiyingMoney,
                       TextView tv_maxZhisun, TextView tv_maxZhiying) {
        this.skbZhiying = skbZhiying;
        this.skbZhisun = skbZhisun;
        this.tv_zhisunValue = tv_zhisunValue;
        this.tv_zhiyingValue = tv_zhiyingValue;
        this.tv_maxZhisun = tv_maxZhisun;
        this.tv_maxZhiying = tv_maxZhiying;
        this.tv_zhisunMoney = tv_zhisunMoney;
        this.tv_zhiyingMoney = tv_zhiyingMoney;
    }

    /**
     * 设置止盈
     *
     * @param product 产品
     */
    public void initSeekBarZhiying(final TradeCommonObj product) {
        if (product == null)
            return;
        //设置显示的最大只止盈点数
        tv_maxZhiying.setText(NumberUtils.moveLast0(product.getMaxStopProfit()) + "点");
        //可设置的最大点数-可设置的最小点数
        double d = product.getMaxStopProfit() - product.getMinStopProfit();
        //乘以放大倍数
        d = NumberUtils.multiply(d, getScale(product));
        //后台设置的移动一个点的值
        double step = NumberUtils.multiply(product.getMinMovePoint(), getScale(product));
        //后台设置的移动一个点的值
        d = NumberUtils.divide(d, step,2);
        //按照step
        //有小数点的话 需要增加一位
        d = Math.ceil(d);
        int max = (int) d;
        max += 1;
        skbZhiying.setMax(max);
//        if (product instanceof TradeOrder) {
//            //持仓
//            int currentPro = getProByPoint(product.getStopProfitPoint(), product, true);
//            if (currentPro > max)
//                currentPro = max;
//            skbZhiying.setProgress(currentPro);
//        } else {
//            skbZhiying.setProgress(0);
//        }
        setSeekBarVal(product, skbZhiying, tv_zhiyingValue, tv_zhiyingMoney, true);
        skbZhiying.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (product == null) {
                    tv_zhiyingValue.setText("");
                    return;
                }
                setSeekBarVal(product, skbZhiying, tv_zhiyingValue, tv_zhiyingMoney, true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 设置止损
     *
     * @param product 产品
     */
    public void initSeekBarZhisun(final TradeCommonObj product) {
        if (product == null)
            return;
        tv_maxZhisun.setText(NumberUtils.moveLast0(product.getMaxStopLoss()) + "点");
        double d = product.getMaxStopLoss() - product.getMinStopLoss();
        //乘以放大倍数
        d = NumberUtils.multiply(d, getScale(product));
        //后台设置的移动一个点的值
        double step = NumberUtils.multiply(product.getMinMovePoint(), getScale(product));
        //后台设置的移动一个点的值
        d = NumberUtils.divide(d, step,2);
        //有小数点的话 需要增加一位
        d = Math.ceil(d);
        int max = (int) d;
        max += 1;
        skbZhisun.setMax(max);
//        if (product instanceof TradeOrder) {
//            //持仓
//            int currentPro = getProByPoint(product.getStopLossPoint(), product, false);
//            if (currentPro > max)
//                currentPro = max;
//            skbZhisun.setProgress(currentPro);
//        } else {
//            skbZhisun.setProgress(0);
//        }

        setSeekBarVal(product, skbZhisun, tv_zhisunValue, tv_zhisunMoney, false);
        skbZhisun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (product == null) {
                    tv_zhisunValue.setText("");
                    return;
                }
                setSeekBarVal(product, skbZhisun, tv_zhisunValue, tv_zhisunMoney, false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * seekbar都是int类型的滑动,所以小数点放大倍数
     * 获取放大倍数
     *
     * @param product
     * @return
     */
    public int getScale(TradeCommonObj product) {
        if (product == null)
            return 1;
        //如果包含小数点，就认为是需要放大100倍
//        if (product.getMinStopLoss() > (int) product.getMinStopLoss())
//            return 100;
        //如果是镍的品种的话
//        if (TradeProduct.CODE_HGNI.equals(product.getContract()))
//            return 100;
        //根据getMinMovePoint 最小单位包含多少位小数点来确定
        int powY = NumberUtils.getPointPow(product.getMinMovePoint()+"");
        if (powY != 0) {
            return (int) Math.pow(10, powY);
        }
        return 1;
    }

    /**
     * 根据点位获取到seekbar的位置
     *
     * @param d       点位
     * @param product 产品或者持仓对象
     * @param zhiying true 止盈
     * @return
     */
    public int getProByPoint(double d, TradeCommonObj product, boolean zhiying) {
//        double realPoint = NumberUtil.add(start, NumberUtil.multiply((p - 1), product.getMinMovePoint()));
        double start = product.getMinStopLoss();
        if (zhiying)
            start = product.getMinStopProfit();
        double p = NumberUtils.divide(NumberUtils.subtract(d, start), product.getMinMovePoint(),2) + 1;
        //向上取整
        return (int) Math.ceil(p);
    }


    /**
     * 滑动的时候设置止盈止损的显示值
     *
     * @param product
     * @param skb
     * @param tvValue   显示止盈止损点数tetview
     * @param tvMoney   显示止盈止损金额
     * @param isZhiying true:止盈
     */
    public void setSeekBarVal(TradeCommonObj product, SeekBar skb, TextView tvValue, TextView tvMoney, boolean isZhiying) {
        if (product == null)
            return;
        int p = skb.getProgress();
        if (p == 0) {
            tvValue.setText("不限");
            tvMoney.setText("");
        } else {
            //注意这里的百分比是只 金额的百分值多少，不是getProgress/maxProgress
            int percent = 0;
            //当前产品真实爆仓点，由最小点对应的最小的百分比算出
//                    getMinStopProfitRate 10：表示10%，所以要除以100
            double pMaxPoint = NumberUtils.divide(product.getMinStopProfit(),
                    NumberUtils.divide(product.getMinStopProfitRate(), 100D,2),2);

            double realPoint = getPointByPro(product, skb, isZhiying);
            String str = NumberUtils.moveLast0(realPoint) + "点";

            //四舍五入获取到int值
            percent = (int) (NumberUtils.divide(realPoint, pMaxPoint,2) * 100);

            if (showPercent) {
                //多少点 加金额的百分比
                tvValue.setText(str + "(" + percent + "%)");
            } else {
                //算出止盈止损的钱
//                当前点位/calculatePoint*yk*buyCount
                double money = NumberUtils.multiply(NumberUtils.divide(realPoint, product.getCalculatePoint(),2),
                        NumberUtils.multiply(product.getYkDouble(), buyCount));

                if (isQuan && !isZhiying) {
                    // 如果是使用券并且是止损的话  设置是0
                    money = 0;
                }
                String strMoney = "(" + NumberUtils.moveLast0(money) + "元)";
                tvValue.setText(str);
                tvMoney.setText(strMoney);
            }
        }
    }

    /**
     * 根据进度条的位置得到真实的点数
     *
     * @param product   产品或者持仓单
     * @param isZhiying true表示止盈
     * @return 点位
     */
    public double getPointByPro(TradeCommonObj product, SeekBar skb, boolean isZhiying) {
        int p = skb.getProgress();
        //0表示最左边的位置
        if (p == 0)
            return 0;

        //最左边开始的值
        double start = 0;
        if (isZhiying) {
            //如果是止盈
            start = product.getMinStopProfit();
        } else {
            //止损
            start = product.getMinStopLoss();
        }
        double realPoint = NumberUtils.add(start, NumberUtils.multiply((p - 1), product.getMinMovePoint()));
        if (isZhiying) {
            if (realPoint > product.getMaxStopProfit())
                realPoint = product.getMaxStopProfit();
        } else {
            if (realPoint > product.getMaxStopLoss())
                realPoint = product.getMaxStopLoss();
        }
        return realPoint;
    }


    /**
     * @param product
     * @param isQuan
     * @param buyCount
     */
    public void updateSeekMoney(TradeCommonObj product, boolean isQuan, int buyCount) {
        this.isQuan = isQuan;
        this.buyCount = buyCount;
        //止损
        double realPoint = getPointByPro(product, skbZhisun, false);
        double money = 0;
        if (isQuan) {
            if (realPoint == 0) {
                tv_zhisunMoney.setText("");
            } else {
                tv_zhisunMoney.setText("(0元)");
            }
        } else {
            if (realPoint == 0) {
                tv_zhisunMoney.setText("");
            } else {
                money = NumberUtils.multiply(NumberUtils.divide(realPoint, product.getCalculatePoint(),2),
                        NumberUtils.multiply(product.getYkDouble(), buyCount));
                String strMoney = "(" + NumberUtils.moveLast0(money) + "元)";
                tv_zhisunMoney.setText(strMoney);
            }
        }


        //止赢
        realPoint = getPointByPro(product, skbZhiying, true);
        if (realPoint == 0) {
            tv_zhiyingMoney.setText("");
        } else {
            money = NumberUtils.multiply(NumberUtils.divide(realPoint, product.getCalculatePoint(),2),
                    NumberUtils.multiply(product.getYkDouble(), buyCount));
            String strMoney = "(" + NumberUtils.moveLast0(money) + "元)";
            tv_zhiyingMoney.setText(strMoney);
        }
    }

    /**
     * 得到真实的止盈点
     *
     * @param seekBar
     * @param product
     * @return
     */
    public static String getZhiying(SeekBar seekBar, TradeCommonObj product) {
        double value = 0, start = 0;
        int p = seekBar.getProgress();
        if (p == 0) {
            value = 0;
        } else {
            start = product.getMinStopProfit();
            value = NumberUtils.add(start, NumberUtils.multiply((p - 1), product.getMinMovePoint()));
        }
        //镍有可能超过step 0.02 递增
        if (value > product.getMaxStopProfit())
            value = product.getMaxStopProfit();
        return NumberUtils.moveLast0(value);
    }

    /**
     * 得到真实的止损点
     *
     * @param seekBar
     * @param product
     * @return
     */
    public static String getZhisun(SeekBar seekBar, TradeCommonObj product) {
        double value = 0, start = 0;
        int p = seekBar.getProgress();
        if (p == 0) {
            value = 0;
        } else {
            start = product.getMinStopLoss();
            value = NumberUtils.add(start, NumberUtils.multiply((p - 1), product.getMinMovePoint()));
        }
        //镍有可能超过step 0.02 递增
        if (value > product.getMaxStopLoss())
            value = product.getMaxStopLoss();
        return NumberUtils.moveLast0(value);
    }

    public boolean isQuan() {
        return isQuan;
    }

    public void setQuan(boolean quan) {
        isQuan = quan;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

}
