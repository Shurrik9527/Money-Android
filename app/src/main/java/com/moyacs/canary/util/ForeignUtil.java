package com.moyacs.canary.util;

import android.text.TextUtils;

public class ForeignUtil {

    public static String formatForeignUtil(String foreignCode) {
        if (TextUtils.equals("XAUUSD", foreignCode)
                || TextUtils.equals("XAGUSD", foreignCode)) {
            return "盎司";
        } else if (TextUtils.equals("WTI", foreignCode)) {
            return "桶";
        } else if (TextUtils.equals("EURUSD", foreignCode)) {
            return "欧元";
        } else if (TextUtils.equals("GBPUSD", foreignCode)) {
            return "英镑";
        } else if (TextUtils.equals("USDCAD", foreignCode)
                || TextUtils.equals("USDJPY", foreignCode)
                || TextUtils.equals("USDCHF", foreignCode)
                || TextUtils.equals("NZDUSD", foreignCode)) {
            return "美元";
        } else if (TextUtils.equals("AUDUSD", foreignCode)) {
            return "澳元";
        } else if (TextUtils.equals("CAD", foreignCode)
                || TextUtils.equals("NID", foreignCode)) {
            return "千克";
        }
        return "";
    }

    /**
     * 通过code查询对应的中文名称
     *
     * @param code
     * @return 对应的中文名称
     */
    public static String codeFormatCN(String code) {
        if (code == null && TextUtils.isEmpty(code)) {
            return "";
        }
        if (TextUtils.equals(code, "AUDUSD")) {
            return "澳元/美元";
        } else if (TextUtils.equals(code, "USDCAD")) {
            return "美元/加拿大元";
        } else if (TextUtils.equals(code, "NZDUSD")) {
            return "新西兰/美元";
        } else if (TextUtils.equals(code, "USDCHF")) {
            return "美元/瑞士法郎";
        } else if (TextUtils.equals(code, "EURUSD")) {
            return "欧元/美元";
        } else if (TextUtils.equals(code, "GBPUSD")) {
            return "英镑/美元";
        } else if (TextUtils.equals(code, "USDJPY")) {
            return "美元/日元";
        } else if (TextUtils.equals(code, "XAUUSD")) {
            return "黄金";
        } else if (TextUtils.equals(code, "XAGUSD")) {
            return "白银";
        } else if (TextUtils.equals(code, "CAD")) {
            return "伦敦铜";
        } else if (TextUtils.equals(code, "NID")) {
            return "伦敦镍";
        } else if (TextUtils.equals(code, "WTI")) {
            return "美原油";
        } else {
            return "";
        }


    }
}
