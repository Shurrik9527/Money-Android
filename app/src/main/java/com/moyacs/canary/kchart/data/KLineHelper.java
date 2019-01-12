package com.moyacs.canary.kchart.data;



import android.util.Log;

import com.moyacs.canary.util.StringUtil;
import com.moyacs.canary.kchart.entity.KCandleObj;
import com.moyacs.canary.product_fxbtg.Product_constans;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangzhu
 * 分时 k线数据集合
 * 返回KCandleObj
 */
public class KLineHelper {

    public static String TAG = "KLineHelper";

    /**
     * 获取周期的k线数据 时间降序排列
     * @param source
     * @param code
     * @param type
     * @return
     */
    public static List<KCandleObj> getKlineDes (String source, String code, String type) {
        List<KCandleObj> list = new ArrayList<KCandleObj>();
        try {
            String url = KlineDataUtils.setCommonParam4get(KlineDataUtils.URL_GET_KLINE, source);

            url = url.replace("{source}", source);
            url = url.replace("{code}", code);
            url = url.replace("{type}", type);
            return getKlineByUrl(url, type, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    /**
     * 获取周期的k线数据 时间升序排列
     * @param source
     * @param code
     * @param type
     * @return
     */
    public static List<KCandleObj> getKlineAsc (String source, String code, String type) {
        List<KCandleObj> list = new ArrayList<KCandleObj>();
        try {
            String url = KlineDataUtils.setCommonParam4get(KlineDataUtils.URL_GET_KLINE, source);

            url = url.replace("{source}", source);
            url = url.replace("{code}", code);
            url = url.replace("{type}", type);
            return getKlineByUrl(url, type, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



    /**
     * 非周期K线
     * @param url
     * @param asc  升序
     * @return
     */
    public static List<KCandleObj> getKlineByUrl (String url, String cycle, boolean asc) {
        List<KCandleObj> list = new ArrayList<KCandleObj>();
        try {
            String res = HttpClientHelper.getStringFromGet(null, url);
            JSONArray jsonArr = new JSONArray(res);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            if (Product_constans.PARAM_KLINE_1D.equals(cycle)
                    || Product_constans.PARAM_KLINE_WEEK.equals(cycle)
                    || Product_constans.PARAM_KLINE_MONTH.equals(cycle) ) {
                //周期大于日线级别了 时间格式化就不显示时分秒
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            double sum = 0;
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                KCandleObj entity = new KCandleObj();
                entity.setId(JSONObjectUtil.getString(jsonObj, "id", ""));
                entity.setHigh(JSONObjectUtil.getDouble(jsonObj, "High", 0));
                entity.setLow(JSONObjectUtil.getDouble(jsonObj, "Low", 0));
                entity.setOpen(JSONObjectUtil.getDouble(jsonObj, "Open", 0));
                entity.setClose(JSONObjectUtil.getDouble(jsonObj, "Close", 0));
                entity.setTimeLong(JSONObjectUtil.getLong(jsonObj, "UpdateTime", 0) * 1000);
                entity.setTime(sdf.format(entity.getTimeLong()));//2015-02-10 15:15

                //绘制均线使用
                sum += entity.getClose();
                entity.setNormValue(sum / (i + 1));

                if (asc)
                    list.add(entity);
                else
                    list.add(0, entity);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取周期的k线数据 时间降序排列
     * @param source
     * @param code
     * @param type
     * @return
     */
    public static List<KCandleObj> getKlineDes4Weipan (String source, String code, String type) {
        List<KCandleObj> list = new ArrayList<KCandleObj>();
        try {
            String url = Product_constans.URL_GET_KLINE_WEIPAN;
            url = url.replace("{excode}", source);
            url = url.replace("{code}", code);
            url = url.replace("{type}", type);
            return getKlineByUrl4Weipan(url, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取周期的k线数据 时间升序排列
     * @param source
     * @param code
     * @param type
     * @return
     */
    public static List<KCandleObj> getKlineAsc4Weipan (String source, String code, String type) {
        List<KCandleObj> list = new ArrayList<KCandleObj>();
        try {
            String url = Product_constans.URL_GET_KLINE_WEIPAN;
            url = url.replace("{excode}", source);
            url = url.replace("{code}", code);
            url = url.replace("{type}", type);
            return getKlineByUrl4Weipan(url, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }



    /**
     * 获取的周期K线
     * @param url
     * @param asc  升序
     * @return
     */
    public static List<KCandleObj> getKlineByUrl4Weipan (String url, boolean asc) {
        List<KCandleObj> list = new ArrayList<KCandleObj>();
        try {
            String res = HttpClientHelper.getStringFromGet(null, url);
            if (res == null || res.trim().length() == 0)
                return null;
            Log.v(TAG, "res=" + res);
            JSONObject jsonObject = new JSONObject(res);
            if (!jsonObject.has("data"))
                return list;
            JSONObject data = jsonObject.getJSONObject("data");
            if (!data.has("candle"))
                return list;
            if (data.get("candle") == null || StringUtil.isEmpty(data.get("candle").toString()))
                return list;

            JSONArray jsonArr = data.getJSONArray("candle");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                KCandleObj entity = new KCandleObj();
                entity.setId(JSONObjectUtil.getString(jsonObj, "id", ""));
                entity.setHigh(JSONObjectUtil.getDouble(jsonObj, "h", 0));
                entity.setLow(JSONObjectUtil.getDouble(jsonObj, "l", 0));
                entity.setOpen(JSONObjectUtil.getDouble(jsonObj, "o", 0));
                entity.setClose(JSONObjectUtil.getDouble(jsonObj, "c", 0));
                //设置long类型的time
                entity.setTimeLong(JSONObjectUtil.getLong(jsonObj, "u", 0) * 1000);
                //后台格式化好的数据
                entity.setTime(JSONObjectUtil.getString(jsonObj, "t", ""));
                if (entity.getOpen() == 0)
                    continue;
                if (entity.getHigh() == 0)
                    continue;
                if (entity.getLow() == 0)
                    continue;
                if (entity.getClose() == 0)
                    continue;

                if (asc)
                    list.add(entity);
                else
                    list.add(0, entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }
}
