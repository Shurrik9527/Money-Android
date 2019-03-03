package com.moyacs.canary.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe盈利率基类
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ProfitLinesBean {

    private String id;
    private String name;
    private String rate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ProfitLinesBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }


}
