package com.moyacs.canary.kchart.util;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by fangzhu
 * 格式数字  包括价格  成交额等
 */
public class KNumberUtil {

    /**
     * 四舍五入
     * @param d
     * @return
     */
    public static int roundUp(double d) {
        try {
            BigDecimal big = new BigDecimal(d);
            return big.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int)d;
    }


    /**
     * 格式化数字 默认两位小数点
     *
     * @param d
     * @return
     */
    public static String beautifulDouble(double d) {
        return beautifulDouble(d, 2);
    }

    /**
     * 格式化数字
     *
     * @param d
     * @param scale 小数点位数
     * @return
     */
    public static String beautifulDouble(double d, int scale) {
        try {
            BigDecimal big = new BigDecimal(d);
            double res = big.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
            return String.format("%." + scale + "f", res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d + "";
    }

    /**
     * 格式化数字
     *
     * @param d     数字的str
     * @param scale 小数点位数
     * @return
     */
    public static String beautifulDouble(String d, int scale) {
        try {
            if (d == null || "null".equalsIgnoreCase(d)) {
//                return "";
                d = "0";
            }
            d = d.replace("%", "");
            d = d.replace("--", "");
            Double dd = Double.parseDouble(d);
            return beautifulDouble(dd, scale);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 金额格式化
     *
     * @param moneyStr 金额
     * @param scale    小数点位数
     * @return
     */
    public static String formatNumberStr(String moneyStr, int scale) {
        if (TextUtils.isEmpty(moneyStr)) {
            return "";
        }
        double money = 0;
        try {
            money = Float.valueOf(moneyStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return beautifulDouble(money, scale);
    }

    /**
     * 成交量  成交额
     * 根据double  获取 万  亿  万亿
     *
     * @param d
     * @return
     */
    public static String formartBigNumber(double d) {
        //万以下 直接返回
        if (d < 10000)
            return beautifulDouble(d, 2);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);

        double wan = Math.pow(10, 8); //  一亿以下
        double yi = Math.pow(10, 12); //  万亿以下

        Log.e("tag", wan + "======" + yi);

        if (d < wan) {
            return df.format(d / (double) (Math.pow(10, 4))) + "万";
        } else if (d < yi) {
            return df.format(d / (double) (Math.pow(10, 8))) + "亿";
        } else {
            return df.format(d / (double) (Math.pow(10, 12))) + "万亿";
        }

    }


    public static String getPercentString(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        return df.format(d * 100) + "%";
    }

    public static double convertNumberString(String valueStr) {
        double number = 0;
        if (!TextUtils.isEmpty(valueStr)) {
            //匹配是否可转换
            if (valueStr.matches("-?(0|[1-9]\\d*)(\\.\\d+)?")) {
                number = Double.valueOf(valueStr);
            }
        }
        return number;
    }

}
