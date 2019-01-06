package com.moyacs.canary.main.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.login.ForgetPasswordActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.tv_account)
    TextView tvAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        tvNickName.setText(SPUtils.getInstance().getString(AppConstans.USER_NICK_NAME, "这家伙很懒~"));
        tvAccount.setText(SPUtils.getInstance().getString(AppConstans.USER_PHONE));
        initEventBus();
    }

    private void initEventBus() {
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.iv_back, R.id.ll_nickName, R.id.ll_author, R.id.ll_updatePsw, R.id.b_outLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_nickName:
                //修改昵称
                startActivity(new Intent(this, SetNickNameActivity.class));
                break;
            case R.id.ll_author:
                //实名认证
                ToastUtils.showShort("正在开发中...");
                break;
            case R.id.ll_updatePsw:
                //修改密码
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.b_outLogin:
                //退出登录
                DialogUtils.login_please("确定要退出吗？", this);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(EvenVo vo) {
        if (vo.getCode() == EvenVo.EVENT_CODE_UPDATE_NICK_NAME) {
            tvNickName.setText(SPUtils.getInstance().getString(AppConstans.USER_NICK_NAME, "这家伙很懒~"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
