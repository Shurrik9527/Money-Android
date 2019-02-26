package com.moyacs.canary.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class BannerBean {

    private int id;//BannerID
    private String imgAddress;//Banner图地址
    private String bannerDesc;//Banner描述
    private String link;//点击之后的链接地址
    private String sortNo; //浏览次数
    private String createTime;//时间日期

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    public String getBannerDesc() {
        return bannerDesc;
    }

    public void setBannerDesc(String bannerDesc) {
        this.bannerDesc = bannerDesc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "id=" + id +
                ", imgAddress='" + imgAddress + '\'' +
                ", bannerDesc='" + bannerDesc + '\'' +
                ", link='" + link + '\'' +
                ", sortNo='" + sortNo + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
