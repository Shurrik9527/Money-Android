package com.moyacs.canary.main.market.net;

import java.util.List;

/**
 * 作者：luoshen on 2018/3/6 0006 18:07
 * 邮箱：rsf411613593@gmail.com
 * 说明：行情 数据结构
 */

public class MarketDataBean {
    /*  *
     * symbol : EURUSD
     * symbol_cn : 欧元/美元
     * digit : 5
     * visible : true
     * trading : true
     * open : 1088.97
     * high : 1089.68
     * low : 1088.83
     * close : 1089.59*/


    private String symbol;//英文名 代码
    private String symbol_cn;//中文名
    private int digit;//小数点位数   主要是为了设置买入价显示的位数
    private boolean visible;//是否显示
    private boolean trading;//是否可交易
    private double open;//开仓价格 今开
    private double high;//最高
    private double low;//最低
    private double close;//平仓价格  昨收
    //买入价，通过 socket 获取
    private Double price_buy = 0.00000;

    //卖出价，通过 socket 获取
    private Double price_sale = 0.00000;


    //涨跌幅，（最新买入价 - 昨收）/昨收
    private String rang_ = "0.00%";
    //止损点位
    private int stops_level;


    //买入价 卖出价 涨跌幅背景的字体颜色
    private int rangeColor = 0;

    private TradeVo.Trade trade;

    public void setStops_level(int stops_level) {
        this.stops_level = stops_level;
    }

    public int getStops_level() {
        return stops_level;
    }

    public Double getPrice_sale() {
        return price_sale;
    }

    public void setPrice_sale(Double price_sale) {
        this.price_sale = price_sale;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol_cn() {
        return symbol_cn;
    }

    public void setSymbol_cn(String symbol_cn) {
        this.symbol_cn = symbol_cn;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isTrading() {
        return trading;
    }

    public void setTrading(boolean trading) {
        this.trading = trading;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public Double getPrice_buy() {
        return price_buy;
    }

    public void setPrice_buy(Double price_buy) {
        this.price_buy = price_buy;
    }

    public String getRang_() {
        return rang_;
    }

    public void setRang_(String rang_) {
        this.rang_ = rang_;
    }

    public int getRangeColor() {
        return rangeColor;
    }

    public void setRangeColor(int rangeColor) {
        this.rangeColor = rangeColor;
    }

    public TradeVo.Trade getTrade() {
        return trade;
    }

    public void setTrade(TradeVo.Trade trade) {
        this.trade = trade;
    }

    @Override
    public String toString() {
        return "MarketDataBean{" +
                "symbol='" + symbol + '\'' +
                ", symbol_cn='" + symbol_cn + '\'' +
                ", digit=" + digit +
                ", visible=" + visible +
                ", trading=" + trading +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", price_buy=" + price_buy +
                ", price_sale=" + price_sale +
                ", rang_='" + rang_ + '\'' +
                ", stops_level=" + stops_level +
                ", rangeColor=" + rangeColor +
                '}';
    }
}
