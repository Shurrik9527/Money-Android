package com.moyacs.canary.product_fxbtg;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by fangzhu on 2015/3/25.
 */
public class Goods implements Serializable {
    public static String GOODS_ID = "goodsId";
    public static String GOODS_SOURCE = "source";

    @DatabaseField(generatedId = true)
    private int localId;
    @DatabaseField
    private int goodsId;
    @DatabaseField
    private String code;
    @DatabaseField
    private String sourceCode;
    @DatabaseField
    private String name;
    @DatabaseField
    private String source;
    @DatabaseField
    private String customCode;

    public Goods(){}

    public Goods(String source, String name) {
        this.name = name;
        this.source = source;
    }

    public Goods(int goodsId, String code, String name, String source, String customCode) {
        this.goodsId = goodsId;
        this.code = code;
        this.name = name;
        this.source = source;
        this.customCode = customCode;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }
}
