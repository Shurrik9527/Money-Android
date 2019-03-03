package com.moyacs.canary.bean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 解析盈利率类
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ProfitLinesVo {

    private List<ProfitLinesBean> list;

    public List<ProfitLinesBean> getList() {
        return list;
    }

    public void setList(List<ProfitLinesBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ProfitLinesVo{" +
                "list=" + list +
                '}';
    }
}
