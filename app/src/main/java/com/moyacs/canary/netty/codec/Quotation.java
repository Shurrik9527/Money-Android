package com.moyacs.canary.netty.codec;

import java.util.Date;

public class Quotation {

    private String symbol;//当前品种 代码
    private Date dateTime;//当前时间 data
    private double bid;//卖出价
    private double ask;//买入价
    private Long unixTime;//当前时间的毫秒数
    private String time;// 2018-03-15 10:27:44 这样格式的时间字符串


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public Long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(Long unixTime) {
        this.unixTime = unixTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Quotation{" +
                "symbol='" + symbol + '\'' +
                ", dateTime=" + dateTime +
                ", bid=" + bid +
                ", ask=" + ask +
                ", unixTime=" + unixTime +
                ", time='" + time + '\'' +
                '}';
    }
}
