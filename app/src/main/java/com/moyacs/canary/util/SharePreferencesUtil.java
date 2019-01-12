package com.moyacs.canary.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.moyacs.canary.MyApplication;

/**
 * @Author: Akame
 * @Date: 2019/1/11
 * @Description:
 */
public class SharePreferencesUtil {
    private SharedPreferences spf;
    private static final SharePreferencesUtil ourInstance = new SharePreferencesUtil();

    public static SharePreferencesUtil getInstance() {
        return ourInstance;
    }

    private SharePreferencesUtil() {
        spf = MyApplication.instance.getSharedPreferences("MY_DATA", Context.MODE_PRIVATE);
    }

    public void setNickName(String nickName) {
        spf.edit().putString("nickName", nickName).apply();
    }

    public String getNickName() {
        return spf.getString("nickName", "这家伙很懒~");
    }

    public void setUserPhone(String userPhone) {
        spf.edit().putString("userPhone", userPhone).apply();
    }

    public String getUserPhone() {
        return spf.getString("userPhone", "");
    }

    public void setServerAuthor(String serverAuthor) {
        spf.edit().putString("serverAuthor", serverAuthor).apply();
    }

    public String getServerAuthor() {
        return spf.getString("serverAuthor", "");
    }

    public void clean() {
        spf.edit().clear().apply();
    }
}
