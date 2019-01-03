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
}
