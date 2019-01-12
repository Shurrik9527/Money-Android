package com.moyacs.canary.login;

import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import www.moyacs.com.myapplication.R;

public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btnGetCode)
    Button btnGetCode;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private GetCodeCountDownTimer downTimer;
    private String phone, id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initView() {
        setBtnSubmitStatue();
    }

    @Override
    protected void intListener() {
        initEdViewListener();
        btnGetCode.setOnClickListener(v -> {
            downTimer.start();
            getCode();
        });
        btnSubmit.setOnClickListener(v -> forgetPassword());
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
            etPhone.setText(SharePreferencesUtil.getInstance().getUserPhone());
        }
        downTimer = new GetCodeCountDownTimer(60000, 1000);
    }

    private void initEdViewListener() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setBtnSubmitStatue();
            }
        });
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setBtnSubmitStatue();
            }
        });

        etPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setBtnSubmitStatue();
            }
        });
    }

    private void setBtnSubmitStatue() {
        if (TextUtils.isEmpty(etPhone.getText().toString())
                || TextUtils.isEmpty(etCode.getText().toString())
                || TextUtils.isEmpty(etPsw.getText().toString())) {
            btnSubmit.getBackground().setAlpha(120);
            btnSubmit.setEnabled(false);
        } else {
            btnSubmit.getBackground().setAlpha(225);
            btnSubmit.setEnabled(true);
        }
    }


    private void getCode() {
        phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            ToastUtils.showShort("输入的手机号码不符合规范");
            return;
        }
        addSubscribe(ServerManger.getInstance().getServer().getCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.showShort(e.getMessage());
                    }


                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        id = data.getData();
                        ToastUtils.showShort("验证码已下发，请注意查收");
                    }
                }));

    }

    private void forgetPassword() {
        String code = etCode.getText().toString();
        String pwd = etPsw.getText().toString();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort("您输入的验证码不符合规范");
            return;
        }
        if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 12) {
            ToastUtils.showShort("您输入的密码不符合规范");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("loginName", phone);
        map.put("verificationCode", code);
        String pw = "zst" + pwd.trim() + "013";
        String base64Pw;
        try {
            base64Pw = Base64.encodeToString(pw.getBytes("utf-8"), Base64.DEFAULT).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ToastUtils.showShort("密码格式错误，请更换密码再试");
            return;
        }
        map.put("password", base64Pw);
        addSubscribe(ServerManger.getInstance().getServer().forgetPassWord(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObservable<ServerResult<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    protected void requestSuccess(ServerResult<String> data) {
                        ToastUtils.showShort("密码重置成功");
                    }
                }));
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
}
