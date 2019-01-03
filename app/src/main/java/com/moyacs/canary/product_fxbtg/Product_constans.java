package com.moyacs.canary.product_fxbtg;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：luoshen on 2018/3/21 0021 15:51
 * 邮箱：rsf411613593@gmail.com
 * 说明：
 */

public class Product_constans {


    public static String TRUDE_SOURCE_WH = "WH";//外汇
    public static String TRUDE_SOURCE_SZPEC = "SZPEC";//深石油
    public static String TRUDE_SOURCE_TJPME = "TJPME";//津贵所
    public static String TRUDE_SOURCE_IPE = "IPE";//布伦特原油
    public static String TRUDE_SOURCE_WGJS = "WGJS";//国际黄金
    public static String TRUDE_SOURCE_NYMEX = "NYMEX";//美国原油

    public static String TRUDE_SOURCE_WEIPAN = "weiPan";//广贵 weiPan
    public static String TRUDE_SOURCE_WEIPANP_HG = "HPME";//哈贵
    public static String TRUDE_SOURCE_WEIPANP_JN = "JCCE";//农交所
    public static String TRUDE_SOURCE_WEIPANP_HN = "HNCE";//华凝所
    public static String TRUDE_SOURCE_WEIPANP_FXBTG = "FXBTG";

    //非http://htmmarket.fx678.com 域名的就当作是特殊交易所数据处理  这里就当成是的
    public static List<String> specialSource = new ArrayList<String>();

    static {
        specialSource.add(TRUDE_SOURCE_WEIPAN);
        specialSource.add(TRUDE_SOURCE_WEIPANP_HG);
        specialSource.add(TRUDE_SOURCE_WEIPANP_JN);
        specialSource.add(TRUDE_SOURCE_WEIPANP_HN);
        specialSource.add(TRUDE_SOURCE_WEIPANP_FXBTG);

    }



    public static final String PARAM_CUSTOM = "custom";

    public static final String HOST_WEIPAN_MARKET = "https://q.8caopan.com/";//行情,如果测试环境没有，就使用正式的


    //品种的数据查询
    public static String URL_GET_PRODUCTS_WEIPAN = HOST_WEIPAN_MARKET + "/wp/quotation/v2/realTime?codes={codes}";
    //k线图
    public static String URL_GET_KLINE_WEIPAN = HOST_WEIPAN_MARKET + "/wp/quotation/v3/kChart?excode={excode}&code={code}&type={type}";//type=1

    //旧接口
    public static final String PARAM_KLINE_1M_WEIPAN = "10";//一分钟
    public static final String PARAM_KLINE_5M_WEIPAN = "2";
    public static final String PARAM_KLINE_15M_WEIPAN = "3";
    public static final String PARAM_KLINE_30M_WEIPAN = "4";
    public static final String PARAM_KLINE_60M_WEIPAN = "5";
    //    public static final String PARAM_KLINE_2H_WEIPAN = "";
    public static final String PARAM_KLINE_4H_WEIPAN = "9";
    public static final String PARAM_KLINE_WEEK_WEIPAN = "7";
    public static final String PARAM_KLINE_1D_WEIPAN = "6";
    //新接口对应的
//    1 5 15 30 60 240 1440 10080   43200
    public static final String PARAM_KLINE_1M_WEIPAN_new = "1";//一分钟
    public static final String PARAM_KLINE_5M_WEIPAN_new = "5";//五分钟
    public static final String PARAM_KLINE_15M_WEIPAN_new = "15";//十五分钟
    public static final String PARAM_KLINE_30M_WEIPAN_new = "30";//三十分钟
    public static final String PARAM_KLINE_60M_WEIPAN_new = "60";//一小时
    public static final String PARAM_KLINE_4H_WEIPAN_new = "240";//四小时
    public static final String PARAM_KLINE_1D_WEIPAN_new = "1440";//日线
    public static final String PARAM_KLINE_WEEK_WEIPAN_new = "10080";//周线
    public static final String PARAM_KLINE_MONTH_WEIPAN_new = "43200";//月线

    /*对应周期的时间间隔*/
    public static Map<String, Long> cycleTMap = new HashMap<>();

    static {
        cycleTMap.put(PARAM_KLINE_1M_WEIPAN_new, 1 * 60 * 1000L);
        cycleTMap.put(PARAM_KLINE_5M_WEIPAN_new, 5 * 60 * 1000L);
        cycleTMap.put(PARAM_KLINE_15M_WEIPAN_new, 15 * 60 * 1000L);
        cycleTMap.put(PARAM_KLINE_30M_WEIPAN_new, 30 * 60 * 1000L);
        cycleTMap.put(PARAM_KLINE_60M_WEIPAN_new, 60 * 60 * 1000L);
        cycleTMap.put(PARAM_KLINE_4H_WEIPAN_new, 4 * 60 * 1000L);
        cycleTMap.put(PARAM_KLINE_WEEK_WEIPAN_new, 7 * 24 * 60 * 1000L);
        cycleTMap.put(PARAM_KLINE_1D_WEIPAN_new, 24 * 60 * 60 * 1000L);
    }

    /*k线图参数对应
          日线=day
  周线=week
  5分钟=min5
  15分钟=min15
  30分钟=min30
  60分钟=min60
  2小时=hr2
  4小时=hr4*/
    public static final String PARAM_KLINE_TIME = "min1";//分时
    public static final String PARAM_KLINE_5M = "min5";
    public static final String PARAM_KLINE_15M = "min15";
    public static final String PARAM_KLINE_30M = "min30";
    public static final String PARAM_KLINE_60M = "min60";
    public static final String PARAM_KLINE_2H = "hr2";
    public static final String PARAM_KLINE_4H = "hr4";
    public static final String PARAM_KLINE_WEEK = "week";
    public static final String PARAM_KLINE_1D = "day";
    public static final String PARAM_KLINE_MONTH = "month";

    public static boolean isBakSource = true;//备用数据源

    public static final String PARAM_TOKEN_VALUE = "310242a4068f1cad5096aec31a774f6c";

    //品种的数据查询
    //http://htmmarket.fx678.com/custom.php?excode=custom&code=WGJS|XAP,WH|USDHKD,WH|EURJPY,WH|USDCAD,NYMEX|CONC&time=1429155426&token=310242a4068f1cad5096aec31a774f6c&key=4955f666347a6521ff0274dfc9bacc32
    public static String URL_GET_PRODUCTS = "http://htmmarket.fx678.com/custom.php?excode=custom&code={code}&time={time}&token={token}&key={key}";

    //交易所下所有品种的最新数据
    //http://htmmarket.fx678.com/list.php?excode=CCDPT&time=1429076077&token=310242a4068f1cad5096aec31a774f6c&key=ed284ef243ee3c6c02f85875842cf21f
    public static String URL_SOURCE_ALLPRODUCT = "http://htmmarket.fx678.com/list.php?excode={source}&time={time}&token={token}&key={key}";
    //接口
    //交易所下所有品种的最新数据  excode
    public static String URL_SOURCE_ALLPRODUCT_WEIPAN =HOST_WEIPAN_MARKET + "/wp/quotation/v2/realTime/excode/list?excode={excode}";//&time={time}&token={token}&key={key}
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

    public static String getUnixTimeParam(long time) {
        String timeStr = String.valueOf(time);
        int length = timeStr.length();
        if (length < 6) {
            return null;
        }
        return timeStr.substring(length - 6, length);
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
}
