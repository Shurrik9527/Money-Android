package com.moyacs.canary.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 作者：luoshen on 2018/3/14 0014 16:01
 * 邮箱：rsf411613593@gmail.com
 * 说明：数字计算相关
 */

public class NumberUtils {


    /**
     * 计算涨跌值和涨跌幅
     *
     * @param close    平仓价格，即昨日关闭的时候价格
     * @param newPrice 获取的最新价格
     * @return 将涨跌值和涨跌幅放在数组中，第一个为 涨跌值，第二个为涨跌幅,第三个为涨或者跌，涨为“1”，跌为“-1”
     */
    public static String[] getValueAndRange(double close, double newPrice) {
        String[] arr = new String[3];
        //涨跌值
        String value = null;
        //涨跌幅
        String range = null;
        //是否为涨
        String isRise = "";

        //先比较大小，确定正负号 即：涨或跌
        int compare = compare(newPrice, close);
        if (compare == -1) {//表示 新价格 < 旧价格 ，跌

            isRise = "-1";

            //计算新旧价格差,为了保证为正，大 - 小
            double subtract = subtract(close, newPrice);
            //计算涨跌值 ，符号为全角符号
            value = "－ " + doubleToString(subtract);
            //计算涨跌幅
            range = "－ " + divide(subtract, newPrice, 5) + "%";
        } else if (compare == 0) {


        } else {// 新价格 > 旧价格 ， 涨

            isRise = "1";

            //计算新旧价格差,为了保证为正，大 - 小
            double subtract = subtract(newPrice, close);
            //计算涨跌值 符号为全角符号
            value = "＋ " + doubleToString(subtract);
            //计算涨跌幅
            range = "＋ " + divide(subtract, newPrice, 5) + "%";

        }
        arr[0] = value;
        arr[1] = range;
        arr[2] = isRise;

        return arr;


    }

    /**
     * 两个double 类型的数相减
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 将科学计数法的 double 转为 String
     *
     * @param d
     * @return
     */
    public static String doubleToString(double d) {
        DecimalFormat df = new DecimalFormat("###########0.######");
        String s = df.format(d);
        return s;
    }

    /**
     * 比较两个 double 类型数据的大小
     *
     * @param v1
     * @param v2
     * @return -1 表示 v1 < V2
     * 0 表示 v1 = v2
     * 1 表示 v1 > v2
     */
    public static int compare(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。因为乘以 100 ，所以需要 + 2
     */
    public static double divide(double v1, double v2, int scale) {
        if (v2 == 0) {
            return 0;
        }
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        // 在运算完之后再乘以 100 ，是计算涨幅
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue();
    }

    /**
     * 固定保留两位小数
     * <p>
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     */
    public static double divide(double v1, double v2) {


        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        // 在运算完之后再乘以 100 ，是计算涨幅
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 根据 保留小数位截取 double 类型数据（四舍五入）,小数位不满足的 用  0 表示
     *
     * @param v1    double
     * @param scale 保留的小数位
     * @return
     */
    public static String setScale(double v1, int scale) {
        if (scale == 0) {
            return String.valueOf(v1);
        }
        BigDecimal b = new BigDecimal(v1);
        double value = b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        String format = null;
        //小数点位不满足用 0 表示

        switch (scale) {
            case 1:
                format = new DecimalFormat("0.0").format(value);
                break;
            case 2:
                format = new DecimalFormat("0.00").format(value);
                break;
            case 3:
                format = new DecimalFormat("0.000").format(value);
                break;
            case 4:
                format = new DecimalFormat("0.0000").format(value);
                break;
            case 5:
                format = new DecimalFormat("0.00000").format(value);
                break;

        }
        return format;

    }

    /**
     * 保留两位小数，不四舍五入
     *
     * @param v1
     * @return
     */
    public static String setScale_down(double v1) {
        BigDecimal b = new BigDecimal(v1);
        double value = b.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        String format = new DecimalFormat("0.00").format(value);

        return format;

    }

    /**
     * 小数点不满两位的 用 0 表示，满足两位的，其后四舍五入
     *
     * @param v1
     * @return
     */
    public static String setScale2(double v1) {
        String format = new DecimalFormat("0.00").format(v1);
        return format;
    }

    /**
     * 乘法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double multiply(double num1, double num2) {
        BigDecimal big1 = new BigDecimal(num1);
        BigDecimal big2 = new BigDecimal(num2);
        return big1.multiply(big2).doubleValue();
    }


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
     * 仅仅针对报价所用的
     * 最多三位小数   120.0  120.01 120.123
     * 如果上千(>= 1000)并且是整数就显示整数部分 3224 3224.2
     *
     * @param d
     * @return
     */
    public static String beautifulDouble(double d) {
        String str = String.valueOf(d);
        try {
            if (str.contains(".")) {
                String s = str.split("\\.")[1];
                int length = s.length();
                if (length > 3) {
                    BigDecimal big = new BigDecimal(d);
                    double res = big.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                    DecimalFormat df = new DecimalFormat("######0.000");
                    return df.format(res);
                }
                if (d >= 1000) {
                    //小数部分是0
                    if (Integer.parseInt(s) == 0) {
                        return (int) d + "";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d + "";
    }

    /**
     * 去掉无效小数点
     * 去掉小数点最后补齐的0
     * 100.01 ＝ 100.01
     * 100.10 ＝ 100.1
     * 100.00 ＝ 100
     * 100 ＝ 100
     *
     * @param d
     * @return
     */
    public static String moveLast0(double d) {
        String str = String.valueOf(d);
        return moveLast0(str);
    }

    public static String moveLast0(String str) {
        try {
            if (str == null)
                return "";
            BigDecimal bigDecimal = new BigDecimal(str);
            str = bigDecimal.toString();
            if (str.contains(".")) {
                String[] ss = str.split("\\.");
                if (Integer.parseInt(ss[1]) > 0) {
                    if (ss[1].endsWith("0")) {
                        int index = ss[1].lastIndexOf("0");
                        str = ss[0] + "." + ss[1].substring(0, index);
                        if (str.endsWith("0")) {
                            return moveLast0(str);
                        }
                    } else {
                        str = ss[0] + "." + ss[1];
                    }
                } else {
                    str = ss[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取小数点之后有几位小数点
     *
     * @param str
     * @return 小数点有效位数
     */
    public static int getPointPow(String str) {
        try {
            //去科学计数发
            BigDecimal bigDecimal = new BigDecimal(str);
            str = bigDecimal.toString();
            if (str.contains(".")) {
                String[] ss = str.split("\\.");
                return ss[1].length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 加法
     *
     * @param num1
     * @param num2
     * @return
     */
    public static double add(double num1, double num2) {
        BigDecimal big1 = new BigDecimal(num1);
        BigDecimal big2 = new BigDecimal(num2);
        return big1.add(big2).doubleValue();
    }


}
