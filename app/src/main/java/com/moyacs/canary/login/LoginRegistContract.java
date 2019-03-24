package com.moyacs.canary.login;

import android.content.Context;

import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseViews;


import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/26
 * @email 252774645@qq.com
 */
public interface LoginRegistContract {


    interface LoginRegistView extends BaseViews<Presenter> {

        void  showLoginSuccess();
        void  showRegistSuccess();
        void  dissLoading();
        void  getCodeSuccess(String code);
        void  getCodeFailed();
        void  showRongIMToken(String token);
    }


    interface Presenter extends BasePresenter {

        /**
         * 获取验证码
         * @param phone 手机号
         */
        void getcode(String phone);

        /**
         * 登陆
         * @param userName 用户账号
         * @param passWord 登陆密码
         */
        void doLogin(String userName, String passWord);


        /**
         * 上传用户公钥
         * @param pubKey 公钥
         * @param type 0注册 1登录
         */
        void uploadPubKey(String pubKey,String type);

        /**
         * 注册
         * @param registerId 注册id
         * @param phone 手机号
         * @param passWord 密码
         * @param fullName  真实名字
         * @param code 验证码
         */
        void doRegister(String registerId, String phone, String passWord, String fullName, String code);

        /**
         * 获取用户Token
         * @param userid
         * @param phone
         */
        void getRongIMUserInform(String userid,String phone);

        /**
         * 连接融云服务
         * @param token
         */
        void connectRonIM(Context context,String token);



        void  resetPulPriKey();

    }
}
