package com.moyacs.canary.moudle.guide;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.moyacs.canary.main.MainActivity;
import com.moyacs.canary.util.SharePreferencesUtil;
import com.moyacs.canary.util.ToastUtils;

import java.lang.ref.WeakReference;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.moyacs.com.myapplication.R;


/**
 * [类功能说明]
 *启动页面
 * @author HeGuoGui
 * @version 1.0.0
 * @time 2017/3/6 0006.
 */

public class WelcomeActivity extends Activity {

    private static final int SHOW_TIME = 2500;
    private static final int PERMISSIONS_REQUEST_CODE = 1002;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new Handler().postDelayed(new MyRunnable(WelcomeActivity.this), SHOW_TIME);

    }

    private  class MyRunnable implements Runnable {

        WeakReference<WelcomeActivity> mActivity;

        public MyRunnable(WelcomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            if (mActivity.get() == null) {
                return;
            }
            requestPermission();
        }

    }

    private void jumpActivity() {
        boolean isFirst = SharePreferencesUtil.getInstance().getIsFirst();
        if(!isFirst){
            startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
            SharePreferencesUtil.getInstance().setIsFirst(true);
        }else {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        }
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSIONS_REQUEST_CODE);

        } else {
            jumpActivity();
        }
    }

    /**
     * 权限获取回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {

                // 用户取消了权限弹窗
                if (grantResults.length == 0) {
                    ToastUtils.showShort("获取权限失败，请重新登录");
                }

                // 用户拒绝了某些权限
                for (int x : grantResults) {
                    if (x == PackageManager.PERMISSION_DENIED) {
                        ToastUtils.showShort("获取权限失败，请重新登录");
                        return;
                    }
                }
                // 所需的权限均正常获取
                jumpActivity();
            }
        }
    }


}
