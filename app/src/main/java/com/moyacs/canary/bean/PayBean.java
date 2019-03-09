package com.moyacs.canary.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public class PayBean {

    private String out_trade_no;
    private String err_msg;
    private String err_code;
    private String mer_trade_no;
    private String pay_url;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getMer_trade_no() {
        return mer_trade_no;
    }

    public void setMer_trade_no(String mer_trade_no) {
        this.mer_trade_no = mer_trade_no;
    }

    public String getPay_url() {
        return pay_url;
    }

    public void setPay_url(String pay_url) {
        this.pay_url = pay_url;
    }

    @Override
    public String toString() {
        return "PayBean{" +
                "out_trade_no='" + out_trade_no + '\'' +
                ", err_msg='" + err_msg + '\'' +
                ", err_code='" + err_code + '\'' +
                ", mer_trade_no='" + mer_trade_no + '\'' +
                ", pay_url='" + pay_url + '\'' +
                '}';
    }
}
