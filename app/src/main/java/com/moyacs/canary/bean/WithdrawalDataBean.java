package com.moyacs.canary.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class WithdrawalDataBean implements Parcelable {

    private List<ContentBean> content;

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean implements Parcelable {
        /**
         * accountName : 张园
         * accountNumber : 6236681240000405706
         * amount : 1550
         * bankAddress : 中国建设银行江苏省江阴市周庄支行
         * bankName : 中国建议银行
         * commission : N
         * commit : 已出金
         * currency : USD
         * date : 1514693434000
         * exchangeRate : 0
         * id : 120
         * mt4User : {"balance":0,"currency":"USD","id":2745,"mt4id":812999,"regdate":1519089296000,"type":"DEMO","userId":1577}
         * paidAmount : 9873.5
         * paidCurrency : N/A
         * status : DONE
         * swiftCode :
         * type : WireTransfer
         */

        private String accountName;//账户名
        private String accountNumber;//账号
        private double amount;//出金金额
        private String bankAddress;//银行地址，开户行
        private String bankName;//银行名称
        private String commission;//手续费
        private String commit;//备注
        private String currency;//出金 币种
        private long date;//提现 时间
        private double exchangeRate;//汇率
        private long id;
        private Mt4UserBean mt4User;
        private double paidAmount;//转出金额
        private String paidCurrency;//转出币种
        private String status;//WAIT 等待处理  DONE 已转账 FAILD 提现失败
        private String swiftCode;
        private String type;//出金类型

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public void setBankAddress(String bankAddress) {
            this.bankAddress = bankAddress;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public void setCommit(String commit) {
            this.commit = commit;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public void setExchangeRate(double exchangeRate) {
            this.exchangeRate = exchangeRate;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setMt4User(Mt4UserBean mt4User) {
            this.mt4User = mt4User;
        }

        public void setPaidAmount(double paidAmount) {
            this.paidAmount = paidAmount;
        }

        public void setPaidCurrency(String paidCurrency) {
            this.paidCurrency = paidCurrency;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setSwiftCode(String swiftCode) {
            this.swiftCode = swiftCode;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccountName() {
            return accountName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public double getAmount() {
            return amount;
        }

        public String getBankAddress() {
            return bankAddress;
        }

        public String getBankName() {
            return bankName;
        }

        public String getCommission() {
            return commission;
        }

        public String getCommit() {
            return commit;
        }

        public String getCurrency() {
            return currency;
        }

        public long getDate() {
            return date;
        }

        public double getExchangeRate() {
            return exchangeRate;
        }

        public long getId() {
            return id;
        }

        public Mt4UserBean getMt4User() {
            return mt4User;
        }

        public double getPaidAmount() {
            return paidAmount;
        }

        public String getPaidCurrency() {
            return paidCurrency;
        }

        public String getStatus() {
            return status;
        }

        public String getSwiftCode() {
            return swiftCode;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "accountName='" + accountName + '\'' +
                    ", accountNumber='" + accountNumber + '\'' +
                    ", amount=" + amount +
                    ", bankAddress='" + bankAddress + '\'' +
                    ", bankName='" + bankName + '\'' +
                    ", commission='" + commission + '\'' +
                    ", commit='" + commit + '\'' +
                    ", currency='" + currency + '\'' +
                    ", date=" + date +
                    ", exchangeRate=" + exchangeRate +
                    ", id=" + id +
                    ", mt4User=" + mt4User +
                    ", paidAmount=" + paidAmount +
                    ", paidCurrency='" + paidCurrency + '\'' +
                    ", status='" + status + '\'' +
                    ", swiftCode='" + swiftCode + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        public static class Mt4UserBean implements Parcelable {
            /**
             * balance : 0
             * currency : USD
             * id : 2745
             * mt4id : 812999
             * regdate : 1519089296000
             * type : DEMO
             * userId : 1577
             */

            private int balance;
            private String currency;
            private int id;
            private int mt4id;
            private long regdate;
            private String type;
            private int userId;

            public int getBalance() {
                return balance;
            }

            public void setBalance(int balance) {
                this.balance = balance;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

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

            public long getRegdate() {
                return regdate;
            }

            public void setRegdate(long regdate) {
                this.regdate = regdate;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.balance);
                dest.writeString(this.currency);
                dest.writeInt(this.id);
                dest.writeInt(this.mt4id);
                dest.writeLong(this.regdate);
                dest.writeString(this.type);
                dest.writeInt(this.userId);
            }

            public Mt4UserBean() {
            }

            protected Mt4UserBean(Parcel in) {
                this.balance = in.readInt();
                this.currency = in.readString();
                this.id = in.readInt();
                this.mt4id = in.readInt();
                this.regdate = in.readLong();
                this.type = in.readString();
                this.userId = in.readInt();
            }

            public static final Creator<Mt4UserBean> CREATOR = new Creator<Mt4UserBean>() {
                @Override
                public Mt4UserBean createFromParcel(Parcel source) {
                    return new Mt4UserBean(source);
                }

                @Override
                public Mt4UserBean[] newArray(int size) {
                    return new Mt4UserBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.accountName);
            dest.writeString(this.accountNumber);
            dest.writeDouble(this.amount);
            dest.writeString(this.bankAddress);
            dest.writeString(this.bankName);
            dest.writeString(this.commission);
            dest.writeString(this.commit);
            dest.writeString(this.currency);
            dest.writeLong(this.date);
            dest.writeDouble(this.exchangeRate);
            dest.writeLong(this.id);
            dest.writeParcelable(this.mt4User, flags);
            dest.writeDouble(this.paidAmount);
            dest.writeString(this.paidCurrency);
            dest.writeString(this.status);
            dest.writeString(this.swiftCode);
            dest.writeString(this.type);
        }

        public ContentBean() {
        }

        protected ContentBean(Parcel in) {
            this.accountName = in.readString();
            this.accountNumber = in.readString();
            this.amount = in.readDouble();
            this.bankAddress = in.readString();
            this.bankName = in.readString();
            this.commission = in.readString();
            this.commit = in.readString();
            this.currency = in.readString();
            this.date = in.readLong();
            this.exchangeRate = in.readDouble();
            this.id = in.readLong();
            this.mt4User = in.readParcelable(Mt4UserBean.class.getClassLoader());
            this.paidAmount = in.readDouble();
            this.paidCurrency = in.readString();
            this.status = in.readString();
            this.swiftCode = in.readString();
            this.type = in.readString();
        }

        public static final Parcelable.Creator<ContentBean> CREATOR = new Parcelable.Creator<ContentBean>() {
            @Override
            public ContentBean createFromParcel(Parcel source) {
                return new ContentBean(source);
            }

            @Override
            public ContentBean[] newArray(int size) {
                return new ContentBean[size];
            }
        };
    }

    @Override
    public String toString() {
        return "WithdrawalDataBean{" +
                "content=" + content +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.content);
    }

    public WithdrawalDataBean() {
    }

    protected WithdrawalDataBean(Parcel in) {
        this.content = in.createTypedArrayList(ContentBean.CREATOR);
    }

    public static final Parcelable.Creator<WithdrawalDataBean> CREATOR = new Parcelable.Creator<WithdrawalDataBean>() {
        @Override
        public WithdrawalDataBean createFromParcel(Parcel source) {
            return new WithdrawalDataBean(source);
        }

        @Override
        public WithdrawalDataBean[] newArray(int size) {
            return new WithdrawalDataBean[size];
        }
    };
}
