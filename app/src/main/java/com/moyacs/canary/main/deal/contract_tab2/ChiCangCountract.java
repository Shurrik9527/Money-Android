package com.moyacs.canary.main.deal.contract_tab2;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.main.deal.net_tab3.TransactionRecordVo;

import java.util.List;

/**
 * 作者：luoshen on 2018/4/16 0016 20:43
 * 邮箱：rsf411613593@gmail.com
 * 说明：持仓列表接口封装
 */

public interface ChiCangCountract {

    interface ChiCangView extends BaseView {
        /**
         * 持仓列表  成功
         *
         * @param result
         */
        void setRecordList(List<TransactionRecordVo.Record> result);

        /**
         * 持仓列表  失败
         */
        void getRecordListFailed(String msg);
    }

    interface ChiCangPresenter extends BasePresenter {
        /**
         * 持仓列表
         */
        void getRecordList();
    }
}
