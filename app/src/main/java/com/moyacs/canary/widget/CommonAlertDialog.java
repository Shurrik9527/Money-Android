package com.moyacs.canary.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import www.moyacs.com.myapplication.R;

/**
 * [类功能说明]
 * 通用弹框
 * @author ex-heguogui
 * @version v 1.4.0
 * @email ex-heguoguo@xianglin.cn
 */


public class CommonAlertDialog extends Dialog implements View.OnClickListener {

    private String title;
    private String content;
    private ConfirmListener confirmListener;
    private TextView contentTv;
    private TextView confirmTv;
    private String mBtntex;

    public CommonAlertDialog(Context context,String content,String btntex, ConfirmListener confirmListener) {
        super(context, R.style.common_alert_dialog);
        this.content = content;
        this.confirmListener = confirmListener;
        this.mBtntex = btntex;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        contentTv = (TextView) findViewById(R.id.content);
        confirmTv = (TextView) findViewById(R.id.confirm_tv);

        if ((TextUtils.isEmpty(title) && TextUtils.isEmpty(content))) {
            return;
        }

        contentTv.setText(content);


        if (!TextUtils.isEmpty(mBtntex)) {
            confirmTv.setText(mBtntex + "");
        }
        confirmTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_tv: {
                confirmListener.callback();
            }
            break;
        }
        dismiss();
    }

    public interface ConfirmListener {
        void callback();
    }

}
