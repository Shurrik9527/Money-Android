package com.moyacs.canary.main.market;

/**
 * 作者：luoshen on 2018/3/9 0009 16:41
 * 邮箱：rsf411613593@gmail.com
 * 说明：从 MT4 上获取的数据 封装
 */

public class MT4DataBean {

    private String symbol;//名称
    private String time_year;//时间：年月日
    private String time_hour;//时间：时分
    private String price_sale;//卖出价
    private String price_buy;//买入价


    public MT4DataBean(String symbol, String time_year, String time_hour, String price_sale, String price_buy) {
        this.symbol = symbol;
        this.time_year = time_year;
        this.time_hour = time_hour;
        this.price_sale = price_sale;
        this.price_buy = price_buy;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTime_year() {
        return time_year;
    }

    public String getTime_hour() {
        return time_hour;
    }

    public String getPrice_sale() {
        return price_sale;
    }

    public String getPrice_buy() {
        return price_buy;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setTime_year(String time_year) {
        this.time_year = time_year;
    }

    public void setTime_hour(String time_hour) {
        this.time_hour = time_hour;
    }

    public void setPrice_sale(String price_sale) {
        this.price_sale = price_sale;
    }

    public void setPrice_buy(String price_buy) {
        this.price_buy = price_buy;
    }
}
