package com.moyacs.canary.main.market.net;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

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

        public void setQuantityPriceFluctuation(int quantityPriceFluctuation) {
            this.quantityPriceFluctuation = quantityPriceFluctuation;
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
    }

}
