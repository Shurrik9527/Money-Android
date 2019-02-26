package com.moyacs.canary.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class TradeVo implements Serializable {
    private List<Trade> list;

    public List<Trade> getList() {
        return list;
    }

    public void setList(List<Trade> list) {
        this.list = list;
    }

    public class Trade implements Serializable {
        private String symbolCode;
        private String symbolName;
        //1可交易 2不可交易
        private String status;
        //1外汇 2贵金属 3原油 4全球指数
        private String symbolType;
        //手续费单价
        private float quantityCommissionCharges;
        //过夜费单价
        private float quantityOvernightFee;
        //每个数量对应的波动价格
        private float quantityPriceFluctuation;
        //挂单点位浮动单价
        private float entryOrders;
        //1展示 2不展示
        private String symbolShow;
        private int unitPriceOne; // 单价一
        private int unitPriceTwo;
        private int unitPriceThree;
        private int quantityOne; //数量一
        private int quantityTwo;
        private int quantityThree;
        private double open;//开仓价格 今开
        private double maxPrice;//最高
        private double minPrice;//最低
        private double close;//平仓价格  昨收

        public String getSymbolCode() {
            return symbolCode;
        }

        public void setSymbolCode(String symbolCode) {
            this.symbolCode = symbolCode;
        }

        public String getSymbolName() {
            return symbolName;
        }

        public void setSymbolName(String symbolName) {
            this.symbolName = symbolName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean canTrade() {
            return TextUtils.equals(getStatus(), "1");
        }

        public int getSymbolType() {
            return Integer.parseInt(symbolType);
        }

        public void setSymbolType(String symbolType) {
            this.symbolType = symbolType;
        }

        public float getQuantityCommissionCharges() {
            return quantityCommissionCharges;
        }

        public void setQuantityCommissionCharges(float quantityCommissionCharges) {
            this.quantityCommissionCharges = quantityCommissionCharges;
        }

        public float getQuantityOvernightFee() {
            return quantityOvernightFee;
        }

        public void setQuantityOvernightFee(float quantityOvernightFee) {
            this.quantityOvernightFee = quantityOvernightFee;
        }

        public float getQuantityPriceFluctuation() {
            return quantityPriceFluctuation;
        }


        public float getEntryOrders() {
            return entryOrders;
        }

        public void setEntryOrders(float entryOrders) {
            this.entryOrders = entryOrders;
        }

        public String getSymbolShow() {
            return symbolShow;
        }

        public void setSymbolShow(String symbolShow) {
            this.symbolShow = symbolShow;
        }

        public int getUnitPriceOne() {
            return unitPriceOne;
        }

        public void setUnitPriceOne(int unitPriceOne) {
            this.unitPriceOne = unitPriceOne;
        }

        public int getUnitPriceTwo() {
            return unitPriceTwo;
        }

        public void setUnitPriceTwo(int unitPriceTwo) {
            this.unitPriceTwo = unitPriceTwo;
        }

        public int getUnitPriceThree() {
            return unitPriceThree;
        }

        public void setUnitPriceThree(int unitPriceThree) {
            this.unitPriceThree = unitPriceThree;
        }

        public int getQuantityOne() {
            return quantityOne;
        }

        public void setQuantityOne(int quantityOne) {
            this.quantityOne = quantityOne;
        }

        public int getQuantityTwo() {
            return quantityTwo;
        }

        public void setQuantityTwo(int quantityTwo) {
            this.quantityTwo = quantityTwo;
        }

        public int getQuantityThree() {
            return quantityThree;
        }

        public void setQuantityThree(int quantityThree) {
            this.quantityThree = quantityThree;
        }

        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        public double getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(double maxPrice) {
            this.maxPrice = maxPrice;
        }

        public double getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(double minPrice) {
            this.minPrice = minPrice;
        }

        public double getClose() {
            return close;
        }

        public void setClose(double close) {
            this.close = close;
        }

        public void setQuantityPriceFluctuation(float quantityPriceFluctuation) {
            this.quantityPriceFluctuation = quantityPriceFluctuation;
        }


        //买入价，卖出价和买入价相同  通过 socket 获取
        private double priceBuy = 0.00000;
        //买入价 卖出价 涨跌幅背景的字体颜色
        private int rangeColor = 0;
        //涨跌幅 0.1% 显示文本
        private String rangString;
        //涨跌值 -0.001 显示文本
        private String rangValue;
        //当前是否是上涨
        private boolean isUp;
        //刷新时间
        private String time;
        //保存几位小数点
        private int digit = 3;

        public double getPriceBuy() {
            return priceBuy;
        }

        public void setPriceBuy(double priceBuy) {
            this.priceBuy = priceBuy;
        }

        public int getRangeColor() {
            return rangeColor;
        }

        public void setRangeColor(int rangeColor) {
            this.rangeColor = rangeColor;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getDigit() {
            return digit;
        }
    }

}
