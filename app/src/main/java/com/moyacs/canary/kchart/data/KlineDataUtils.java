package com.moyacs.canary.kchart.data;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者：luoshen on 2018/3/21 0021 17:14
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class KlineDataUtils {
    //k线图
    //http://htmmarket.fx678.com/kline.php?excode=SZPEC&code=QHO10S&type=min60&time=1429079043&token=310242a4068f1cad5096aec31a774f6c&key=f76a5fd395fae769151101d74fca7202
    public static String URL_GET_KLINE = "http://htmmarket.fx678.com/kline.php?excode={source}&code={code}&type={type}&time={time}&token={token}&key={key}";

    public static final String PARAM_TOKEN_VALUE = "310242a4068f1cad5096aec31a774f6c";
    /**
     * {time}&token={token}&key={key}
     *
     * @param url
     * @return
     */
    public static String setCommonParam4get(String url, String code) {
        url = url.replace("{time}", System.currentTimeMillis() / 1000 + "");
        url = url.replace("{token}", PARAM_TOKEN_VALUE);
        url = url.replace("{key}", getKey(code));
        return url;
    }
    public static String getKey(String code) {
        try {
            return hashParams(code + getUnixTimeParam(unixTimeExceptMilisecond(System.currentTimeMillis())) + PARAM_TOKEN_VALUE + "htm_key_market_2099");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PARAM_TOKEN_VALUE;//Exception value
    }
    /**
     * 计算key
     *
     * @param paramString
     * @return
     */
    public static String hashParams(String paramString) {
        StringBuilder localStringBuilder;
        try {
            byte[] arrayOfByte = MessageDigest.getInstance("MD5").digest(paramString.getBytes("UTF-8"));
            localStringBuilder = new StringBuilder(2 * arrayOfByte.length);
            int i = arrayOfByte.length;
            for (int j = 0; j < i; j++) {
                int k = arrayOfByte[j];
                if ((k & 0xFF) < 16)
                    localStringBuilder.append("0");
                localStringBuilder.append(Integer.toHexString(k & 0xFF));
            }
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            throw new RuntimeException("Huh, MD5 should be supported?", localNoSuchAlgorithmException);
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", localUnsupportedEncodingException);
        }
        return localStringBuilder.toString();
    }

    /**
     * unix 时间 去除毫秒
     *
     * @param unixTime
     * @return
     */
    public static long unixTimeExceptMilisecond(long unixTime) {
        return unixTime / 1000L;
    }

    public static String getUnixTimeParam(long time) {
        String timeStr = String.valueOf(time);
        int length = timeStr.length();
        if (length < 6) {
            return null;
        }
        return timeStr.substring(length - 6, length);
    }
}
