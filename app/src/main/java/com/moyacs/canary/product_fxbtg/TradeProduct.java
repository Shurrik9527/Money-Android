package com.moyacs.canary.product_fxbtg;

import android.content.Context;


import com.moyacs.canary.common.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by developer on 16/1/10.
 * 交易 的实体类
 */
public class TradeProduct extends TradeCommonObj {
    //type 购买方向：1＝跌 2＝涨
    //isJuan 使用代金劵: 1=使用0=不使用
    public static final int TYPE_BUY_DOWN = 1;
    public static final int TYPE_BUY_UP = 2;
    public static final String JUAN_ENABLE = "1";
    public static final String JUAN_DISENABLE = "0";

    //是否休市:1=正常，2=休市
    public static final String IS_CLOSE_YES = "2";
    public static final String IS_CLOSE_NO = "1";

    public static final String CODE_XAG = "XAG1";
    public static final String CODE_OIL = "OIL";
    public static final String CODE_HGNI = "HGNI";
    //是否持仓过夜(1-是,0-否)
    public static final String IS_DEFERRED_YES = "1";
    public static final String IS_DEFERRED_NO = "0";

    //农交所的code
    public static final String CODE_JN_AG = "AG";//银
    public static final String CODE_JN_CO = "CO";//油
    public static final String CODE_JN_KO = "KC";//咖啡
    public static final String CODE_JN_URP = "URP";//尿素

    /**
     * 建仓
     * userId	Long(数字)	用户id
     * productId	Integer (数字)	产品id
     * orderNumber	Integer (数字)	购买数量，1-50之内
     * type	Integer (数字)	购买方向：1＝跌 2＝涨
     * isJuan	Integer (数字)	使用代金劵: 1=使用0=不使用
     * stopProfit	Integer (数字)	止盈，值：10-90
     * stopLoss	Integer (数字)	止损，值：10-90
     */
    //参数常量定义  方便统一修改
    public static final String PARAM_UID = "userId";
    public static final String PARAM_ORDER_ID = "orderId";
    public static final String PARAM_EXCHANGE_ID = "exchangeId";
    public static final String PARAM_PID = "productId";
    public static final String PARAM_ORDER_NB = "orderNumber";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_ISJUAN = "isJuan";
    public static final String PARAM_STOPPROFIT = "stopProfit";
    public static final String PARAM_STOPLOSS = "stopLoss";
    public static final String PARAM_COUPONID = "couponId";

    //农交所红包
    public static final String PARAM_IS_DEFERRED = "deferred";


    /**
     * 代金券类型  id 和价格都是后台已经定好了的
     * 关系是在客户端定义好的，服务端改变没有用
     * 都用数字  方便计算
     */
    public static final Map<Integer, Double> couponIdMap = new HashMap<Integer, Double>();

    static {
        couponIdMap.put(1, 8D);
        couponIdMap.put(2, 80D);
        couponIdMap.put(3, 200D);
    }

    private String productId;//产品id
    private long quotationId;//对应得行情id
    private String name;
    private String weight;//产品重量
    private String price;

    private String sxf;//手续费
    private String unit;//重量单位
    private String contract;//产品对应得行情代码code
    private String buyRate;//买涨跌比列
    private String mp;//行情浮动比列
    private String sell;//实时行情价格
    private String margin;//行情浮动点数
    private String isClosed;//是否休市:1=正常，2=休市
    //代金劵类型id（1，2，3） id：1 （8元劵），id：（80元劵）id：200（200元的劵
    private int couponId;
    private int maxSl;//产品可以购买最大手数
    private String excode;//交易所

    //农交所
    private String deferred;
    //休市提示语
    private String closePrompt;

    //交易时间提示
    private String exchangeTimePrompt;

    public static String getNameByCode(Context context, String name, String code) {
        if (StringUtil.isEmpty(code))
            return name;

        return name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 广贵油
     * 广贵银
     *
     * @return
     */
    public String getNameByCode(Context context) {
        if (StringUtil.isEmpty(getContract()))
            return name;

        return name;
    }

    public String getExchangeTimePrompt() {
        return exchangeTimePrompt;
    }

    public void setExchangeTimePrompt(String exchangeTimePrompt) {
        this.exchangeTimePrompt = exchangeTimePrompt;
    }

    public String getDeferred() {
        return deferred;
    }

    public void setDeferred(String deferred) {
        this.deferred = deferred;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getSxf() {
        return sxf;
    }

    public void setSxf(String sxf) {
        this.sxf = sxf;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(String buyRate) {
        this.buyRate = buyRate;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getMaxSl() {
        return maxSl;
    }

    public void setMaxSl(int maxSl) {
        this.maxSl = maxSl;
    }





    public long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(long quotationId) {
        this.quotationId = quotationId;
    }

    public String getExcode() {
        return excode;
    }

    public void setExcode(String excode) {
        this.excode = excode;
    }

    public String getClosePrompt() {
        return closePrompt;
    }

    public void setClosePrompt(String closePrompt) {
        this.closePrompt = closePrompt;
    }


}
