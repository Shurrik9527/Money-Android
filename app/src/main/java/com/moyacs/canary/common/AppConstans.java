package com.moyacs.canary.common;

import com.moyacs.canary.main.market.net.MarketDataBean;

import java.util.List;

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

    /**
     * 全局所有列表行情数据,首页获取所有行情列表之后赋值
     */
    public static List<MarketDataBean> marketDataBeanList = null;


}
