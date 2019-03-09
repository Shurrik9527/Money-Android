package com.moyacs.canary.common;

import com.moyacs.canary.bean.MarketDataBean;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 作者：luoshen on 2018/3/5 0005 17:48
 * 邮箱：rsf411613593@gmail.com
 * 说明：App 相关的 字段（网络请求除外）
 */

public class AppConstans {

    //全局 token
    public static final String token = "token";
    /**
     * intent 传递数据, MarketFragment 向 ProductDetailsActivity
     */
    //品种中文名称
    public static final String symbol_cn = "symbol_cn";
    //品种英文名称
    public static final String symbol = "symbol";
    //品种价格改变时间
    public static final String time = "time";
    //涨跌幅
    public static final String range = "range";
    //买价
    public static final String price_buy = "price_buy";
    //卖出价
    public static final String price_sell = "price_sell";
    //涨跌值
    public static final String value = "value";
    //ture 为红色， false 为绿色
    public static final String whatColor = "whatColor";
    //今开
    public static final String open = "open";
    //昨收
    public static final String close = "close";
    //最高
    public static final String high = "high";
    //最低
    public static final String low = "low";
    //买入价的小数点位数
    public static final String digit = "digit";
    //止损 止盈 点位
    public static final String stops_level = "stops_level";

    /**
     * ----------------------------------------------------------------------------------------
     * sp 相关字段
     */
    // 类型 DEMO 或者 live
    public static final String type = "type";
    //MT4 id
    public static final String mt4id = "mt4id";
    /**
     * 用户名（真实姓名）
     */
    public static final String fullName = "fullName";
    /**
     * 信息是否完善  1 未完善 2 完善部分 3 完善成功
     */
    public static final String infoFillStep = "infoFillStep";
    //sp 对象
    public static final String allMarket_en_cn = "allMarket_en_cn";
    /**
     * sp 对象，存储所有行情的中文名称
     */
    public static final String allMarket = "allMarket";

    public static final String USER_PHONE = "user_phone";


    /**
     * 挂单接口 type 类型
     * 买涨限价： BUY_LIMIT
     * 买涨止损： BUY_STOP
     * 买跌限价： SELL_LIMIT
     * 买跌止损： SELL_STOP
     */

    public static final String type_BUY_LIMIT = "BUY_LIMIT";
    public static final String type_BUY_STOP = "BUY_STOP";
    public static final String type_SELL_LIMIT = "SELL_LIMIT";
    public static final String type_SELL_STOP = "SELL_STOP";
    public static final String type_BUY = "BUY";
    public static final String type_SELL_ = "SELL";
    public static final String USER_NICK_NAME = "USER_NICK_NAME";

    /**
     * 全局所有列表行情数据,首页获取所有行情列表之后赋值
     */
    public static List<MarketDataBean> marketDataBeanList = null;
    //一分钟了解汇大师
    public static final String KNOW_APP="http://www.zhangstz.com/xsxt/zh-CN/index.html";
    //关于我们
    public static final String ABOUT_US = "http://www.zhangstz.com/aboutus/aboutus-zh-CN.html";

    public static RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public static int HOME_ADAPTER_ITEM_BANNER_TYPE =1;           // 轮播图
    public static int HOME_ADAPTER_ITEM_FRESH_RANK_TYPE =2;       //新手及盈利榜
    public static int HOME_ADAPTER_ITEM_SPECIES_TYPE =3;          //种类
    public static int HOME_ADAPTER_ITEM_CHANGE_TYPE =4;           //交易机会
    public static int HOME_ADAPTER_ITEM_DETAIL_TYPE =5;           //交易详情



    public static final String PAY_TYPE_WEIXING ="1";
    public static final String PAY_TYPE_ZHIFUBAO ="2";
    public static final String PAY_TYPE_KUAISHAN ="3";

}
