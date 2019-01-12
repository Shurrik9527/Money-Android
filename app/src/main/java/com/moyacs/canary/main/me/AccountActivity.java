package com.moyacs.canary.main.me;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.login.ForgetPasswordActivity;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

public class AccountActivity extends BaseActivity {

    @BindView(R.id.tv_nickName)
    TextView tvNickName;
    @BindView(R.id.tv_account)
    TextView tvAccount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initView() {
        tvNickName.setText(SharePreferencesUtil.getInstance().getNickName());
        tvAccount.setText(SharePreferencesUtil.getInstance().getUserPhone());
    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {
        registerEventBus();
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
            tvNickName.setText(SharePreferencesUtil.getInstance().getNickName());
        }
    }
}
