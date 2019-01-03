package com.moyacs.canary.main.market;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.moyacs.canary.base.BaseActivity2;

import www.moyacs.com.myapplication.R;

/**
 * 自选页面
 */
public class OptionalActivity extends BaseActivity2 {


    @Override
    protected void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_msg).setVisible(false);
    }

    @Override
    protected void initIntentData(Intent intent) {

    }

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        return null;
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
        return "我的自选";
    }
}
