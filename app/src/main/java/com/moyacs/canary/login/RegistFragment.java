package com.moyacs.canary.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/5 0005 10:02
 * 邮箱：rsf411613593@gmail.com
 * 说明：注册
 */

public class RegistFragment extends LoginAndRegistFragment {
    @BindView(R.id.ed_uname)
    EditText edUname;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.ed_fullName)
    EditText edFullName;
    @BindView(R.id.btnGetCode)
    Button btnGetCode;
    @BindView(R.id.codeView)
    RelativeLayout codeView;
    @BindView(R.id.ed_upwd)
    EditText edUpwd;
    @BindView(R.id.img_showpwd)
    ImageView imgShowpwd;
    @BindView(R.id.showPwdView)
    RelativeLayout showPwdView;
    @BindView(R.id.btn_submit_reg)
    Button btnSubmitReg;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.ruleView)
    LinearLayout ruleView;
    Unbinder unbinder;
    private View rootView;
    //短信计时器
    private GetCodeCountDownTimer countDownTimer;
    //登陆id 验证码返回过来的状态码
    private String registerId;
    /**
     * 是否显示密码
     */
    private boolean isShowPwd;
    private String TAG = "RegistFragment";

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        rootView = inflater.inflate(R.layout.fragment_regist, null, false);
        unbinder = ButterKnife.bind(this, rootView);
        initViews();
        //初始化计数器
        countDownTimer = new GetCodeCountDownTimer(60000, 1000);
        return rootView;
    }

    /**
     * 初始化相关 view 配置和监听
     */
    private void initViews() {

        //添加电话号码输入监听
        edUname.addTextChangedListener(new PhoneNumberChangeLister());
        //添加密码输入监听
        edUpwd.addTextChangedListener(new PwdChangeLister());

    }

    /**
     * 判断是否是电话号码
     */
    private boolean isMobileExact;
    /**
     * 判断密码长度是否在 6 到 12 位之间
     */
    private boolean isPwdLengthEnough;
    /**
     * 登录的账号 手机号
     */
    private String mobile;
    /**
     * 登录密码
     */
    private String passWord;
    /**
     * 用户全名
     */
    private String fullName;

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
            mobile = s.toString().trim();
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
            //输入密码的时候，空格也会触发此事件
            //因为在布局文件中已经限制了 密码的范围，所以，直接判断长度即可
            String trim = s.toString().trim();
            int pwdLength = trim.length();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isShowPwd = false;
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

    @OnClick({R.id.btnGetCode, R.id.img_showpwd, R.id.btn_submit_reg, R.id.tv_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGetCode://获取验证码按钮
                //开始倒计时 60 秒
                if (!RegexUtils.isMobileExact(mobile)) {
                    ToastUtils.showShort("请输入正确的手机号码");
                    return;
                }
                //开始计时
                countDownTimer.start();
                //网络请求
                presenter.getCode(mobile);
                break;
            case R.id.img_showpwd://是否显示密码
                if (!isShowPwd) {
                    imgShowpwd.setImageResource(R.mipmap.icon_show_pwd);
                    edUpwd.setSelection(edUpwd.getText().length());
                    edUpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edUpwd.setSelection(edUpwd.getText().toString().length());
                } else {
                    imgShowpwd.setImageResource(R.mipmap.icon_hide_pwd);
                    edUpwd.setSelection(edUpwd.getText().length());
                    edUpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edUpwd.setSelection(edUpwd.getText().toString().length());
                }
                isShowPwd = !isShowPwd;
                break;
            case R.id.btn_submit_reg://注册
                //全名
                fullName = edFullName.getText().toString().trim();
                //验证码
                String code = edCode.getText().toString().trim();
                Log.i(TAG, "mobile:" + mobile + "\n"
                        + "passWord:" + passWord + "\n"
                        + "fullName:" + fullName + "\n"
                        + "code:" + code + "\n"
                );
                presenter.register(registerId, mobile, passWord, fullName, code);
                break;
            case R.id.tv_rule:
                break;
        }
    }

    /**
     * 发送验证码 倒计时
     */
    private class GetCodeCountDownTimer extends CountDownTimer {

        public GetCodeCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            if (btnGetCode != null) {
                //防止计时过程中重复点击
                btnGetCode.setClickable(false);
                btnGetCode.setText("剩余 " + l / 1000 + " s");
                btnGetCode.setTextColor(getResources().getColor(R.color.app_common_content_40));
            }
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            btnGetCode.setText("重新获取");
            //设置可点击
            btnGetCode.setClickable(true);
            btnGetCode.setTextColor(getResources().getColor(R.color.app_common_selected));
        }
    }

    /**
     * 获取验证码成功
     *
     * @param result
     */
    @Override
    public void getCodeResponseSucessed(String result) {
        registerId = result;
        LogUtils.d(TAG, "获取验证码成功: " + result);
        ToastUtils.showShort("验证码发送成功");

    }

    /**
     * 获取验证码失败
     *
     * @param errormsg
     */
    @Override
    public void getCodeResponseFailed(String errormsg) {
        countDownTimer.onFinish();
        countDownTimer.cancel();
        LogUtils.d(TAG, "获取验证码失败: ");
        ToastUtils.showShort("验证码发送失败");
    }

    /**
     * 注册成功
     *
     * @param result
     */
    @Override
    public void registerResponseSucessed(String result) {
        Log.i(TAG, "注册成功: " + result);
        ToastUtils.showShort("注册成功");
    }

    /**
     * 注册失败
     *
     * @param errormsg
     */
    @Override
    public void registerResponseFailed(String errormsg) {
        Log.i(TAG, "注册失败: " + errormsg);
        ToastUtils.showShort("注册失败");
    }


}
