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
        private int symbolType;
        //手续费单价
        private int quantity_commission_charges;
        //过夜费单价
        private int quantity_overnight_fee;
        //每个数量对应的波动价格
        private int quantity_price_fluctuation;
        //挂单点位浮动单价
        private int entryOrders;
        //1展示 2不展示
        private String symbolShow;
        private int unit_price_one; // 单价一
        private int unit_price_two;
        private int unit_price_three;
        private int quantity_one; //数量一
        private int quantity_two;
        private int quantity_three;


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
            return symbolType;
        }

        public void setSymbolType(int symbolType) {
            this.symbolType = symbolType;
        }

        public int getQuantity_commission_charges() {
            return quantity_commission_charges;
        }

        public void setQuantity_commission_charges(int quantity_commission_charges) {
            this.quantity_commission_charges = quantity_commission_charges;
        }

        public int getQuantity_overnight_fee() {
            return quantity_overnight_fee;
        }

        public void setQuantity_overnight_fee(int quantity_overnight_fee) {
            this.quantity_overnight_fee = quantity_overnight_fee;
        }

        public int getQuantity_price_fluctuation() {
            return quantity_price_fluctuation;
        }

        public void setQuantity_price_fluctuation(int quantity_price_fluctuation) {
            this.quantity_price_fluctuation = quantity_price_fluctuation;
        }

        public int getEntryOrders() {
            return entryOrders;
        }

        public void setEntryOrders(int entryOrders) {
            this.entryOrders = entryOrders;
        }

        public String getSymbolShow() {
            return symbolShow;
        }

        public void setSymbolShow(String symbolShow) {
            this.symbolShow = symbolShow;
        }

        public int getUnit_price_one() {
            return unit_price_one;
        }

        public void setUnit_price_one(int unit_price_one) {
            this.unit_price_one = unit_price_one;
        }

        public int getUnit_price_two() {
            return unit_price_two;
        }

        public void setUnit_price_two(int unit_price_two) {
            this.unit_price_two = unit_price_two;
        }

        public int getUnit_price_three() {
            return unit_price_three;
        }

        public void setUnit_price_three(int unit_price_three) {
            this.unit_price_three = unit_price_three;
        }

        public int getQuantity_one() {
            return quantity_one;
        }

        public void setQuantity_one(int quantity_one) {
            this.quantity_one = quantity_one;
        }

        public int getQuantity_two() {
            return quantity_two;
        }

        public void setQuantity_two(int quantity_two) {
            this.quantity_two = quantity_two;
        }

        public int getQuantity_three() {
            return quantity_three;
        }

        public void setQuantity_three(int quantity_three) {
            this.quantity_three = quantity_three;
        }
    }

}
