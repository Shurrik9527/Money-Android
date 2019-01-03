package com.moyacs.canary.kchart.chart.cross;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.moyacs.canary.kchart.util.KLogUtil;


/**
 * Created by fangzhu
 * touch 出现十字线的view，作为一个层放在k线图的上方
 * 每次移动的效果重复onDraw出现十字线
 */
public class KCrossLineView extends View {
    public static final String TAG = "KCrossLineView";

    //十字线的横线是否画在手指的地方
    boolean crossXbyTouch = true;

    public KCrossLineView(Context context) {
        super(context);
    }

    public KCrossLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KCrossLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        KLogUtil.v(TAG, "onDraw");
        if (iDrawCrossLine != null) {
            iDrawCrossLine.drawCrossLine(canvas);
        }
    }

    /**
     * 这里使用接口来实现画十字线的方法
     * 如分时图中
     * 方便在分时的View中直接使用，这样不用把代码copy到这里来
     */
    public interface IDrawCrossLine {
        void drawCrossLine(Canvas canvas);
    }

    IDrawCrossLine iDrawCrossLine;

    public IDrawCrossLine getiDrawCrossLine() {
        return iDrawCrossLine;
    }

    public void setiDrawCrossLine(IDrawCrossLine iDrawCrossLine) {
        this.iDrawCrossLine = iDrawCrossLine;
    }

    public boolean isCrossXbyTouch() {
        return crossXbyTouch;
    }

    public void setCrossXbyTouch(boolean crossXbyTouch) {
        this.crossXbyTouch = crossXbyTouch;
    }
}
