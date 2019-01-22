package com.moyacs.canary.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyUtil {

    public static DecimalFormat fnum = new DecimalFormat("0.000");

    /**
     * 格式化金额
     *
     * @param value
     * @return
     */
    public static String formatMoney(String value) {
        if (value == null || value.equals("")) {
            value = "0.00";
        }
        value = value.replaceAll("元", "").trim();
        value = value.replace(",", "").trim();
        return fnum.format(new BigDecimal(value));
    }


    public static String transitionMoney(String value) {
        if (value == null || value == "") {
            return "0.00";
        }
        float money = Float.parseFloat(formatMoney(value));
        return String.valueOf((money * 10000) / 100);
    }


    public static double moneyMulOfNotDouble(String value) {
        if (value == null || value.equals("")) {
            value = "0.00";
            return Double.parseDouble(fnum.format(value));
        }
        value = value.replaceAll("元", "").trim();
        value = value.replaceAll(",", "").trim();
        return Double.parseDouble(fnum.format(new BigDecimal(value)));
    }


    /**
     * 金额相加
     *
     * @param valueStr 基础值
     * @param addStr   被加数
     * @return
     */
    public static String moneyAdd(String valueStr, String addStr) {
        BigDecimal value = new BigDecimal(valueStr);
        BigDecimal augend = new BigDecimal(addStr);
        return moveLast0(fnum.format(value.add(augend)));
    }

    /**
     * 金额相加
     *
     * @param value  基础值
     * @param augend 被加数
     * @return
     */
    public static BigDecimal moneyAdd(BigDecimal value, BigDecimal augend) {
        return value.add(augend);
    }

    /**
     * 金额相减
     *
     * @param valueStr 基础值
     * @param minusStr 减数
     * @return
     */
    public static String moneySub(String valueStr, String minusStr) {
        if (valueStr == null || valueStr.equals("")) {
            valueStr = "0.00";
        }
        if (minusStr == null || minusStr.equals("")) {
            minusStr = "0.00";
        }

        valueStr = valueStr.replaceAll("元", "").trim();
        valueStr = valueStr.replace(",", "").trim();
        minusStr = minusStr.replaceAll("元", "").trim();
        minusStr = minusStr.replace(",", "").trim();
        BigDecimal value = new BigDecimal(valueStr);
        BigDecimal subtrahend = new BigDecimal(minusStr);
        return fnum.format(value.subtract(subtrahend));
    }

    /**
     * 金额相减
     *
     * @param value      基础值
     * @param subtrahend 减数
     * @return
     */
    public static BigDecimal moneySub(BigDecimal value, BigDecimal subtrahend) {
        return value.subtract(subtrahend);
    }


    /**
     * 金额相乘
     *
     * @param valueStr 基础值
     * @param mulStr   被乘数
     * @return
     */
    public static String moneyMul(String valueStr, String mulStr) {
        BigDecimal value = new BigDecimal(valueStr);
        BigDecimal mulValue = new BigDecimal(mulStr);
        return moveLast0(fnum.format(value.multiply(mulValue)));
    }


    public static BigDecimal moneyMulForBig(String valueStr, String mulStr) {
        BigDecimal value = new BigDecimal(valueStr);
        BigDecimal mulValue = new BigDecimal(mulStr);
        return value.multiply(mulValue);
    }

    /**
     * 金额相乘
     *
     * @param value    基础值
     * @param mulValue 被乘数
     * @return
     */
    public static BigDecimal moneyMul(BigDecimal value, BigDecimal mulValue) {
        return value.multiply(mulValue);
    }

    /**
     * 金额相除 <br/>
     * 精确小位小数
     *
     * @param valueStr  基础值
     * @param divideStr 被乘数
     * @return
     */
    public static String moneydiv(String valueStr, String divideStr) {
        BigDecimal value = new BigDecimal(valueStr);
        BigDecimal divideValue = new BigDecimal(divideStr);
        return moveLast0(fnum.format(value.divide(divideValue, 2, BigDecimal.ROUND_HALF_UP)));
    }

    /**
     * 金额相除 <br/>
     * 精确小位小数
     *
     * @param value       基础值
     * @param divideValue 被乘数
     * @return
     */
    public static BigDecimal moneydiv(BigDecimal value, BigDecimal divideValue) {
        return value.divide(divideValue, 2, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 值比较大小
     * <br/>如果valueStr大于等于compValueStr,则返回true,否则返回false
     * true 代表可用余额不足
     *
     * @param valueStr     (需要消费金额)
     * @param compValueStr (可使用金额)
     * @return
     */
    public static boolean moneyComp(String valueStr, String compValueStr) {
        BigDecimal value = new BigDecimal(valueStr);
        BigDecimal compValue = new BigDecimal(compValueStr);
        //0:等于    >0:大于    <0:小于
        int result = value.compareTo(compValue);
        if (result >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 值比较大小
     * <br/>如果valueStr大于等于compValueStr,则返回true,否则返回false
     * true 代表可用余额不足
     *
     * @param valueStr     (需要消费金额)
     * @param compValueStr (可使用金额)
     * @return
     */
    public static boolean moneyComp(BigDecimal valueStr, BigDecimal compValueStr) {
        //0:等于    >0:大于    <0:小于
        int result = valueStr.compareTo(compValueStr);
        if (result >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 金额乘以，省去小数点
     *
     * @param valueStr 基础值
     * @return
     */
    public static String moneyMulOfNotPoint(String valueStr, String divideStr) {
        BigDecimal value = new BigDecimal(valueStr);
        BigDecimal mulValue = new BigDecimal(divideStr);
        valueStr = fnum.format(value.multiply(mulValue));
        return fnum.format(value.multiply(mulValue)).substring(0, valueStr.length() - 3);
    }

    /**
     * 给金额加逗号切割
     *
     * @param str
     * @return
     */
    public static String addComma(String str) {
        try {
            String banNum = "";
            if (str.contains(".")) {
                String[] arr = str.split("\\.");
                if (arr.length == 2) {
                    str = arr[0];
                    banNum = "." + arr[1];
                }
            }
            // 将传进数字反转
            String reverseStr = new StringBuilder(str).reverse().toString();
            String strTemp = "";
            for (int i = 0; i < reverseStr.length(); i++) {
                if (i * 3 + 3 > reverseStr.length()) {
                    strTemp += reverseStr.substring(i * 3, reverseStr.length());
                    break;
                }
                strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
            }
            // 将[789,456,] 中最后一个[,]去除
            if (strTemp.endsWith(",")) {
                strTemp = strTemp.substring(0, strTemp.length() - 1);
            }
            // 将数字重新反转
            String resultStr = new StringBuilder(strTemp).reverse().toString();
            resultStr += banNum;
            return resultStr;
        } catch (Exception e) {
            return str;
        }

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


}
