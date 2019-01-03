package com.moyacs.canary.kchart.fragment;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * 作者：Created by ocean
 * 时间：on 2017/6/7.
 * 产品的精度配置
 * 原油-3，黄金-2，白银-3，美日-3，欧美-5，澳美-5，沪深-2，纳斯达克-2
 */

public class ProFormatConfig {
    public static HashMap<String, Integer> proFormatMap = new HashMap<>();

    static {
        proFormatMap.put("FXBTG|USOIL", 3);//美原油
        proFormatMap.put("FXBTG|XAUUSD", 2);//黄金
        proFormatMap.put("FXBTG|XAGUSD", 3);//白银
        proFormatMap.put("FXBTG|USDJPY", 3);//美元日元
        proFormatMap.put("FXBTG|EURUSD", 5);//欧元美元
        proFormatMap.put("FXBTG|AUDUSD", 5);//澳元美元
        proFormatMap.put("FXBTG|CNH300", 2);//沪深300指数
        proFormatMap.put("FXBTG|NAS100", 2);//纳斯达克指数
    }

    /**
     * 根据产品获取所需精度
     *
     * @param codes
     * @return
     */
    public static int getProFormatMap(String codes) {
        if (proFormatMap.containsKey(codes)) {
            return proFormatMap.get(codes);
        }
        return -1;
    }

    public static int getProPoint(String exCode, String code) {
        if (exCode == null
                || code == null)
            return -1;
        String str = exCode + "|" + code;
        if (proFormatMap.containsKey(str)) {
            return proFormatMap.get(str);
        }
        return -1;
    }


    /**
     * 根据code码格式化
     *
     * @param codes
     * @param d
     * @return
     */
    public static String formatByCodes(String codes, double d) {
        int formatPrecision = getProFormatMap(codes);

        if (formatPrecision != -1) {// 在产品列表之列
            String formatResult = format(d, formatPrecision);
            return formatResult;
        }
        return d + "";// 不在产品列表之列,去0返回
    }

    /**
     * 根据code码格式化
     *
     * @param codes
     * @param d
     * @return
     */
    public static String formatByCodes(String codes, String d) {
        if (TextUtils.isEmpty(d)) {
            return "";
        }
        int formatPrecision = getProFormatMap(codes);
        if (formatPrecision != -1) {// 在产品列表之列
            String formatResult = format(d, formatPrecision);
            return formatResult;
        }
        return d;// 不在产品列表之列,去0返回
    }

    /**
     * 格式化数据
     *
     * @param d
     * @param scale
     * @return
     */
    public static String format(String d, int scale) {
        try {
            BigDecimal big = new BigDecimal(d);
            double res = big.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
            return format(res, scale);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 格式化数据
     *
     * @param d
     * @param scale
     * @return
     */
    public static String format(double d, int scale) {
        //构造不能传double，double会丢失进度
        BigDecimal big = new BigDecimal(d + "");
        double res = big.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat df = getDecimalFormat(scale);
        return df.format(res);
    }

    /**
     * setScale(2);//表示保留2位小数，默认用四舍五入方式
     * setScale(2,BigDecimal.ROUND_DOWN);//直接删除多余的小数位  11.116约=11.11
     * setScale(2,BigDecimal.ROUND_UP);//临近位非零，则直接进位；临近位为零，不进位。11.114约=11.12
     * setScale(2,BigDecimal.ROUND_HALF_UP);//四舍五入 2.335约=2.33，2.3351约=2.34
     * setScaler(2,BigDecimal.ROUND_HALF_DOWN);//四舍五入；2.335约=2.33，2.3351约=2.34，11.117约11.12
     *
     * @param d
     * @param scale
     * @param round
     * @return
     */
    public static String format(double d, int scale, int round) {
        //构造不能传double，double会丢失进度
        BigDecimal big = new BigDecimal(d + "");
        double res = big.setScale(scale, round).doubleValue();
        DecimalFormat df = getDecimalFormat(scale);
        return df.format(res);
    }

    /**
     * 格式化数据DecimalFormat
     *
     * @param scale 保留的小数点位数 小数点不够补齐0
     * @return DecimalFormat
     */
    public static DecimalFormat getDecimalFormat(int scale) {
        //######0.00000"
        StringBuilder pattern = new StringBuilder("######0");
        if (scale > 0)
            pattern.append(".");
        for (int i = 0; i < scale; i++)
            pattern.append("0");
        return new DecimalFormat(pattern.toString());
    }

    public static void main(String[] args) {

    }

}
