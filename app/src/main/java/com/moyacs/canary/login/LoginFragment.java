package com.moyacs.canary.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.contrarywind.view.WheelView;
import com.contrarywind.view.WheelView.DividerType;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.RSAKeyManger;
import com.moyacs.canary.login.net.MT4Users;
import com.moyacs.canary.main.MainActivity2;
import com.moyacs.canary.main.me.LoginMessageEvent;
import com.moyacs.canary.network.HttpResult;
import com.moyacs.canary.network.HttpServerManager;
import com.moyacs.canary.network.ServerResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/5 0005 10:02
 * 邮箱：rsf411613593@gmail.com
 * 说明：登录
 */

public class LoginFragment extends LoginAndRegistFragment {
    @BindView(R.id.et_phonenumber)
    EditText etPhonenumber;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.iv_showpwd)
    ImageView ivShowpwd;
    @BindView(R.id.showPwdView)
    RelativeLayout showPwdView;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_findPwd)
    Button btnFindPwd;
    Unbinder unbinder;
    /**
     * 判断是否是电话号码
     */
    private boolean isMobileExact;
    /**
     * 判断密码长度是否在 6 到 12 位之间
     */
    private boolean isPwdLengthEnough;
    /**
     * 登录的账号
     */
    private String userName;
    /**
     * 登录密码
     */
    private String passWord;
    /**
     * 是否显示密码
     */
    private boolean isShowPwd;

    ;
    private String TAG = "LoginFragment";

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_login, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isShowPwd = false;

        //添加电话号码输入监听
        etPhonenumber.addTextChangedListener(new PhoneNumberChangeLister());
        //添加密码输入监听
        etPwd.addTextChangedListener(new PwdChangeLister());

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint: " + isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_showpwd, R.id.btn_login, R.id.btn_findPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_showpwd://显示密码
                if (!isShowPwd) {
                    ivShowpwd.setImageResource(R.mipmap.icon_show_pwd);
                    etPwd.setSelection(etPwd.getText().length());
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPwd.setSelection(etPwd.getText().toString().length());
                } else {
                    ivShowpwd.setImageResource(R.mipmap.icon_hide_pwd);
                    etPwd.setSelection(etPwd.getText().length());
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPwd.setSelection(etPwd.getText().toString().length());
                }
                isShowPwd = !isShowPwd;
                break;
            case R.id.btn_login:
                //账号密码格式不正确的提示信息
                if (!isMobileExact && !isPwdLengthEnough) {
                    ToastUtils.showShort("请输入正确的电话号码和密码长度");
                    return;
                } else if (!isMobileExact) {
                    ToastUtils.showShort("请输入正确的电话号码");
                    return;
                } else if (!isPwdLengthEnough) {
                    ToastUtils.showShort("请输入 6 到 12 位密码");
                    return;
                }
                LogUtils.d("登录的账号：" + userName + "    密码：" + passWord);
                //执行登录操作
                presenter.doLogin(userName, passWord);
                break;
            case R.id.btn_findPwd://找回密码
                startActivity(new Intent(getContext(), ForgetPasswordActivity.class));
                break;
        }
    }

    /**
     * 电话号码输入监听
     */
    private class PhoneNumberChangeLister implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isMobileExact = RegexUtils.isMobileExact(s);
            userName = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 密码输入监听
     */
    private class PwdChangeLister implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            passWord = s.toString();
            //因为在布局文件中已经限制了 密码的范围，所以，直接判断长度即可
            int pwdLength = s.length();
            if (pwdLength >= 6 && pwdLength <= 12) {
                isPwdLengthEnough = true;
            } else {
                isPwdLengthEnough = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void doOnNext(ServerResult<String> result) {
        LogUtils.d("doOnNext  :  " + result.getData());
        //将 token 存入本地
        SPUtils.getInstance().put(AppConstans.token, result.getData());

    }

    /**
     * 登录成功
     *
     * @param result
     */
    @Override
    public void LoginSucessed(ServerResult<String> result) {

        //设置 token
//        HttpServerManager.setHeaderValue(result.getDataObject());
        //获取MT4账户信息
//        presenter.getMT4Info();
        LogUtils.d("登陆成功，上传公钥");
        presenter.uploadPubKey(RSAKeyManger.pubKey);
    }

    /**
     * 登录失败
     *
     * @param errormsg
     */
    @Override
    public void LoginFailed(String errormsg) {
        LogUtils.d("登录失败：  " + errormsg);
        ToastUtils.showShort(errormsg);
        SPUtils.getInstance().put(AppConstans.USER_PHONE, userName);
    }

    /**
     * 获取 用户 mt4 账户成功
     *
     * @param result
     */
    @Override
    public void getMT4InfoSucess(MT4Users result) {
        Log.i(TAG, "获取 用户 mt4 账户成功:     " + result.toString());
        /**
         * 存储全名，用于在 “我” 页面展示
         */
        SPUtils.getInstance().put(AppConstans.fullName, result.getFullname());
        /**
         * 存储信息完善状态  1 未完善 2 完善一部分 3 完善成功
         */
        SPUtils.getInstance().put(AppConstans.infoFillStep, result.getInfoFillStep());


        showPopwindow_MT4Users2(result.getMt4Users());
//        myRecyclerAdapter.notifyDataSetChanged();

    }

    /**
     * 显示用户选择账户类型的 popwindow
     *
     * @param mt4Users
     */
    private void showPopwindow_MT4Users2(final List<MT4Users.Mt4UsersBean> mt4Users) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < mt4Users.size(); i++) {
            MT4Users.Mt4UsersBean mt4UsersBean = mt4Users.get(i);
            int mt4id = mt4UsersBean.getMt4id();
            String type = mt4UsersBean.getType();
            list.add("► " + mt4id + "  " + type + " ◄");
        }

        //条件选择器
        OptionsPickerView pvOptions2 = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                MT4Users.Mt4UsersBean mt4UsersBean = mt4Users.get(options1);
                int mt4id = mt4UsersBean.getMt4id();
                String type = mt4UsersBean.getType();
                //保存本地
                SPUtils instance = SPUtils.getInstance();
                instance.put(AppConstans.type, type);
                instance.put(AppConstans.mt4id, mt4id);

                LogUtils.d("登录成功:   ");
                EventBus.getDefault().post(new LoginMessageEvent());
                getActivity().finish();


            }

        })
                .setTitleText("请选择账户")
                .setTitleSize(17)
                .setTitleColor(getResources().getColor(R.color.app_common_content))//标题颜色
                .setSubmitColor(getResources().getColor(R.color.app_common_selected))//确定取消颜色
                .setDividerColor(getResources().getColor(R.color.mt4user_not_select))
                .setTextColorCenter(getResources().getColor(R.color.app_common_selected)) //设置选中项文字颜色
                .setContentTextSize(17)
                .build();


        pvOptions2.setPicker(list);//一级选择器

        pvOptions2.show();
    }

    /**
     * 获取 用户 mt4 账户失败
     *
     * @param errorMsg
     */
    @Override
    public void getMT4InfoFailed(String errorMsg) {
        ToastUtils.showShort("获取 mt4 账户失败");
        Log.i(TAG, "getMT4InfoFailed: 获取 用户 mt4 账户失败  :" + errorMsg);
    }

    @Override
    public void uploadPubKeyFailed() {
        ToastUtils.showShort("服务器异常，请稍后再试");
    }

    @Override
    public void uploadPubKeySuccess() {
        ToastUtils.showShort("登陆成功");
        startActivity(new Intent(getContext(), MainActivity2.class));
    }
}
