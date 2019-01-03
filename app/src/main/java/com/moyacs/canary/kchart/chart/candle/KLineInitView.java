package com.moyacs.canary.kchart.chart.candle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.moyacs.canary.kchart.entity.KCandleObj;
import com.moyacs.canary.kchart.entity.KLineNormal;
import com.moyacs.canary.kchart.entity.KLineObj;
import com.moyacs.canary.kchart.util.KLogUtil;
import com.moyacs.canary.kchart.util.KNumberUtil;

import java.util.List;


/**
 * k线的初始化处理逻辑
 * 1、初始化view的宽度
 * 2、算出数据的最大值最小值
 * 3、算出主图的y坐标值list
 * 4、实现画边框，画title的方法
 */
public class KLineInitView extends KLineBaseView {
    String TAG = "KLineInitView";

    //主图指标参数
    protected int mainNormal = KLineNormal.NORMAL_SMA;
    //辅图指标参数
    protected int subNormal = KLineNormal.NORMAL_VOL;
    //上方主图的蜡烛图数据
    protected List<KCandleObj> kCandleObjList;
    //主图的指标线 指标线的size < kCandleObjList,所以从右边开始画 比较方便
    protected List<KLineObj<KCandleObj>> mainLineData;
    //副图的指标线
    protected List<KLineObj<KCandleObj>> subLineData;
    //下方副图的macd数据
    protected List<KCandleObj> subList;


    public KLineInitView(Context context) {
        super(context);
        init(context);
    }

    public KLineInitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KLineInitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {

    }


    /**
     * 初始化图标的内容区域
     * isAxisTitlein
     * 是否y轴的文字在图表内部
     * 设置x轴的宽度
     */
    protected void initWidth() {
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;

        Paint textPaint = getTextPaintX();
        String text = kCandleObjList.get(0).getOpen() + "";
        if (!isAxisTitlein) {
            //如果x轴的文字不在线条内 重新计算一下 左右两边的距离
            float lw = textPaint.measureText(text);
            if (axisXleftWidth < lw) {
                //留下的宽度不够文字显示
                axisXleftWidth = lw + commonPadding * 5;//commonPadding留白距离
            }
            if (axisYrightTitles != null && axisYrightTitles.size() > 0) {
                float lr = textPaint.measureText(axisYrightTitles.get(0));
                if (axisXrightWidth < lr) {
                    //留下的宽度不够文字显示
                    axisXrightWidth = lr + commonPadding * 5;
                }
            }

//            KLogUtil.v(TAG, "axisXleftWidth="+axisXleftWidth + " axisXrightWidth="+axisXrightWidth);
        } else {
            axisXleftWidth = commonPadding;
            axisXrightWidth = commonPadding;
        }
        //设置了  isAxisTitlein之后需要重新计算drawcount
        initDrawCount();
    }

    /**
     * 初始化一屏幕需要画出的蜡烛数
     * 宽度改变的时候需要重新计算
     * 同时需要算出当前画出一屏幕的 initMaxMinValue();
     */
    protected void initDrawCount() {
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;
        drawCount = KNumberUtil.roundUp((getDataWidth() / candleWidth));
        //按照整drawCount 重新再数算出candleWidth
        candleWidth = getDataWidth() / drawCount;

        KLogUtil.v(TAG, "drawCount=" + drawCount);
        if (drawIndexEnd == 0) {
            //第一次初始化
            drawIndexEnd = kCandleObjList.size();
        }
        //这里每次先定位drawIndexEnd，然后算出drawIndexStart
        if (drawIndexEnd > kCandleObjList.size()) {
            drawIndexEnd = kCandleObjList.size();
        }

        if (drawIndexEnd < drawCount)
            drawIndexEnd = drawCount;

        drawIndexStart = drawIndexEnd - drawCount;
        if (drawCount > kCandleObjList.size())
            drawIndexEnd = kCandleObjList.size();
        if (drawIndexStart < 0)
            drawIndexStart = 0;

        initMaxMinValue();
    }

    /**
     * 算出画一屏数据的最大值最小值
     * 分主图和副图的
     * 如果需要画线的时候 需要包含指标一起计算
     */
    protected void initMaxMinValue() {
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;


        for (int i = drawIndexStart; i < drawIndexEnd; i++) {
            if (i < 0 || i > kCandleObjList.size() - 1)
                continue;
            KCandleObj obj = kCandleObjList.get(i);

            if (i == drawIndexStart) {
                //初始赋值
                maxValue = obj.getHigh();
                minValue = obj.getLow();

                maxHigh = obj.getHigh();
                minLow = obj.getLow();

                if (subNormal == KLineNormal.NORMAL_VOL) {
                    subMaxValue = obj.getVol();
                    subMinValue = 0;
                } else if (subNormal == KLineNormal.NORMAL_MACD) {
                    //macd 需要画线和矩形
                    if (subList != null && subList.size() > 0) {
                        subMaxValue = subList.get(i).getHigh();
                        subMinValue = subList.get(i).getLow();
                    }
                    if (subLineData != null && subLineData.size() > 0) {
                        //幅图指标线一起算出来max min
                        for (int j = 0; j < subLineData.size(); j++) {
                            KLineObj<KCandleObj> line = subLineData.get(j);
                            if (line == null)
                                continue;
                            if (line.getLineData() == null)
                                continue;
//                            if (j == 0) {
//                                subMaxValue = line.getLineData().get(i).getNormValue();
//                                subMinValue = line.getLineData().get(i).getNormValue();
//                            }
                            if (subMaxValue < line.getLineData().get(i).getNormValue()) {
                                subMaxValue = line.getLineData().get(i).getNormValue();
                            }
                            if (subMinValue > line.getLineData().get(i).getNormValue() && line.getLineData().get(i).getNormValue() != 0) {
                                subMinValue = line.getLineData().get(i).getNormValue();
                            }
                        }
                    }

                } else if (subLineData != null && subLineData.size() > 0) {
                    //幅图指标线一起算出来max min
                    for (int j = 0; j < subLineData.size(); j++) {
                        KLineObj<KCandleObj> line = subLineData.get(j);
                        if (line == null)
                            continue;
                        if (line.getLineData() == null)
                            continue;

                        if (j == 0) {
                            subMaxValue = line.getLineData().get(i).getNormValue();
                            subMinValue = line.getLineData().get(i).getNormValue();
                        }
                        if (subMaxValue < line.getLineData().get(i).getNormValue()) {
                            subMaxValue = line.getLineData().get(i).getNormValue();
                        }
                        if (subMinValue > line.getLineData().get(i).getNormValue() && line.getLineData().get(i).getNormValue() != 0) {
                            subMinValue = line.getLineData().get(i).getNormValue();
                        }
                    }
                }

                //同时需要和指标线一起算出来max min
                if (mainLineData != null && mainLineData.size() > 0) {
                    for (KLineObj<KCandleObj> line : mainLineData) {
                        if (line == null)
                            continue;
                        if (line.getLineData() == null)
                            continue;
                        if (maxValue < line.getLineData().get(i).getNormValue()) {
                            maxValue = line.getLineData().get(i).getNormValue();
                        }
                        if (minValue > line.getLineData().get(i).getNormValue() && line.getLineData().get(i).getNormValue() != 0) {
                            minValue = line.getLineData().get(i).getNormValue();
                        }
                    }
                }
            } else {
                if (maxValue < obj.getHigh()) {
                    maxValue = obj.getHigh();
                }
                if (minValue > obj.getLow()) {
                    minValue = obj.getLow();
                }

                if (maxHigh < obj.getHigh()) {
                    maxHigh = obj.getHigh();
                }
                if (minLow > obj.getLow()) {
                    minLow = obj.getLow();
                }


                if (subNormal == KLineNormal.NORMAL_VOL) {
                    if (subMaxValue < obj.getVol()) {
                        subMaxValue = obj.getVol();
                    }
//                    if (subMinValue > obj.getVol()) {
//                        subMinValue = obj.getVol();
//                    }
                    subMinValue = 0;
                } else if (subNormal == KLineNormal.NORMAL_MACD) {
                    //macd 需要画线和矩形
                    if (subList != null && subList.size() > 0) {
                        if (subMaxValue < subList.get(i).getHigh()) {
                            subMaxValue = subList.get(i).getHigh();
                        }
                        if (subMinValue > subList.get(i).getLow()) {
                            subMinValue = subList.get(i).getLow();
                        }
                    }
                    if (subLineData != null && subLineData.size() > 0) {
                        //幅图指标线一起算出来max min
                        for (int j = 0; j < subLineData.size(); j++) {
                            KLineObj<KCandleObj> line = subLineData.get(j);
//                            if (j == 0) {
//                                subMaxValue = line.getLineData().get(i).getNormValue();
//                                subMinValue = line.getLineData().get(i).getNormValue();
//                            }
                            if (subMaxValue < line.getLineData().get(i).getNormValue()) {
                                subMaxValue = line.getLineData().get(i).getNormValue();
                            }
                            if (subMinValue > line.getLineData().get(i).getNormValue() && line.getLineData().get(i).getNormValue() != 0) {
                                subMinValue = line.getLineData().get(i).getNormValue();
                            }
                        }
                    }

                } else if (subLineData != null && subLineData.size() > 0) {
                    //幅图指标线一起算出来max min
                    for (KLineObj<KCandleObj> line : subLineData) {
                        if (line == null)
                            continue;
                        if (line.getLineData() == null)
                            continue;
                        if (subMaxValue < line.getLineData().get(i).getNormValue()) {
                            subMaxValue = line.getLineData().get(i).getNormValue();
                        }
                        if (subMinValue > line.getLineData().get(i).getNormValue() && line.getLineData().get(i).getNormValue() != 0) {
                            subMinValue = line.getLineData().get(i).getNormValue();
                        }
                    }
                }


                //同时需要和指标线一起算出来max min
                if (mainLineData != null && mainLineData.size() > 0) {
                    for (KLineObj<KCandleObj> line : mainLineData) {
                        if (line == null)
                            continue;
                        if (line.getLineData() == null)
                            continue;
                        if (maxValue < line.getLineData().get(i).getNormValue()) {
                            maxValue = line.getLineData().get(i).getNormValue();
                        }
                        if (minValue > line.getLineData().get(i).getNormValue() && line.getLineData().get(i).getNormValue() > 0) {
                            minValue = line.getLineData().get(i).getNormValue();
                        }
                    }
                }

            }
        }
        //是否上下对称 中间是0
        if (isSubLineZero) {
            if (subMaxValue > 0 && subMinValue < 0) {
                if (subMaxValue > Math.abs(subMinValue)) {
                    subMinValue = -subMaxValue;
                } else {
                    subMaxValue = -subMinValue;
                }
            }
        }
//        startYdouble = minValue;
//        stopYdouble = maxValue;
        //最顶部和底部留白距离
        startYdouble = minValue - (maxValue - minValue) / latitudeLineNumber / 2;
        stopYdouble = maxValue + (maxValue - minValue) / latitudeLineNumber / 2;
        KLogUtil.v(TAG, "maxValue=" + maxValue + " minValue=" + minValue + " maxVol=" + subMaxValue);
    }


    /**
     * 画纬线 X轴方向的横线
     *
     * @param canvas
     */
    protected void drawLatitudeLine(Canvas canvas) {
        //这里按照数学上的XY轴画线 （0,0）在左下方， 不是按照view的XY轴，（0,0）在左上方
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;

        int linesCounts = latitudeLineNumber;
        float dateHeight = getDataHeightMian();//高度---div

        Paint paint = getPaint();
        paint.setColor(latitudeColor);
        paint.setStrokeWidth(borderStrokeWidth);

        Paint textPaint = getTextPaintX();

        float startX = axisXleftWidth;
        float stopX = getWidth() - axisXrightWidth;
        float startY = axisYtopHeight + getDataHeightMian();

        //成交量的纬线
//        float subY = getHeight() - axisYbottomHeight;
        float subY = 0;
        float subOffect = getDataHeightSub() / (subLatitudeLineNumber - 1);
        for (int i = 0; i < subLatitudeLineNumber; i++) {
//            subY = subY - i * subOffect;
            subY = mainF * getHeight() + i * subOffect;
            //附图的横线框，画所有
            canvas.drawLine(startX, subY, stopX, subY, paint);
            //附图的横线框，附图除去最上方和最先方线的中间线
//            if (i == 0 || i == subLatitudeLineNumber - 1)
//                canvas.drawLine(startX, subY, stopX, subY, paint);
            //成交量
            String maxv = KNumberUtil.formartBigNumber(subMaxValue);
            String minStr = KNumberUtil.formartBigNumber(subMinValue);
            if (subNormal == KLineNormal.NORMAL_VOL) {
                minStr = "";
            }
            float w = textPaint.measureText(maxv);
            float wMin = textPaint.measureText(minStr);
            textPaint.setColor(latitudeFontColorBottom);
            float leftXTop = axisXleftWidth - w - 10;
            float leftXBottom = axisXleftWidth - wMin - 10;
            if (leftXTop < 0)
                leftXTop = 0;
            if (leftXBottom < 0)
                leftXBottom = 0;
            //这里暂时只显示最大
//            if (i == 0) {
//                if (isAxisTitlein)
//                    canvas.drawText(maxv, axisXleftWidth, mainF * getHeight() + latitudeFontSize, textPaint);
//                else
//                    canvas.drawText(maxv, leftXTop, mainF * getHeight() + latitudeFontSize, textPaint);
//            } else if (i == subLatitudeLineNumber - 1) {
//                if (isAxisTitlein)
//                    canvas.drawText(minStr, axisXleftWidth, getHeight() - axisYbottomHeight - latitudeFontSize / 2, textPaint);
//                else
//                    canvas.drawText(minStr, leftXBottom, getHeight() - axisYbottomHeight - latitudeFontSize / 2, textPaint);
//            }
        }

        float offsetY = 0;
        if (linesCounts > 1) {
            offsetY = dateHeight / (linesCounts - 1);//Y增加的幅度
        }

        Paint paintEffect = getPaint();
        paintEffect.setStyle(Paint.Style.STROKE);//STROKE 属性必须加上  不加画不出虚线
        paintEffect.setColor(effectColor);
        paintEffect.setStrokeWidth(borderStrokeWidth);//height
        paintEffect.setPathEffect(pathEffect);

        for (int i = 0; i < linesCounts; i++) {
            float y = startY - i * offsetY;
            if (i == 0 || i == linesCounts - 1) {
                //第一条和最后一条画实线
                canvas.drawLine(startX, y, stopX, y, paint);
            } else {
                //画虚线
                Path p = new Path();
                p.moveTo(startX, y);
                p.lineTo(stopX, y);
                canvas.drawPath(p, paintEffect);
            }

            //Y轴字体画笔颜色
            textPaint.setColor(latitudeFontColor);
            String textLeft = axisYleftTitles.get(i);
            float textW = textPaint.measureText(textLeft);

            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            //Rect.left  对于左边X轴的距离， top 对于顶部 Y轴的距离， right - left = 宽度 对于左边X轴的距离， bottom - top = 高度 Y轴
            Rect targetRect = new Rect((int) (startX - textW - commonPadding), (int) (y - (fontMetrics.bottom - fontMetrics.top) / 2), (int) startX, (int) (y + (fontMetrics.bottom - fontMetrics.top) / 2));//the size is copy from canvas.drawRect
            //不需要在线条的内部
            if (isAxisTitlein)
                targetRect = new Rect((int) (startX + commonPadding), (int) (y - (fontMetrics.bottom - fontMetrics.top) / 2), (int) (startX + textW + commonPadding), (int) (y + (fontMetrics.bottom - fontMetrics.top) / 2));//the size is copy from canvas.drawRect
//            int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
//            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
//            textPaint.setTextAlign(Paint.Align.CENTER);
//            if (i == 0)
//                canvas.drawText(textLeft, targetRect.centerX(), (y), textPaint);
//            else if (i == axisYleftTitles.size() - 1)
//                canvas.drawText(textLeft, targetRect.centerX(), (y + (fontMetrics.bottom - fontMetrics.top) / 2), textPaint);
//            else
//                canvas.drawText(textLeft, targetRect.centerX(), baseline, textPaint);

        }

    }

    /**
     * 初始化y轴的坐标
     * @param canvas
     */
    protected void initTitleValue (Canvas canvas) {
        axisYleftTitles.clear();
        double offset = (stopYdouble - startYdouble) / (latitudeLineNumber - 1);
        for (int i = 0; i < latitudeLineNumber; i++) {
            double currentValue = startYdouble + offset * i;
//            axisYrightTitles.add(KNumberUtil.beautifulDouble(currentValue, numberScal) + "");
            axisYleftTitles.add(KNumberUtil.beautifulDouble(currentValue, numberScal) + "");
//            axisYleftTitles.add(KNumberUtil.roundUp(currentValue) + "");
        }
    }
    /**
     * 画纬线方向的titles
     * Y轴坐标
     * 避免被挡住 最后画 在view的最上层
     *
     * @param canvas
     */
    protected void drawLatitudeTitle(Canvas canvas) {
        //这里按照数学上的XY轴画线 （0,0）在左下方， 不是按照view的XY轴，（0,0）在左上方
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;

        int linesCounts = latitudeLineNumber;
        float dateHeight = getDataHeightMian();//高度---div
//
//        Paint paint = new Paint();
//        paint.setColor(latitudeColor);
//        paint.setStrokeWidth(strokeWidth);

        Paint textPaint = getTextPaintX();

        float startX = axisXleftWidth;
        float stopX = getWidth() - axisXrightWidth;
        float startY = axisYtopHeight + getDataHeightMian();

        //成交量的纬线
//        float subY = getHeight() - axisYbottomHeight;
        float subY = 0;
        float subYdiv = getDataHeightSub() / (subLatitudeLineNumber - 1);
        for (int i = 0; i < subLatitudeLineNumber; i++) {
//            subY = subY - i * subOffect;
            subY = mainF * getHeight() + i * subYdiv;
            //附图的横线框，画所有
//            canvas.drawLine(startX, subY, stopX, subY, paint);
            //附图的横线框，附图除去最上方和最先方线的中间线
//            if (i == 0 || i == subLatitudeLineNumber - 1)
//                canvas.drawLine(startX, subY, stopX, subY, paint);
            //成交量
            String maxv = KNumberUtil.formartBigNumber(subMaxValue);
            String minStr = KNumberUtil.formartBigNumber(subMinValue);

            if (subNormal == KLineNormal.NORMAL_VOL) {
                minStr = "";
            }
            double subOffset = (subMaxValue - subMinValue) / (subLatitudeLineNumber - 1);
            String subStr = KNumberUtil.formartBigNumber(subMinValue + subOffset * i);
            float w = textPaint.measureText(maxv);
            float wMin = textPaint.measureText(minStr);
            textPaint.setColor(latitudeFontColorBottom);
            float leftXTop = axisXleftWidth - w - 10;
            float leftXBottom = axisXleftWidth - wMin - 10;
            if (leftXTop < 0)
                leftXTop = 0;
            if (leftXBottom < 0)
                leftXBottom = 0;
//            Rect ret = new Rect((int) (leftXTop), (int) (subY - (latitudeFontSize) / 2), (int) (axisXleftWidth + textPaint.measureText(subStr)), (int) (subY + (latitudeFontSize) / 2));
//            if (isAxisTitlein)
//                ret = new Rect((int) (axisXleftWidth), (int) (subY - (latitudeFontSize) / 2), (int) (axisXleftWidth + textPaint.measureText(subStr)), (int) (subY + (latitudeFontSize) / 2));


            if (i == 0) {
                //显示最大
                if (isAxisTitlein)
                    canvas.drawText(maxv, axisXleftWidth, mainF * getHeight() + latitudeFontSize, textPaint);
                else
                    canvas.drawText(maxv, leftXTop, mainF * getHeight() + latitudeFontSize, textPaint);
            } else if (i == subLatitudeLineNumber - 1) {
                if (isAxisTitlein)
                    canvas.drawText(minStr, axisXleftWidth, getHeight() - axisYbottomHeight - latitudeFontSize / 2, textPaint);
                else
                    canvas.drawText(minStr, leftXBottom, getHeight() - axisYbottomHeight - latitudeFontSize / 2, textPaint);
            } else {
                //不需要在线条的内部
//               drawText(canvas, subStr, ret, textPaint);

                if (isAxisTitlein)
                    canvas.drawText(subStr, axisXleftWidth, subY + latitudeFontSize / 2 - 2, textPaint);
                else
                    canvas.drawText(subStr, axisXleftWidth - textPaint.measureText(subStr) - commonPadding, subY + latitudeFontSize / 2 - 2, textPaint);
            }
        }

        float offsetY = 0;
        if (linesCounts > 1) {
            offsetY = dateHeight / (linesCounts - 1);//Y增加的幅度
        }

//        Paint paintEffect = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paintEffect.setStyle(Paint.Style.STROKE);//STROKE 属性必须加上  不加画不出虚线
//        paintEffect.setColor(effectColor);
//        paintEffect.setStrokeWidth(strokeWidth);//height
//        paintEffect.setPathEffect(pathEffect);

        for (int i = 0; i < linesCounts; i++) {
            float y = startY - i * offsetY;
//            if (i == 0 || i == linesCounts - 1) {
//                //第一条和最后一条画实线
//                canvas.drawLine(startX, y, stopX, y, paint);
//            } else {
//                //画虚线
//                Path p = new Path();
//                p.moveTo(startX, y);
//                p.lineTo(stopX, y);
//                canvas.drawPath(p, paintEffect);
//            }

            //Y轴字体画笔颜色
            textPaint.setColor(latitudeFontColor);
            String textLeft = axisYleftTitles.get(i);
            float textW = textPaint.measureText(textLeft);


            //Rect.left  对于左边X轴的距离， top 对于顶部 Y轴的距离， right - left = 宽度 对于左边X轴的距离， bottom - top = 高度 Y轴
            Rect targetRect = new Rect((int) (startX - textW - commonPadding), (int) (y - (latitudeFontSize) / 2), (int) startX, (int) (y + (latitudeFontSize) / 2));//the size is copy from canvas.drawRect
            //不需要在线条的内部
            if (isAxisTitlein)
                targetRect = new Rect((int) (startX + commonPadding), (int) (y - (latitudeFontSize) / 2), (int) (startX + textW + commonPadding), (int) (y + (latitudeFontSize) / 2));//the size is copy from canvas.drawRect
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            textPaint.setTextAlign(Paint.Align.CENTER);
            if (i == 0)
                canvas.drawText(textLeft, targetRect.centerX(), (y), textPaint);
            else if (i == linesCounts - 1)
                canvas.drawText(textLeft, targetRect.centerX(), (y + latitudeFontSize), textPaint);
            else
                drawText(canvas, textLeft, targetRect, textPaint);

        }
    }

    /**
     * 画经度线
     * 1、先画出需要的第一条和最后一条实线边框
     * 2、画出经线和x轴的时间
     * 随滑动动态画出，不做固定
     *
     * @param canvas
     */
    protected void drawLongitudeLineTitle(Canvas canvas) {
        if (kCandleObjList == null || kCandleObjList.size() == 0)
            return;

        int linesCounts = longitudeLineNumber;
        float dateHeight = getDataHeightMian();//经线的高度

        Paint linePaint = getPaint();
        linePaint.setColor(longitudeColor);
        linePaint.setStrokeWidth(borderStrokeWidth);

        Paint textPaint = getPaint();
        textPaint.setColor(longitudeFontColor);
        textPaint.setTextSize(longitudeFontSize);
//        textPaint.setTextAlign(Paint.Align.CENTER);

        float startX = axisXleftWidth;
        float stopX = axisXleftWidth + getDataWidth();
        float startY = axisYtopHeight;

        float substartY = axisYtopHeight + getDataHeightMian() + axisYmiddleHeight;
        float substopY = getHeight() - axisYbottomHeight;
        //第一条
        canvas.drawLine(startX, startY, startX, startY + dateHeight, linePaint);
        canvas.drawLine(startX, substartY, startX, substopY, linePaint);
        //最后一条
        canvas.drawLine(stopX, startY, stopX, startY + dateHeight, linePaint);
        canvas.drawLine(stopX, substartY, stopX, substopY, linePaint);

        //分开格子的时间位置
        if (kCandleObjList == null)
            return;
        RectF rectCandle = new RectF();
        //矩形的起始位置
        rectCandle.left = axisXleftWidth;

        //重新设置y轴画的时间分隔竖线画笔
        linePaint = getPaint();
        linePaint.setColor(longitudeColor);
        linePaint.setStrokeWidth(borderStrokeWidth);

        for (int i = drawIndexStart; i < drawIndexEnd; i++) {
            KCandleObj klineObj = kCandleObjList.get(i);
            rectCandle.right = rectCandle.left + candleWidth * (1 - CANDLE_RATE);
            //画出竖直方向每月的Y轴线
            if (i >= drawIndexStart && i < drawIndexEnd - 1 && kCandleObjList.size() > 3 && kCandleObjList.size() >= drawCount) {
                //如果后一个大于前一个
//                String next = KDateUtil.formatDate(kCandleObjList.get(i + 1).getTime(), formartSource, formart);
                String current = klineObj.getTime();
                float lineX = rectCandle.left + candleWidth * (1 - CANDLE_RATE) / 2;

                //这里可以考虑 item的宽度大小，暂时用size 处理
                int count = 3;
                int itemSize = (int) Math.ceil((double) drawCount / (double) count);
                int offest = drawCount % count;
                if (i < drawIndexEnd && i >= drawIndexStart && (i - 1 - offest) % itemSize == 0) {
                    canvas.drawLine(lineX, axisYtopHeight, lineX, axisYtopHeight + getDataHeightMian(), linePaint);

                    //附图的竖线位置
//                    float substartY = axisYtopHeight + getDataHeightMian() + axisYmiddleHeight;
//                    float substopY = getHeight() - axisYbottomHeight;
//                    canvas.drawLine(lineX, substartY, lineX, substopY, linePaint);

                    Paint timePanit = getTextPaintY();
                    float textW = timePanit.measureText(current);
                    lineX = lineX - textW / 2;
                    if (lineX < axisXleftWidth)
                        lineX = axisXleftWidth;
                    if (lineX + textW > getWidth() - axisXrightWidth)
                        lineX = getWidth() - axisXrightWidth - textW;
//                    canvas.drawText(current, lineX, axisYtopHeight + getDataHeightMian() + axisYmiddleHeight / 2 + longitudeFontSize / 2, timePanit);
                    canvas.drawText(current, lineX, axisYtopHeight + getDataHeightMian() + longitudeFontSize, timePanit);
                }
            }
            //递增矩形左边的位置
            rectCandle.left += candleWidth;
        }
    }


}
