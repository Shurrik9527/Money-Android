package com.moyacs.canary.main.me;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.login.LoginActivity;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.news.NewsActivity;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/2 0002 10:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：会员页面
 */

public class MeFragment extends BaseFragment {
    @BindView(R.id.text_uname)
    TextView tvNickName;
    @BindView(R.id.ll_unlogin)
    LinearLayout llUnLogin;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void intListener() {

    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
            getUserInfo();
        }
        registerEventBus();
    }


    /**
     * 登录成功之后，改变相关状态
     */
    private void setNickName() {
        String nickName = SharePreferencesUtil.getInstance().getNickName();
        tvNickName.setText(nickName);
    }

    @OnClick({R.id.btn_login, R.id.rl_openaccount, R.id.rl_intergral_market, R.id.rl_mission_center,
            R.id.ll_customer_service, R.id.ll_account_manager, R.id.super_huidashi, R.id.super_version,
            R.id.super_aboutus, R.id.text_needexp})
    public void onViewClicked(View view) {
        Intent intent;
        String url;
        switch (view.getId()) {
            case R.id.btn_login:
                //登录
                intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_openaccount:
                //开通账号
                ToastUtils.showShort("正在开发中...");
                break;
            case R.id.rl_intergral_market:
                //积分商城
                break;
            case R.id.rl_mission_center:
                //任务中心
                break;
            case R.id.ll_customer_service:
                //在线客服
                ToastUtils.showShort("正在开发中...");
                break;
            case R.id.ll_account_manager:
                //账号管理
                if (TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
                    DialogUtils.login_please("请先登录", getContext());
                } else {
                    startActivity(new Intent(getContext(), AccountActivity.class));
                }
                break;
            case R.id.super_huidashi:
                //一分钟了解汇大师
                intent = new Intent(mActivity, NewsActivity.class);
                url = "http://47.91.164.170:8080/pages/xsxt/zh-CN/index.html";
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.super_version:
                //版本检测
                break;
            case R.id.super_aboutus:
                //关于我们
                intent = new Intent(mActivity, NewsActivity.class);
                url = "http://47.91.164.170:8080/pages/aboutus/aboutus-zh-CN.html";
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.text_needexp:
                //完善资料
                String url_wanShanZiLiao = "http://uc.moyacs.com/real_app_v2.html";
                intent = new Intent(mActivity, UserInformationActivity.class);
                intent.putExtra("url", url_wanShanZiLiao);
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginMessageEvent object) {
        llUnLogin.setVisibility(View.GONE);
        llLogin.setVisibility(View.VISIBLE);
        getUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(EvenVo vo) {
        if (vo.getCode() == EvenVo.EVENT_CODE_UPDATE_NICK_NAME) {
            setNickName();
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        addSubscribe(ServerManger.getInstance().getServer().getUserInfo()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<UserInfoVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<UserInfoVo> data) {
                        llUnLogin.setVisibility(View.GONE);
                        llLogin.setVisibility(View.VISIBLE);
                        SharePreferencesUtil.getInstance().setNickName(data.getData().getLoginName());
                        setNickName();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showShort("服务器异常");
                    }
                }));
    }

}
