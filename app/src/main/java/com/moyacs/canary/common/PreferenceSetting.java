package com.moyacs.canary.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceActivity;

import java.io.File;
import java.util.Map;

/**
 * Created by fangzhu on 2014/12/16.
 */
public class PreferenceSetting {

    public static final String PRE_REFERSH_TIME = "PreferenceSetting_refershtime";
    public static final String KEY_REFERSH_TIME_LIVEROOM = "key_liveroom";
    public static final String KEY_REFERSH_TIME_TABROOM = "key_tabroom";
    public static final String KEY_REFERSH_TIME_TABCHAT = "key_tabchat";
    public static final String KEY_REFERSH_TIME_TABCALLLIST = "key_tabcalllist";
    public static final String KEY_REFERSH_TIME_TABHISTORY = "key_tabhistory";
    public static final String PRE_CHAT_MESSAGE = "PreferenceSetting_chat_msg";
    public static final String PRE_USER_INFO = "PreferenceSetting_USER_INFO";
    public static final String PRE_CHCHE_APP = "PreferenceSetting_CACE_APP";
    public static final String KEY_REFERSH_TIME_GLOBAL_NEWS = "key_global_news";
    public static final String KEY_REFERSH_TIME_MACRO_HOT = "key_macro_hot";
    public static final String KEY_REFERSH_TIME_OILSILVER = "key_oilsilver";
    public static final String KEY_REFERSH_TIME_ZIXUN_NEWS = "key_zixun_news";
    public static final String KEY_REFERSH_TIME_NEWS_MAIN = "key_main_news";
    public static final String KEY_REFERSH_TIME_NEWS_FORECAST = "key_forecast_news";
    public static final String KEY_REFERSH_TIME_NEWS_CALLLIST = "key_calllist_news";
    public static final String KEY_REFERSH_TIME_PERSON_BEANS = "key_person_beans";
    public static final String KEY_REFERSH_TIME_PERSON_BEANS_EXCHANGE_HISTORY = "key_person_beans_exchange_history";
    public static final String KEY_REFERSH_TIME_HOME_FRAGMENT = "key_home_fragment";
    public static final String KEY_REFERSH_TIME_PERSON_GENDAN = "key_person_gendan";
    public static final String KEY_REFERSH_TIME_PERSON_POPULARITY = "key_person_popularity";

    public static final String PRE_UMENG_EVENT = "PreferenceSetting_umeng_event";//记录umeng统计
    public static final String SETTING_FILE_NAME = "PreferenceSetting_default";

    public static final String NOT_ALLOW_UNIFYPWDDLG = "not_allow_unifypwddlg";// 是否允许统一交易密码弹窗
    public static final String ACCOUNT_INTEGRAL_VERSION = "account_integral_version";// 本地等级版本
    public static final String ACCOUNT_INTEGRAL_INFO = "account_integral_info";// 本地等级str
    public static final String MY_INTEGRALLEVEL = "my_integrallevel";// 我的积分等级
    public static final String ISINITZIXUAN = "isInitZixuan";// 是否初始化了自选
    public static final String ISSHOWTRADECREATE_FENGXIAN = "isShowTradeCreateFengXian";// 建仓的风险提示


    public static String getDownloadDir(Context app) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在

        if (sdCardExist)      //如果SD卡存在，则获取跟目录
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            return sdDir.toString() + "/myapp/cache/download";
        }
        return app.getExternalCacheDir() + "myapp/cache/download";
    }

    //get history refreshtime by key
    public static long getRefershTime(Context app, String key, long defaultValu) {
        return getSharedPreferences(app, PRE_REFERSH_TIME, key, defaultValu);
    }

    public static void setRefershTime(Context app, String key, long value) {
        setSharedPreferences(app, PRE_REFERSH_TIME, key, value);
    }

    public static String getCacheString(Context app, String key, String defaule) {
        String result = null;
        SharedPreferences settings = app.getSharedPreferences(PRE_CHCHE_APP, 0);
        result = settings.getString(key, defaule);
        return result;
    }

    public static void setCacheString(Context app, String key, String value) {
        SharedPreferences settings = app.getSharedPreferences(PRE_CHCHE_APP, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static String getSharedPreferences(Context app, String preferName, String key) {
        String result = null;
        SharedPreferences settings = app.getSharedPreferences(preferName, 0);
        result = settings.getString(key, null);
        return result;
    }

    public static void setSharedPreferences(Context app, String preferName, String key, String value) {
        SharedPreferences settings = app.getSharedPreferences(preferName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreferences(Application app, String preferName, String key) {
        String result = null;
        SharedPreferences settings = app.getSharedPreferences(preferName, 0);
        result = settings.getString(key, null);
        return result;
    }

    public static Map<String, String> getSharedPreferences(Context app, String preferName) {
        SharedPreferences settings = app.getSharedPreferences(preferName, 0);
        Map<String, String> preferences = (Map<String, String>) settings.getAll();
        return preferences;
    }

    public static void removeSharedPreferences(Context context, String preferName, String key) {
        SharedPreferences settings = context.getSharedPreferences(preferName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void removeSharedPreferences(Context app, String preferName) {
        SharedPreferences settings = app.getSharedPreferences(preferName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public static long getSharedPreferences(Context app, String preferName, String key, long defaultValue) {
        long result = 0;
        SharedPreferences settings = app.getSharedPreferences(preferName, 0);
        result = settings.getLong(key, defaultValue);
        return result;
    }

    public static void setSharedPreferences(Context app, String preferName, String key, long value) {
        SharedPreferences settings = app.getSharedPreferences(preferName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getUmengEventTimeTag(Context app, String key) {
        SharedPreferences settings = app.getSharedPreferences(PRE_UMENG_EVENT, 0);
        return settings.getString(key, null);
    }

    public static void setUmengEventTimeTag(Context app, String key, String value) {
        SharedPreferences settings = app.getSharedPreferences(PRE_UMENG_EVENT, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        return sp.getLong(key, -1L);
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        sp.edit().putLong(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        return sp.getInt(key, -1);
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }
}
