package com.moyacs.canary.main.deal.net_tab2;

import java.util.List;

/**
 * 作者：luoshen on 2018/4/16 0016 20:32
 * 邮箱：rsf411613593@gmail.com
 * 说明：持仓列表数据封装类
 */

public class ChiCangDateBean {


    /**
     * login : 812999
     * ticket : 93006
     * volume : 0.01
     * symbol : EURUSD
     * cmd : BUY
     * open_time : 1522842701000
     * close_time : null
     * open_price : 1.22944
     * close_price : 0
     * sl : 0
     * tp : 0
     * profit : 0
     */

    private int login;
    private int ticket;//订单号
    private double volume;
    private String symbol;
    private String cmd;
    private long open_time;
    private long close_time;
    private double open_price;
    private double close_price;
    private double sl;
    private double tp;
    private double profit;

    private double price_buy;//买入价
    private double price_sell;//卖出价
    private int digit;//小数点位数

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public double getPrice_sell() {
        return price_sell;
    }

    public void setPrice_sell(double price_sell) {
        this.price_sell = price_sell;
    }

    public double getPrice_buy() {
        return price_buy;
    }

    public void setClose_price(double close_price) {
        this.close_price = close_price;
    }

    public void setSl(double sl) {
        this.sl = sl;
    }

    public void setTp(double tp) {
        this.tp = tp;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public void setPrice_buy(double price_buy) {
        this.price_buy = price_buy;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public long getOpen_time() {
        return open_time;
    }

    public void setOpen_time(long open_time) {
        this.open_time = open_time;
    }

    public long getClose_time() {
        return close_time;
    }

    public void setClose_time(long close_time) {
        this.close_time = close_time;
    }

    public double getOpen_price() {
        return open_price;
    }

    public void setOpen_price(double open_price) {
        this.open_price = open_price;
    }

    public double getClose_price() {
        return close_price;
    }

    public void setClose_price(int close_price) {
        this.close_price = close_price;
    }

    public double getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public double getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "ChiCangDateBean{" +
                "login=" + login +
                ", ticket=" + ticket +
                ", volume=" + volume +
                ", symbol='" + symbol + '\'' +
                ", cmd='" + cmd + '\'' +
                ", open_time=" + open_time +
                ", close_time=" + close_time +
                ", open_price=" + open_price +
                ", close_price=" + close_price +
                ", sl=" + sl +
                ", tp=" + tp +
                ", profit=" + profit +
                '}';
    }
}
