package com.moyacs.canary.login;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.ToastUtils;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/5 0005 10:02
 * 邮箱：rsf411613593@gmail.com
 * 说明：注册
 */

public class RegistFragment extends BaseFragment implements LoginRegistContract.LoginRegistView{
    @BindView(R.id.et_phone)
    EditText etPhone;
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
    //短信计时器
    private GetCodeCountDownTimer countDownTimer;
    //登陆id 验证码返回过来的状态码
    private String registerId;
    /**
     * 是否显示密码
     */
    private boolean isShowPwd;
    private String phone;
    private LoginRegistContract.Presenter mPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_regist;
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

        //初始化计数器
        countDownTimer = new GetCodeCountDownTimer(60000, 1000);
        isShowPwd = false;
    }

    @OnClick({R.id.btnGetCode, R.id.img_showpwd, R.id.btn_submit_reg, R.id.tv_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnGetCode://获取验证码按钮
                phone = etPhone.getText().toString();
                //开始倒计时 60 秒
                if (phone.equals("null")||phone.length()!=11) {
                    ToastUtils.showShort("请输入正确的手机号码");
                    return;
                }
                //开始计时
                countDownTimer.start();
                //网络请求
                if(mPresenter!=null){
                    showLoadingDialog();
                    mPresenter.getcode(phone);
                }
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
                String fullName = edFullName.getText().toString().trim();
                //验证码
                String code = edCode.getText().toString().trim();

                String passWord = edUpwd.getText().toString().trim();
                if (TextUtils.isEmpty(fullName)) {
                    showMag("真实名字不能为空");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showMag("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(passWord)) {
                    showMag("密码不能为空");
                    return;
                }
                if (passWord.length() < 6 || passWord.length() > 12) {
                    showMag("密码长度不符合规范");
                    return;
                }
                if(mPresenter!=null){
                    showLoadingDialog();
                    mPresenter.doRegister(registerId, phone, passWord, fullName, code);
                }
                break;
            case R.id.tv_rule:
                break;
        }
    }



    @Override
    public void showSuccess() {
        dismissLoadingDialog();
        ToastUtils.showShort("注册成功");
    }

    @Override
    public void dissLoading() {
        dismissLoadingDialog();
    }



    @Override
    public void setPresenter(LoginRegistContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtils.showShort(msg+"");
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
     */
    public void getCodeSuccess(String result) {
        registerId = result;
        ToastUtils.showShort("验证码发送成功");
    }

    /**
     * 获取验证码失败
     */
    @Override
    public void getCodeFailed() {
        countDownTimer.onFinish();
        countDownTimer.cancel();
        ToastUtils.showShort("验证码发送失败");
    }

}
