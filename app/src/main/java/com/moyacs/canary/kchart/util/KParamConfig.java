package com.moyacs.canary.kchart.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceActivity;


import com.moyacs.canary.kchart.entity.KCandleObj;
import com.moyacs.canary.kchart.entity.KLineObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangzhu
 * 设置指标的参数值，如果不设置使用默认值
 */
public class KParamConfig {
    /*SharedPreferences 的tag*/
    public static final String SHAREED_CONFIG = "SHAREED_CONFIG";

    public static void setSmaParam1(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("SmaParam1", param);
        editor.commit();
    }

    public static int getSmaParam1(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("SmaParam1", 5);
    }

    public static void setSmaParam2(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("SmaParam2", param);
        editor.commit();
    }

    public static int getSmaParam2(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("SmaParam2", 10);
    }

    public static void setSmaParam3(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("SmaParam3", param);
        editor.commit();
    }

    public static int getSmaParam3(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("SmaParam3", 20);
    }

    public static void setEmaParam(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("EmaParam", param);
        editor.commit();
    }

    public static int getEmaParam(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("EmaParam", 20);
    }

    public static int getEmaParam1(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("EmaParam1", 5);
    }

    public static int getEmaParam2(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("EmaParam2", 10);
    }

    public static int getEmaParam3(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("EmaParam3", 20);
    }

    public static void setEmaParam1(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("EmaParam1", param);
        editor.commit();
    }

    public static void setEmaParam2(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("EmaParam2", param);
        editor.commit();
    }

    public static void setEmaParam3(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("EmaParam3", param);
        editor.commit();
    }

    public static void setBoolTParam(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("BoolTParam", param);
        editor.commit();
    }

    public static int getBoolTParam(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("BoolTParam", 20);
    }

    public static void setBoolKParam(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("BoolKParam", param);
        editor.commit();
    }

    public static int getBoolKParam(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("BoolKParam", 2);
    }


    public static void setMacdTParam1(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("MacdTParam1", param);
        editor.commit();
    }

    public static int getMacdTParam1(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("MacdTParam1", 12);
    }

    public static void setMacdTParam2(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("MacdTParam2", param);
        editor.commit();
    }

    public static int getMacdTParam2(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("MacdTParam2", 26);
    }

    public static void setMacdKParam(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("MacdKParam", param);
        editor.commit();
    }

    public static int getMacdKParam(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("MacdKParam", 9);
    }

    public static void setKdjParam(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("KdjParam", param);
        editor.commit();
    }

    public static int getKdjKParam(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("KdjParam", 9);
    }

    public static void setRsiParam1(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("RsiParam1", param);
        editor.commit();
    }

    public static int getRsiParam1(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("RsiParam1", 6);
    }

    public static void setRsiParam2(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("RsiParam2", param);
        editor.commit();
    }

    public static int getRsiParam2(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("RsiParam2", 12);
    }

    public static void setRsiParam3(Context context, int param) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("RsiParam3", param);
        editor.commit();
    }

    public static int getRsiParam3(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHAREED_CONFIG,
                PreferenceActivity.MODE_PRIVATE);
        return sp.getInt("RsiParam3", 24);
    }

    /**
     * 获取sma的均线参数list
     *
     * @param context
     * @return sma5 10 20 60 120
     */
    public static List<KLineObj<KCandleObj>> getSMAcfg(Context context, boolean longCycle) {
        List<KLineObj<KCandleObj>> lineObjs = new ArrayList<>();
        KLineObj<KCandleObj> obj = null;
        int value = 5;//周期
        String prex = "MA";

        obj = new KLineObj<>();
        value = KParamConfig.getSmaParam1(context);
        obj.setTitle(prex + value);
        obj.setValue(value);
        obj.setLineColor(Color.parseColor("#00f4a7"));
        lineObjs.add(obj);

        obj = new KLineObj<>();
        value = KParamConfig.getSmaParam2(context);
        obj.setTitle(prex + value);
        obj.setValue(value);
        obj.setLineColor(Color.parseColor("#fe4a87"));
        lineObjs.add(obj);

        obj = new KLineObj<>();
        value = KParamConfig.getSmaParam3(context);
        obj.setTitle(prex + value);
        obj.setValue(value);
        obj.setLineColor(Color.parseColor("#feb705"));
        lineObjs.add(obj);

        if (longCycle) {
            obj = new KLineObj<>();
            value = 60;
            obj.setTitle(prex + value);
            obj.setValue(value);
            obj.setLineColor(Color.parseColor("#5386FF"));
            lineObjs.add(obj);

            obj = new KLineObj<>();
            value = 120;
            obj.setTitle(prex + value);
            obj.setValue(value);
            obj.setLineColor(Color.parseColor("#FF616F"));
            lineObjs.add(obj);
        }
        return lineObjs;


    }
}
