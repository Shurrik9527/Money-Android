package com.moyacs.canary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.moyacs.canary.MyApplication;
import com.moyacs.canary.common.AppConstans;
import com.moyacs.canary.web.VideoWebViewActivity;

/**
 * @Author: Akame
 * @Date: 2019/1/11
 * @Description:
 */
public class SharePreferencesUtil {
    private static final String TAG =SharePreferencesUtil.class.getName();
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
        return spf.getString("nickName", "");
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

    public void setUserHead(String head) {
        spf.edit().putString("userHead", head).apply();
    }

    public String getUserHead() {
        return spf.getString("userHead", "");
    }


    public void setIsFirst(boolean isfirst) {
        spf.edit().putBoolean("isFirst", isfirst).apply();
    }


    public boolean getIsFirst() {
        return spf.getBoolean("isFirst", false);
    }


    public String getPrivateKey(){
        return spf.getString("PRIVATE_KEY","");
    }

    public String getPublicKey(){
        return spf.getString("PUBLIC_KEY","");
    }

    public void savePublicPrivateKey(String pubkey,String priKey){
        Log.i(TAG,"生成公钥并保存="+pubkey);
        Log.i(TAG,"生成私钥并保存="+priKey);
        spf.edit().putString("PUBLIC_KEY", pubkey).apply();
        spf.edit().putString("PRIVATE_KEY", priKey).apply();
    }

    public void cleanPublicPrivateKey(){
        Log.i(TAG,"清理公钥并保存");
        Log.i(TAG,"清理私钥并保存");
        spf.edit().putString("PUBLIC_KEY", "").apply();
        spf.edit().putString("PRIVATE_KEY", "").apply();
    }

    public void setPartyId(long partyid) {
        spf.edit().putLong(AppConstans.PARTY_ID, partyid).apply();
    }

    public long getPartyId() {
        return spf.getLong(AppConstans.PARTY_ID, -1L);
    }



    public void clean() {
        spf.edit().clear().apply();
    }
}
