package com.moyacs.canary.util;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.moyacs.canary.MyApplication;

import www.moyacs.com.myapplication.R;

/**
 * @Author: Akame
 * @Date: 2019/1/12
 * @Description:
 */
public class ToastUtils {

    private static Toast toast;

    public static void showShort(String msg) {
        View view = LayoutInflater.from(MyApplication.instance)
                .inflate(R.layout.layout_toast_bg, null);
        TextView tvData = view.findViewById(R.id.tv_date);
        if (toast == null) {
            toast = new Toast(MyApplication.instance);
        }
        toast.setView(view);
        tvData.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
