package com.moyacs.canary.service;

import com.moyacs.canary.util.DateUtil;

import java.util.Date;

/**
 * Socket 链接返回当前可交易信息
 * @Author: Akame
 * @Date: 2019/1/22
 * @Description:
 */
public class SocketQuotation {
    private String symbolCode;
    private double price;
    private MarketTime marketTime;

    public MarketTime getMarketTime() {
        return marketTime;
    }

    public void setMarketTime(MarketTime marketTime) {
        this.marketTime = marketTime;
    }

    public String getSymbolCode() {
        return symbolCode;
    }

    public void setSymbolCode(String symbolCode) {
        this.symbolCode = symbolCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

   public class MarketTime {
        private String time;

        public String getTime() {
            return DateUtil.parseDateToStr(new Date(Long.parseLong(time)), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        }

        public void setTime(String time) {
            this.time = time;
        }

       public long getLongTime() {
           return Long.valueOf(time);
       }
   }
}
