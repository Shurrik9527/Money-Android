package com.moyacs.canary.pay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.moyacs.canary.base.BaseActivity2;
import com.moyacs.canary.widget.ProgressWebView;

import www.moyacs.com.myapplication.R;

/**
 * 充值页面
 */
public class PayActivity extends BaseActivity2 {


    private String url;
    private WebView webView;
    private String TAG = "PayActivity";
    /**
     * 支付成功的 url
     */
    private final String paySucess = "xpayment.moamarkets.com/return";

    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_msg).setVisible(false);
    }

    @Override
    protected void initIntentData(Intent intent) {
        url = intent.getStringExtra("url");

        webView.loadUrl(url);
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //处理各种通知 & 请求事件 webview 中每个url 都会走这个方法
        webView.setWebViewClient(new WebViewClient() {
            /**
             * 打开网页时不调用系统浏览器， 而是在本WebView中显示；在网页上的所有加载都经过这个方法
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("PayActivity", "shouldOverrideUrlLoading:    url:  " + url);


                // 获取上下文, H5PayDemoActivity为当前页面
                final Activity context = PayActivity.this;

                // ------  对alipays:相关的scheme处理 -------
                if (url.startsWith("alipays:") || url.startsWith("alipay")) {
                    try {
                        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    } catch (Exception e) {
                        new AlertDialog.Builder(context)
                                .setMessage("未检测到支付宝客户端，请安装后重试。")
                                .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                        context.startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                    return true;
                }
                // ------- 处理结束 -------

                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }

                view.loadUrl(url);
                return true;
            }

            /**.
             * 开始载入页面调用的
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            /**
             * 在页面加载结束时调用
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "onPageFinished: " +url);

                /**
                 * 根据获取的 url 来判断是否支付成功
                 *
                 *  xpayment.moamarkets.com/return
                 */

                if (url.contains(paySucess)) {
                    LogUtils.d("支付成功");
                }

            }

            /**
             * 加载页面的服务器出现错误时（如404）调用
             * @param view
             * @param request
             * @param error
             */
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });


    }

    private LinearLayout mLinearLayout;

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        /**
         * 防止内存泄漏
         *  不在xml中定义 Webview ，而是在需要的时候在Activity中创建，并且Context使用 getApplicationgContext()
         */
        webView = new WebView(getApplicationContext());
        return webView;
    }

    @Override
    protected Boolean isShowBack() {
        return true;
    }

    @Override
    protected boolean isshowToolbar() {
        return true;
    }

    @Override
    protected String setToolbarTitle(String title) {
        return "充值";
    }

    /**
     * 防止 webView 内存泄漏
     */
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }



}
