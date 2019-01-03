package com.moyacs.canary.kchart.data;


import android.util.Log;

import java.net.URLEncoder;
import java.util.Map;

/**
 * 作者：Created by ocean
 * 时间：on 17/1/12.
 */

public class NetWorkUtils {

    public static String TAG = NetWorkUtils.class.getSimpleName();
    /**
     * 生成get url 请求格式  拼接参数
     * @param api
     * @param paraMap
     * @return
     */
    public static String setParam4get(String api, Map<String, String> paraMap) {
        StringBuffer sb = new StringBuffer(api);
        int i = 0;
        if (paraMap != null && !paraMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paraMap.entrySet()) {
                String key = entry.getKey().toString();
                String value = "";
                try {
                    value = entry.getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (api.contains("?")) {
                    sb.append("&");
                } else {
                    if (i == 0)
                        sb.append("?");
                    else
                        sb.append("&");
                }
                try {
                    value = URLEncoder.encode(value, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sb.append(key + "=" + value);
                i++;
            }
        }
        return sb.toString();
    }

    /**
     * post参数拼接
     *
     * @param params
     * @return
     */
    public static StringBuffer getRequestData(Map<String, String> params) {
        // 存储封装好的请求体信息
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value == null)
                    value = "";
                stringBuffer.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(value, "utf-8"))
                        .append("&");
            }
            // 删除最后的一个"&"
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v(TAG, "param=" + stringBuffer.toString());
        return stringBuffer;
    }
}
