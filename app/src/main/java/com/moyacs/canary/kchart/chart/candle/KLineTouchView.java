package com.moyacs.canary.kchart.chart.candle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import com.moyacs.canary.kchart.chart.cross.KCrossLineView;
import com.moyacs.canary.kchart.entity.KCandleObj;
import com.moyacs.canary.kchart.entity.KLineNormal;
import com.moyacs.canary.kchart.entity.KLineObj;
import com.moyacs.canary.kchart.listener.OnKCrossLineMoveListener;
import com.moyacs.canary.kchart.util.KLogUtil;
import com.moyacs.canary.kchart.util.KNumberUtil;


/**
 * k线的touch处理逻辑都放在这里
 * 1、k线滑动的逻辑
 * 2、十字线滑动的逻辑
 */
public class KLineTouchView extends KLineInitView implements KCrossLineView.IDrawCrossLine {
    String TAG = "KLineTouchView";
    //是否双击了view，解决重复调用onlongPress
    protected boolean isDoubClick = false;

    public KLineTouchView(Context context) {
        super(context);
        init(context);
    }

    public KLineTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KLineTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
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


    /**
     * 获取点击touch的时候 索引的位置
     *
     * @return
     */
    public int getTouchIndex() {
        int index = KNumberUtil.roundUp(((touchX - axisXleftWidth) / (candleWidth)));
        if (index <= 0)
            index = 0;
        if (index >= drawCount)
            index = drawCount - 1;
        //定位到准确的位置
        index = drawIndexStart + index;
        KLogUtil.v(TAG, "drawIndexEnd drawIndexEnd=" + drawIndexEnd);
        KLogUtil.v(TAG, "drawCrossLine touchX=" + touchX);
        if (index <= 0)
            index = 0;
        if (index > kCandleObjList.size() - 1)
            index = kCandleObjList.size() - 1;
        return index;
    }

    /**
     * 十字线
     *
     * @param canvas
     */
    public void drawCrossLine(Canvas canvas) {
        KLogUtil.v(TAG, "drawCrossLine");
        if (!showCross)
            return;
        if (!isCrossEnable)
            return;
//        Paint p = getTextPaint();
//        p.setColor(Color.RED);
//        canvas.drawLine(touchX, axisYtopHeight, touchX, axisYtopHeight + getDataHeightMian(), p);
//        p.setColor(Color.GREEN);
//        canvas.drawLine(lastX, axisYtopHeight, lastX, axisYtopHeight + getDataHeightMian(), p);

        try {
            //从左边开始算
            int index = getTouchIndex();
            KLogUtil.v(TAG, "drawCrossLine index=" + index);
            //重新定位出touch的位置  可能数据不够 就留在最后一条
            float x = axisXleftWidth + (index - drawIndexStart) * (candleWidth);
            //蜡烛图实心部分中间的位置
            x = x + candleWidth * (1 - CANDLE_RATE) / 2;
            KLogUtil.v(TAG, "drawIndexEnd drawIndexEnd=" + drawIndexEnd);
            KLogUtil.v(TAG, "drawCrossLine touchX=" + touchX);
            KLogUtil.v(TAG, "index=" + index);
            KCandleObj obj = kCandleObjList.get(index);
            if (obj == null) {
                return;
            } else {
                if (onKCrossLineMoveListener != null)
                    onKCrossLineMoveListener.onCrossLineMove(obj);

                // 绘制点击十字线
                Paint crossPaint = getPaint();
                crossPaint.clearShadowLayer();
                crossPaint.reset();
                crossPaint.setStrokeWidth(crossStrokeWidth);
                crossPaint.setColor(crossLineColor);
                crossPaint.setStyle(Paint.Style.FILL);

                //价格paint
                Paint textPaint = getPaint();
                //使用默认的填充色
                textPaint.setColor(rectFillColor);
                textPaint.setTextSize(crossLatitudeFontSize);
                textPaint.setStyle(Paint.Style.FILL);
                //时间paint
                Paint textPaintT = getPaint();
                //使用默认的填充色
                textPaintT.setColor(rectFillColor);
                textPaintT.setTextSize(crossLongitudeFontSize);
                textPaintT.setStyle(Paint.Style.FILL);

                //竖线
                canvas.drawLine(x, axisYtopHeight, x, getHeight() - axisYbottomHeight, crossPaint);

                double price = obj.getClose();

                //默认画在K线的最高价或者最低价的位置
                float y = getYbyPrice(price);

                if (crossLineView != null && crossLineView.isCrossXbyTouch()) {
                    //手指的位置画出横线
                    y = lastY;
                    if (y > axisYtopHeight + getDataHeightMian())
                        y = axisYtopHeight + getDataHeightMian();
                    if (y < axisYtopHeight)
                        y = axisYtopHeight;
                    price = getPriceByY(y);

                }
                String priceStr = KNumberUtil.beautifulDouble(price, numberScal);
                //价格文字宽度
                float w = textPaint.measureText(priceStr);
                //横线x开始位置坐标
                float horX = axisXleftWidth;
                if (isAxisTitlein) {
                    horX = axisXleftWidth + w;
                }
                //横线 框画左侧
//                canvas.drawLine(horX, y, getWidth() - axisXrightWidth, y, crossPaint);

                if (isToucInLeftChart()) {
                    //同方向，touch在屏幕左侧 就把价格框画在左边
                    canvas.drawLine(horX, y, getWidth() - axisXrightWidth, y, crossPaint);
                } else {
                    //同方向，touch在屏幕右侧 就把价格框画在右侧
                    canvas.drawLine(axisXleftWidth, y, getWidth() - axisXrightWidth - w, y, crossPaint);
                }
                //时间文字
                float wT = textPaintT.measureText(obj.getTime() + "");
                //价格 RectF 的宽度和坐标一致 就可以啦  框画左侧
//                RectF rectF = new RectF(axisXleftWidth - w, y - latitudeFontSize / 2 - 0,
//                        axisXleftWidth, y + latitudeFontSize / 2 + 0);
//                if (isAxisTitlein) {
//                    rectF = new RectF(axisXleftWidth, y - latitudeFontSize / 2 - 0,
//                            axisXleftWidth + w, y + latitudeFontSize / 2 + 0);
//                }
                RectF rectF = null;
                if (isToucInLeftChart()) {
                    //同方向，touch在屏幕左侧 就把价格框画在左边
                    rectF = new RectF(axisXleftWidth - w, y - latitudeFontSize / 2 - 0,
                            axisXleftWidth, y + latitudeFontSize / 2 + 0);
                    if (isAxisTitlein) {
                        rectF = new RectF(axisXleftWidth, y - latitudeFontSize / 2 - 0,
                                axisXleftWidth + w, y + latitudeFontSize / 2 + 0);
                    }
                } else {
                    //同方向，touch在屏幕右侧 就把价格框画在右侧
//                    rectF = new RectF(getWidth() - axisXrightWidth, y - latitudeFontSize / 2 - 0,
//                            getWidth() - axisXrightWidth + w, y + latitudeFontSize / 2 + 0);
                    rectF = new RectF(getWidth() - axisXrightWidth - w, y - latitudeFontSize / 2 - 0,
                            getWidth() - axisXrightWidth, y + latitudeFontSize / 2 + 0);
                    if (isAxisTitlein) {
                        rectF = new RectF(getWidth() - axisXrightWidth - w, y - latitudeFontSize / 2 - 0,
                                getWidth() - axisXrightWidth, y + latitudeFontSize / 2 + 0);
                    }
                }
                //设置超过流白位置,最顶部的位置超过了
                if (rectF.top < axisYtopHeight) {
                    rectF.top = axisYtopHeight;
                    rectF.bottom = rectF.top + latitudeFontSize;
                }
                //最底部的位置
                if (rectF.bottom > getDataHeightMian() + axisYtopHeight) {
                    rectF.bottom = getDataHeightMian() + axisYtopHeight;
                    rectF.top = rectF.bottom - latitudeFontSize;
                }

                //时间
                float left = x - wT / 2;
                float right = x + wT / 2;
                if (left < axisXleftWidth) {
                    left = axisXleftWidth;
                    right = left + 1 * wT;
                }
                if (right > getWidth() - axisXrightWidth) {
                    right = getWidth() - axisXrightWidth;
                    left = right - 1 * wT;
                }
                RectF rectFT = new RectF(left, axisYtopHeight - longitudeFontSize,
                        right, axisYtopHeight);


                canvas.drawRect(rectF, textPaint);
                //高度不够就不画时间了
                if (axisYtopHeight > longitudeFontSize)
                    canvas.drawRect(rectFT, textPaintT);
                textPaint.setColor(crossFontColor);
                textPaintT.setColor(crossFontColor);

//                canvas.drawText(price + "", rectF.centerX() - w / 2, y + latitudeFontSize / 2, textPaint);
//                canvas.drawText(obj.getTime(), rectFT.left + (rectFT.right - rectFT.left) / 2 - wT / 2, axisYtopHeight / 2 + latitudeFontSize / 2, textPaintT);

                drawText(canvas, priceStr, rectF, textPaint);
                //高度不够就不画时间了
                if (axisYtopHeight > longitudeFontSize)
                    drawText(canvas, obj.getTime(), rectFT, textPaintT);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        //十字线出现的时候，增加一个画tips的方法
        drawTips(canvas);

    }


    protected OnKCrossLineMoveListener onKCrossLineMoveListener;

    public OnKCrossLineMoveListener getOnKCrossLineMoveListener() {
        return onKCrossLineMoveListener;
    }

    public void setOnKCrossLineMoveListener(OnKCrossLineMoveListener onKCrossLineMoveListener) {
        this.onKCrossLineMoveListener = onKCrossLineMoveListener;
    }

    float lastX, lastY;
    float mStartDis;
    int touchMode = TOUCH_NONE;
    Runnable mLongPressRunnable;
    public static final long LONG_PRESS_TIME = 500;
    //放大缩小的时候不够平分的时候 左右替换放大缩小
    int zoomCount = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!touchEnable) {
            if (onKLineTouchDisableListener != null)
                onKLineTouchDisableListener.event();
            return false;
        }
//        if (velocityTracker == null)
//            velocityTracker = VelocityTracker.obtain();
//        velocityTracker.addMovement(event);

        boolean b = gestureDetector.onTouchEvent(event);
        if (b)
            return true;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                KLogUtil.v(TAG, "ACTION_DOWN");
                touchMode = TOUCH_DRAG;
                touchX = event.getX();
                lastX = event.getX();
                lastY = event.getY();
//                if (mLongPressRunnable == null) {
//                    mLongPressRunnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            showCross = true;
//                            if (crossLineView != null)
//                                crossLineView.postInvalidate();
//                            postInvalidate();
//                        }
//                    };
//                }
//                if (mLongPressRunnable != null)
//                    removeCallbacks(mLongPressRunnable);
//                postDelayed(mLongPressRunnable, LONG_PRESS_TIME);
//                if (showCross) {
//                    //按下的时候如果十字线出现了，去掉十字线
//                    showCross = false;
//                    if (onKCrossLineMoveListener != null)
//                        onKCrossLineMoveListener.onCrossLineHide();
//                    if (crossLineView != null)
//                        crossLineView.postInvalidate();
////                    postInvalidate();
//                }


                break;
            //多手指 触摸 缩放
            case MotionEvent.ACTION_POINTER_DOWN:
                touchMode = TOUCH_ZOOM;
                if (mLongPressRunnable != null)
                    removeCallbacks(mLongPressRunnable);
                mStartDis = distance(event);
                KLogUtil.v(TAG, "ACTION_POINTER_DOWN");
                if (mStartDis > MINDIS) {
                    touchMode = TOUCH_ZOOM;

                } else {
                    KLogUtil.v(TAG, "ACTION_POINTER_DOWN < MINDIS");
                    return true;
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                touchMode = TOUCH_NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                float distX = event.getX() - lastX;
                //k线的touch
                if (Math.abs(distX) > MINDIS) {
                    if (mLongPressRunnable != null)
                        removeCallbacks(mLongPressRunnable);
                }

                touchX = event.getX();
//                lastX = event.getX();
                lastY = event.getY();
                //判断是图标的touch还是十字线的touch
                if (showCross) {
                    KLogUtil.v(TAG, "ACTION_MOVE showCross");
                    //十字线的touch逻辑,这里值处理长按事件之后的拖动
                    //按下出现十字线的，然后拖动的逻辑放在gesture onScroll中
                    if (isLongPress) {
                        if (crossLineView != null)
                            crossLineView.postInvalidate();
                    }
                    break;
                } else {
                    if (touchMode == TOUCH_ZOOM) {

                        int pointCount = event.getPointerCount();
                        KLogUtil.v(TAG, "ACTION_MOVE TOUCH_ZOOM pointCount=" + pointCount);
                        boolean test = false;
                        if (test)
                            return true;
                        if (mStartDis < MINDIS)
                            return true;
                        KLogUtil.v(TAG, "ACTION_MOVE TOUCH_ZOOM");
                        //缩放
                        float currentDis = distance(event);

                        float scale = currentDis / mStartDis;
                        float zoomDisx = Math.abs(currentDis - mStartDis);
                        if (zoomDisx < MINDIS)
                            return true;

//                        //向右边移动
                        if (scale > 1) {
                            //放大方式一，每次drawcount - 1,向一个方向(这里暂时是右边)增加
//                            int zoomSize = (int)(zoomDisx / candleWidth);
//                            if(zoomSize < 0)
//                                zoomSize = 1;
//                            float newCandleWidth = getDataWidth() / (drawCount - zoomSize);
//                            //最大倍数内
//                            if (newCandleWidth <= DEFAULT_CANDLE_WIDTH * MAX_CANDLE_RATE) {
//                                candleWidth = newCandleWidth;
//                                drawIndexEnd = drawIndexEnd - zoomSize;
//
//                            } else {
//                                candleWidth = DEFAULT_CANDLE_WIDTH * MAX_CANDLE_RATE;
//                            }

//                            放大方式二，按照scale的缩放比例改变 candleWidth大小，
//                             缩放大于2的时候可以左右同时缩放
                            float newCandleWidth = candleWidth * scale;
                            int count = (int) (getDataWidth() / newCandleWidth);
                            //左右两边各自移动多少个k线
                            int zoomSize = (drawCount - count) / 2;
//                            if (zoomSize <= 0 && (drawCount - count) <= 0)
//                                return true;
                            if (zoomSize <= 0) {
                                zoomSize = 1;
                            }
                            if (zoomSize == 1)
                                count = count - 1;

                            zoomCount++;
                            if (zoomCount > 10000)
                                zoomCount = 0;
                            //最大倍数内
                            if (newCandleWidth <= DEFAULT_CANDLE_WIDTH * MAX_CANDLE_RATE) {
//                                KLogUtil.v(TAG, "%2=="+zoomCount%2);
//                                candleWidth = newCandleWidth;
                                candleWidth = getDataWidth() / (count);
                                //右边放大一个位置
                                if (zoomCount % 2 == 0) {
                                    drawIndexEnd = drawIndexEnd - zoomSize;
                                } else {
                                }
//                                drawIndexEnd = drawIndexEnd - zoomSize;
                            } else {
//                                candleWidth = DEFAULT_CANDLE_WIDTH * MAX_CANDLE_RATE;
                                return true;
                            }
                        } else if (scale < 1) {
//                            int zoomSize = (int)(zoomDisx / candleWidth);
//                            if(zoomSize < 0)
//                                zoomSize = 1;
//                            float newCandleWidth = getDataWidth() / (drawCount + zoomSize);
//                            //最大倍数内
//                            if (newCandleWidth >= DEFAULT_CANDLE_WIDTH * MIN_CANDLE_RATE) {
//                                candleWidth = newCandleWidth;
//                                drawIndexEnd = drawIndexEnd + zoomSize;
//                            } else {
//                                candleWidth = DEFAULT_CANDLE_WIDTH * MIN_CANDLE_RATE;
//                            }

                            //缩小
                            float newCandleWidth = candleWidth * scale;
                            int count = (int) (getDataWidth() / newCandleWidth);
                            //左右两边各自移动多少个k线
                            int zoomSize = (count - drawCount) / 2;
                            if (zoomSize <= 0) {
                                zoomSize = 1;
                            }
                            if (zoomSize == 1)
                                count = count + 1;
                            zoomCount++;
                            if (zoomCount > 10000)
                                zoomCount = 0;

                            //最大倍数内
                            if (newCandleWidth >= DEFAULT_CANDLE_WIDTH * MIN_CANDLE_RATE) {
                                candleWidth = getDataWidth() / count;
                                if (zoomCount % 2 == 0) {
                                    drawIndexEnd = drawIndexEnd + zoomSize;
                                } else {

                                }

                            } else {
                                candleWidth = DEFAULT_CANDLE_WIDTH * MIN_CANDLE_RATE;
                            }
                        }
                        //重新设置开始缩放时候的手指距离
                        mStartDis = distance(event);
                        postInvalidate();

                    } else if (touchMode == TOUCH_DRAG) {
                        KLogUtil.v(TAG, "ACTION_MOVE TOUCH_DRAG");
                        int pointCount = event.getPointerCount();
                        KLogUtil.v(TAG, "ACTION_MOVE TOUCH_DRAG pointCount=" + pointCount);
                        if (event.getPointerCount() > 1)
                            break;

                        //滑动
                        //允许的最小touch值才触发滑动
                        if (Math.abs(distX) < MINDIS)
                            break;

                        //滑动的距离算出 可移动的k线条数
                        float index = (distX / candleWidth);
                        int size = KNumberUtil.roundUp(Math.abs(index));
                        if (size > 0) {
                            if (distX < 0) {
                                kLineMoveRight(size);
                            } else if (distX > 0) {
                                kLineMoveLeft(size);
                            }
                            lastX = event.getX();

//                            actionCancle(event);
                        }
                    }
                }


                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                touchMode = TOUCH_NONE;
                isLongPress = false;
                isDoubClick = false;
//                if (mLongPressRunnable != null)
//                    removeCallbacks(mLongPressRunnable);
//                if (showCross) {
//                    if (onKCrossLineMoveListener != null)
//                        onKCrossLineMoveListener.onCrossLineHide();
//                    if (crossLineView != null)
//                        crossLineView.postInvalidate();
//                    postInvalidate();
//                }
//                showCross = false;
//                actionCancle(event);
                break;

            default:
                break;
        }

//        return super.onTouchEvent(event);
        return true;
    }


    boolean isLongPress = false;
    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            KLogUtil.v(TAG, "onDoubleTap");
            isDoubClick = false;
            //双击事件
            if (onKChartClickListener != null)
                onKChartClickListener.onDoubleClick();
            return super.onDoubleTap(e);
        }

//        @Override
//        public boolean onDoubleTapEvent(MotionEvent e) {
//            KLogUtil.v(TAG, "onDoubleTapEvent");
//            //不用这个  这个是双击中间产生的down up 这个action
//            return super.onDoubleTapEvent(e);
//        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            KLogUtil.v(TAG, "onSingleTapConfirmed");
            //单击事件

            touchX = e.getX();
            lastX = e.getX();
            lastY = e.getY();
            if (onKChartClickListener != null)
                onKChartClickListener.onSingleClick();

            //点击  如果十字线已经出现，隐藏十字线；如果十字线没出现，显示十字线
            if (showCross) {
                //十字线已经出现，隐藏十字线
                showCross = false;
                if (onKCrossLineMoveListener != null)
                    onKCrossLineMoveListener.onCrossLineHide();
            } else {
                //十字线没出现，显示十字线
                showCross = true;
                touchX = e.getRawX();
                if (touchX < 2 || touchX > getWidth() - 2) {
                    return false;
                }
            }
            if (crossLineView != null)
                crossLineView.postInvalidate();
            postInvalidate();//同时klinewView需要重绘一次，清理掉上次的tips数据
//            return super.onSingleTapConfirmed(e);
            return true;
        }


//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            KLogUtil.v(TAG, "onSingleTapUp");
//            //这个不是确定的单击事件，这个是down之后就会触发
//            return super.onSingleTapUp(e);
//        }

        @Override
        public void onLongPress(MotionEvent e) {
            KLogUtil.v(TAG, "onLongPress");
            //如果是双击之后多调用的  不用处理
            if (isDoubClick)
                return;

            //长按
            if (onKChartClickListener != null)
                onKChartClickListener.onLongPress();

            isLongPress = true;
            touchX = e.getX();
            lastX = e.getX();
            lastY = e.getY();
//            //长按 和单击事件是一样的处理方式
            if (showCross) {
                //十字线已经出现，隐藏十字线
                showCross = false;
                if (onKCrossLineMoveListener != null)
                    onKCrossLineMoveListener.onCrossLineHide();
            } else {
                //十字线没出现，显示十字线
                showCross = true;
                touchX = e.getRawX();
                if (touchX < 2 || touchX > getWidth() - 2) {
                    return;
                }
            }
            if (crossLineView != null)
                crossLineView.postInvalidate();
            postInvalidate();//同时klinewView需要重绘一次，清理掉上次的tips数据
//            super.onLongPress(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            KLogUtil.v(TAG, "onFling");
            if (!showCross) {
                //十字线已经隐藏 快速滑动
                flinger.fling((int) velocityX, (int) velocityY);
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            KLogUtil.v(TAG, "onScroll");
            //拖动
            if (showCross) {
                touchX = e2.getX();
                lastX = e2.getX();
                lastY = e2.getY();
                //十字线的拖动逻辑
                if (crossLineView != null)
                    crossLineView.postInvalidate();
                return true;//十字线的拖动逻辑
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            KLogUtil.v(TAG, "onDown");
            //down
            return super.onDown(e);
        }
    };
    GestureDetector gestureDetector = new GestureDetector(getContext(), gestureListener);


    /**
     * 计算移动距离
     */
    protected float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * k线向左边滑动
     * 即手势向右边滑动
     */
    protected void kLineMoveLeft(int size) {
        if (size < 0)
            return;
        //手势向右边滑动
        drawIndexEnd -= size;
//        lastX = event.getX();
        postInvalidate();
    }

    /**
     * k线向右边滑动
     * 即手势向左边滑动
     */
    protected void kLineMoveRight(int size) {
        if (size < 0)
            return;
        //k线向右边边滑动  手势向左边滑动
        drawIndexEnd += size;
//        lastX = event.getX();
        postInvalidate();
    }


    /**
     * 计算出当前价格的Y坐标
     *
     * @param price
     * @return
     */
    public float getYbyPrice(double price) {
//        KLogUtil.v(TAG, "getYbyPrice11=" + (axisYtopHeight + getDataHeightMian()));
//        KLogUtil.v(TAG, "getYbyPrice22=" + (getHeight() * mainF - (float) axisYmiddleHeight));
//        KLogUtil.v(TAG, "getYbyPrice33=" + (float) (((price - startYdouble)) * (double) getDataHeightMian()) / (stopYdouble - startYdouble));
        //Y轴的真实位置 (maxValue - minValue):dataheight = (zuoshou- minValue):y
        double d = price - startYdouble;
        float y = getHeight() * mainF - (float) axisYmiddleHeight - (float) ((d * (double) getDataHeightMian()) / (stopYdouble - startYdouble));
        return y;
    }

    /**
     * 根据Y坐标 计算出当前价格
     *
     * @param y 坐标
     * @return
     */
    public double getPriceByY(float y) {
        //Y轴的真实位置 (maxValue - minValue):dataheight = (zuoshou- minValue):y
//        double d = price - startYdouble;
//        float y = getHeight() * mainF - (float) axisYmiddleHeight - (float) ((d * (double) getDataHeightMian()) / (stopYdouble - startYdouble));
//        return y;
//        if (y <= axisYtopHeight)
//            return stopYdouble;
//        if (y >= axisYtopHeight + getDataHeightMian())
//            return startYdouble;
        double d = ((stopYdouble - startYdouble) * ((getDataHeightMian() + axisYtopHeight) - y)) / getDataHeightMian() + startYdouble;
        if (d < startYdouble)
            d = startYdouble;
        if (d > stopYdouble)
            d = stopYdouble;
        return d;
    }

    /**
     * 幅图的Y坐标
     *
     * @param value
     * @return
     */
    public float getSubY(double value) {
        //Y轴的真实位置 (subMaxValue - subMinValue):dataheight = (value- subMinValue):y
        float y = getDataHeightSub() * ((float) value - (float) subMinValue) / ((float) subMaxValue - (float) subMinValue);
        return getHeight() - axisYbottomHeight - y;
    }


    /**
     * 画出指标的参数值
     * touch在左边的时候，value的框就显示在右边
     * touch在右边的时候，value的框就显示在左边
     *
     * @param canvas
     */
    public void drawTips(Canvas canvas) {
        if (!showTips)
            return;
        if (mainLineData == null || mainLineData.size() == 0)
            return;
        //当前tips的索引位置，如果十字线没出现就，默认用最新的一条数据
        int index = drawIndexEnd - 1;
        if (showCross) {
            //必须和crossline 的算法一样 保持索引正确
            index = getTouchIndex();
        }
        Paint paint = getPaint();
        paint.setTextSize(tipsFontSize);
        //tips左边坐标
        float left = 0;
        //str开始位置
        float startX = 0;
        //tips之间的距离值
        int margin = 10;
        for (int i = 0; i < mainLineData.size(); i++) {
            //上方主图的指标线list
            if (mainLineData.get(i) == null)
                continue;
            if (mainLineData.get(i).getLineData() == null
                    || mainLineData.get(i).getLineData().size() <= index)
                continue;
            //获取title 和 color，在算list的时候传入，设置好
            KLineObj<KCandleObj> lineObj = mainLineData.get(i);
            paint.setColor(lineObj.getLineColor());
            String s = lineObj.getTitle() + ":" + KNumberUtil.beautifulDouble(lineObj.getLineData().get(index).getNormValue());
            //算出在左边还是在右边
            if (tipsMove) {
                if (showCross && touchX < getDataWidth() / 2) {
                    //总长度str 算出总宽度 方便定位距离
                    String countStr = "";
                    for (int j = 0; j < mainLineData.size(); j++) {
                        countStr += mainLineData.get(j).getTitle() + ":" + KNumberUtil.beautifulDouble(mainLineData.get(j).getLineData().get(index).getNormValue());
                    }
                    startX = getWidth() - axisXrightWidth - paint.measureText(countStr) - (mainLineData.size() + 1) * margin;
                } else {
                    startX = axisXleftWidth;
                }
            } else {
                startX = axisXleftWidth;
            }
            //循环画出tips
            if (left == 0) {
                left = startX + margin;
                if (isAxisTitlein) {
                    //留出左边的价格位置
                    left += getTextPaintX().measureText(KNumberUtil.beautifulDouble(lineObj.getLineData().get(index).getNormValue()));
                }
            }


            canvas.drawText(s, left, axisYtopHeight + tipsFontSize + 2, paint);
//            canvas.drawText(s, left, axisYtopHeight, paint);
            left += paint.measureText(s) + margin;
        }

        //附图的指标参数
        if (subLineData == null || subLineData.size() == 0)
            return;
        if (subNormal == KLineNormal.NORMAL_VOL)
            return;
        left = 0;
        for (int i = 0; i < subLineData.size(); i++) {
            if (subLineData.get(i).getLineData() == null || subLineData.get(i).getLineData().size() <= index)
                continue;
            KLineObj<KCandleObj> lineObj = subLineData.get(i);
            if (lineObj.getLineData() == null || lineObj.getLineData().size() <= index)
                continue;
            paint.setColor(lineObj.getLineColor());
            String s = lineObj.getTitle() + ":" + KNumberUtil.beautifulDouble(lineObj.getLineData().get(index).getNormValue());
            if (tipsMove) {
                if (showCross && touchX < getDataWidth() / 2) {
                    //总长度str 算出总宽度
                    String countStr = "";
                    for (int j = 0; j < subLineData.size(); j++) {
                        countStr += subLineData.get(j).getTitle() + ":" + KNumberUtil.beautifulDouble(subLineData.get(j).getLineData().get(index).getNormValue());
                    }
                    if (subNormal == KLineNormal.NORMAL_MACD) {
                        //macd 特殊处理 macd值
                        double value = subList.get(index).getHigh();
                        if (value == 0)
                            value = subList.get(index).getLow();
                        countStr += "MACD:" + KNumberUtil.beautifulDouble(value);
                    }
                    startX = getWidth() - axisXrightWidth - paint.measureText(countStr) - (subLineData.size() + 1) * margin;
                } else {
                    startX = axisXleftWidth;
                }
            } else {
                startX = axisXleftWidth;
            }

            //循环画出tips
            if (left == 0) {
                left = startX + margin;
                if (isAxisTitlein) {
                    //留出左边的价格位置
                    left += getTextPaintX().measureText(KNumberUtil.beautifulDouble(subMaxValue) + "") + 10;
                }
            }
            canvas.drawText(s, left, getDataHeightMian() + axisYtopHeight + axisYmiddleHeight + tipsFontSize + 2, paint);
//            canvas.drawText(s, left, axisYtopHeight, paint);
            left += paint.measureText(s) + margin;
            if (subNormal == KLineNormal.NORMAL_MACD && i == subLineData.size() - 1) {
                paint.setColor(candlePostColor);
                double value = subList.get(index).getHigh();
                if (value == 0) {
                    value = subList.get(index).getLow();
                    paint.setColor(candleNegaColor);
                }
                s = "MACD:" + KNumberUtil.beautifulDouble(value);

                canvas.drawText(s, left, getDataHeightMian() + axisYtopHeight + axisYmiddleHeight + tipsFontSize + 2, paint);
            }
        }
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

    ViewFlinger flinger = new ViewFlinger();

    /**
     * 1、postOnAnimation(); 调用Runnable的run方法，
     * 2、run方法里头递归调用postOnAnimation();直到完成加速度
     */
    private class ViewFlinger implements Runnable {
        private int mLastFlingX;
        private int mLastFlingY;
        private ScrollerCompat mScroller;
        /**
         * 为滑动提供一个增值器
         */
        private final Interpolator sQuinticInterpolator = new Interpolator() {
            public float getInterpolation(float t) {
                t -= 1.0f;
                return t * t * t * t * t + 1.0f;
            }
        };

        public ViewFlinger() {
            mScroller = ScrollerCompat.create(getContext(), sQuinticInterpolator);
        }

        @Override
        public void run() {
            final ScrollerCompat scroller = mScroller;
            if (scroller.computeScrollOffset()) {
                final int x = scroller.getCurrX();
                final int y = scroller.getCurrY();
                final int dx = x - mLastFlingX;
                final int dy = y - mLastFlingY;
                mLastFlingX = x;
                mLastFlingY = y;

//                Log.v(TAG, "dx="+dx);
                //dx>0，手指向右滑，k线向左，产生的加速度；dx<0,手指向左边滑动，k线向右，产生加速度
                float index = (dx / candleWidth);
                int size = KNumberUtil.roundUp(Math.abs(index));
                //滑动方向判断
                if (dx < 0) {
                    kLineMoveRight(size);
                } else if (dx > 0) {
                    kLineMoveLeft(size);
                }
                if (!scroller.isFinished()) {
                    postOnAnimation();
                } else {
                    stop();
                }
            }
        }

        /**
         * 和run方法配合递归调用
         */
        void postOnAnimation() {
            removeCallbacks(this);
            ViewCompat.postOnAnimation(KLineTouchView.this, this);
        }

        /**
         * 判断为是快速滑动的时候调用
         *
         * @param velocityX
         * @param velocityY
         */
        public void fling(int velocityX, int velocityY) {
            //数据不够一个屏幕的时候，没必要滑动
            if (kCandleObjList != null && kCandleObjList.size() > 0
                    && kCandleObjList.size() <= drawCount)
                return;
            mLastFlingX = mLastFlingY = 0;
            mScroller.fling(0, 0, velocityX, velocityY,
                    Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        /**
         * 停止调用
         */
        public void stop() {
            removeCallbacks(this);
            mScroller.abortAnimation();
        }
    }
}
