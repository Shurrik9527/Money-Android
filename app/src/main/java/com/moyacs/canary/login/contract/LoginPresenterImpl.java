package com.moyacs.canary.login.contract;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.login.LoginAndRegistFragment;
import com.moyacs.canary.login.contract.LoginContract;
import com.moyacs.canary.login.contract.LoginContract.LoginPresenter;
import com.moyacs.canary.login.contract.LoginModulImpl;
import com.moyacs.canary.login.net.MT4Users;
import com.moyacs.canary.network.HttpConstants;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerResult;

/**
 * 作者：luoshen on 2018/3/5 0005 16:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class LoginPresenterImpl implements LoginPresenter, LoginContract.LoginRequestListener {

    private LoginContract.LoginView view;
    private LoginContract.LoginModul modul;
    private String TAG = "LoginPresenterImpl";

    public LoginPresenterImpl(LoginAndRegistFragment view) {
        this.view = view;
        modul = new LoginModulImpl(this);
    }

    @Override
    public void unsubscribe() {
        modul.unsubscribe();
    }

    @Override
    public void doLogin(String userName, String passWord) {
        modul.doLogin(userName, passWord);
    }

    @Override
    public void getMT4Info() {
        modul.getMT4Info();
    }

    @Override
    public void getCode(String mobile) {
        modul.getCode(mobile);
    }

    @Override
    public void register(String registerId, String mobile, String password, String fullname, String vcode) {
        modul.register(registerId, mobile, password, fullname, vcode);
    }

    @Override
    public void uploadPubKey(String pubKey) {
        modul.uploadPubKey(pubKey);
    }

    @Override
    public void beforeRequest() {
        view.showLoadingDailog();
    }

    @Override
    public void doOnNext(ServerResult<String> result) {
        view.doOnNext(result);
    }

    @Override
    public void getMT4InfoSucess(ServerResult<MT4Users> result) {
        if (result == null) {
            view.getMT4InfoFailed("数据为空");
        }
        int result1 = result.getMsgCode();
        Log.i(TAG, "getMT4InfoSucess: " + result.toString());
        LogUtils.d("服务器返回的  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.getMT4InfoSucess(result.getData());
        } else {
            view.getMT4InfoFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getMT4InfoFailed(String errorMsg) {
        view.getMT4InfoFailed(errorMsg);
    }

    @Override
    public void getCodeResponseSucessed(ServerResult<String> result) {
        if (result == null) {
            view.getCodeResponseFailed("数据为空");
        }
        int result1 = result.getMsgCode();
        LogUtils.d("服务器返回的  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.getCodeResponseSucessed(result.getData());
        } else {
            view.getCodeResponseFailed("服务器返回数据错误");
        }
    }

    @Override
    public void getCodeResponseFailed(String errormsg) {
        view.getCodeResponseFailed(errormsg);
    }

    @Override
    public void registerResponseSucessed(ServerResult<String> result) {
        if (result == null) {
            view.registerResponseFailed("数据为空");
        }
        int result1 = result.getMsgCode();
        Log.i(TAG, "getMT4InfoSucess: " + result.toString());
        LogUtils.d("服务器返回的  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.registerResponseSucessed(result.getData());
        } else {
            view.registerResponseFailed("服务器返回数据错误");
        }
    }

    @Override
    public void registerResponseFailed(String errormsg) {
        view.registerResponseFailed(errormsg);
    }

    @Override
    public void uploadPubKeySuccess() {
        view.uploadPubKeySuccess();
    }

    @Override
    public void uploadPubKeyFailed() {
        view.uploadPubKeyFailed();
    }

    @Override
    public void afterRequest() {
        view.dismissLoadingDialog();
    }

    @Override
    public void LoginResponseSucessed(ServerResult<String> result) {
        if (result == null) {
            view.LoginFailed("数据为空");
        }
        int result1 = result.getMsgCode();
        LogUtils.d("服务器返回的  code   " + result1);
        if (result1 == HttpConstants.result_sucess) {
            view.LoginSucessed(result);
        } else {
            view.LoginFailed(result.getMsg());
        }
    }

    @Override
    public void LoginResponseFailed(String errormsg) {
        view.LoginFailed(errormsg);
    }
}
