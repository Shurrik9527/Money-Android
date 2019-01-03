package com.moyacs.canary.main.deal.net_tab3;

import java.util.List;

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
        private double lot;
        private String unitPrice;
        private String money;
        private String overnightFee;
        private String commissionCharges;
        private String transactionStatus;
        private String exponent;
        private String closeOutPrice;
        private String entryOrdersPrice;
        private String errorRange;
        private String endTime;
        private String stopLossCount;
        private String stopProfitCount;
        private String ransactionType;
        private String isOvernight;
        private double profit;
        private String entryOrdersTime;

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

        public double getLot() {
            return lot;
        }

        public void setLot(double lot) {
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

        public String getTransactionStatus() {
            return transactionStatus;
        }

        public void setTransactionStatus(String transactionStatus) {
            this.transactionStatus = transactionStatus;
        }

        public String getExponent() {
            return exponent;
        }

        public void setExponent(String exponent) {
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

        public String getStopLossCount() {
            return stopLossCount;
        }

        public void setStopLossCount(String stopLossCount) {
            this.stopLossCount = stopLossCount;
        }

        public String getStopProfitCount() {
            return stopProfitCount;
        }

        public void setStopProfitCount(String stopProfitCount) {
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
    }
}
