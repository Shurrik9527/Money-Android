package com.moyacs.canary.kchart.chart.candle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.util.AttributeSet;

import com.moyacs.canary.kchart.chart.KBaseGraphView;
import com.moyacs.canary.kchart.chart.cross.KCrossLineView;
import com.moyacs.canary.kchart.util.KDisplayUtil;


/**
 * Created by fangzhu
 * k线的base类
 * 定义基本属性，设置颜色，字体，蜡烛图的大小
 */
public class KLineBaseView extends KBaseGraphView {
    //最高点最低点是否显示价格
    protected boolean valueText = true;
    //最高点最低点文字大小
    protected int valueTextSize = 10;
    //最高点最低点文字颜色
    protected int valueTextColor = Color.WHITE;
    //最高点最低点矩形填充颜色
    protected int valueTextFillColor = Color.parseColor("#848999");
    //两个蜡烛图之间的距离相对蜡烛图宽度的比例
    public final float CANDLE_RATE = 0.12f;
    //默认的蜡烛图宽度
    protected float DEFAULT_CANDLE_WIDTH = 20;
    // 最大允许的k线放大倍数
    static final float MAX_CANDLE_RATE = 3;
    // 最小允许的k线缩小倍数
    static final float MIN_CANDLE_RATE = 0.2F;

    //蜡烛图矩阵的宽度 这里的宽度 包括矩形的宽度 和 CANDLE_RATE两个蜡烛图之间的宽度
    protected float candleWidth = DEFAULT_CANDLE_WIDTH;
    //true表示X轴的文字位置在线条的内部
    protected boolean isAxisTitlein = false;
    //十字线控件 v1 这个版本暂时不用
    protected KCrossLineView crossLineView;
    //开盘最大值 最小值
    protected double maxValue, minValue;
    //当前屏幕中k线的最大值high ，K线的最小值low，用于确定，当前屏幕中的最高最低位置的K线
    protected double maxHigh, minLow;
    //成交量最大值，最小值为0
    protected double subMaxValue, subMinValue;
    //应该是画出线条数242－1，index 从0开始的
    protected int drawCount = 0;
    //tips参数指标的字体大小
    protected int tipsFontSize;
    //画图数据源的list索引 开始和结束
    protected int drawIndexStart, drawIndexEnd;

    //Y轴开始结束的值 价格最大和最小
    protected double startYdouble, stopYdouble;
    //成交量矩形宽度
    protected float volW = 2;

    //上方主图纬线文字的颜色
    protected int latitudeFontColorTop = DEFAULT_FONT_COLOR;
    //下方主图纬线文字的颜色
    protected int latitudeFontColorBottom = DEFAULT_FONT_COLOR;
    //昨收位置虚线效果
    protected PathEffect pathEffect = new DashPathEffect(new float[]{10, 10, 10, 10}, 1);
    //昨收位置虚线颜色
    protected int effectColor = DEFAULT_LINE_COLOR;
    //path的填充颜色
    protected int areaColor = Color.parseColor("#469cff");
    //path的填充颜色透明度
    protected int areaAlph = 70;
    //线条的填充颜色
    protected int lineColor = Color.BLUE;
    //手指按下的位置
    protected float touchX;
    //是否显示指标参数值
    protected boolean showTips;
    //指标tips 随手指移动，true:当手指在屏幕左边的时候，tips在右；当手指在屏幕右边的时候，tips在左；
    protected boolean tipsMove = false;
    //幅图出现负数的时候，是否中间线是0，让最大最小值对称
    protected boolean isSubLineZero = false;
    //屏幕中是否显示最大值和最小值
    protected boolean isMaxMinShowInScreen = true;
    //开始画k线的起点，从最左边开始画还是从最右边开始画
    protected int drawOrientation = 0;
    //开始画k线的起点，从最左边开始
    public static final int DRAW_ORIENTATION_LEFT = 0;
    //开始画k线的起点，从最右边开始
    public static final int DRAW_ORIENTATION_RIGHT = 1;

    public KLineBaseView(Context context) {
        super(context);
        initDefaultValue(context);
    }

    public KLineBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultValue(context);
    }

    public KLineBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultValue(context);
    }

    /**
     * 初始化默认值
     */
    public void initDefaultValue(Context context) {
        super.initDefaultValue(context);
        DEFAULT_CANDLE_WIDTH = KDisplayUtil.dip2px(context, 8);
        candleWidth = DEFAULT_CANDLE_WIDTH;
        tipsFontSize = KDisplayUtil.dip2px(context, 8);
        valueTextSize = KDisplayUtil.dip2px(context, 10);
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }


    public int getAreaColor() {
        return areaColor;
    }

    public void setAreaColor(int areaColor) {
        this.areaColor = areaColor;
    }

    public int getEffectColor() {
        return effectColor;
    }

    public void setEffectColor(int effectColor) {
        this.effectColor = effectColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getAreaAlph() {
        return areaAlph;
    }

    public void setAreaAlph(int areaAlph) {
        this.areaAlph = areaAlph;
    }

    public PathEffect getPathEffect() {
        return pathEffect;
    }

    public void setPathEffect(PathEffect pathEffect) {
        this.pathEffect = pathEffect;
    }


    public int getLatitudeFontColorTop() {
        return latitudeFontColorTop;
    }

    public void setLatitudeFontColorTop(int latitudeFontColorTop) {
        this.latitudeFontColorTop = latitudeFontColorTop;
    }

    public int getLatitudeFontColorBottom() {
        return latitudeFontColorBottom;
    }

    public void setLatitudeFontColorBottom(int latitudeFontColorBottom) {
        this.latitudeFontColorBottom = latitudeFontColorBottom;
    }


    public float getCandleWidth() {
        return candleWidth;
    }

    public void setCandleWidth(float candleWidth) {
        this.candleWidth = candleWidth;
        DEFAULT_CANDLE_WIDTH = candleWidth;
    }

    public boolean isAxisTitlein() {
        return isAxisTitlein;
    }

    public void setAxisTitlein(boolean axisTitlein) {
        isAxisTitlein = axisTitlein;
    }

    public boolean isShowTips() {
        return showTips;
    }

    public void setShowTips(boolean showTips) {
        this.showTips = showTips;
    }

    public boolean isTipsMove() {
        return tipsMove;
    }

    public void setTipsMove(boolean tipsMove) {
        this.tipsMove = tipsMove;
    }

    public float getTouchX() {
        return touchX;
    }

    public void setTouchX(float touchX) {
        this.touchX = touchX;
    }

    public int getTipsFontSize() {
        return tipsFontSize;
    }

    public void setTipsFontSize(int tipsFontSize) {
        this.tipsFontSize = tipsFontSize;
    }

    public int getDrawIndexStart() {
        return drawIndexStart;
    }

    public void setDrawIndexStart(int drawIndexStart) {
        this.drawIndexStart = drawIndexStart;
    }

    public int getDrawIndexEnd() {
        return drawIndexEnd;
    }

    public void setDrawIndexEnd(int drawIndexEnd) {
        this.drawIndexEnd = drawIndexEnd;
    }

    public double getStartYdouble() {
        return startYdouble;
    }

    public void setStartYdouble(double startYdouble) {
        this.startYdouble = startYdouble;
    }

    public double getStopYdouble() {
        return stopYdouble;
    }

    public void setStopYdouble(double stopYdouble) {
        this.stopYdouble = stopYdouble;
    }

    public float getVolW() {
        return volW;
    }

    public void setVolW(float volW) {
        this.volW = volW;
    }


    public int getValueTextFillColor() {
        return valueTextFillColor;
    }

    public void setValueTextFillColor(int valueTextFillColor) {
        this.valueTextFillColor = valueTextFillColor;
    }

    public int getValueTextColor() {
        return valueTextColor;
    }

    public void setValueTextColor(int valueTextColor) {
        this.valueTextColor = valueTextColor;
    }

    public int getValueTextSize() {
        return valueTextSize;
    }

    public void setValueTextSize(int valueTextSize) {
        this.valueTextSize = valueTextSize;
    }

    public boolean isValueText() {
        return valueText;
    }

    public void setValueText(boolean valueText) {
        this.valueText = valueText;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    public int getDrawOrientation() {
        return drawOrientation;
    }

    public void setDrawOrientation(int drawOrientation) {
        this.drawOrientation = drawOrientation;
    }

    public boolean isSubLineZero() {
        return isSubLineZero;
    }

    public void setSubLineZero(boolean subLineZero) {
        isSubLineZero = subLineZero;
    }

    public boolean isMaxMinShowInScreen() {
        return isMaxMinShowInScreen;
    }

    public void setMaxMinShowInScreen(boolean maxMinShowInScreen) {
        isMaxMinShowInScreen = maxMinShowInScreen;
    }
}
