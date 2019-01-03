package com.moyacs.canary.kchart.chart.candle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.moyacs.canary.kchart.chart.cross.KCrossLineView;
import com.moyacs.canary.kchart.entity.KCandleObj;
import com.moyacs.canary.kchart.entity.KLineNormal;
import com.moyacs.canary.kchart.entity.KLineObj;
import com.moyacs.canary.kchart.util.KDisplayUtil;
import com.moyacs.canary.kchart.util.KLogUtil;

import java.util.List;


/**
 * android:hardwareAccelerated="true"
 * 硬件加速问题  导致多层次的view重新绘制
 * 设置hardwareAccelerated true
 * <p/>
 * Created by fangzhu
 * list 按照时间升序
 * 画图的时候从左边向右边开始画
 */
public class KLineView extends KLineTouchView implements KCrossLineView.IDrawCrossLine {
    String TAG = "KLineView";


    public KLineView(Context context) {
        super(context);
        init(context);
    }

    public KLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        super.init(context);

    }

    public KCrossLineView getCrossLineView() {
        return crossLineView;
    }

    public void setCrossLineView(KCrossLineView crossLineView) {
        this.crossLineView = crossLineView;
        crossLineView.setiDrawCrossLine(this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        KLogUtil.v(TAG, "onDraw");
        super.onDraw(canvas);
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;
        try {
            initWidth();
            initTitleValue(canvas);
            drawLatitudeLine(canvas);
            drawLongitudeLineTitle(canvas);

//            //主图指标
            if (mainNormal == KLineNormal.NORMAL_SMA) {
                drawSMA(canvas);
            } else if (mainNormal == KLineNormal.NORMAL_EMA) {
                drawEMA(canvas);
            } else if (mainNormal == KLineNormal.NORMAL_BOLL) {
                drawBOLL(canvas);
            }
//
            //幅图指标
            if (subNormal == KLineNormal.NORMAL_MACD) {
                drawMACD(canvas);
            } else if (subNormal == KLineNormal.NORMAL_KDJ) {
                drawKDJ(canvas);
            } else if (subNormal == KLineNormal.NORMAL_RSI) {
                drawRSI(canvas);
            } else if (subNormal == KLineNormal.NORMAL_VOL) {
                drawSubVol(canvas);
            }

            //没有出现十字线的时候，也要画出tips
            if (!showCross) {
                drawTips(canvas);
            } else {
                if (crossLineView != null)
                    crossLineView.postInvalidate();
            }
            //避免被挡住 最后画 在view的最上层
            drawLatitudeTitle(canvas);
            //最高点 最低点
            if (isMaxMinShowInScreen)
                drawValueText(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 画出最大最小值的K线文字
     *
     * @param canvas
     */
    protected void drawValueText(Canvas canvas) {
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;
        //出现多个最大值最小值 只需要画一个
        boolean drawedMaxValue = false, drawedMinValue = false;
        RectF rectCandle = new RectF();
        //矩形的右边位置
        rectCandle.left = axisXleftWidth;
        for (int i = drawIndexStart; i < drawIndexEnd; i++) {
            KCandleObj klineObj = kCandleObjList.get(i);

            rectCandle.right = rectCandle.left + candleWidth * (1 - CANDLE_RATE);
            float x = rectCandle.left + candleWidth * (1 - CANDLE_RATE) / 2F;

            //画出最高价最低价,注意了，这里是maxHigh minLow 才是K线的最大最小值
            int lineW = KDisplayUtil.dip2px(getContext(), 12);
            float lineY = getYbyPrice(klineObj.getHigh());

            Paint p = getPaint();
            p.setColor(valueTextFillColor);
            p.setTextSize(valueTextSize);
            p.setStrokeWidth(2);
            float stopX = x + lineW;
            if (x > getWidth() / 2)
                stopX = x - lineW;
            int margin = 2;//文字的边距
            if (!drawedMaxValue && maxHigh == klineObj.getHigh()) {
                //指引线
                canvas.drawLine(x, lineY, stopX, lineY, p);

//                String text = KNumberUtil.beautifulDouble(maxHigh);
                String text = maxHigh + "";
                float textW = p.measureText(text) + margin * 2;
                RectF rec = new RectF(stopX, lineY - valueTextSize / 2 - margin, stopX + textW, lineY + valueTextSize / 2 + margin);
                if (x > getWidth() / 2) {
                    rec = new RectF(stopX - textW, lineY - valueTextSize / 2 - margin, stopX, lineY + valueTextSize / 2 + margin);
                }
                canvas.drawRect(rec, p);
                p.setColor(valueTextColor);
                drawText(canvas, text, rec, p);
                drawedMaxValue = true;
            }

            if (!drawedMinValue && minLow == klineObj.getLow()) {
                lineY = getYbyPrice(klineObj.getLow());
                //指引线
                canvas.drawLine(x, lineY, stopX, lineY, p);

//                String text = KNumberUtil.beautifulDouble(minLow);
                String text = minLow + "";
                float textW = p.measureText(text) + margin * 2;
                RectF rec = new RectF(stopX, lineY - valueTextSize / 2 - margin, stopX + textW, lineY + valueTextSize / 2 + margin);
                if (x > getWidth() / 2) {
                    rec = new RectF(stopX - textW, lineY - valueTextSize / 2 - margin, stopX, lineY + valueTextSize / 2 + margin);
                }

                canvas.drawRect(rec, p);
                p.setColor(valueTextColor);
                drawText(canvas, text, rec, p);
                drawedMinValue = true;
            }

            //递增矩形左边的位置
            rectCandle.left += candleWidth;
        }

    }

    /**
     * 主图的蜡烛图
     *
     * @param canvas
     */
    protected void drawMainChart(Canvas canvas) {
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;

        //蜡烛画笔
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(candlePostColor);
        paint.setStrokeWidth(candleStrokeWidth);

        RectF rectCandle = new RectF();
        //矩形的起始位置
        rectCandle.left = axisXleftWidth;
        for (int i = drawIndexStart; i < drawIndexEnd; i++) {
            KCandleObj klineObj = kCandleObjList.get(i);

            rectCandle.right = rectCandle.left + candleWidth * (1 - CANDLE_RATE);
            float x = rectCandle.left + candleWidth * (1 - CANDLE_RATE) / 2F;
            //画出蜡烛图，开高低收
            if (klineObj.getClose() > klineObj.getOpen()) {
                //收盘价大于开盘价 红色
                paint.setColor(candlePostColor);
                rectCandle.top = getYbyPrice(klineObj.getClose());
                rectCandle.bottom = getYbyPrice(klineObj.getOpen());

                //在屏幕内 从下往上画
                if (x > axisXleftWidth) {
                    float startY = getYbyPrice(klineObj.getClose());
                    float stopY = getYbyPrice(klineObj.getHigh());
                    if (startY - stopY < 1)
                        stopY = startY - 1;
                    canvas.drawLine(x, startY, x, stopY, paint);

                    startY = getYbyPrice(klineObj.getLow());
                    stopY = getYbyPrice(klineObj.getOpen());
                    if (startY - stopY < 1)
                        stopY = startY - 1;
                    canvas.drawLine(x, startY, x, stopY, paint);
                }

            } else if (klineObj.getClose() < klineObj.getOpen()) {
                //收盘价小于开盘价 绿色
                paint.setColor(candleNegaColor);
                rectCandle.top = getYbyPrice(klineObj.getOpen());
                rectCandle.bottom = getYbyPrice(klineObj.getClose());
                //在屏幕内
                if (x > axisXleftWidth) {
                    float startY = getYbyPrice(klineObj.getOpen());
                    float stopY = getYbyPrice(klineObj.getHigh());
                    if (startY - stopY < 1)
                        stopY = startY - 1;
                    canvas.drawLine(x, startY, x, stopY, paint);

                    startY = getYbyPrice(klineObj.getLow());
                    stopY = getYbyPrice(klineObj.getClose());
                    if (startY - stopY < 1)
                        stopY = startY - 1;
                    canvas.drawLine(x, startY, x, stopY, paint);
                }
            } else {
                //十字线 T字线
                paint.setColor(candleCrossColor);
                rectCandle.top = getYbyPrice(klineObj.getOpen());
                rectCandle.bottom = getYbyPrice(klineObj.getClose());
                //在屏幕内
                if (x > axisXleftWidth) {
                    float startY = getYbyPrice(klineObj.getLow());
                    float stopY = getYbyPrice(klineObj.getHigh());
                    if (startY - stopY < 1)
                        stopY = startY - 1;
                    canvas.drawLine(x, startY, x, stopY, paint);
                }

            }

            //处理最左边超过屏幕的K线
            int outCount = 0;
            if (rectCandle.left < axisXleftWidth) {
                outCount++;
                rectCandle.left = axisXleftWidth;
            }
            //还有第二个超出屏幕就不用画了
            if (outCount > 1)
                continue;
            //高度太小 设置1个单位
            if (rectCandle.bottom - rectCandle.top < 1)
                rectCandle.bottom = rectCandle.top + 1;
            canvas.drawRect(rectCandle, paint);

            //递增矩形左边的位置
            rectCandle.left += candleWidth;
        }

    }

    /**
     * 主图的指标线
     *
     * @param canvas
     */
    protected void drawMianLine(Canvas canvas) {
        if (mainLineData == null || mainLineData.size() == 0)
            return;
        Paint linePaint = getPaint();
        linePaint.setStrokeWidth(normalLineStrokeWidth);

        for (int j = 0; j < mainLineData.size(); j++) {
            RectF rectCandle = new RectF();
            PointF pointF = null;

            KLineObj<KCandleObj> lineObj = mainLineData.get(j);
            if (lineObj == null)
                continue;
            linePaint.setColor(lineObj.getLineColor());
            if (!lineObj.isDisplay())
                continue;

            for (int i = drawIndexStart; i < drawIndexEnd; i++) {
                KCandleObj klineObj = lineObj.getLineData().get(i);
                if (klineObj.getNormValue() == 0)
                    continue;
                //矩形的起始位置
                rectCandle.left = axisXleftWidth + (i - drawIndexStart) * candleWidth;
                rectCandle.right = rectCandle.left + candleWidth * (1 - CANDLE_RATE);
                float x = rectCandle.left + candleWidth * (1 - CANDLE_RATE) / 2F;
                PointF currentPoint = new PointF();
                currentPoint.set(x, getYbyPrice(klineObj.getNormValue()));

                //处理最左边超过屏幕的K线
                int outCount = 0;
                if (rectCandle.left < axisXleftWidth) {
                    outCount++;
                    rectCandle.left = axisXleftWidth;
                }
                //还有第二个超出屏幕就不用画了
                if (outCount > 1)
                    continue;

                //在屏幕内
                if (x > axisXleftWidth && i < drawIndexEnd && pointF != null) {
                    canvas.drawLine(pointF.x, pointF.y, currentPoint.x, currentPoint.y, linePaint);
                }
                pointF = currentPoint;
            }
        }
    }

    /**
     * sma指标
     *
     * @param canvas
     */
    protected void drawSMA(Canvas canvas) {
        drawMainChart(canvas);
        drawMianLine(canvas);
    }

    /**
     * ema指标
     *
     * @param canvas
     */
    protected void drawEMA(Canvas canvas) {
        drawMainChart(canvas);
        drawMianLine(canvas);
    }

    /**
     * boll指标
     *
     * @param canvas
     */
    protected void drawBOLL(Canvas canvas) {
        drawMainChart(canvas);
        drawMianLine(canvas);
    }

    protected void drawRSI(Canvas canvas) {
        drawSubLine(canvas);
    }

    protected void drawKDJ(Canvas canvas) {
        drawSubLine(canvas);
    }

    protected void drawMACD(Canvas canvas) {
        drawMacdStick(canvas);
        drawSubLine(canvas);

    }

    /**
     * 附图的指标线
     *
     * @param canvas
     */
    protected void drawSubLine(Canvas canvas) {
        if (subLineData == null || subLineData.size() == 0)
            return;

        Paint linePaint = getPaint();
        linePaint.setStrokeWidth(normalLineStrokeWidth);

        RectF rectCandle = new RectF();

        for (int j = 0; j < subLineData.size(); j++) {
            PointF pointF = null;
            KLineObj<KCandleObj> lineObj = subLineData.get(j);
            if (lineObj == null)
                continue;
            linePaint.setColor(lineObj.getLineColor());
            if (!lineObj.isDisplay())
                continue;

            for (int i = drawIndexStart; i < drawIndexEnd; i++) {
                if (lineObj.getLineData() == null)
                    continue;
                KCandleObj klineObj = lineObj.getLineData().get(i);
                if (klineObj.getNormValue() == 0)
                    continue;
                //矩形的起始位置
                rectCandle.left = axisXleftWidth + candleWidth * (i - drawIndexStart);

                rectCandle.right = rectCandle.left + candleWidth * (1 - CANDLE_RATE);
                float x = rectCandle.left + candleWidth * (1 - CANDLE_RATE) / 2F;
                PointF currentPoint = new PointF();
                float subY = getSubY(klineObj.getNormValue());

                currentPoint.set(x, subY);

                //处理最左边超过屏幕的K线
                int outCount = 0;
                if (rectCandle.left < axisXleftWidth) {
                    outCount++;
                    rectCandle.left = axisXleftWidth;
                }
                //还有第二个超出屏幕就不用画了
                if (outCount > 1)
                    continue;
                //超出附图应该所在的内容区域
                if (subY > getHeight())
                    continue;
                if (subY < getHeight() * getMainF())
                    continue;
                //在屏幕内
                if (pointF != null && x > axisXleftWidth && i < drawIndexEnd) {
                    canvas.drawLine(pointF.x, pointF.y, currentPoint.x, currentPoint.y, linePaint);
                }
                //递增矩形左边的位置
                pointF = currentPoint;
            }
        }
    }

    /**
     * 幅图的成交量
     *
     * @param canvas
     */
    protected void drawSubVol(Canvas canvas) {
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;

        //成交量画笔
        Paint volPaint = getPaint();
        volPaint.setStyle(Paint.Style.FILL);
        volPaint.setStrokeWidth(candleStrokeWidth);


        Paint linePaint = getPaint();
        linePaint.setColor(longitudeColor);
        linePaint.setStrokeWidth(borderStrokeWidth);

        //最底部的交易量启始位置
        float subBottom = getHeight() - axisYbottomHeight;

        RectF rectCandle = new RectF();
        //矩形的起始位置
        rectCandle.left = axisXleftWidth;
        for (int i = drawIndexStart; i < drawIndexEnd; i++) {
            KCandleObj klineObj = kCandleObjList.get(i);

            rectCandle.right = rectCandle.left + candleWidth * (1 - CANDLE_RATE);
            float x = rectCandle.left + candleWidth * (1 - CANDLE_RATE) / 2F;
            //处理最左边超过屏幕的K线
            int outCount = 0;
            if (rectCandle.left < axisXleftWidth) {
                outCount++;
                rectCandle.left = axisXleftWidth;
            }
            //还有第二个超出屏幕就不用画了
            if (outCount > 1)
                continue;

            //画出幅图的成交量
            //getDataHeightSub :maxVol = y:vol
//            float volY = getDataHeightSub() * (float) klineObj.getVol() / (float) subMaxValue;
            float volY = getSubY(klineObj.getVol());
            RectF rectVol = new RectF();
            rectVol.left = rectCandle.left;
            rectVol.right = rectCandle.right;
//            rectVol.top = subBottom - volY;
            rectVol.top = volY;
            rectVol.bottom = subBottom;

            //设置颜色
            if (klineObj.getClose() >= klineObj.getOpen()) {
                volPaint.setColor(candlePostColor);
            } else {
                volPaint.setColor(candleNegaColor);
            }

            //高度太小 设置1个单位
            if (rectVol.bottom - rectVol.top < 1)
                rectVol.bottom = rectVol.top + 1;

            canvas.drawRect(rectVol, volPaint);

            //递增矩形左边的位置
            rectCandle.left += candleWidth;
        }

    }

    /**
     * 幅图 macd 矩形
     *
     * @param canvas
     */
    protected void drawMacdStick(Canvas canvas) {
        if (subList == null || subList.size() == 0)
            return;

        //矩形画笔
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(candleStrokeWidth);

        //macd 0的位置
        float subBottom = getSubY(0);

        RectF rectCandle = new RectF();
        //矩形的左边位置
        rectCandle.left = axisXrightWidth;
        for (int i = drawIndexStart; i < drawIndexEnd; i++) {
            KCandleObj klineObj = subList.get(i);
            rectCandle.right = rectCandle.left + candleWidth * (1 - CANDLE_RATE);

            //处理最左边超过屏幕的K线
            int outCount = 0;
            if (rectCandle.left < axisXleftWidth) {
                outCount++;
                rectCandle.left = axisXleftWidth;
            }
            //还有第二个超出屏幕就不用画了
            if (outCount > 1)
                continue;

            //画出幅图的成交量
            //getDataHeightSub :maxVol = y:vol
//            float volY = getDataHeightSub() * (float) klineObj.getVol() / (float) subMaxValue;
            double value = klineObj.getHigh();
            //设置颜色
            paint.setColor(candlePostColor);
            if (value == 0) {
                value = klineObj.getLow();
                //设置颜色
                paint.setColor(candleNegaColor);
            }

            float volY = getSubY(value);
            RectF rectVol = new RectF();
            rectVol.left = rectCandle.left;
            rectVol.right = rectCandle.right;
//            rectVol.top = subBottom - volY;
            rectVol.top = volY;
            rectVol.bottom = subBottom;
            if (volY > subBottom) {
                rectVol.top = subBottom;
                rectVol.bottom = volY;
            }

            //高度太小 设置1个单位;value != 0 前面周期内的数据 都是0 不用画线
            if (value != 0 && rectVol.bottom - rectVol.top < 1)
                rectVol.bottom = rectVol.top + 1;

            canvas.drawRect(rectVol, paint);

            //递增矩形左边的位置
            rectCandle.left += candleWidth;
        }

    }


    public List<KCandleObj> getkCandleObjList() {
        return kCandleObjList;
    }

    public void setkCandleObjList(List<KCandleObj> kCandleObjList) {
        this.kCandleObjList = kCandleObjList;
        postInvalidate();
    }


    public List<KLineObj<KCandleObj>> getMainLineData() {
        return mainLineData;
    }

    public void setMainLineData(List<KLineObj<KCandleObj>> mainLineData) {
        this.mainLineData = mainLineData;
        //可以考虑是否需要重第一个位置开始
//        if (kCandleObjList != null)
//            drawIndexEnd = kCandleObjList.size() - 1;
        postInvalidate();
    }

    public int getSubNormal() {
        return subNormal;
    }

    public void setSubNormal(int subNormal) {
        this.subNormal = subNormal;
        postInvalidate();
    }

    public int getMainNormal() {
        return mainNormal;
    }

    public void setMainNormal(int mainNormal) {
        this.mainNormal = mainNormal;
        postInvalidate();
    }

    public List<KLineObj<KCandleObj>> getSubLineData() {
        return subLineData;
    }

    public void setSubLineData(List<KLineObj<KCandleObj>> subLineData) {
        this.subLineData = subLineData;
    }

    public List<KCandleObj> getSubList() {
        return subList;
    }

    public void setSubList(List<KCandleObj> subList) {
        this.subList = subList;
    }

    /**
     * 按下是否在左边
     * 这里是内容区域，边框的中心算起
     *
     * @return
     */
    public boolean isToucInLeftChart() {
        //得到主图的宽度
//        float chatW = getWidth() - axisXleftWidth - axisXrightWidth;
//        float centerX = axisXleftWidth + chatW / 2;
//        if (touchX > centerX) {
//            return false;
//        }
//        return true;

        if (kCandleObjList == null)
            return false;
        //上面是按照宽度算的，
        // 用index可能更好,数据不够的时候可以效果好些
        int index = getTouchIndex();
        if (index > kCandleObjList.size())
            index = kCandleObjList.size() - 1;
        if (index < drawCount / 2)
            return true;
        if (index > drawIndexStart + (drawIndexEnd - drawIndexStart) / 2)
            return false;
        return true;
    }

}
