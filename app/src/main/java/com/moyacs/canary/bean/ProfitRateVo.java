package com.moyacs.canary.bean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 解析盈利率类
 * @date 2019/2/27
 * @email 252774645@qq.com
 */
public class ProfitRateVo {

    private List<ProfitRateBean> list;

    public List<ProfitRateBean> getList() {
        return list;
    }

    public void setList(List<ProfitRateBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ProfitRateVo{" +
                "list=" + list +
                '}';
    }
}
