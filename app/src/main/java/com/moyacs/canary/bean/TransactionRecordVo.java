package com.moyacs.canary.bean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class TransactionRecordVo {
    private List<Record> list;

    public List<Record> getList() {
        return list;
    }

    public void setList(List<Record> list) {
        this.list = list;
    }

    public class Record {
        private String id;
        private String loginName;
        private String symbolCode;
        private String createTime;
        private int lot;
        private String unitPrice;
        private String money;
        private String overnightFee;
        private String commissionCharges;
        private int transactionStatus;
        private double exponent;
        private String closeOutPrice;
        private String entryOrdersPrice;
        private String errorRange;
        private String endTime;
        private double stopLossCount;
        private double stopProfitCount;
        private String ransactionType;
        private String isOvernight;
        private double profit;
        private String entryOrdersTime;

        private int digit;
        private double price_buy;
        private double price_sell;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getSymbolCode() {
            return symbolCode;
        }

        public void setSymbolCode(String symbolCode) {
            this.symbolCode = symbolCode;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getLot() {
            return lot;
        }

        public void setLot(int lot) {
            this.lot = lot;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getOvernightFee() {
            return overnightFee;
        }

        public void setOvernightFee(String overnightFee) {
            this.overnightFee = overnightFee;
        }

        public String getCommissionCharges() {
            return commissionCharges;
        }

        public void setCommissionCharges(String commissionCharges) {
            this.commissionCharges = commissionCharges;
        }

        public int getTransactionStatus() {
            return transactionStatus;
        }

        public void setTransactionStatus(int transactionStatus) {
            this.transactionStatus = transactionStatus;
        }

        public double getExponent() {
            return exponent;
        }

        public void setExponent(double exponent) {
            this.exponent = exponent;
        }

        public String getCloseOutPrice() {
            return closeOutPrice;
        }

        public void setCloseOutPrice(String closeOutPrice) {
            this.closeOutPrice = closeOutPrice;
        }

        public String getEntryOrdersPrice() {
            return entryOrdersPrice;
        }

        public void setEntryOrdersPrice(String entryOrdersPrice) {
            this.entryOrdersPrice = entryOrdersPrice;
        }

        public String getErrorRange() {
            return errorRange;
        }

        public void setErrorRange(String errorRange) {
            this.errorRange = errorRange;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public double getStopLossCount() {
            return stopLossCount;
        }

        public void setStopLossCount(double stopLossCount) {
            this.stopLossCount = stopLossCount;
        }

        public double getStopProfitCount() {
            return stopProfitCount;
        }

        public void setStopProfitCount(double stopProfitCount) {
            this.stopProfitCount = stopProfitCount;
        }

        public String getRansactionType() {
            return ransactionType;
        }

        public void setRansactionType(String ransactionType) {
            this.ransactionType = ransactionType;
        }

        public String getIsOvernight() {
            return isOvernight;
        }

        public void setIsOvernight(String isOvernight) {
            this.isOvernight = isOvernight;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public String getEntryOrdersTime() {
            return entryOrdersTime;
        }

        public void setEntryOrdersTime(String entryOrdersTime) {
            this.entryOrdersTime = entryOrdersTime;
        }

        public int getDigit() {
            return digit;
        }

        public void setDigit(int digit) {
            this.digit = digit;
        }

        public void setPrice_buy(double price_buy) {
            this.price_buy = price_buy;
        }

        public void setPrice_sell(double price_sell) {
            this.price_sell = price_sell;
        }

        public double getPrice_buy() {
            return price_buy;
        }

        public double getPrice_sell() {
            return price_sell;
        }
    }
}
