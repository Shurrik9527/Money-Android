package com.moyacs.canary.util;

import android.widget.Toast;

import com.moyacs.canary.MyApplication;

/**
 * @Author: Akame
 * @Date: 2019/1/12
 * @Description:
 */
public class ToastUtils {

    public static void showShort(String msg) {
        Toast.makeText(MyApplication.instance, msg, Toast.LENGTH_SHORT).show();
    }
}
