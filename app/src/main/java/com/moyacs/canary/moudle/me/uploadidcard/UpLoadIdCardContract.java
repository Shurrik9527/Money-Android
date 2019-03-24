package com.moyacs.canary.moudle.me.uploadidcard;

import android.content.Context;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;


import java.io.File;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/21
 * @email 252774645@qq.com
 */
public interface UpLoadIdCardContract {
    interface View extends BaseViews<UpLoadIdCardContract.Presenter> {
        void showNextResult(boolean state);
        void uploadBeforImgSuccess(String imgPath);
        void uploadAfterImgSuccess(String imgPath);
    }

    interface Presenter extends BasePresenter {
        void uploadIdCard(Context context,File file, boolean isBefor, boolean after);
        void submitRealNameInform(String gender, String birthdate, String id_number,String card_front, String card_reverse,String userName);
    }
}
