package com.moyacs.canary.main.me;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.bean.UserAmountVo;
import com.moyacs.canary.bean.UserInfoVo;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.bean.event.LoginMessageEvent;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.login.LoginActivity;
import com.moyacs.canary.main.me.account.AccountActivity;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.web.WebActivity;
import com.moyacs.canary.widget.CircleImageView;

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
    @BindView(R.id.tv_asset)
    TextView tvAsset;
    @BindView(R.id.tv_profit)
    TextView tvProfit;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.img_person)
    CircleImageView ivPerson;

    private boolean isLoadData = false;

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
        registerEventBus();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoadData) {
            if (!TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
                getUserInfo();
            }
            isLoadData = true;
        }
    }

    /**
     * 登录成功之后，改变相关状态
     */
    private void setNickName() {
        if (TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
            llUnLogin.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
        } else {
            llLogin.setVisibility(View.VISIBLE);
            llUnLogin.setVisibility(View.GONE);
            tvNickName.setText( SharePreferencesUtil.getInstance().getNickName());
            Glide.with(mActivity)
                    .load(SharePreferencesUtil.getInstance().getUserHead())
                    .into(ivPerson);

        }
    }

    @OnClick({R.id.btn_login, R.id.img_person, R.id.super_huidashi, R.id.super_version,
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
            case R.id.img_person:
                //点击头像进去个人信息设置
                if (TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
                    DialogUtils.login_please("请先登录", getContext());
                } else {
                    startActivity(new Intent(getContext(), AccountActivity.class));
                }
                break;
            case R.id.super_huidashi:
                //一分钟了解汇大师
                intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("title","一分钟了解汇大师");
                intent.putExtra("loadUrl", AppConstans.KNOW_APP);
                startActivity(intent);
                break;
            case R.id.super_version:
                //版本检测
                break;
            case R.id.super_aboutus:
                //关于我们
                intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("title","关于我们");
                intent.putExtra("loadUrl", AppConstans.ABOUT_US);
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
        } else if (vo.getCode() == EvenVo.CHANGE_ORDER_SUCCESS) {
            getUserBalance();
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
                        SharePreferencesUtil.getInstance().setNickName(data.getData().getUserName());
                        getUserBalance();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.showShort("服务器异常");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        setNickName();
                    }
                }));
    }

    /**
     * 获取用户资产信息
     */
    private void getUserBalance() {
        addSubscribe(ServerManger.getInstance().getServer().getUserAmountInfo()
                .compose(RxUtils.rxSchedulerHelper())
                .subscribeWith(new BaseObservable<ServerResult<UserAmountVo>>() {
                    @Override
                    protected void requestSuccess(ServerResult<UserAmountVo> data) {
                        UserAmountVo amount = data.getData();
                        if (amount != null) {
                            tvAsset.setText(data.getData().getRechargeAmount());
                            tvBalance.setText(data.getData().getBalance());
                        }
                    }
                }));
    }

}
