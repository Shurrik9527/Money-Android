package com.moyacs.canary.main.homepage.net;


/**
 * 作者：luoshen on 2018/4/10 0010 14:34
 * 邮箱：rsf411613593@gmail.com
 * 说明：交易机会数据封装
 */

public class DealChanceDate {

    /**
     * id : 2
     * name : 白银
     * trend : 看空
     * type : 专家解读
     * title : 震荡回落低位寻求支持
     * suggest : 目标1.3800,止损30%
     * date : 2018-03-13
     * time : 00:00:02
     * range : 20
     * basic_title : 市场鼻香情绪减缓
     * basic_trend : 利空
     * basic_detail : 白宫方面表示,美国总统特朗普可能...
     * technology_image : 技术分析图片地址
     * technology_detail : 日内银价维持窄幅区间震荡...
     */

    private int id;
    private String name;//名称
    private String trend;//趋势
    private String type;//类型
    private String title;//标题
    private String suggest;//建议
    private String date;//年月日
    private String time;//时分秒
    private int range;//买涨比例 0-100
    private String basic_title;
    private String basic_trend;
    private String basic_detail;
    private String technology_image;
    private String technology_detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getBasic_title() {
        return basic_title;
    }

    public void setBasic_title(String basic_title) {
        this.basic_title = basic_title;
    }

    public String getBasic_trend() {
        return basic_trend;
    }

    public void setBasic_trend(String basic_trend) {
        this.basic_trend = basic_trend;
    }

    public String getBasic_detail() {
        return basic_detail;
    }

    public void setBasic_detail(String basic_detail) {
        this.basic_detail = basic_detail;
    }

    public String getTechnology_image() {
        return technology_image;
    }

    public void setTechnology_image(String technology_image) {
        this.technology_image = technology_image;
    }

    public String getTechnology_detail() {
        return technology_detail;
    }

    public void setTechnology_detail(String technology_detail) {
        this.technology_detail = technology_detail;
    }
}
