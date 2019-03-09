package com.moyacs.canary.moudle.recharge;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.PayBean;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.util.PayResult;
import com.moyacs.canary.util.ScreenUtil;
import com.moyacs.canary.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import www.moyacs.com.myapplication.R;

public class RechargeActivity extends BaseActivity implements RechargeContract.View{

    private static final String TAG =RechargeActivity.class.getName();
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_rmb)
    TextView tvRmb;
    @BindView(R.id.iv_01)
    ImageView iv01;
    @BindView(R.id.iv_02)
    ImageView iv02;
    @BindView(R.id.iv_03)
    ImageView iv03;
    private ImageView oldSelectImg;

    private RechargeContract.Presenter mPresenter;
    private List<String> mLists;
    private RechargeAdapter mAdapter;
    private String mPrice;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private WebView mWebView;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    ToastUtils.showShort("支付返回结果成功");
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.i(TAG,"resultStatus==="+resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
//                    if (TextUtils.equals(resultStatus, "9000")) {
//                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        showAlert(PayDemoActivity.this, getString(R.string.pay_success) + payResult);
//                    } else {
//                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        showAlert(PayDemoActivity.this, getString(R.string.pay_failed) + payResult);
//                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
//                    @SuppressWarnings("unchecked")
//                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
//                    String resultStatus = authResult.getResultStatus();
//
//                    // 判断resultStatus 为“9000”且result_code
//                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
//                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
//                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
//                        // 传入，则支付账户为该授权账户
//                        showAlert(PayDemoActivity.this, getString(R.string.auth_success) + authResult);
//                    } else {
//                        // 其他状态值则为授权失败
//                        showAlert(PayDemoActivity.this, getString(R.string.auth_failed) + authResult);
//                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        onViewClicked(findViewById(R.id.ll_bank));
        mLists = new ArrayList<>();
        rvContent.setLayoutManager(new GridLayoutManager(this, 3, LinearLayout.VERTICAL, false));
        rvContent.addItemDecoration(new MyItemDecoration());
        mAdapter =new RechargeAdapter(mLists);
        rvContent.setAdapter(mAdapter);

    }

    @Override
    protected void intListener() {
        mAdapter.setItemClickListener(new RechargeAdapter.OnViewLister() {
            @Override
            public void onItemClickListener(int pos) {
                if(mLists!=null){
                    mPrice =mLists.get(pos);
                }
            }
        });
    }

    @Override
    protected void initData() {
        new RechargePresenter(this);
        if(mPresenter!=null){
            mPresenter.getRechargeAmount();
        }
    }

    @OnClick({R.id.iv_break, R.id.ll_bank, R.id.ll_ali, R.id.ll_wei_chat, R.id.b_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_break:
                finish();
                break;
            case R.id.ll_bank:
                setSelectImage(iv01);
                break;
            case R.id.ll_ali:
                setSelectImage(iv02);
                break;
            case R.id.ll_wei_chat:
                setSelectImage(iv03);
                break;
            case R.id.b_recharge:

                if(!TextUtils.isEmpty(mPrice)&&mPresenter!=null){
                    mPresenter.rechargePay(AppConstans.PAY_TYPE_ZHIFUBAO,"https://www.baidu.com",mPrice);
                }
                break;
        }
    }

    private void setSelectImage(ImageView selectImage) {
        if (oldSelectImg != null) {
            oldSelectImg.setImageResource(R.mipmap.ic_un_select);
        }
        selectImage.setImageResource(R.mipmap.ic_select);
        oldSelectImg = selectImage;
    }

    @Override
    public void showRechargeAmount(List<String> mlists) {
        mLists.addAll(mlists);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showPayResult(PayBean bean) {


//        Runnable payRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(RechargeActivity.this);
//                Map<String,String> result = alipay.payV2(bean.getPay_url(),true);
//
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//        // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();


//        Intent intent=new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri CONTENT_URI_BROWSERS = Uri.parse(bean.getPay_url());
//        intent.setData(CONTENT_URI_BROWSERS);
//        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//        startActivity(intent);

        Intent intent1= new Intent();
//        intent1.setAction(Intent.ACTION_VIEW);
//        Uri content_url = Uri.parse(bean.getPay_url());
//        intent1.setData(content_url);
//        startActivity(Intent.createChooser(intent1,"选择浏览器"));


        intent1.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(bean.getPay_url());
        intent1.setData(url);
        startActivity(intent1);


//        WebView.setWebContentsDebuggingEnabled(true);
//        mWebView = new WebView(getApplicationContext());
//        mWebView.setWebContentsDebuggingEnabled(true);
//        mWebView.resumeTimers();
//        Intent mIntent = new Intent(RechargeActivity.this, H5AliPayActivity.class);
//        Bundle extras = new Bundle();
//        extras.putString("url", bean.getPay_url());
//        mIntent.putExtras(extras);
//        startActivity(mIntent);
    }

    @Override
    public void setPresenter(RechargeContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void showMessageTips(String msg) {

    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            int offset = ScreenUtil.dip2px(RechargeActivity.this, 16);
            int itemContent = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            if ((pos + 1) <= itemContent) {
                outRect.top = offset;
            }
            if ((pos + 1) % itemContent == 0) {
                outRect.right = offset;
            }
            outRect.bottom = offset;
            outRect.left = offset;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mWebView!=null){
            mWebView.resumeTimers();
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mWebView!=null){
            mWebView.onPause();
            mWebView.pauseTimers();
        }
    }
}
