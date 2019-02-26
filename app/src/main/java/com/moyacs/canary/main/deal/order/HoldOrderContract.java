package com.moyacs.canary.main.deal.order;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;
import com.moyacs.canary.bean.TransactionRecordVo;


import java.util.List;
import java.util.Map;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 持仓契约
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public interface HoldOrderContract {

    interface HoldOrderView extends BaseViews<HoldOrderPresenter> {
       //显示持仓记录
        void showRecordList(List<TransactionRecordVo.Record> result);

        //平仓状态
        void closeOrderState(boolean state);

        void getRecordListFailed(String msg);


    }

    interface HoldOrderPresenter extends BasePresenter {
        /**
         * 持仓列表
         */
        void getRecordList();

        /**
         * 下单
         */
        void closeOrder(Map<String, Object> map);
    }


}
