package com.moyacs.canary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import www.moyacs.com.myapplication.R;

/**
 * 行情页面，顶部 tab 自定义布局
 */
public class UnderLineTextView extends TextView {

    /*线条高度*/
    private int mlineHeight = 5;
    /*线条宽度*/
    private int mlineWidth = 0;
    /*线条颜色*/
    private int lineColor = Color.parseColor("#469cff");
    /*是否从从textVeiw padding的位置开始*/
    private boolean fitsPadding = false;
    /*是否显示线条*/
    private boolean isLineEnable = false;
    /*TextView selected的时候是否显示线条*/
    private boolean isLineEnableWhileSelected = true;
    /*TextView加粗*/
    private boolean isBlodWhileSelected = true;
    /*线条画在顶部还是下方*/
    private int upOrunder = 0;


    public UnderLineTextView(Context context) {
        this(context, null, 0);
    }

    public UnderLineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.UnderLineTextView, defStyleAttr, 0);
        lineColor = (int) attributes.getColor(R.styleable.UnderLineTextView_lineColor, getCurrentTextColor());
        mlineHeight = (int) attributes.getDimension(R.styleable.UnderLineTextView_mlineHeight, 5F);
        mlineWidth = (int) attributes.getDimension(R.styleable.UnderLineTextView_mlineWidth, 0F);
        upOrunder = (int) attributes.getInt(R.styleable.UnderLineTextView_upOrunder, 0);
        fitsPadding = attributes.getBoolean(R.styleable.UnderLineTextView_fitsPadding, false);
        isLineEnable = attributes.getBoolean(R.styleable.UnderLineTextView_isLineEnable, false);
        isLineEnableWhileSelected = attributes.getBoolean(R.styleable.UnderLineTextView_isLineEnableWhileSelected, true);
        isBlodWhileSelected = attributes.getBoolean(R.styleable.UnderLineTextView_isBlodWhileSelected, isBlodWhileSelected);

        attributes.recycle();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isLineEnable) {
            Paint paint = new Paint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(lineColor);
            paint.setStrokeWidth(mlineHeight);
            float startX = 0;
            float stopX = getWidth();

            if (mlineWidth == 0) {
                if (fitsPadding) {
                    startX = getPaddingLeft();
                    stopX = getWidth() - getPaddingRight();
                } else {
                    startX = 0;
                    stopX = getWidth();
                }
            } else {
                startX = getWidth() / 2 - mlineWidth / 2;
                stopX = getWidth() / 2 + mlineWidth / 2;
            }
            //下方
            if (upOrunder == 0) {
                canvas.drawLine(startX, getHeight() - mlineHeight / 2, stopX, getHeight() - mlineHeight / 2, paint);

            } else if (upOrunder == 1) {
                //上方
                canvas.drawLine(startX, mlineHeight, stopX, mlineHeight, paint);

            } else if (upOrunder == 2) {
                //位置在左边 这里mlineWidth是宽度，getheight是高度
                paint.setStrokeWidth(mlineWidth);
                if (mlineHeight == 0)
                    mlineHeight = getHeight();
                //线条的位置是从中间开始画的 所以除以2得到中心点
                //left
                canvas.drawLine(mlineWidth / 2, getHeight() / 2 - mlineHeight / 2, mlineWidth / 2, getHeight() / 2 + mlineHeight / 2, paint);

            } else if (upOrunder == 3) {
                if (mlineHeight == 0)
                    mlineHeight = getHeight();
                //right
                paint.setStrokeWidth(mlineWidth);
                //left
                canvas.drawLine(getWidth() - mlineWidth / 2, getHeight() / 2 - mlineHeight / 2, getWidth() - mlineWidth / 2, getHeight() / 2 + mlineHeight / 2, paint);


            }
        }


    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        //加粗字体
        if (isBlodWhileSelected) {
            if (selected) {
                //加粗
                setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                //去掉加粗
                setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }

        if (isLineEnableWhileSelected)
            setIsLineEnable(selected);
    }

    @Override
    public int getLineHeight() {
        return mlineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.mlineHeight = lineHeight;
    }

    public boolean isFitsPadding() {
        return fitsPadding;
    }

    public void setFitsPadding(boolean fitsPadding) {
        this.fitsPadding = fitsPadding;
    }

    public boolean isLineEnable() {
        return isLineEnable;
    }

    public void setIsLineEnable(boolean isLineEnable) {
        this.isLineEnable = isLineEnable;
        postInvalidate();
    }

    public boolean isLineEnableWhileSelected() {
        return isLineEnableWhileSelected;
    }

    public void setIsLineEnableWhileSelected(boolean isLineEnableWhileSelected) {
        this.isLineEnableWhileSelected = isLineEnableWhileSelected;
    }
}
