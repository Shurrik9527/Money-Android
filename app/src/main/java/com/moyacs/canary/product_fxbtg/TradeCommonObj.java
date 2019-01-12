package com.moyacs.canary.product_fxbtg;



import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.util.StringUtil;

import java.io.Serializable;

/**
 * Created by dufangzhu on 2017/3/6.
 * 交易大厅的产品列表和持仓订单共有的字段
 * 主要用来设置止盈止损字段
 */

public class TradeCommonObj implements Serializable {

    private double minStopLoss;//止损最小点
    private double minStopLossRate;//止损最小百分比
    private double minStopProfit;//止盈最小点
    private double minStopProfitRate;//止盈最小百分比
    private double maxStopLoss;//止损最高点
    private double maxStopLossRate;//止损最高点百分比
    private double maxStopProfit;//最高止盈点
    private double maxStopProfitRate;//最高止盈点百分比
    /*最小移动点位 比如哈贵镍移动点位是0.02*/
    private double minMovePoint = 1;
    /*最小单位波动的单位 如镍最小波动0.01个点，银最小是1个点;算点位的金钱：当前点位/calculatePoint*yk*/
    private double calculatePoint = 1;


    //交易大厅特有的字段
    /*产品浮动最小单位1个点的金额  接口会传空字符串*/
    private String yk;


    //持仓特有的字段
    //止损止盈点数 支持小数点
    private double stopLossPoint;//止损点数
    private double stopProfitPoint;//止盈点数

    public double getMinStopLoss() {
        return minStopLoss;
    }

    public void setMinStopLoss(double minStopLoss) {
        this.minStopLoss = minStopLoss;
    }

    public double getMinStopLossRate() {
        return minStopLossRate;
    }

    public void setMinStopLossRate(double minStopLossRate) {
        this.minStopLossRate = minStopLossRate;
    }

    public double getMinStopProfit() {
        return minStopProfit;
    }

    public void setMinStopProfit(double minStopProfit) {
        this.minStopProfit = minStopProfit;
    }

    public double getMinStopProfitRate() {
        return minStopProfitRate;
    }

    public void setMinStopProfitRate(double minStopProfitRate) {
        this.minStopProfitRate = minStopProfitRate;
    }

    public double getMaxStopLoss() {
        return maxStopLoss;
    }

    public void setMaxStopLoss(double maxStopLoss) {
        this.maxStopLoss = maxStopLoss;
    }

    public double getMaxStopLossRate() {
        return maxStopLossRate;
    }

    public void setMaxStopLossRate(double maxStopLossRate) {
        this.maxStopLossRate = maxStopLossRate;
    }

    public double getMaxStopProfit() {
        return maxStopProfit;
    }

    public void setMaxStopProfit(double maxStopProfit) {
        this.maxStopProfit = maxStopProfit;
    }

    public double getMaxStopProfitRate() {
        return maxStopProfitRate;
    }

    public void setMaxStopProfitRate(double maxStopProfitRate) {
        this.maxStopProfitRate = maxStopProfitRate;
    }

    public double getMinMovePoint() {
        return minMovePoint;
    }

    public void setMinMovePoint(double minMovePoint) {
        this.minMovePoint = minMovePoint;
    }

    public double getCalculatePoint() {
        return calculatePoint;
    }

    public void setCalculatePoint(double calculatePoint) {
        this.calculatePoint = calculatePoint;
    }

    public double getYkDouble() {
        if (StringUtil.isEmpty(yk))
            return 0;
        try {
            return Double.parseDouble(yk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getYk() {
        return yk;
    }

    public void setYk(String yk) {
        this.yk = yk;
    }

    public double getStopProfitPoint() {
        return stopProfitPoint;
    }

    public void setStopProfitPoint(double stopProfitPoint) {
        this.stopProfitPoint = stopProfitPoint;
    }

    public double getStopLossPoint() {
        return stopLossPoint;
    }

    public void setStopLossPoint(double stopLossPoint) {
        this.stopLossPoint = stopLossPoint;
    }


    /**
     *
     * @param point 波动的点数
     * @param countSize 手数
     * @return
     */
    public double getMoneyByPoint(double point,int countSize) {
        try {
            //getCalculatePoint  最小单位
            //getYkDouble()最小单位表示的金额
            double money = NumberUtils.multiply(NumberUtils.multiply(NumberUtils.divide(point, getCalculatePoint(),4),
                    getYkDouble()),countSize);
            return money;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
