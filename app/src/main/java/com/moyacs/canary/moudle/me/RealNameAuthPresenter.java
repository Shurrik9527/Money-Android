package com.moyacs.canary.moudle.me;


import com.moyacs.canary.bean.PayBean;
import com.moyacs.canary.bean.RateBean;
import com.moyacs.canary.moudle.recharge.RechargeContract;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.RateObservable;
import com.moyacs.canary.network.RateResult;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerOtherManager;
import com.moyacs.canary.network.ServerResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public class RealNameAuthPresenter implements RealNameAuthContract.Presenter{

    private static final String TAG = RealNameAuthPresenter.class.getName();

    private final RealNameAuthContract.View mView;
    private CompositeDisposable disposable;
    public RealNameAuthPresenter(RealNameAuthContract.View view) {
        mView = view;
        mView.setPresenter(this);
        disposable = new CompositeDisposable();
    }


    @Override
    public void submitRealNameInform(String phone, String name, String sex, String age, String address, String beforPicture, String afterPicture) {

    }

    @Override
    public void unsubscribe() {

    }
}
