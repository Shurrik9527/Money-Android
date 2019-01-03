package com.moyacs.canary.product_fxbtg;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.moyacs.canary.common.NumberUtils;
import com.moyacs.canary.common.StringUtil;
import com.moyacs.canary.kchart.entity.KCandleObj;


import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Optional implements Serializable {
    public static final String ISINITDATA = "isInitData";
    /*涨跌值保留的最长有效小数点位*/
    public static final int NUM_SCAL_RATE = 5;
    /*涨跌幅保留的百分比点位*/
    public static final int NUM_SCAL_RATE_CHANGE = 2;

    @DatabaseField(generatedId = true)
    private int localId;
    @DatabaseField
    private int drag;
    @DatabaseField
    private int top;
    @DatabaseField
    private boolean optional; //是否是自选股
    @DatabaseField
    private boolean isHostory; //
    @DatabaseField
    private String type; //  sg  tg
    @DatabaseField
    private String treaty;    //代码
    @DatabaseField
    private String title;       // 名称
    @DatabaseField
    private String date;
    @DatabaseField
    private String time;
    @DatabaseField
    private String opening;
    @DatabaseField
    private String highest;
    @DatabaseField
    private String lowest;
    @DatabaseField
    private String newest;
    @DatabaseField
    private String buyone;
    @DatabaseField
    private String sellone;
    @DatabaseField
    private String buyquantity;
    @DatabaseField
    private String sellquantity;
    @DatabaseField
    private String volume;
    @DatabaseField
    private String price;
    @DatabaseField
    private String position;
    @DatabaseField
    private String lastsettle;
    @DatabaseField
    private String closed;
    @DatabaseField
    private String add_time;
    @DatabaseField
    private String unit;
    @DatabaseField(defaultValue = "0.1f")
    private String ratio;

    //code在客户端显示的品种名称 code标示，treaty才是真实的代码值
    /**
     * type == source
     * treaty  == customCode
     * title == name
     */
    @DatabaseField
    private String customCode;

    @DatabaseField
    private int goodsid;

    @DatabaseField
    private String productCode;//code

    @DatabaseField
    private boolean isInitData;

    /*开盘时间 如09:00*/
    @DatabaseField
    private String baksourceStart;
    /*休盘时间 如15：00*/
    @DatabaseField
    private String baksourceEnd;
    /*中间休盘时间断 如11：30/13:00*/
    @DatabaseField
    private String baksourceMiddle;

    @DatabaseField
    private int tradeFlag;//`0=不能交易，1=能交易

    public static final int  TRADEFLAG_YES = 1;
    public static final int  TRADEFLAG_NO = 0;


    public Optional() {
    }

    public Optional(Goods goods) {
        this.type = goods.getSource();
        this.treaty = goods.getCustomCode();
        this.goodsid = goods.getGoodsId();
        this.title = goods.getName();
        this.productCode = goods.getCode();
        this.customCode = goods.getCustomCode();
        this.isInitData = false;
    }

    public Optional(FXBTGProductData fxbtgProductData){
        this.type = fxbtgProductData.getExcode();
        this.treaty = fxbtgProductData.getCode();
        this.productCode = fxbtgProductData.getCode();
        this.customCode = fxbtgProductData.getCode();
        this.title = fxbtgProductData.getName();
    }

    public int getTradeFlag() {
        return tradeFlag;
    }

    public void setTradeFlag(int tradeFlag) {
        this.tradeFlag = tradeFlag;
    }

    public String getCustomCode() {
        if (customCode == null || customCode.trim().length() == 0)
            return treaty;
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getDrag() {
        return drag;
    }

    public void setDrag(int drag) {
        this.drag = drag;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean isHostory() {
        return isHostory;
    }

    public void setHostory(boolean isHostory) {
        this.isHostory = isHostory;
    }

    public String getTreaty() {
        return treaty;
    }

    public void setTreaty(String treaty) {
        this.treaty = treaty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getHighest() {
        return highest;
    }

    public void setHighest(String highest) {
        this.highest = highest;
    }

    public String getLowest() {
        return lowest;
    }

    public void setLowest(String lowest) {
        this.lowest = lowest;
    }

    public String getNewest() {
        return newest;
    }

    public void setNewest(String newest) {
        this.newest = newest;
    }

    public String getBuyone() {
        return buyone;
    }

    public void setBuyone(String buyone) {
        this.buyone = buyone;
    }

    public String getSellone() {
        return sellone;
    }

    public void setSellone(String sellone) {
        this.sellone = sellone;
    }

    public String getBuyquantity() {
        return buyquantity;
    }

    public void setBuyquantity(String buyquantity) {
        this.buyquantity = buyquantity;
    }

    public String getSellquantity() {
        return sellquantity;
    }

    public void setSellquantity(String sellquantity) {
        this.sellquantity = sellquantity;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLastsettle() {
        return lastsettle;
    }

    public void setLastsettle(String lastsettle) {
        this.lastsettle = lastsettle;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getUnit() {
        if (unit == null || "".equals(unit)) {
            return "10";
        }
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isInitData() {
        return isInitData;
    }

    public void setInitData(boolean isInitData) {
        this.isInitData = isInitData;
    }

    public String getBaksourceStart() {
        return baksourceStart;
    }

    public void setBaksourceStart(String baksourceStart) {
        this.baksourceStart = baksourceStart;
    }

    public String getBaksourceEnd() {
        return baksourceEnd;
    }

    public void setBaksourceEnd(String baksourceEnd) {
        this.baksourceEnd = baksourceEnd;
    }

    public String getBaksourceMiddle() {
        return baksourceMiddle;
    }

    public void setBaksourceMiddle(String baksourceMiddle) {
        this.baksourceMiddle = baksourceMiddle;
    }

    /**
     * 获取产品 excode|code
     * @return
     */
    public String getCode(){
        return type + "|" + treaty;
    }

    /**
     * 封装算出 涨跌值
     * 使用BigDecimal相除 防止出现精度问题
     * @return
     */
    public double getRate() {
        double diff = 0;
        if (!StringUtil.isNull(getSellone())
                && !StringUtil.isNull(getClosed())) {
           try {
                diff = NumberUtils.subtract(Double.parseDouble(getSellone()), Double.parseDouble(getClosed()));
                BigDecimal bigDecimal = new BigDecimal(diff);
                return bigDecimal.setScale(NUM_SCAL_RATE,
                        BigDecimal.ROUND_HALF_UP).doubleValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return diff;
    }

    /**
     * 封装算出 涨跌幅
     * 使用BigDecimal相除 防止出现精度问题
     * @return x100之后的涨跌幅
     */
    public double getRateChange() {
        try {
            if (StringUtil.isNull(getClosed()))
                return 0;
            double rateChange = NumberUtils.divide(NumberUtils.multiply(getRate(), 100),
                    Double.parseDouble(getClosed()),4);
            BigDecimal bigDecimal = new BigDecimal(rateChange);
            return bigDecimal.setScale(NUM_SCAL_RATE_CHANGE,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将实时的行情转换成一个 k线对象
     *
     * @return
     */
    public KCandleObj obj2KCandleObj() {
        try {
            KCandleObj kCandleObj = new KCandleObj();
            kCandleObj.setOpen(Double.parseDouble(opening));
            kCandleObj.setHigh(Double.parseDouble(highest));
            kCandleObj.setLow(Double.parseDouble(lowest));
            kCandleObj.setClose(Double.parseDouble(sellone));
            //2016-10-14 17:21:50
            kCandleObj.setTime(time);
            Log.v("Optional", "time=" + time);
            kCandleObj.setTimeLong(parser(time, "yyyy-MM-dd HH:mm:ss").getTime());
            return kCandleObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public  Date parser(String strDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(strDate);
        } catch (Exception e) {
            return null;
        }
    }
}
