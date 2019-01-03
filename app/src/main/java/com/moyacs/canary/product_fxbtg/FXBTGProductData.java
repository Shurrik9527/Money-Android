package com.moyacs.canary.product_fxbtg;

import java.io.Serializable;

/**
 * 作者：Created by ocean
 * 时间：on 2017/6/2.
 */

public class FXBTGProductData implements Serializable {

    private String code;// "AUDUSD",
    private String excode;// "FXBTG",
    private String name;// "澳元美元",
    private int tradeFlag;//1

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExcode() {
        return excode;
    }

    public void setExcode(String excode) {
        this.excode = excode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTradeFlag() {
        return tradeFlag;
    }

    public void setTradeFlag(int tradeFlag) {
        this.tradeFlag = tradeFlag;
    }
}
