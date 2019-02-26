package com.moyacs.canary.login;

import android.content.Intent;
import android.util.Base64;

import com.moyacs.canary.common.RSAKeyManger;
import com.moyacs.canary.main.MainActivity;
import com.moyacs.canary.main.homepage.HomeContract;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;

import java.io.UnsupportedEncodingException;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 注册登录契约
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class LoginRegistPresenter implements LoginRegistContract.Presenter{

    private LoginRegistContract.LoginRegistView mView;
    private CompositeDisposable disposable;
    public LoginRegistPresenter(LoginRegistContract.LoginRegistView view) {
        this.mView = view;
        disposable = new CompositeDisposable();
        mView.setPresenter(this);
    }


    @Override
    public void getcode(String phone) {
        disposable.add(ServerManger.getInstance().getServer()
                .getCode(phone)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        if(mView!=null){
                            mView.getCodeSuccess(data.getData());
                            mView.dissLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.getCodeFailed();
                            mView.dissLoading();
                        }
                    }
                }));
    }

    @Override
    public void doLogin(String userName, String passWord) {
        String pw = "zst" + passWord.trim() + "013";
        String base64Pw;
        try {
            base64Pw = Base64.encodeToString(pw.getBytes("utf-8"), Base64.DEFAULT).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ToastUtils.showShort("密码格式输入错误");
            return;
        }
        disposable.add(ServerManger.getInstance().getServer().doLogin(userName, base64Pw)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        if(data!=null&&mView!=null){
                            uploadPubKey(RSAKeyManger.pubKey);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.showMessageTips(e.getMessage()+"");
                            mView.dissLoading();
                        }
                    }
                }));
    }

    @Override
    public void uploadPubKey(String pubKey) {
        disposable.add(ServerManger.getInstance().getServer()
                .uploadPubKey(pubKey)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        if(mView!=null){
                            mView.showMessageTips("登录成功");
                            mView.showSuccess();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.dissLoading();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(mView!=null){
                            mView.dissLoading();
                        }
                    }
                }));
    }

    @Override
    public void doRegister(String registerId, String phone, String passWord, String fullName, String code) {
        String pw = "zst" + passWord.trim() + "013";
        String base64Pw;
        try {
            base64Pw = Base64.encodeToString(pw.getBytes("utf-8"), Base64.DEFAULT).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ToastUtils.showShort("密码格式错误，请更换密码再试");
            return;
        }
        disposable.add(ServerManger.getInstance().getServer().register(registerId, phone, base64Pw, fullName, code)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                       if(mView!=null){
                           mView.showSuccess();
                       }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(mView!=null){
                            mView.showMessageTips(e.getMessage()+"");
                            mView.dissLoading();
                        }
                    }
                }));
    }

    @Override
    public void unsubscribe() {

    }
}
