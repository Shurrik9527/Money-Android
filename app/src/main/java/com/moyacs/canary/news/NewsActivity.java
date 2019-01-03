package com.moyacs.canary.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.moyacs.canary.base.BaseActivity2;
import com.moyacs.canary.widget.ProgressWebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import www.moyacs.com.myapplication.R;

public class NewsActivity extends BaseActivity2 {
    @BindView(R.id.wv_news)
    ProgressWebView wvDetail;
    private Unbinder unbinder;

    @Override
    protected void updateOptionsMenu(Menu menu) {

    }

    @Override
    protected void initIntentData(Intent intent) {
        String url = intent.getStringExtra("url");
        wvDetail.loadUrl(url);
        WebSettings webSettings = wvDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        wvDetail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }
        });
    }

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_news, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected boolean isshowToolbar() {
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
