package com.moyacs.canary.moudle.recharge;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;
import com.moyacs.canary.bean.PayBean;

import java.util.List;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 充值契约
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public interface RechargeContract {

    interface View extends BaseViews<Presenter> {
        void showRechargeAmount(List<String> mlists);
        void showPayResult(PayBean bean);
    }

    interface Presenter extends BasePresenter {
        void rechargePay(String type,String url,String amount);
        void getRechargeAmount();
    }
}
