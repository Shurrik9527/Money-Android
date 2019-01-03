package com.moyacs.canary.pay.tixian;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * 作者：luoshen on 2018/4/19 0019 17:09
 * 邮箱：rsf411613593@gmail.com
 * 说明：输入银行卡号的限制规则 工具类
 */

public class BankNumberUtils {
    /**
     * 是否是信用卡号
     * @param idCard
     * @return
     */
    public static boolean isCreditNumber(String idCard) {
        return !TextUtils.isEmpty(idCard) && idCard.matches("^\\d{16}$");
    }

    /**
     * 是否是银行卡号
     * @param bankNumber
     * @return
     */
    public static boolean isBankNumber(String bankNumber) {
        return !TextUtils.isEmpty(bankNumber) && (bankNumber.matches("^\\d{16}$") || bankNumber.matches("^\\d{19}$"));
    }

    /**
     * 去空
     * @param text
     * @return
     */
    public static String getTextTrim(EditText text) {
        return text.getText().toString().replaceAll(" ", "");
    }

    /**
     * 不去空
     * @param text
     * @return
     */
    public static String getText(EditText text) {
        return text.getText().toString();
    }

    /**
     * 每4位添加一个空格
     *
     * @param content
     * @return
     */
    public static String addSpeaceByCredit(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        content = content.replaceAll(" ", "");
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        StringBuilder newString = new StringBuilder();
        for (int i = 1; i <= content.length(); i++) {
            if (i % 4 == 0 && i != content.length()) {
                newString.append(content.charAt(i - 1) + " ");
            } else {
                newString.append(content.charAt(i - 1));
            }
        }
//        Log.i("mengyuan", "添加空格后："+newString.toString());
        return newString.toString();
    }
}
