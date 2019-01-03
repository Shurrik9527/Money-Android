package com.moyacs.canary.main.deal.net_tab3;

import java.util.List;

/**
 * 作者：luoshen on 2018/4/18 0018 13:36
 * 邮箱：rsf411613593@gmail.com
 * 说明：入金记录
 */

public class PaymentDateBean {

    private List<ContentBean> content;

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 971
         * transId : moa_global_1511243120603
         * amount : 1000
         * amountCNY : 6550
         * currency : USD
         * gateway : ECOREPAY
         * commit : null
         * mt4User : {"id":2745,"mt4id":812999,"currency":"USD","type":"DEMO","regdate":1519089296000,"userId":1577,"mt4Record":null,"balance":0}
         * date : 1511243121000
         * status : WAIT
         */

        private long id;
        private String transId;////支付网关流水
        private double amount;//入金金额
        private double amountCNY;//入金人民币
        private String currency;//入金币种
        private String gateway;//支付网关
        private String commit;//备注
        private Mt4UserBean mt4User;
        private long date;//操作日期
        private String status;//WAIT 等待支付     SUCCESS 支付成功

        public void setId(long id) {
            this.id = id;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public void setAmountCNY(double amountCNY) {
            this.amountCNY = amountCNY;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setGateway(String gateway) {
            this.gateway = gateway;
        }

        public void setCommit(String commit) {
            this.commit = commit;
        }

        public void setMt4User(Mt4UserBean mt4User) {
            this.mt4User = mt4User;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getId() {
            return id;
        }

        public String getTransId() {
            return transId;
        }

        public double getAmount() {
            return amount;
        }

        public double getAmountCNY() {
            return amountCNY;
        }

        public String getCurrency() {
            return currency;
        }

        public String getGateway() {
            return gateway;
        }

        public String getCommit() {
            return commit;
        }

        public Mt4UserBean getMt4User() {
            return mt4User;
        }

        public long getDate() {
            return date;
        }

        public String getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "id=" + id +
                    ", transId='" + transId + '\'' +
                    ", amount=" + amount +
                    ", amountCNY=" + amountCNY +
                    ", currency='" + currency + '\'' +
                    ", gateway='" + gateway + '\'' +
                    ", commit=" + commit +
                    ", mt4User=" + mt4User +
                    ", date=" + date +
                    ", status='" + status + '\'' +
                    '}';
        }

        public static class Mt4UserBean {
            /**
             * id : 2745
             * mt4id : 812999
             * currency : USD
             * type : DEMO
             * regdate : 1519089296000
             * userId : 1577
             * mt4Record : null
             * balance : 0
             */

            private int id;
            private int mt4id;
            private String currency;
            private String type;
            private long regdate;
            private int userId;
            private Object mt4Record;
            private int balance;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMt4id() {
                return mt4id;
            }

            public void setMt4id(int mt4id) {
                this.mt4id = mt4id;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public long getRegdate() {
                return regdate;
            }

            public void setRegdate(long regdate) {
                this.regdate = regdate;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public Object getMt4Record() {
                return mt4Record;
            }

            public void setMt4Record(Object mt4Record) {
                this.mt4Record = mt4Record;
            }

            public int getBalance() {
                return balance;
            }

            public void setBalance(int balance) {
                this.balance = balance;
            }

        }
    }

    @Override
    public String toString() {
        return "PaymentDateBean{" +
                "content=" + content +
                '}';
    }
}
