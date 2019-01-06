package com.moyacs.canary.main.me;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.moyacs.canary.pay.PayActivity;

import www.moyacs.com.myapplication.R;

/**
 * 完善用户资料页面
 */
public class UserInformationActivity extends BaseActivity2 {


    private WebView webView;
    private String url;

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
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
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
        return "完善资料";
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
