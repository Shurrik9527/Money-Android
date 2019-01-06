package com.moyacs.canary.main.me;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.im.KefuActivity;
import com.moyacs.canary.login.LoginActivity2;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.news.NewsActivity;
import com.moyacs.canary.widget.CircleImageView;
import com.moyacs.canary.widget.RoundProgressBar;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import www.moyacs.com.myapplication.R;

/**
 * 作者：luoshen on 2018/3/2 0002 10:15
 * 邮箱：rsf411613593@gmail.com
 * 说明：会员页面
 */

public class MeFragment2 extends BaseFragment2 {
    private static final String TAG = "MeFragment2";
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.userInfoView)
    LinearLayout userInfoView;
    @BindView(R.id.img_person)
    CircleImageView imgPerson;
    @BindView(R.id.text_uname)
    TextView textUname;
    @BindView(R.id.text_needexp)
    TextView textNeedexp;
    @BindView(R.id.ic_needAuth)
    ImageView icNeedAuth;
    @BindView(R.id.tv_needAuth)
    TextView tvNeedAuth;
    @BindView(R.id.rl_openaccount)
    RelativeLayout rlOpenaccount;
    @BindView(R.id.rl_intergral_market)
    RelativeLayout rlIntergralMarket;
    @BindView(R.id.rl_mission_center)
    RelativeLayout rlMissionCenter;
    @BindView(R.id.line_open_account)
    View lineOpenAccount;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.ll_customer_service)
    LinearLayout llCustomerService;
    @BindView(R.id.ll_account_manager)
    LinearLayout llAccountManager;
    @BindView(R.id.super_msg)
    SuperTextView superMsg;
    @BindView(R.id.super_huidashi)
    SuperTextView superHuidashi;
    @BindView(R.id.super_version)
    SuperTextView superVersion;
    @BindView(R.id.super_aboutus)
    SuperTextView superAboutus;
    Unbinder unbinder;
    @BindView(R.id.ll_unlogin)
    LinearLayout llUnlogin;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    private Context context;

    @Override
    protected View addChildInflaterView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_me, null, false);
        unbinder = ButterKnife.bind(this, rootView);
        context = getActivity();
        String userPhone = SPUtils.getInstance().getString(AppConstans.USER_PHONE, "");
        if (!TextUtils.isEmpty(userPhone)) {
            getUserInfo();
        }
        registerEventBus();
        return rootView;
    }

    private void registerEventBus() {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 登录成功之后，改变相关状态
     */
    private void changeLoginSucessState() {
        String nickName = SPUtils.getInstance().getString(AppConstans.USER_NICK_NAME, "这家伙很懒~");
        textUname.setText(nickName);
        //1 未完善 2 完善一部分 3 完善成功
        String infoFillStep = SPUtils.getInstance().getString(AppConstans.infoFillStep, "");
//        if (infoFillStep.equals("3")) {
//            textNeedexp.setVisibility(View.GONE);
//        }else {
//            textNeedexp.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    protected void initBundleData(Bundle bundle) {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
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
                intent = new Intent(context, LoginActivity2.class);
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
//                doLogin("kefu01", "25b9ef37654b723c8e71254d531f05b5");
                break;
            case R.id.ll_account_manager:
                //账号管理
                if (TextUtils.isEmpty(SPUtils.getInstance().getString(AppConstans.USER_PHONE))) {
                    DialogUtils.login_please("请先登录", getContext());
                } else {
                    startActivity(new Intent(getContext(), AccountActivity.class));
                }
                break;
            case R.id.super_huidashi:
                //一分钟了解汇大师
                intent = new Intent(context, NewsActivity.class);
                url = "http://47.91.164.170:8080/pages/xsxt/zh-CN/index.html";
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.super_version:
                //版本检测
                break;
            case R.id.super_aboutus:
                //关于我们
                intent = new Intent(context, NewsActivity.class);
                url = "http://47.91.164.170:8080/pages/aboutus/aboutus-zh-CN.html";
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.text_needexp:
                //完善资料
                String url_wanShanZiLiao = "http://uc.moyacs.com/real_app_v2.html";
                Intent intent1 = new Intent(context, UserInformationActivity.class);
                intent1.putExtra("url", url_wanShanZiLiao);
                startActivity(intent1);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSucessEvent(LoginMessageEvent object) {
        LogUtils.d("onEvent   登录成功");
        llUnlogin.setVisibility(View.GONE);
        llLogin.setVisibility(View.VISIBLE);
        getUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(EvenVo vo) {
        if (vo.getCode() == EvenVo.EVENT_CODE_UPDATE_NICK_NAME) {
            changeLoginSucessState();
        }
    }

    public void doLogin(String account, String token) {
        LoginInfo info = new LoginInfo(account, token);
        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtils.d("onSuccess");
                Intent intent = new Intent(getActivity(), KefuActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(int code) {
                LogUtils.d("onFailed");
            }

            @Override
            public void onException(Throwable exception) {
                LogUtils.d("onException");
            }
        };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

    private void getUserInfo() {
        ServerManger.getInstance().getServer().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ServerResult<UserInfoVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ServerResult<UserInfoVo> userInfoVoServerResult) {
                        if (userInfoVoServerResult.isSuccess()) {
                            llUnlogin.setVisibility(View.GONE);
                            llLogin.setVisibility(View.VISIBLE);
                            SPUtils.getInstance().put(AppConstans.USER_NICK_NAME, userInfoVoServerResult.getData().getLoginName());
                            changeLoginSucessState();
                        } else {
                            ToastUtils.showShort("服务器异常");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("服务器异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
