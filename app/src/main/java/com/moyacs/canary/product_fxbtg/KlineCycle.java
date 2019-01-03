package com.moyacs.canary.product_fxbtg;

import java.io.Serializable;

/**
 * Created by fangzhu on 2015/5/15.
 */
public class KlineCycle implements Serializable {

    private String name;
    private String code;
    private int time;
    private int normal;
    public KlineCycle() {
    }

    public KlineCycle(String name, int normal, int time) {
        this.name = name;
        this.normal = normal;
        this.time = time;
    }
    public KlineCycle(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
