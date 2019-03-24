package com.moyacs.canary.moudle.recharge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/3/7
 * @email 252774645@qq.com
 */
public class H5AliPayActivity extends AppCompatActivity {
    private static final String TAG = H5AliPayActivity.class.getName();
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = null;
        try {
            extras = getIntent().getExtras();
        } catch (Exception e) {
            finish();
            return;
        }
        if (extras == null) {
            finish();
            return;
        }
        String url = null;
        try {
            url = extras.getString("url");
        } catch (Exception e) {
            finish();
            return;
        }
        if (TextUtils.isEmpty(url)) {
            // 测试H5支付，必须设置要打开的url网站
            new AlertDialog.Builder(H5AliPayActivity.this).setTitle("异常提示")
                    .setMessage("支付路径为空")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    }).show();

        }
        LinearLayout layout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout, params);

        mWebView = new WebView(getApplicationContext());
        params.weight = 1;
        mWebView.setVisibility(View.VISIBLE);
        layout.addView(mWebView, params);

        WebSettings settings = mWebView.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
        settings.setAllowFileAccess(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {


            if(url.contains("https://www.baidu.com")){
                finish();
            }

//            // 获取上下文, H5PayDemoActivity为当前页面
//            final Activity context = H5AliPayActivity.this;
//
//            // ------  对alipays:相关的scheme处理 -------
//            if(url.startsWith("alipays:") || url.startsWith("alipay")) {
//                try {
//                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
//                } catch (Exception e) {
//                    new AlertDialog.Builder(context)
//                            .setMessage("未检测到支付宝客户端，请安装后重试。")
//                            .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Uri alipayUrl = Uri.parse("https://d.alipay.com");
//                                    context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
//                                }
//                            }).setNegativeButton("取消", null).show();
//                }
//                return true;
//            }
//            // ------- 处理结束 -------

//
//            final PayTask task = new PayTask(H5AliPayActivity.this);
//            boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
//                @Override
//                public void onPayResult(final H5PayResultModel result) {
//                    final String url=result.getReturnUrl();
//                    if(!TextUtils.isEmpty(url)){
//                        H5AliPayActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                view.loadUrl(url);
//                            }
//                        });
//                    }
//                }
//            });
//
//            /**
//             * 判断是否成功拦截
//             * 若成功拦截，则无需继续加载该URL；否则继续加载
//             */
//            if (!isIntercepted) {
//                view.loadUrl(url);
//            }
//            Log.i(TAG,"isIntercepted="+isIntercepted);

            final Activity context = H5AliPayActivity.this;
            //url.contains("alipays://platformapi")
            if(url.startsWith("alipays:") || url.startsWith("alipay")){
                try {
                    Intent intent =new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    startActivity(intent);
                    context.finish();
                }catch (Exception e){
                    new AlertDialog.Builder(context)
                            .setMessage("未检测到支付宝客户端，请点击下载支付宝安装后重试。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("取消", null).show();
                }
                return true;
            }else if(url.startsWith("weixin://wap/pay?")){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }else if(url.contains("alipayobjects")){//下载支付宝App

                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri contentUri =Uri.parse(url);
                    intent.setData(contentUri);
                    if(hasbrowser(context,intent)) {
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setComponent(new ComponentName("com.android.browser", "com.android.browser.BrowserActivity"));
                    }
                    startActivity(intent);
                    context.finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (!(url.startsWith("http") || url.startsWith("https"))){
                return true;
            }
            view.loadUrl(url);
            return true;
           // return super.shouldOverrideUrlLoading(view,url);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
         //   view.loadUrl("javascript:function getShare(){android.getShare();}");
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            try {
                mWebView.destroy();
            } catch (Throwable t) {
            }
            mWebView = null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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


}
