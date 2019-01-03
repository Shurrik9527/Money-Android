package com.moyacs.canary.login.contract;

import com.moyacs.canary.base.BaseModul;
import com.moyacs.canary.base.BasePresenter;
import com.moyacs.canary.base.BaseRequestListener;
import com.moyacs.canary.base.BaseView;
import com.moyacs.canary.login.net.MT4Users;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.ServerResult;

/**
 * 作者：luoshen on 2018/3/5 0005 15:21
 * 邮箱：rsf411613593@gmail.com
 * 说明：约束类， MVP 三层合一 ，方便查看业务逻辑
 */

public interface LoginContract {

    interface LoginView extends BaseView {
        /**
         * 登录 成功
         *
         * @param result
         */
        void LoginSucessed(ServerResult<String> result);

        /**
         * 登录 失败
         *
         * @param errormsg
         */
        void LoginFailed(String errormsg);

        /**
         * (用于对请求到的数据进行二次处理)
         */
        void doOnNext(ServerResult<String> result);

        /**
         * 获取用户 mt4 账户信息成功
         *
         * @param result
         */
        void getMT4InfoSucess(MT4Users result);

        /**
         * 获取用户  MT4 账户信息失败
         *
         * @param errorMsg
         */
        void getMT4InfoFailed(String errorMsg);


        /**
         * 获取验证码成功
         *
         * @param result
         */
        void getCodeResponseSucessed(String result);

        /**
         * 获取验证码失败
         *
         * @param errormsg
         */
        void getCodeResponseFailed(String errormsg);

        /**
         * 注册成功
         *
         * @param result
         */
        void registerResponseSucessed(String result);

        /**
         * 注册失败
         *
         * @param errormsg
         */
        void registerResponseFailed(String errormsg);

        void uploadPubKeySuccess();

        void uploadPubKeyFailed();

    }

    interface LoginPresenter extends BasePresenter {
        /**
         * 登录
         *
         * @param userName 账号
         * @param passWord 密码
         */
        void doLogin(String userName, String passWord);

        /**
         * 获取用户所有的  MT4 账户信息
         */
        void getMT4Info();

        /**
         * 获取验证码
         *
         * @param mobile
         */
        void getCode(String mobile);

        /**
         * 注册
         *
         * @param mobile
         * @param password
         * @param fullname
         * @param vcode
         */
        void register(String registerId,
                      String mobile,
                      String password,
                      String fullname,
                      String vcode);

        void uploadPubKey(String pubKey);
    }

    interface LoginModul extends BaseModul {
        /**
         * 登录
         *
         * @param userName 账号
         * @param passWord 密码
         */
        void doLogin(String userName, String passWord);

        /**
         * 获取用户所有的  MT4 账户信息
         */
        void getMT4Info();

        /**
         * 获取验证码
         *
         * @param mobile
         */
        void getCode(String mobile);

        /**
         * 注册
         *
         * @param mobile
         * @param password
         * @param fullname
         * @param vcode
         */
        void register(String registerId,
                      String mobile,
                      String password,
                      String fullname,
                      String vcode);

        void uploadPubKey(String pubKey);
    }

    interface LoginRequestListener extends BaseRequestListener {

        /**
         * 登录网络响应成功
         *
         * @param result
         */
        void LoginResponseSucessed(ServerResult<String> result);

        /**
         * 登录网络响应失败
         *
         * @param errormsg
         */
        void LoginResponseFailed(String errormsg);

        void doOnNext(ServerResult<String> result);

        /**
         * 获取用户 mt4 账户信息成功
         *
         * @param result
         */
        void getMT4InfoSucess(ServerResult<MT4Users> result);

        /**
         * 获取用户  MT4 账户信息失败
         *
         * @param errorMsg
         */
        void getMT4InfoFailed(String errorMsg);

        /**
         * 获取验证码成功
         *
         * @param result
         */
        void getCodeResponseSucessed(ServerResult<String> result);

        /**
         * 获取验证码失败
         *
         * @param errormsg
         */
        void getCodeResponseFailed(String errormsg);

        /**
         * 注册成功
         *
         * @param result
         */
        void registerResponseSucessed(ServerResult<String> result);

        /**
         * 注册失败
         *
         * @param errormsg
         */
        void registerResponseFailed(String errormsg);

        void uploadPubKeySuccess();

        void uploadPubKeyFailed();
    }
}
