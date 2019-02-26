package com.moyacs.canary.bean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe json 返回解析类
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class BannerJsons {

    private List<BannerBean> list;

    public List<BannerBean> getList() {
        return list;
    }

    public void setList(List<BannerBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BannerDate{" +
                "list=" + list +
                '}';
    }
}
