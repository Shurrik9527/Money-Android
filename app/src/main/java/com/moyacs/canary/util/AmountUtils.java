/*
 * 乡邻小站
 *  Copyright (c) 2016 XiangLin,Inc.All Rights Reserved.
 */

package com.moyacs.canary.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 金额处理类
 * ex-heguogui
 * 2016/8/15
 */
public class AmountUtils {

    /**
     * 保留两位有效数字
     *
     * @param amount
     * @return
     */
    public static String formatAmount(BigDecimal amount) {

        if (amount == null) {
            return " 0.00";
        }
        String mResult = String.valueOf(amount);
        return mResult;
    }

    public static String getCommaFormat(String value) {

        try {
            return parseMoney(",###,##0.00", new BigDecimal(value));
        } catch (Exception e) {
            return "0.00";
        }

    }

    //自定义数字格式方法
    public static String getFormat(String style, BigDecimal value) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(style);// 将格式应用于格式化器
        return df.format(value.doubleValue());
    }

    /*  BigDecimal bd=new BigDecimal(123456789);
     System.out.println(mf.parseMoney(",###,###",bd)); //out: 123,456,789

     System.out.println(mf.parseMoney("##,####,###",bd)); //out: 123,456,789

     System.out.println(mf.parseMoney("######,###",bd)); //out: 123,456,789

     System.out.println(mf.parseMoney("#,##,###,###",bd)); //out: 123,456,789

     System.out.println(mf.parseMoney(",###,###.00",bd)); //out: 123,456,789.00

     System.out.println(mf.parseMoney(",###,##0.00",bd)); //out: 123,456,789.00

     BigDecimal bd=new BigDecimal(0);
     System.out.println(mf.parseMoney(",###,###",bd)); //out: 0

     System.out.println(mf.parseMoney(",###,###.00",bd)); //out: .00

     System.out.println(mf.parseMoney(",###,##0.00",bd)); //out: 0.00*/
    public static String parseMoney(String pattern, BigDecimal bd) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(bd);
    }


    public static String mul3(String v1, String v2, String v3) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal b3 = new BigDecimal(v3);
        return b1.multiply(b2).multiply(b3).toString();
    }

    public static String mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    public static String div(String v1, String v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度不能为0");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String round(String v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度不能为0");
        }
        BigDecimal b = new BigDecimal(v);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

}
