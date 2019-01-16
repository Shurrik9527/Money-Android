/*
package com.moyacs.canary.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.login.contract.LoginContract;
import com.moyacs.canary.login.contract.LoginContract.LoginView;
import com.moyacs.canary.login.net.MT4Users;
import com.moyacs.canary.network.ServerResult;

*/
/**
 * 作者：luoshen on 2018/3/5 0005 15:59
 * 邮箱：rsf411613593@gmail.com
 * 说明：登录和注册 fragment 的基类
 *//*


public abstract class LoginAndRegistFragment extends BaseFragment2 implements LoginView {
    // P 层 引用
    protected LoginContract.LoginPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenterImpl(this);
    }

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        return null;
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void unsubscribe() {
        presenter.unsubscribe();
    }

    @Override
    public void showLoadingDialog() {
        stopLoading();
    }

    @Override
    public void dismissLoadingDialog() {
        stopLoading();
    }

    @Override
    public void doOnNext(ServerResult<String> result) {

    }

    @Override
    public void getMT4InfoSucess(MT4Users result) {

    }

    @Override
    public void getMT4InfoFailed(String errorMsg) {

    }

    @Override
    public void getCodeResponseSucessed(String result) {

    }

    @Override
    public void getCodeResponseFailed(String errormsg) {

    }

    @Override
    public void registerResponseSucessed(String result) {

    }

    @Override
    public void registerResponseFailed(String errormsg) {

    }


    @Override
    public void LoginSucessed(ServerResult<String> result) {

    }

    @Override
    public void LoginFailed(String errormsg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    @Override
    public void uploadPubKeySuccess() {

    }

    @Override
    public void uploadPubKeyFailed() {

    }
}
*/
