package com.moyacs.canary.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.moyacs.canary.util.ScreenUtil;
import com.wang.avi.AVLoadingIndicatorView;

import www.moyacs.com.myapplication.R;

/**
 * @Author: Akame
 * @Date: 2018/11/21
 * @Description: 加载提示框
 */
public class LoadingDialog extends Dialog {
    private AVLoadingIndicatorView loadView;
    private View view;

    public LoadingDialog(Context context) {
        super(context, R.style.MyDialog);
        initView(context);
    }

    private void initView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        loadView = view.findViewById(R.id.av_loading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = ScreenUtil.dip2px(getContext(),140);
        lp.height = ScreenUtil.dip2px(getContext(),140);
        dialogWindow.setAttributes(lp);
        setCancelable(false);
        loadView.show();
    }
}
