package com.moyacs.canary.bean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class HomeDealChanceVo {

    private List<HomeDealChanceBean> list;

    public List<HomeDealChanceBean> getList() {
        return list;
    }

    public void setList(List<HomeDealChanceBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "HomeDealChanceVo{" +
                "list=" + list +
                '}';
    }
}
