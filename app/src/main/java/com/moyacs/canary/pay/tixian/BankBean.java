package com.moyacs.canary.pay.tixian;

/**
 * 作者：luoshen on 2018/4/19 0019 15:58
 * 邮箱：rsf411613593@gmail.com
 * 说明：银行列表封装对象
 */

public class BankBean {

    /**
     * value : CDB
     * text : 国家开发银行
     */

    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
