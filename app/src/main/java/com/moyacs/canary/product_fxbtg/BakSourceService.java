package com.moyacs.canary.product_fxbtg;

import android.content.Context;


import com.moyacs.canary.common.AppSetting;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.common.StringUtil;
import com.moyacs.canary.kchart.data.HttpClientHelper;
import com.moyacs.canary.kchart.data.JSONObjectUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fangzhu on 2015/4/17.
 */
public class BakSourceService {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final int INIT_SHOW_SIZE = 4;//默认最多显示15条

    /**
     * 根据交易所的type 获取当前交易所的品种
     * <p/>
     * 添加品种页面需要调用
     */
    public static List<Optional> getOptionalsByType(Context context, String realType, String localSource)
            throws Exception {
        try {
            if (realType == null)
                return null;

            String url = null;
            if (Product_constans.specialSource.contains(realType)) {
                url = Product_constans.URL_SOURCE_ALLPRODUCT_WEIPAN.replace("{excode}", realType);
            } else {
                url = Product_constans.setCommonParam4get(Product_constans.URL_SOURCE_ALLPRODUCT, realType);
                url = url.replace("{source}", realType);
            }


            String res = HttpClientHelper.getStringFromGet(context, url);
            List<Optional> optionals = null;
            if (Product_constans.specialSource.contains(realType)) {
                optionals = parseOptionals4Weipan(res);
                List<Optional> list = new ArrayList<>();

                //修改排序把油放在第一位
                if (optionals != null) {
                    for (Optional o: optionals) {
                        //行情过滤掉吉银 油 2017-02-13
                        //吉银
                        if (TradeConfig.code_jn.equals(realType)
                            && TradeProduct.CODE_JN_AG.equals(o.getProductCode())) {
                            continue;
                        }
                        //吉油
                        if (TradeConfig.code_jn.equals(realType)
                                && TradeProduct.CODE_JN_CO.equals(o.getProductCode())) {
                            continue;
                        }

                        if (TradeProduct.CODE_OIL.equals(o.getProductCode())) {
                            list.add(0, o);
                        } else
                            list.add(o);
                    }
                    optionals = list;
                }
            } else {
                optionals = parseOptionals(res, localSource);
            }
            if (optionals != null) {
                AppSetting.getInstance(context).setInitOptionalType(localSource, true);

            }
            return optionals;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 刷新行情
     *
     * @param context
     * @param list
     * @return
     */
    public static List<Optional> getOptionals(Context context, List<Optional> list) {
        try {
            if (list == null)
                return null;
            StringBuffer stringBuffer = new StringBuffer();

            StringBuffer specialStringBuffer = new StringBuffer();//非fx678接口的刷新


            //code=WGJS|XAP,WH|USDHKD
            for (int i = 0; i < list.size(); i++) {
                Optional o = list.get(i);
                String source = o.getType();
                if (source != null && Product_constans.specialSource.contains(source)) {
                    if (i != list.size() - 1)
                        specialStringBuffer.append(o.getType() + "|" + o.getProductCode() + ",");
                    else
                        specialStringBuffer.append(o.getType() + "|" + o.getProductCode());
                } else {
                    if (i != list.size() - 1)
                        stringBuffer.append(o.getType() + "|" + o.getProductCode() + ",");
                    else
                        stringBuffer.append(o.getType() + "|" + o.getProductCode());
                }

            }
            String ids = stringBuffer.toString();
            if (ids.endsWith(",")) {
                ids = ids.substring(0, ids.length() - 1);
            }
            List<Optional> optionals = new ArrayList<>();
            if (ids != null && ids.trim().length() > 0) {
                String url = Product_constans.setCommonParam4get(Product_constans.URL_GET_PRODUCTS, Product_constans.PARAM_CUSTOM);
                url = url.replace("{code}", ids);

                String res = HttpClientHelper.getStringFromGet(context, url);
                optionals = parseOptionals(res, "");
                if (optionals != null) {

                }
            }


            //刷新特殊交易所下的数据
            String specialIds = specialStringBuffer.toString();
            if (specialIds != null && specialIds.trim().length() > 0) {
                if (specialIds.endsWith(",")) {
                    specialIds = specialIds.substring(0, specialIds.length() - 1);
                }

                String specialUrl = Product_constans.URL_GET_PRODUCTS_WEIPAN;
                specialUrl = specialUrl.replace("{codes}", specialIds);
                String specialRes = HttpClientHelper.getStringFromGet(context, specialUrl);
                List<Optional> specialOptionals = parseOptionals4Weipan(specialRes);
                if (specialOptionals != null) {

                }
                optionals.addAll(specialOptionals);//每次都是在本地再去取得数据，可以不用管顺序
            }
            return optionals;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 单个刷新
     *
     * @param context
     * @param optional
     * @return
     */
    public static Optional getOptionals(Context context, Optional optional) {
        if (Product_constans.specialSource.contains(optional.getType())) {
            try {
                String specialUrl = Product_constans.URL_GET_PRODUCTS_WEIPAN;
                //   获取单个品种详情的 url ：     https://q.8caopan.com//wp/quotation/v2/realTime?codes=FXBTG|USOIL
                specialUrl = specialUrl.replace("{codes}", optional.getType() + "|"+optional.getProductCode());
                // //请求完毕之后返回的数据源
                String specialRes = HttpClientHelper.getStringFromGet(context, specialUrl);
                if (specialRes == null)
                    return null;
                JSONObject resJson = new JSONObject(specialRes);
                if (resJson.has("data")) {
                    JSONObject data = resJson.getJSONObject("data");
                    if (data.has("list")) {
                        //只有一个
                        JSONArray list = data.getJSONArray("list");
                        // 将 jsonObject 转化为 optional 对象
                        return parseWeipan(list.getJSONObject(0));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            List<Optional> list = new ArrayList<Optional>();
            list.add(optional);
            list = getOptionals(context, list);
            if (list != null && list.size() > 0) {
                if (list.size() == 1) {
                    Optional op = list.get(0);

                    return op;
                }
            }
        }
        return null;
    }

    private static List<Optional> parseOptionals(String res, String source) {
        try {
            if (res == null)
                return null;
            List<Optional> optionals = new ArrayList<Optional>();
            JSONArray jsonArray = new JSONArray(res);
            if (jsonArray != null && jsonArray.length() > 0) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Optional optional = new Optional();
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (object.has("Name")) {
                        String name = object.getString("Name");
                        if (name != null)
                            name = name.replace("[T]", "");
                        if (name != null && source != null && source.equalsIgnoreCase(Product_constans.TRUDE_SOURCE_TJPME)) {
                            Pattern pattern = Pattern.compile("\\d");
                            Matcher matcher = pattern.matcher(name);
                            if (matcher.find())
                                continue;
                        }
                        optional.setTitle(name);
                    }
                    try {
                        String s = sf.format(new Date(JSONObjectUtil.getLong(object, "QuoteTime", 0) * 1000));
                        optional.setTime(s);
                        optional.setAdd_time(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (object.has("Code"))
                        optional.setProductCode(object.getString("Code"));
                    if (object.has("Code"))
                        optional.setCustomCode(object.getString("Code"));

                    if (object.has("Open")) {
                        optional.setOpening(NumberUtils.moveLast0(NumberUtils.beautifulDouble(object.getDouble("Open"),5)));
                    }
                    if (object.has("High")) {
                        optional.setHighest(NumberUtils.moveLast0(NumberUtils.beautifulDouble(object.getDouble("High"),5)));
                    }
                    if (object.has("Low")) {
                        optional.setLowest(NumberUtils.moveLast0(NumberUtils.beautifulDouble(object.getDouble("Low"),5)));
                    }
                    double currentPrice = 0;
                    if (object.has("Last")) {
                        currentPrice = object.getDouble("Last");
                        optional.setNewest(currentPrice + "");
                    }
                    if (object.has("LastClose")) {
                        optional.setClosed(NumberUtils.moveLast0(NumberUtils.beautifulDouble(object.getDouble("LastClose"),5)));
                    }
                    //代码里头是把sellone 当成是最新价格newest的
                    if (object.has("Last")) {
                        optional.setSellone(NumberUtils.moveLast0(NumberUtils.beautifulDouble(object.getDouble("Last"),5)));
                    } else {
                        optional.setSellone("0");
                    }

                    if (object.has("Buy")) {
                        optional.setBuyone(NumberUtils.beautifulDouble(object.getDouble("Buy")));
                    } else {
                        optional.setBuyone("0");
                    }


                    optional.setTreaty(optional.getCustomCode());//if need

                    //just while isbaksource is true for currentMinView---start
                    if (object.has("End"))
                        optional.setBaksourceEnd(object.getString("End"));
                    if (object.has("Start"))
                        optional.setBaksourceStart(object.getString("Start"));
                    if (object.has("Middle"))
                        optional.setBaksourceMiddle(object.getString("Middle"));
                    //just while isbaksource is true for currentMinView---end


                    //处理停盘 sellone buyone 为0的时候 使用最新数据
                    try {
                        if (Double.parseDouble(optional.getSellone()) <= 0
                                && Double.parseDouble(optional.getBuyone()) <= 0
                                && optional.getNewest() != null) {
                            optional.setBuyone(optional.getNewest());
                            optional.setSellone(optional.getNewest());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!StringUtil.isEmpty(source))
                        optional.setType(source);//
                    optional.setInitData(true);//标记当前goods已经初始化了

                    //处理特殊需要修改的品种  美原油连续--->原油指数
                    if ("CONC".equals(optional.getProductCode())) {
                        optional.setTitle("原油指数");
                    }

                    optionals.add(optional);
                }
            }
            return optionals;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 这里解析交易所的数据和详情的数据使用同一个
     * 注意 详情页返回的数据有多余的
     *
     * @param res
     * @return
     */
    private static List<Optional> parseOptionals4Weipan(String res) {
        try {
            if (res == null)
                return null;
            List<Optional> optionals = new ArrayList<Optional>();
            JSONObject jsonObject = new JSONObject(res);
            if (!jsonObject.has("data")) {
                return optionals;
            }
            JSONObject data = jsonObject.getJSONObject("data");
            if (data == null)
                return optionals;
            if (!data.has("list") && !data.has("candle"))
                return optionals;
            JSONArray jsonArray = null;
            if (data.has("list")) {
                jsonArray = data.getJSONArray("list");
            } else if (data.has("candle")) {
                jsonArray = data.getJSONArray("candle");
            }

            if (jsonArray != null && jsonArray.length() > 0) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    Optional optional = parseWeipan(object);
                    if (optional == null)
                        continue;
                    optionals.add(optional);
                }
            }
            return optionals;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 JSONObject 解析为 Optional
     * @param object
     * @return
     */
    public static Optional parseWeipan(JSONObject object) {
        try {
            Optional optional = new Optional();

            if (object.has("name")) {
                optional.setTitle(JSONObjectUtil.getString(object, "name", ""));
            }
            if (object.has("excode")) {
                optional.setType(JSONObjectUtil.getString(object, "excode", ""));
            }

            try {
                String s = JSONObjectUtil.getString(object, "time", "");
                optional.setTime(s);
                optional.setAdd_time(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (object.has("open")) {
                optional.setOpening(JSONObjectUtil.getString(object, "open", "0"));
            }
            if (object.has("top")) {
                optional.setHighest(JSONObjectUtil.getString(object, "top", "0"));
            }
            if (object.has("low")) {
                optional.setLowest(JSONObjectUtil.getString(object, "low", "0"));
            }

            //Last 没有
//                    double currentPrice = 0;
//                    if (object.has("Last")) {
//                        currentPrice = object.getDouble("Last");
//                        optional.setNewest(currentPrice + "");
//                    }
//                    //LastClose 昨收没有拿今开是每天强行平仓的。
            if (object.has("last_close"))
                optional.setClosed(JSONObjectUtil.getString(object, "last_close", "0"));
//            if(!StringUtils.isEmpty(optional.getOpening())){
//                optional.setClosed(optional.getOpening());
//            }
            if (object.has("sell")) {
                optional.setSellone(JSONObjectUtil.getString(object, "sell", "0"));
            } else {
                optional.setSellone("0");
            }
            if (object.has("buy")) {
                optional.setBuyone(JSONObjectUtil.getString(object, "buy", "0"));
            } else {
                optional.setBuyone("0");
            }
//                                    if (object.has("id"))
//                                        optional.setGoodsid(object.getInt("id"));
            //code may be null,if code is null use name
            String code = NVL(JSONObjectUtil.getString(object, "code", ""), optional.getTitle());
            optional.setProductCode(code);
            optional.setCustomCode(code);

//            if (object.has("id"))
//                optional.setGoodsid(object.getInt("id"));

            optional.setTreaty(optional.getCustomCode());//if need

            //  没有 just while isbaksource is true for currentMinView---start
            if (object.has("End"))
                optional.setBaksourceEnd(object.getString("End"));
            if (object.has("Start"))
                optional.setBaksourceStart(object.getString("Start"));
            if (object.has("Middle"))
                optional.setBaksourceMiddle(object.getString("Middle"));
            //just while isbaksource is true for currentMinView---end


            optional.setInitData(true);//标记当前goods已经初始化了
            return optional;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String NVL(Object obj, String value) {
        try {
            if (obj == null) {
                return value;
            }
            if (String.valueOf(obj).trim().equals("")) {
                return value;
            }
            if (String.valueOf(obj).trim().equalsIgnoreCase("null")) {
                return value;
            }
            return String.valueOf(obj);
        } catch (Exception e) {
            return value;
        }
    }
}
