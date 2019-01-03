package com.moyacs.canary.main.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.moyacs.canary.base.BaseFragment2;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.im.KefuActivity;
import com.moyacs.canary.login.LoginActivity2;
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

    //    @BindView(R.id.pro_personintegral)
//    RoundProgressBar proPersonintegral;
    @BindView(R.id.img_person)
    CircleImageView imgPerson;
    //    @BindView(R.id.img_accout_lv)
//    ImageView imgAccoutLv;
    @BindView(R.id.text_uname)
    TextView textUname;
    //    @BindView(R.id.icon_auth)
//    ImageView iconAuth;
    @BindView(R.id.text_needexp)
    TextView textNeedexp;
    //    @BindView(R.id.text_accoutintegral)
//    TextView textAccoutintegral;
//    @BindView(R.id.text_accoutexp)
//    TextView textAccoutexp;
//    @BindView(R.id.line_accountintegral)
//    LinearLayout lineAccountintegral;
//    @BindView(R.id.text_accout_defeatpercent)
//    TextView textAccoutDefeatpercent;
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
        Log.i(TAG, "onCreateView:   addChildInflaterView");
        //eventbus 注册
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }
        context = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_me, null, false);
        unbinder = ButterKnife.bind(this, rootView);
        int mt4id = SPUtils.getInstance().getInt(AppConstans.mt4id, -1);
        if (mt4id != -1) {
            llUnlogin.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            changeLoginSucessState();
        }
        return rootView;
    }

    /**
     * 登录成功之后，改变相关状态
     */
    private void changeLoginSucessState() {
        String fullName = SPUtils.getInstance().getString(AppConstans.fullName);
        textUname.setText(fullName);
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
        Log.i(TAG, "onDestroyView:   onDestroyView");

    }

    @OnClick({R.id.btn_login, R.id.rl_openaccount, R.id.rl_intergral_market, R.id.rl_mission_center,
            R.id.ll_customer_service, R.id.ll_account_manager, R.id.super_huidashi, R.id.super_version,
            R.id.super_aboutus, R.id.text_needexp})
    public void onViewClicked(View view) {
        Intent intent;
        String url;
        switch (view.getId()) {
            case R.id.btn_login:
                intent = new Intent(context, LoginActivity2.class);
                startActivity(intent);
                break;
            case R.id.rl_openaccount:
                break;
            case R.id.rl_intergral_market:
                break;
            case R.id.rl_mission_center:
                break;
            case R.id.ll_customer_service:
                doLogin("kehu5", "abe93548e46eb35dce55c307fa35723a");
                break;
            case R.id.ll_account_manager:
                doLogin("kefu01", "25b9ef37654b723c8e71254d531f05b5");
                break;
            case R.id.super_huidashi:
                intent = new Intent(context, NewsActivity.class);
                url = "http://47.91.164.170:8080/pages/xsxt/zh-CN/index.html";
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.super_version:
                break;
            case R.id.super_aboutus:
                intent = new Intent(context, NewsActivity.class);
                url = "http://47.91.164.170:8080/pages/aboutus/aboutus-zh-CN.html";
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.text_needexp://完善资料
//                String url_wanShanZiLiao = "file:///android_asset/real_app_v2.html";
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
        changeLoginSucessState();
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

}
