package com.moyacs.canary.main.homepage.net;

import java.util.List;

/**
 * 作者：luoshen on 2018/4/10 0010 09:41
 * 邮箱：rsf411613593@gmail.com
 * 说明：首页 banner 数据封装对象
 */

public class BannerDate {
    private List<Banner> list;

    public List<Banner> getList() {
        return list;
    }

    public void setList(List<Banner> list) {
        this.list = list;
    }

    /**
     * id : 2
     * image : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523273680158&di=e839e0521a9ebeacd832259440188e85&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2017-10-14%2F59e1bbd4dafcb.jpg
     * desc : 样样精品
     * link : 链接地址
     * view_count : 4
     * datetime : 2015-08-08 00:00:00
     */
    public class Banner {

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

        public String getImage() {
            return imgAddress;
        }

        public void setImage(String image) {
            this.imgAddress = image;
        }

        public String getDesc() {
            return bannerDesc;
        }

        public void setDesc(String desc) {
            this.bannerDesc = desc;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getView_count() {
            return sortNo;
        }

        public void setView_count(String view_count) {
            this.sortNo = view_count;
        }

        public String getDatetime() {
            return createTime;
        }

        public void setDatetime(String datetime) {
            this.createTime = datetime;
        }
    }

}
