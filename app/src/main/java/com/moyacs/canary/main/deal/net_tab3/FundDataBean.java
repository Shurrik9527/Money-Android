package com.moyacs.canary.main.deal.net_tab3;

/**
 * 作者：luoshen on 2018/4/18 0018 11:24
 * 邮箱：rsf411613593@gmail.com
 * 说明：资金页面 余额和保证金 等封装对象
 */

public class FundDataBean {

    /**
     * login : 813000
     * group : lmoa-main
     * balance : 5
     * credit : 0
     * margin : 0
     * margin_level : 0
     * margin_free : 5
     */

 /*   private int login; // MT4id
    private String group;//组名
    private Double balance;//余额
    private Double credit;//信用额度
    private Double margin;//保证金
    private Double margin_level;//保证金水平
    private Double margin_free;//可用保证金*/

    private String id; // id
    private String loginName; //登录账号
    private String rechargeAmount; //总金额充值
    private String balance; //可用余额
    private String grantAmount; //赠送金额
    private String withdrawAmount; //已提现金额
    private String cashDeposit; //保证金

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

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getGrantAmount() {
        return grantAmount;
    }

    public void setGrantAmount(String grantAmount) {
        this.grantAmount = grantAmount;
    }

    public String getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(String withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getCashDeposit() {
        return cashDeposit;
    }

    public void setCashDeposit(String cashDeposit) {
        this.cashDeposit = cashDeposit;
    }
}
