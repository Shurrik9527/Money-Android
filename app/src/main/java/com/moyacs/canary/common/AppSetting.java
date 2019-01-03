package com.moyacs.canary.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceActivity;

public class AppSetting {
    public static final String TAG = "AppSetting";
    // sourceId  8元操盘
    public static final int APPSOURCE_TRADE = 10;

    private static AppSetting instance;
    //验证码的长度
    public static final int VAilCODE_LENGTH = 6;
    //本地 token允许的保留时间20160707 5小时
    public static final long TIME_WP_TOKEN = 5 * 60 * 60 * 1000;
    //哈贵
    public static final long TIME_WP_TOKEN_HG = 5 * 60 * 60 * 1000;

    public static final String SETTING_FILE_NAME = "setting";
    //和token相关的  的share key，时间 token
    public static final String SHARE_KEY = "share_token";

    private Context context;

    private AppSetting(Context context) {
        this.context = context;
    }

    public static AppSetting getInstance(Context context) {
        if (instance == null) {
            instance = new AppSetting(context);
        } else {
            instance.context = context;
        }
        return instance;
    }

    /**
     * 获取app packagename
     *
     * @return
     */
    public int getAppSource() {

        return APPSOURCE_TRADE;
    }

    //这里是手机号
    public void setUserName(String usernane) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("username", usernane);
        editor.commit();
    }

    public String getUserName() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getString("username", "");
    }


    //=================行情刷新时间======start=======
    //设置直播室行情刷新
    public void setRefreshTimeChatRoom(long time) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong("RefreshTimeChatRoom", time);
        editor.commit();
    }

    //直播室行情刷新
    public long getRefreshTimeChatRoom() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getLong("RefreshTimeChatRoom", 2000L);
    }

    //行情刷新时间，不和交易相关
    public void setRefreshTimeWPQ(long time) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong("RefreshTimeWPHanging", time);
        editor.commit();
    }

    //行情刷新时间，不和交易相关
    public long getRefreshTimeWPQ() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getLong("RefreshTimeWPHanging", 1000L);
    }

    //交易大厅列表刷新
    public void setRefreshTimeWPList(long time) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong("RefreshTimeWPList", time);
        editor.commit();
    }

    //交易大厅列表刷新时间
    public long getRefreshTimeWPList() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getLong("RefreshTimeWPList", 5000L);
    }


    //持仓刷新时间
    public void setRefreshTimeWPHoldOrder(long time) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong("RefreshTimeWPHoldOrder", time);
        editor.commit();
    }

    //持仓刷新时间
    public long getRefreshTimeWPHoldOrder() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getLong("RefreshTimeWPHoldOrder", 2000L);
    }

    //行情刷新时间
    public void setRefreshTime(long time) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong("RefreshTime", time);
        editor.commit();
    }

    //行情刷新时间
    public long getRefreshTime() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getLong("RefreshTime", 5000L);
    }
    //=================行情刷新时间======end=======


    //=================token======start=======

    //token 过期时间 分交易所的
//    public void setRefreshTimeWPToken(Context context, long time) {
//        SharedPreferences sp = context.getSharedPreferences(SHARE_KEY,
//                PreferenceActivity.MODE_PRIVATE);
//        Editor editor = sp.edit();
//        editor.putLong(TradeConfig.getCurrentTradeCode(context) + "RefreshTimeWPToken", time);
//        editor.commit();
//    }

    //token 过期时间 按交易所的
    public void setRefreshTimeWPToken(Context context, String source, long time) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_KEY,
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong(source + "RefreshTimeWPToken", time);
        editor.commit();
    }

    //token 过期时间 分交易所的
//    public long getRefreshTimeWPToken(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(SHARE_KEY,
//                PreferenceActivity.MODE_PRIVATE);
//        return sp.getLong(TradeConfig.getCurrentTradeCode(context) + "RefreshTimeWPToken", 0L);
//    }

    //token 过期时间 按交易所的
    public long getRefreshTimeWPToken(Context context, String tradeCode) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_KEY,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getLong(tradeCode + "RefreshTimeWPToken", 0L);
    }

    //token 本地保存 分交易所的
//    public void setWPToken(Context context, String token) {
//        SharedPreferences sp = context.getSharedPreferences(SHARE_KEY,
//                PreferenceActivity.MODE_PRIVATE);
//        Editor editor = sp.edit();
//        editor.putString(TradeConfig.getCurrentTradeCode(context) + "WPToken", token);
//        editor.commit();
//    }

    //token 本地保存 按交易所的
    public void setWPToken(Context context, String source, String token) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_KEY,
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(source + "WPToken", token);
        editor.commit();
    }

    //token 本地保存 分交易所的
//    public String getWPToken(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(SHARE_KEY,
//                PreferenceActivity.MODE_PRIVATE);
//        return sp.getString(TradeConfig.getCurrentTradeCode(context) + "WPToken", "");
//    }

    //token 本地保存 按交易所的
    public String getWPToken(Context context, String tradeCode) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_KEY,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getString(tradeCode + "WPToken", "");
    }

    /**
     * 清除token
     */
    public void clearWPToken() {
        SharedPreferences sp = context.getSharedPreferences(SHARE_KEY,
                PreferenceActivity.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    /**
     * 更新本地token
     * @param
     */
//    public void updateToken (String token) {
//        if (StringUtil.isEmpty(token))
//            return;
//        //本地设置token
//        AppSetting.getInstance(context).setWPToken(context, token);
//        //本地设置token获取时间
//        AppSetting.getInstance(context).setRefreshTimeWPToken(context, System.currentTimeMillis());
//
//    }
    //=================token======end=========


    public void setHighlightFlag(boolean isHight) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("HighlightFlag", isHight);
        editor.commit();
    }

    public boolean isHighlightFlag() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getBoolean("HighlightFlag", false);
    }

    public void setTuisongFlag(boolean isHight) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("TuisongFlag", isHight);
        editor.commit();
    }

    public boolean isTuisongFlagFlag() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getBoolean("TuisongFlag", true);
    }

    /**
     * 资金有变化，只要有新的平仓；
     * 忽略使用代金券没有赚到钱的情况
     *
     * @param value
     */
    public void setMoneyFlag(boolean value) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("MoneyFlag", value);
        editor.commit();
    }

    public boolean isMoneyFlag() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getBoolean("MoneyFlag", true);
    }

    public boolean isSmallWindowFlag() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getBoolean("SmallWindowFlag", true);
    }

    public void setSmallWindowFlag(boolean isShow) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("SmallWindowFlag", isShow);
        editor.commit();
    }

    public void setSmallWindowCheckOptional(String localId) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("CheckOptional", localId);
        editor.commit();
    }

    public String getSmallWindowCheckOptional() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getString("CheckOptional", "");
    }

    public void clearUserInfo() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


    /***
     * Sma
     *
     * @param param
     */
    public void setSmaParam1(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("SmaParam1", param);
        editor.commit();
    }

    public int getSmaParam1() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("SmaParam1", 5);
    }

    public void setSmaParam2(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("SmaParam2", param);
        editor.commit();
    }

    public int getSmaParam2() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("SmaParam2", 10);
    }

    public void setSmaParam3(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("SmaParam3", param);
        editor.commit();
    }

    public int getSmaParam3() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("SmaParam3", 20);
    }

    public void setEmaParam(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("EmaParam", param);
        editor.commit();
    }

    public int getEmaParam() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("EmaParam", 20);
    }

    public void setBoolTParam(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("BoolTParam", param);
        editor.commit();
    }

    public int getBoolTParam() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("BoolTParam", 20);
    }

    public void setBoolKParam(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("BoolKParam", param);
        editor.commit();
    }

    public int getBoolKParam() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("BoolKParam", 2);
    }

    /***
     * macd
     */
    public void setMacdTParam1(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("MacdTParam1", param);
        editor.commit();
    }

    public int getMacdTParam1() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("MacdTParam1", 12);
    }

    public void setMacdTParam2(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("MacdTParam2", param);
        editor.commit();
    }

    public int getMacdTParam2() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("MacdTParam2", 26);
    }

    public void setMacdKParam(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("MacdKParam", param);
        editor.commit();
    }

    public int getMacdKParam() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("MacdKParam", 9);
    }

    public void setKdjParam(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("KdjParam", param);
        editor.commit();
    }

    public int getKdjKParam() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("KdjParam", 9);
    }

    public void setRsiParam1(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("RsiParam1", param);
        editor.commit();
    }

    public int getRsiParam1() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("RsiParam1", 6);
    }

    public void setRsiParam2(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("RsiParam2", param);
        editor.commit();
    }

    public int getRsiParam2() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("RsiParam2", 12);
    }

    public void setRsiParam3(int param) {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("RsiParam3", param);
        editor.commit();
    }

    public int getRsiParam3() {
        SharedPreferences sp = context.getSharedPreferences("user_info",
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("RsiParam3", 24);
    }

    /**
     * 是否是第一次启动，是否开启导航页面
     *
     * @param
     */
    public void setTureNavi(Boolean isOpen) {
        SharedPreferences sp = context.getSharedPreferences("setting",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();

        //每个新版本都是用引导页 appcode
        int appCode = 0;
        try {
            appCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        editor.putBoolean("Navi" + "_" + appCode, isOpen);

        editor.commit();
    }

    public boolean getIsTureNavi() {
        SharedPreferences sp = context.getSharedPreferences("setting",
                PreferenceActivity.MODE_PRIVATE);
        //每个新版本都是用引导页 appcode
        int appCode = 0;
        try {
            appCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return sp.getBoolean("Navi" + "_" + appCode, false);
    }


    public boolean isShowNewsPic() {
        // TODO Auto-generated method stub
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        return sp.getBoolean("ShowNewsPic", true);
    }

    public void setShowNewsPic(boolean save) {
        SharedPreferences sp = context.getSharedPreferences("setting",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("ShowNewsPic", save);
        editor.commit();
    }

    public boolean isBigTextFont() {
        // TODO Auto-generated method stub
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        return sp.getBoolean("BigTextFont", false);
    }

    public void setBigTextFont(boolean save) {
        SharedPreferences sp = context.getSharedPreferences("setting",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("BigTextFont", save);
        editor.commit();
    }

    public void clearSettingInfo() {
        SharedPreferences sp = context.getSharedPreferences("setting",
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


    public boolean isInitOptionalType(String source) {
        SharedPreferences sp = context.getSharedPreferences(
                SETTING_FILE_NAME, PreferenceActivity.MODE_PRIVATE);
        return sp.getBoolean(source, false);
    }

    public void setInitOptionalType(String source, boolean save) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_FILE_NAME,
                PreferenceActivity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(source, save);
        editor.commit();
    }


    /**
     * 版本信息
     * @return versionName.versionCode
     */
    public String getAppVersion() {
        String versionName = "";
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null)
                versionName = packageInfo.versionName;
            return versionName + "." + packageInfo.versionCode;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取AndroudManifest下的umeng渠道
     *
     * @return
     */
    public String getAppMarket() {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String code = appInfo.metaData.getString("UMENG_CHANNEL");
            if (code != null && code.trim().length() > 0)
                return code;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "other";//if get exception  return other

//        String channel = WalleChannelReader.getChannel(context);
//        if(!TextUtils.isEmpty(channel)){
//            if (channel.equalsIgnoreCase("unknown")) {
//                return "other";
//            }
//            return channel;
//        }
//        return "other";
    }

    /**
     * @return versionName
     */
    public String getAppVersionName() {
        String versionName = "";
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null)
                versionName = packageInfo.versionName;
            return versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return versionCode
     */
    public int getAppVersionCode() {
        int versionCode = 0;
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null)
                versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

}
