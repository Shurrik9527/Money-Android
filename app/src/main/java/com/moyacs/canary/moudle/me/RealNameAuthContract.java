package com.moyacs.canary.moudle.me;

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
public interface RealNameAuthContract {

    interface View extends BaseViews<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void submitRealNameInform(String phone, String name, String sex,String age, String address,String beforPicture,String afterPicture);
    }
}
