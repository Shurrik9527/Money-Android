package com.moyacs.canary.main.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moyacs.canary.base.BaseFragment;
import com.moyacs.canary.bean.UserAmountVo;
import com.moyacs.canary.bean.UserInfoVo;
import com.moyacs.canary.bean.UserInformBean;
import com.moyacs.canary.bean.event.EvenVo;
import com.moyacs.canary.bean.event.LoginMessageEvent;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.login.LoginActivity;
import com.moyacs.canary.main.me.account.AccountActivity;
import com.moyacs.canary.moudle.me.RealNameAuthActiviy;
import com.moyacs.canary.network.BaseObservable;
import com.moyacs.canary.network.RxUtils;
import com.moyacs.canary.network.ServerManger;
import com.moyacs.canary.network.ServerResult;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;
import com.moyacs.canary.web.BrowserActivity;
import com.moyacs.canary.web.VideoWebViewActivity;
import com.moyacs.canary.web.WebActivity;
import com.moyacs.canary.widget.CircleImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
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
    @BindView(R.id.img_person)
    CircleImageView ivPerson;
    @BindView(R.id.me_open_cer_iv)
    ImageView meOpenCerIv;
    @BindView(R.id.me_live_custom_service_iv)
    ImageView meLiveCustomServiceIv;
    @BindView(R.id.me_account_manager_iv)
    ImageView meAccountManagerIv;
    @BindView(R.id.me_iamge_iv)
    CircleImageView meIv;
    Unbinder unbinder;

    private boolean isLoadData = false;
    private UserInfoVo bean;


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
            if (bean != null) {
                if (!TextUtils.isEmpty(bean.getNickname())) {
                    tvNickName.setText(bean.getNickname());
                } else {
                    tvNickName.setText(bean.getLoginName());
                }
                if (!TextUtils.isEmpty(bean.getUserImg())) {
                    Glide.with(mActivity)
                            .load(bean.getUserImg())
                            .into(ivPerson);
                } else {
                    Glide.with(mActivity)
                            .load(R.mipmap.img_me_headimage_default)
                            .into(ivPerson);
                }

                if (!TextUtils.isEmpty(SharePreferencesUtil.getInstance().getNickName())) {
                    tvNickName.setText(SharePreferencesUtil.getInstance().getNickName());
                }
            }


        }
    }

    @OnClick({R.id.btn_login, R.id.img_person, R.id.super_huidashi, R.id.super_version,
            R.id.super_aboutus, R.id.text_needexp,R.id.me_open_cer_iv, R.id.me_live_custom_service_iv, R.id.me_account_manager_iv,R.id.me_iamge_iv})
    public void onViewClicked(View view) {
        Intent intent;
        String url;
        switch (view.getId()) {
            case R.id.me_iamge_iv:
                //登录
                intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                break;
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
                    Intent mIntent = new Intent(getContext(),AccountActivity.class);
                    mIntent.putExtra("USER_INFORM",bean);
                    startActivity(mIntent);
                }

                break;
            case R.id.super_huidashi:
                //一分钟了解汇大师
                Intent mintent =new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstans.KNOW_APP));
                startActivity(mintent);
//                intent = new Intent(mActivity, VideoWebViewActivity.class);
//                intent.putExtra("title", "一分钟了解掌上投");
//                intent.putExtra("loadUrl", AppConstans.KNOW_APP);
//                startActivity(intent);
                break;
            case R.id.super_version:
                //版本检测
                break;
            case R.id.super_aboutus:
                //关于我们
                intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("title", "关于我们");
                intent.putExtra("loadUrl", AppConstans.ABOUT_US);
                startActivity(intent);
                break;
            case R.id.text_needexp:
            case R.id.me_open_cer_iv:
                //完善资料
                intent = new Intent(mActivity, RealNameAuthActiviy.class);
                UserInformBean mBean = new UserInformBean();
                mBean.setIdcard(bean.getIdNumber());
                mBean.setIdcard_afterPath(bean.getCardReverse());
                mBean.setIdcard_beforPath(bean.getCardFront());
                mBean.setAge(bean.getBirthdate());
                mBean.setSex(bean.getGender());
                mBean.setName(bean.getUserName());

                if(!TextUtils.isEmpty(bean.getAuditStatus())){
                    if(bean.getAuditStatus().equals("DONT_SUBMIT")){
                        mBean.setAuth(false);
                    }else {
                        mBean.setAuth(true);
                    }
                }else {
                    mBean.setAuth(false);
                }
                mBean.setPhone(bean.getLoginName());
                intent.putExtra("USER_INFORM",mBean);
                startActivity(intent);
                break;
            case R.id.me_live_custom_service_iv:
                if (TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
                    DialogUtils.login_please("请先登录", getContext());
                } else {
                    RongIM.getInstance().startPrivateChat(getActivity(), AppConstans.CUSTOM_SERVER_ID, "客服");
                }
                break;
            case R.id.me_account_manager_iv:
                //点击头像进去个人信息设置
                if (TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
                    DialogUtils.login_please("请先登录", getContext());
                } else {
                    Intent mIntent = new Intent(getContext(),AccountActivity.class);
                    mIntent.putExtra("USER_INFORM",bean);
                    startActivity(mIntent);
                }
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
        if (vo.getCode() == EvenVo.EVENT_CODE_UPDATE_NICK_NAME||vo.getCode() == EvenVo.EVENT_CODE_REAL_NAME_AUTH) {
            getUserInfo();
            //setNickName();
        } else if (vo.getCode() == EvenVo.CHANGE_ORDER_SUCCESS) {
           // getUserBalance();
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
                        bean = data.getData();
                        SharePreferencesUtil.getInstance().setNickName(data.getData().getNickname());
                        //getUserBalance();
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

//    /**
//     * 获取用户资产信息
//     */
//    private void getUserBalance() {
//        addSubscribe(ServerManger.getInstance().getServer().getUserAmountInfo()
//                .compose(RxUtils.rxSchedulerHelper())
//                .subscribeWith(new BaseObservable<ServerResult<UserAmountVo>>() {
//                    @Override
//                    protected void requestSuccess(ServerResult<UserAmountVo> data) {
//                        UserAmountVo amount = data.getData();
//                        if (amount != null) {
//                            if (!TextUtils.isEmpty(amount.getBalance())) {
//                                tvBalance.setText(amount.getBalance());
//                            }
//                            if (!TextUtils.isEmpty(amount.getRechargeAmount())) {
//                                tvAsset.setText(amount.getRechargeAmount());
//                            }
//                        }
//                    }
//                }));
//    }


    @OnClick({R.id.me_open_cer_iv, R.id.me_live_custom_service_iv, R.id.me_account_manager_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.me_open_cer_iv:
                break;
            case R.id.me_live_custom_service_iv:
                break;
            case R.id.me_account_manager_iv:
                break;
        }
    }
}
