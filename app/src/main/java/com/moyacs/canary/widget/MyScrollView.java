package com.moyacs.canary.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.andview.refreshview.XScrollView;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/28
 * @email 252774645@qq.com
 */
public class MyScrollView extends XScrollView {


    private static StopCall stopCall;

    //ScrollView向上滑动到顶部的距离
    private int upH;

    public MyScrollView(Context context) { super(context);
    }
    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //赋值：200很重要，这个值是顶部2上面的高度
        upH = dpTopx(100);//单位是dp
    }
    public static void setCallback(StopCall c) { stopCall = c;

    } /**
     * 关键部分在这里，测量当前ScrollView滑动的距离
     * <p>
     * 其中t就是，单位是px，不是dp
     * <p>
     * stopCall是一个接口，是为了在Activity中实现设置顶部1/2可不可见
     */

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) { super.onScrollChanged(l, t, oldl, oldt);
        if (t > upH) {//如果向上滑动距离>本例中图片高度
            stopCall.stopSlide(true);//设置顶部1可见，顶部2不可见
        } else {//否则
            stopCall.stopSlide(false);//设置顶部1不可见，顶部2可见

        } } /**
     * F: 将dp转成为px
     */

    private int dpTopx(int dpValue) { final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    } public interface StopCall { void stopSlide(boolean isStop);
    }

}
