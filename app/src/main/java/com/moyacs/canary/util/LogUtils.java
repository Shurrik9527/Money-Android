package com.moyacs.canary.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import www.moyacs.com.myapplication.BuildConfig;

/**
 * @Author: Restring
 * @Date: 2018/5/28
 * @Description:
 */
public class LogUtils {

    public static void d(Object log) {
        if (BuildConfig.DEBUG) Logger.d(log);
    }

    public static void e(String log) {
        if (BuildConfig.DEBUG) Logger.e(log, log);
    }

    public static void xml(String xml) {
        if (BuildConfig.DEBUG) Logger.xml(xml);
    }

    public static void json(String json) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(json) && isJson(json)) {
            Logger.json(json);
        }
    }

    public static boolean isJson(String src) {
        try {
            new Gson().fromJson(src, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
