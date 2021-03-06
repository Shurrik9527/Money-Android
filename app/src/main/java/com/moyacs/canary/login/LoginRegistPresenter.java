package com.moyacs.canary.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.moyacs.canary.common.RSAKeyManger;
import com.moyacs.canary.main.MainActivity;
import com.moyacs.canary.main.homepage.HomeContract;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.AppUtils;
import com.moyacs.canary.util.PublickPriveKeyUtil;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import io.reactivex.disposables.CompositeDisposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 注册登录契约
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public class LoginRegistPresenter implements LoginRegistContract.Presenter{

    private static final String TAG = LoginRegistPresenter.class.getName();
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
                .getCode(phone,"1")
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
                            mView.showMessageTips(e.getMessage()+"");
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
                            String pubKey = SharePreferencesUtil.getInstance().getPublicKey();
                            String mUserName =SharePreferencesUtil.getInstance().getNickName();
                            if(!TextUtils.isEmpty(mUserName)){
                                if(mUserName.equals(userName)){
                                    if(TextUtils.isEmpty(pubKey)){
                                        resetPulPriKey();
                                    }else {
                                        uploadPubKey(pubKey,"1");
                                    }
                                }else {
                                    //注册成功后生成公私秘钥并保存
                                    resetPulPriKey();
                                }
                            }else {//人为清理
                                resetPulPriKey();
                            }
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
    public void uploadPubKey(String pubKey,String type) {
        disposable.add(ServerManger.getInstance().getServer()
                .uploadPubKey(pubKey)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        if(mView!=null){
                            if(!TextUtils.isEmpty(type)&&type.equals("0")){
                                mView.showRegistSuccess();
                            }else {
                                mView.showLoginSuccess();
                            }
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
                           //生成公私钥
                           try {
                               //注册成功清理以前的数据
                               SharePreferencesUtil.getInstance().clean();
                               //注册成功后生成公私秘钥并保存
                               PublickPriveKeyUtil.genKeyPair();
                               //上传公钥
                               String pubKey = SharePreferencesUtil.getInstance().getPublicKey();
                               uploadPubKey(pubKey,"0");
                           } catch (NoSuchAlgorithmException e) {
                               e.printStackTrace();
                           }
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
    public void getRongIMUserInform(String userid, String phone) {

    }

    @Override
    public void connectRonIM(Context context,String token) {
            if(!TextUtils.isEmpty(token)){
            if (context.getApplicationInfo().packageName.equals(AppUtils.getCurProcessName(context.getApplicationContext()))) {

                RongIM.connect(token, new RongIMClient.ConnectCallback() {
                    /**
                     * Token 错误。可以从下面两点检查
                     *  1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                     *  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                     */
                    @Override
                    public void onTokenIncorrect() {

                    }

                    /**
                     * 连接融云成功
                     * @param userid 当前 token 对应的用户 id
                     */
                    @Override
                    public void onSuccess(String userid) {
                        Log.d(TAG, "Rong IM connect onSuccess" + userid);
                    }

                    /**
                     * 连接融云失败
                     * @param errorCode 错误码，可到官网 查看错误码对应的注释
                     */
                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        if(errorCode!=null){
                            Log.d(TAG, "Rong IM connect onSuccess" + errorCode.getMessage());
                        }
                    }
                });
            }
            }

    }

    @Override
    public void resetPulPriKey() {
        //注册成功后生成公私秘钥并保存
        try {
            SharePreferencesUtil.getInstance().cleanPublicPrivateKey();
            PublickPriveKeyUtil.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //上传公钥
        String newPubKey = SharePreferencesUtil.getInstance().getPublicKey();
        uploadPubKey(newPubKey,"1");
    }

    @Override
    public void unsubscribe() {

    }
}
