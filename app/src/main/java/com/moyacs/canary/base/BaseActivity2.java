package com.moyacs.canary.base;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import www.moyacs.com.myapplication.R;

/**
 * 1. 统一设置 toolbar 标题栏居中，左侧为返回按钮或者menu，由子类决定
 * 2. 统一设置 toolbar 标题栏右侧菜单，由子类决定隐藏或者显示
 * 3. 统一设置 getIntent()，由子类决定是否对传递过来的数据处理
 * 4. 统一设置 加载中动画
 */
public abstract class BaseActivity2 extends AppCompatActivity {

    protected TextView tvTitle;
    protected Toolbar toolbar;
    protected LinearLayout rootLayout;
    protected AVLoadingIndicatorView avLoading;
    protected AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        tvTitle = findViewById(R.id.tv_title);
        toolbar = findViewById(R.id.toolbar);
        rootLayout = findViewById(R.id.root_layout);
        avLoading = findViewById(R.id.avi_loading);
        appBarLayout = findViewById(R.id.appbarlayout);
        initContentView();
        initToolbar("汇大师");
        Intent intent = getIntent();
        initIntentData(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (isshowToolbar()) {

            getMenuInflater().inflate(R.menu.menu_base_toolbar, menu);
            updateOptionsMenu(menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 由子类 决定 那个 menu 显示或者隐藏
     *
     * @param menu
     */
    protected abstract void updateOptionsMenu(Menu menu);

    /**
     * 由子类来处理传递过来的数据
     *
     * @param intent 获取到的从其他页面传递过来的 intent
     */
    protected abstract void initIntentData(Intent intent);

    /**
     * 初始化标题栏
     */
    protected void initToolbar(String title) {
        if (!isshowToolbar()) {
            appBarLayout.setVisibility(View.GONE);
            return;
        }
        toolbar.setTitle("");//这步不可省
        tvTitle.setText(setToolbarTitle(title));
        setSupportActionBar(toolbar);
        //显示 menu 按钮
        if (isshowHome()) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
            return;
        }
        //显示返回按钮
        if (isShowBack()) {
            //设置返回键为白色
            Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
            //左侧返回键点击事件
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 是否显示 toolbar
     * @return
     */
    protected boolean isshowToolbar() {
        return true;
    }

    /**
     * 是否显示标题栏左侧返回按钮
     *
     * @return
     */
    protected Boolean isShowBack() {
        return false;
    }

    /**
     * 是否显示标题栏左侧 menu 按钮
     *
     * @return
     */
    protected Boolean isshowHome() {
        return false;
    }

    /**
     * 由子类决定标题栏内容，如果不设置默认为 “易奢购”
     *
     * @return 标题栏内容
     */
    protected String setToolbarTitle(String title) {
        return title;
    }

    /**
     * 将子类布局添加至父类中
     */
    private void initContentView() {
        View view = addChildContentView(rootLayout);
        if (view == null) {
            return;
        }
        rootLayout.addView(view);
    }

    /**
     * 由子类决定 添加给父类添加什么布局
     *
     * @param rootLayout 父类顶层布局 LinearLayout
     * @return
     */
    protected abstract View addChildContentView(LinearLayout rootLayout);

    /**
     * 开启加载中动画
     */
    protected void startLoading() {
        if (avLoading.isShown()) {
            return;
        }
        avLoading.smoothToShow();
    }

    /**
     * 关闭加载中动画
     */
    protected void stopLoading() {
        if (avLoading.isShown()) {
            avLoading.smoothToHide();
        }
    }


}
