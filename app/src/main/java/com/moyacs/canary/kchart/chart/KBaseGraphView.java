package com.moyacs.canary.kchart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import com.moyacs.canary.kchart.listener.OnKChartClickListener;
import com.moyacs.canary.kchart.listener.OnKLineTouchDisableListener;
import com.moyacs.canary.kchart.util.KDisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分时图和k线图的base类
 * 做一些默认值的处理 get set 方法
 * 不做画图处理
 */
public class KBaseGraphView extends View {
    protected boolean touchEnable = true;
    //主图占的比例 真实高度 －axisYtopHeight－axisYmiddleHeight
    protected float mainF = 4F / 5F;
    //附图成交量占的比例
    protected float subF = 1F / 5F;
    //是否需要显示附图
    protected boolean showSubChart = true;
    //默认价格小数点位数
    protected int numberScal = 2;
    //默认字体的大小 initDefaultValue()方法里头初始化
    public static int DEFAULT_FONT_SIZE;
    //默认字体的颜色
    public static final int DEFAULT_FONT_COLOR = Color.WHITE;
    //默认线条的颜色
    public static final int DEFAULT_LINE_COLOR = Color.parseColor("#33848999");//20%透明
    //十字线的颜色
    protected int crossLineColor = Color.WHITE;
    //默认边框线条的宽度
    protected float borderStrokeWidth = 1F;
    //指标线的宽度
    protected float normalLineStrokeWidth = 1F;
    //蜡烛图边框的宽度
    protected float candleStrokeWidth = 2F;
    //十字线的宽度
    protected float crossStrokeWidth = 2F;
    //set the size and color for draw text
    //经线竖线方向的文字大小
    protected int longitudeFontSize = DEFAULT_FONT_SIZE;
    //纬线横线方向的文字大小
    protected int latitudeFontSize = DEFAULT_FONT_SIZE;
    //十字线经线的文字大小的文字大小
    protected int crossLongitudeFontSize = DEFAULT_FONT_SIZE;
    //十字线字体的颜色
    protected int crossFontColor = Color.parseColor("#20222E");

    //用于标记是否显示十字线
    protected boolean showCross;
    //外面调用是否显示十字线
    protected boolean isCrossEnable = true;
    //十字线纬线的文字大小
    protected int crossLatitudeFontSize = DEFAULT_FONT_SIZE;
    //经线的文字颜色 时间的颜色
    protected int longitudeFontColor = Color.parseColor("#848999");
    //纬线的文字颜色 价格颜色
    protected int latitudeFontColor = Color.WHITE;
    //经线的线条颜色
    protected int longitudeColor = DEFAULT_LINE_COLOR;
    //经线的线条颜色
    protected int latitudeColor = DEFAULT_LINE_COLOR;
    //填充的矩型颜色 十字线出现的时候显示时间的巨型框
    protected int rectFillColor = Color.WHITE;
    /**
     * 阳线的填充颜色
     */
    protected int candlePostColor = Color.parseColor("#F15758");

    /**
     * 阴线的填充颜色
     */
    protected int candleNegaColor = Color.parseColor("#46BD5C");

    /**
     * 十字线显示颜色（十字星,一字平线,T形线的情况）
     */
    protected int candleCrossColor = Color.parseColor("#848999");
    //set the padding for draw text
    //padding留白
    protected float commonPadding = 5;
    //画图内容距离左边的距离
    protected float axisXleftWidth = commonPadding;
    //画图内容距离右边的距离
    protected float axisXrightWidth = commonPadding;
    //画图内容距离上边的距离 initDefaultValue()方法里头初始化
    protected float axisYtopHeight;
    //画图内容主图和附图的距离 initDefaultValue()方法里头初始化
    protected float axisYmiddleHeight;
    //画图内容距离下边的距离 initDefaultValue()方法里头初始化
    protected float axisYbottomHeight;
    //经度线条数 非格子间距  暂时没用到
    protected int longitudeLineNumber = 5;
    //纬度线条数 非格子间距
    protected int latitudeLineNumber = 5;
    //成交量经度线条数
    protected int subLongitudeLineNumber = 2;
    //成交量纬度线条数
    protected int subLatitudeLineNumber = 3;
    //时间titles
    protected List<String> axisXtitles = new ArrayList<String>();
    //y轴左边的titles开盘价(不包含成交量)
    protected List<String> axisYleftTitles = new ArrayList<String>();
    //y轴右边的百分比titles
    protected List<String> axisYrightTitles = new ArrayList<String>();
    //touch模式
    public static final int TOUCH_NONE = 0;
    public static final int TOUCH_ZOOM = 1;
    public static final int TOUCH_DOWN = 2;
    public static final int TOUCH_DRAG = 3;

    //缩放与滑动允许的最小距离
    protected float MINDIS = 10F;
    //禁止滑动的时候 处理别的逻辑使用
    protected OnKLineTouchDisableListener onKLineTouchDisableListener;
    //点击监听
    protected OnKChartClickListener onKChartClickListener;

    /**
     * 去掉锯齿
     *
     * @return
     */
    public Paint getPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);//去锯齿
        return paint;
    }

    /**
     * 纬线方向 X轴的文字Paint
     *
     * @return
     */
    public Paint getTextPaintX() {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(latitudeFontColor);
        textPaint.setTextSize(latitudeFontSize);
        return textPaint;
    }

    /**
     * 经线方向 Y轴的文字Paint
     *
     * @return
     */
    public Paint getTextPaintY() {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(longitudeFontColor);
        textPaint.setTextSize(longitudeFontSize);
        return textPaint;
    }

    public KBaseGraphView(Context context) {
        super(context);
        initDefaultValue(context);
    }

    public KBaseGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultValue(context);
    }

    public KBaseGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultValue(context);
    }

    /**
     * 初始化默认值
     */
    protected void initDefaultValue(Context context) {
        DEFAULT_FONT_SIZE = KDisplayUtil.dip2px(context, 12);
        //axisX time
        longitudeFontSize = KDisplayUtil.dip2px(context, 12);
        //axisY price
        latitudeFontSize = KDisplayUtil.dip2px(context, 10);
        //time
        crossLongitudeFontSize = KDisplayUtil.dip2px(context, 10);
        //price
        crossLatitudeFontSize = KDisplayUtil.dip2px(context, 10);

//        borderStrokeWidth = KDisplayUtil.dip2px(context, 1);
////        candleStrokeWidth = KDisplayUtil.dip2px(context, 0.5F);
//        normalLineStrokeWidth = KDisplayUtil.dip2px(context, 0.5F);
//        //crossLine width,axisX axisY
//        crossStrokeWidth = KDisplayUtil.dip2px(context, 1);

        if (normalLineStrokeWidth < 1)
            normalLineStrokeWidth = 1;

        axisYtopHeight = KDisplayUtil.dip2px(context, 15);
        axisYmiddleHeight = KDisplayUtil.dip2px(context, 18);
        axisYbottomHeight = KDisplayUtil.dip2px(context, 5);

    }

    /**
     * 矩形框中居中显示
     *
     * @param canvas
     * @param string
     * @param targetRect
     * @param textPaint
     */
    public void drawText(Canvas canvas, String string, Rect targetRect, Paint textPaint) {
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
//        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(string, targetRect.centerX(), baseline, textPaint);
    }

    public void drawText(Canvas canvas, String string, RectF targetRect, Paint textPaint) {
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = ((int) targetRect.bottom + (int) targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
//        int baseline = (int)targetRect.top + (int)(targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(string, targetRect.centerX(), baseline, textPaint);
    }

    public List<String> getAxisXtitles() {
        return axisXtitles;
    }

    public void setAxisXtitles(List<String> axisXtitles) {
        this.axisXtitles = axisXtitles;
    }

    public List<String> getAxisYleftTitles() {
        return axisYleftTitles;
    }

    public void setAxisYleftTitles(List<String> axisYleftTitles) {
        this.axisYleftTitles = axisYleftTitles;
    }

    public List<String> getAxisYrightTitles() {
        return axisYrightTitles;
    }

    public void setAxisYrightTitles(List<String> axisYrightTitles) {
        this.axisYrightTitles = axisYrightTitles;
    }

    protected float getDataWidth() {
        return super.getWidth() - axisXleftWidth - axisXrightWidth;
    }

    /**
     * 主图上方图表的高度
     *
     * @return
     */
    protected float getDataHeightMian() {
        return super.getHeight() * mainF - axisYtopHeight - axisYmiddleHeight;
    }

    /**
     * 成交量图表的高度
     *
     * @return
     */
    protected float getDataHeightSub() {
        return super.getHeight() * subF - axisYbottomHeight;
    }

    public float getCommonPadding() {
        return commonPadding;
    }

    public void setCommonPadding(float commonPadding) {
        this.commonPadding = commonPadding;
    }

    public float getStrokeWidth() {
        return borderStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.borderStrokeWidth = strokeWidth;
    }

    public int getLongitudeFontSize() {
        return longitudeFontSize;
    }

    public void setLongitudeFontSize(int longitudeFontSize) {
        this.longitudeFontSize = longitudeFontSize;
    }

    public int getLatitudeFontSize() {
        return latitudeFontSize;
    }

    public void setLatitudeFontSize(int latitudeFontSize) {
        this.latitudeFontSize = latitudeFontSize;
    }

    public int getLongitudeFontColor() {
        return longitudeFontColor;
    }

    public void setLongitudeFontColor(int longitudeFontColor) {
        this.longitudeFontColor = longitudeFontColor;
    }

    public int getLatitudeFontColor() {
        return latitudeFontColor;
    }

    public void setLatitudeFontColor(int latitudeFontColor) {
        this.latitudeFontColor = latitudeFontColor;
    }

    public int getLatitudeColor() {
        return latitudeColor;
    }

    public void setLatitudeColor(int latitudeColor) {
        this.latitudeColor = latitudeColor;
    }

    public int getLongitudeColor() {
        return longitudeColor;
    }

    public void setLongitudeColor(int longitudeColor) {
        this.longitudeColor = longitudeColor;
    }

    public float getAxisXleftWidth() {
        return axisXleftWidth;
    }

    public void setAxisXleftWidth(float axisXleftWidth) {
        this.axisXleftWidth = axisXleftWidth;
    }

    public float getAxisXrightWidth() {
        return axisXrightWidth;
    }

    public void setAxisXrightWidth(float axisXrightWidth) {
        this.axisXrightWidth = axisXrightWidth;
    }

    public float getAxisYtopHeight() {
        return axisYtopHeight;
    }

    public void setAxisYtopHeight(float axisYtopHeight) {
        this.axisYtopHeight = axisYtopHeight;
    }

    public float getAxisYbottomHeight() {
        return axisYbottomHeight;
    }

    public void setAxisYbottomHeight(float axisYbottomHeight) {
        this.axisYbottomHeight = axisYbottomHeight;
    }

    public void setAxisYbottomHeight(int axisYbottomHeight) {
        this.axisYbottomHeight = axisYbottomHeight;
    }

    public int getLongitudeLineNumber() {
        return longitudeLineNumber;
    }

    public void setLongitudeLineNumber(int longitudeLineNumber) {
        this.longitudeLineNumber = longitudeLineNumber;
    }

    public int getLatitudeLineNumber() {
        return latitudeLineNumber;
    }

    public void setLatitudeLineNumber(int latitudeLineNumber) {
        this.latitudeLineNumber = latitudeLineNumber;
    }

    public boolean isTouchEnable() {
        return touchEnable;
    }

    public void setTouchEnable(boolean touchEnable) {
        this.touchEnable = touchEnable;
    }

    public float getMainF() {
        return mainF;
    }

    public void setMainF(float mainF) {
        this.mainF = mainF;
    }

    public float getSubF() {
        return subF;
    }

    public void setSubF(float subF) {
        this.subF = subF;
    }

    public float getAxisYmiddleHeight() {
        return axisYmiddleHeight;
    }

    public void setAxisYmiddleHeight(float axisYmiddleHeight) {
        this.axisYmiddleHeight = axisYmiddleHeight;
    }

    public int getSubLongitudeLineNumber() {
        return subLongitudeLineNumber;
    }

    public void setSubLongitudeLineNumber(int subLongitudeLineNumber) {
        this.subLongitudeLineNumber = subLongitudeLineNumber;
    }

    public int getSubLatitudeLineNumber() {
        return subLatitudeLineNumber;
    }

    public void setSubLatitudeLineNumber(int subLatitudeLineNumber) {
        this.subLatitudeLineNumber = subLatitudeLineNumber;
    }

    public int getCrossLatitudeFontSize() {
        return crossLatitudeFontSize;
    }

    public void setCrossLatitudeFontSize(int crossLatitudeFontSize) {
        this.crossLatitudeFontSize = crossLatitudeFontSize;
    }

    public float getNormalLineStrokeWidth() {
        return normalLineStrokeWidth;
    }

    public void setNormalLineStrokeWidth(float normalLineStrokeWidth) {
        this.normalLineStrokeWidth = normalLineStrokeWidth;
    }

    public float getCrossStrokeWidth() {
        return crossStrokeWidth;
    }

    public void setCrossStrokeWidth(float crossStrokeWidth) {
        this.crossStrokeWidth = crossStrokeWidth;
    }

    public int getCrossLongitudeFontSize() {
        return crossLongitudeFontSize;
    }

    public void setCrossLongitudeFontSize(int crossLongitudeFontSize) {
        this.crossLongitudeFontSize = crossLongitudeFontSize;
    }

    public float getBorderStrokeWidth() {
        return borderStrokeWidth;
    }

    public void setBorderStrokeWidth(float borderStrokeWidth) {
        this.borderStrokeWidth = borderStrokeWidth;
    }

    public int getCandlePostColor() {
        return candlePostColor;
    }

    public void setCandlePostColor(int candlePostColor) {
        this.candlePostColor = candlePostColor;
    }

    public int getCandleNegaColor() {
        return candleNegaColor;
    }

    public void setCandleNegaColor(int candleNegaColor) {
        this.candleNegaColor = candleNegaColor;
    }

    public int getCandleCrossColor() {
        return candleCrossColor;
    }

    public void setCandleCrossColor(int candleCrossColor) {
        this.candleCrossColor = candleCrossColor;
    }

    public float getCandleStrokeWidth() {
        return candleStrokeWidth;
    }

    public void setCandleStrokeWidth(float candleStrokeWidth) {
        this.candleStrokeWidth = candleStrokeWidth;
    }

    public boolean isShowSubChart() {
        return showSubChart;
    }

    public void setShowSubChart(boolean showSubChart) {
        this.showSubChart = showSubChart;
        if (!showSubChart) {
            mainF = 1F;
            subF = 0;
            axisYbottomHeight = 0;
        }
    }

    public int getRectFillColor() {
        return rectFillColor;
    }

    public void setRectFillColor(int rectFillColor) {
        this.rectFillColor = rectFillColor;
    }

    public OnKChartClickListener getOnKChartClickListener() {
        return onKChartClickListener;
    }

    public void setOnKChartClickListener(OnKChartClickListener onKChartClickListener) {
        this.onKChartClickListener = onKChartClickListener;
    }

    public int getNumberScal() {
        return numberScal;
    }

    public void setNumberScal(int numberScal) {
        this.numberScal = numberScal;
    }

    public boolean isCrossEnable() {
        return isCrossEnable;
    }

    public void setCrossEnable(boolean crossEnable) {
        isCrossEnable = crossEnable;
    }

    public static boolean isEmpty(String str) {
        if (null == str)
            return true;
        if (str.trim().length() == 0)
            return true;
        if (str.trim().equalsIgnoreCase("null"))
            return true;
        return false;
    }
}
