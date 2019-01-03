package com.moyacs.canary.product_fxbtg.net_kline;

/**
 * 作者：luoshen on 2018/3/23 0023 16:37
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class KLineData {

    /**
     * time : 1521734280000
     * open : 1231.52
     * high : 1231.56
     * low : 1231.01
     * close : 1231.07
     * volume : 334
     */

    private long time;// 时间 long 类型
    private double open;//今开
    private double high;//最高
    private double low;//最低
    private double close;//昨收
    private int volume;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "KLineData{" +
                "time=" + time +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}
