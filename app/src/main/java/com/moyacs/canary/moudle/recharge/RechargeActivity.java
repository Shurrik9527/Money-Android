package com.moyacs.canary.moudle.recharge;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moyacs.canary.base.BaseActivity;
import com.moyacs.canary.bean.PayBean;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.common.DialogUtils;
import com.moyacs.canary.im.CustomerServerActivtiy;
import com.moyacs.canary.util.AmountUtils;
import com.moyacs.canary.util.PayResult;
import com.moyacs.canary.util.ScreenUtil;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
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
    @BindView(R.id.recharge_contract_tv)
    TextView rechargeContractTv;

    private RechargeContract.Presenter mPresenter;
    private List<String> mLists;
    private RechargeAdapter mAdapter;
    private String mPrice;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private WebView mWebView;
    private boolean isSelect =false;
     //http://api.k780.com/?app=finance.rate&scur=USD&tcur=CNY&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        onViewClicked(findViewById(R.id.ll_bank));


        SpannableString spanString = new SpannableString("需要帮组?联系我们");
        //再构造一个改变字体颜色的Span
        ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
        //将这个Span应用于指定范围的字体
        spanString.setSpan(span, 5, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置给EditText显示出来
        rechargeContractTv.setText(spanString);


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
                    if(mPresenter!=null){
                        mPresenter.getRate(mPrice);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        new RechargePresenter(this);
        if(mPresenter!=null){
            mPresenter.getRechargeAmount();
            setSelectImage(iv02);
            mPresenter.getRate("3");
        }
    }

    @OnClick({R.id.iv_break, R.id.ll_bank, R.id.ll_ali, R.id.ll_wei_chat, R.id.b_recharge,R.id.recharge_contract_tv})
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
                if(isSelect){
                    if(!TextUtils.isEmpty(mPrice)&&mPresenter!=null){
                        mPresenter.rechargePay(AppConstans.PAY_TYPE_ZHIFUBAO,"https://www.baidu.com",mPrice);
                    }
                }else {
                    showMessageTips("请选择支付方式");
                }
                break;
            case R.id.recharge_contract_tv:
                if (TextUtils.isEmpty(SharePreferencesUtil.getInstance().getUserPhone())) {
                    DialogUtils.login_please("请先登录", RechargeActivity.this);
                } else {
                    RongIM.getInstance().startPrivateChat(RechargeActivity.this, AppConstans.CUSTOM_SERVER_ID, "客服");
                }
                break;
        }
    }

    private void setSelectImage(ImageView selectImage) {
        if (oldSelectImg != null) {
            oldSelectImg.setImageResource(R.mipmap.ic_un_select);
            isSelect =false;
        }
        selectImage.setImageResource(R.mipmap.ic_select);
        isSelect =true;
        oldSelectImg = selectImage;
    }

    @Override
    public void showRechargeAmount(List<String> mlists) {
        mLists.addAll(mlists);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showPayResult(PayBean bean) {

//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        intent.addCategory(Intent.CATEGORY_BROWSABLE);
//        Uri contentUri = Uri.parse(bean.getPay_url());
//        intent.setData(contentUri);
//        if(hasbrowser(this,intent)) {
//            intent.setComponent(new ComponentName("com.android.browser", "com.android.browser.BrowserActivity"));
//            startActivity(intent);
//        }else {
//            startActivity(intent);
//        }

        WebView.setWebContentsDebuggingEnabled(true);
        mWebView = new WebView(getApplicationContext());
        mWebView.setWebContentsDebuggingEnabled(true);
        mWebView.resumeTimers();
        Intent mIntent = new Intent(RechargeActivity.this, H5AliPayActivity.class);
        Bundle extras = new Bundle();
        extras.putString("url", bean.getPay_url());
        mIntent.putExtras(extras);
        startActivity(mIntent);
    }

    @Override
    public void showTotalRMBAcount(String price, String rate) {
        if(TextUtils.isEmpty(price)||TextUtils.isEmpty(rate)){
            return;
        }

        double mRate = Double.parseDouble(rate);
        int mPrice =Integer.parseInt(price);
        double allTotal =mPrice*mRate;
        tvRmb.setText("充值"+price+"美元，大约需要人民币￥"+AmountUtils.round(String.valueOf(allTotal),2));
    }


    public boolean hasbrowser(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list=pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolve:list) {
            if (resolve.activityInfo.packageName.contains("com.android.browser")){
                return true;
            }
        }
        return false;
    }


    @Override
    public void setPresenter(RechargeContract.Presenter presenter) {
        this.mPresenter =presenter;
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtils.showShort(msg+"");
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
