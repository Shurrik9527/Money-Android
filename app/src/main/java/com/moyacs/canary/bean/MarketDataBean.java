package com.moyacs.canary.bean;
import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 行情基类
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class MarketDataBean implements Serializable {

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

    //涨跌幅 显示文本
    private String rangString;
    //涨跌值  显示文本
    private String rangValue;
    //当前是否是上涨
    private boolean isUp;
    private String type;

    private String time; //刷新时间


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

    public String getRangString() {
        return rangString;
    }

    public void setRangString(String rangString) {
        this.rangString = rangString;
    }

    public String getRangValue() {
        return rangValue;
    }

    public void setRangValue(String rangValue) {
        this.rangValue = rangValue;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
