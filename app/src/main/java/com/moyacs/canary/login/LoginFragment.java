package com.moyacs.canary.login;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.common.RSAKeyManger;
import com.moyacs.canary.main.MainActivity;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/5 0005 10:02
 * 邮箱：rsf411613593@gmail.com
 * 说明：登录
 */

public class LoginFragment extends BaseFragment implements LoginRegistContract.LoginRegistView{
    @BindView(R.id.et_phonenumber)
    EditText etPhoneNun;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.iv_showpwd)
    ImageView ivShowPwd;
    @BindView(R.id.showPwdView)
    RelativeLayout showPwdView;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_findPwd)
    Button btnFindPwd;

    private String userName;//登录的账号
    private String passWord;//登录密码
    private boolean isShowPwd;//是否显示密码
    private LoginRegistContract.Presenter mPresenter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {
        new LoginRegistPresenter(this);
        isShowPwd = false;
    }

    @OnClick({R.id.iv_showpwd, R.id.btn_login, R.id.btn_findPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_showpwd://显示密码
                if (!isShowPwd) {
                    ivShowPwd.setImageResource(R.mipmap.icon_show_pwd);
                    etPwd.setSelection(etPwd.getText().length());
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPwd.setSelection(etPwd.getText().toString().length());
                } else {
                    ivShowPwd.setImageResource(R.mipmap.icon_hide_pwd);
                    etPwd.setSelection(etPwd.getText().length());
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPwd.setSelection(etPwd.getText().toString().length());
                }
                isShowPwd = !isShowPwd;
                break;
            case R.id.btn_login:
                //账号密码格式不正确的提示信息
                userName = etPhoneNun.getText().toString();
                passWord = etPwd.getText().toString();
                if (userName == null || userName.length() != 11) {
                    ToastUtils.showShort("请输入正确的电话号码");
                    return;
                } else if (passWord.length() < 6 || passWord.length() > 12) {
                    ToastUtils.showShort("请输入 6 到 12 位密码");
                    return;
                }
                //执行登录操作
                if(mPresenter!=null){
                    showLoadingDialog();
                    mPresenter.doLogin(userName, passWord);
                }
                break;
            case R.id.btn_findPwd://找回密码
                startActivity(new Intent(getContext(), ForgetPasswordActivity.class));
                break;
        }
    }


    @Override
    public void showLoginSuccess() {
        ToastUtils.showShort("登录成功!");
        SharePreferencesUtil.getInstance().setUserPhone(userName);
        startActivity(new Intent(getContext(), MainActivity.class));

        mActivity.finish();
    }

    @Override
    public void showRegistSuccess() {

    }

    @Override
    public void dissLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void getCodeSuccess(String code) {

    }

    @Override
    public void getCodeFailed() {

    }

    @Override
    public void showRongIMToken(String token) {
        if(!TextUtils.isEmpty(token)){
            if(mPresenter!=null){
                mPresenter.connectRonIM(getContext(),token);
            }
        }
    }


    @Override
    public void setPresenter(LoginRegistContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtils.showShort(msg);
    }
}
