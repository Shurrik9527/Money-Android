package com.moyacs.canary.bean;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class HomeDealChanceBean implements Serializable {


    private int id;
    private String userImg;//专家头像
    private String userName;//专家名称
    private String symbolName;//产品名称
    private String operatingMode;//交易类型
    private String createTime;//发布时间
    private String themeText;//主题
    private String title;//标题
    private String foundationAnalysis;//基本分析
    private int risePercentage;//买涨百分比 买跌自己算（用100减买涨）
    private String technologicalAnalysisImg;//技术分析图片
    private String technologicalAnalysis;//技术分析

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getOperatingMode() {
        return operatingMode;
    }

    public void setOperatingMode(String operatingMode) {
        this.operatingMode = operatingMode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getThemeText() {
        return themeText;
    }

    public void setThemeText(String themeText) {
        this.themeText = themeText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFoundationAnalysis() {
        return foundationAnalysis;
    }

    public void setFoundationAnalysis(String foundationAnalysis) {
        this.foundationAnalysis = foundationAnalysis;
    }

    public int getRisePercentage() {
        return risePercentage;
    }

    public void setRisePercentage(int risePercentage) {
        this.risePercentage = risePercentage;
    }

    public String getTechnologicalAnalysisImg() {
        return technologicalAnalysisImg;
    }

    public void setTechnologicalAnalysisImg(String technologicalAnalysisImg) {
        this.technologicalAnalysisImg = technologicalAnalysisImg;
    }

    public String getTechnologicalAnalysis() {
        return technologicalAnalysis;
    }

    public void setTechnologicalAnalysis(String technologicalAnalysis) {
        this.technologicalAnalysis = technologicalAnalysis;
    }

    @Override
    public String toString() {
        return "HomeDealChanceBean{" +
                "id=" + id +
                ", userImg='" + userImg + '\'' +
                ", userName='" + userName + '\'' +
                ", symbolName='" + symbolName + '\'' +
                ", operatingMode='" + operatingMode + '\'' +
                ", createTime='" + createTime + '\'' +
                ", themeText='" + themeText + '\'' +
                ", title='" + title + '\'' +
                ", foundationAnalysis='" + foundationAnalysis + '\'' +
                ", risePercentage=" + risePercentage +
                ", technologicalAnalysisImg='" + technologicalAnalysisImg + '\'' +
                ", technologicalAnalysis='" + technologicalAnalysis + '\'' +
                '}';
    }
}
