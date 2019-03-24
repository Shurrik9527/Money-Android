package com.moyacs.canary.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 汇率
 * @date 2019/3/18
 * @email 252774645@qq.com
 */
public class RateBean {
 //   {
//        "status": "ALREADY",
//            "scur": "USD", /*原币种*/
//            "tcur": "CNY", /*目标币种*/
//            "ratenm": "美元/人民币",
//            "rate": "6.5793", /*汇率结果(保留6位小数四舍五入) */
//            "update": "2016-06-24 08:30:37" /*数据更新时间*/
//    }

    private String status;
    private String scur;
    private String tcur;
    private String ratenm;
    private String rate;
    private String update;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScur() {
        return scur;
    }

    public void setScur(String scur) {
        this.scur = scur;
    }

    public String getTcur() {
        return tcur;
    }

    public void setTcur(String tcur) {
        this.tcur = tcur;
    }

    public String getRatenm() {
        return ratenm;
    }

    public void setRatenm(String ratenm) {
        this.ratenm = ratenm;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return "RateBean{" +
                "status='" + status + '\'' +
                ", scur='" + scur + '\'' +
                ", tcur='" + tcur + '\'' +
                ", ratenm='" + ratenm + '\'' +
                ", rate='" + rate + '\'' +
                ", update='" + update + '\'' +
                '}';
    }
}
