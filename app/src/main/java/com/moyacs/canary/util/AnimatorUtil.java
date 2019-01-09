package com.moyacs.canary.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

import com.moyacs.canary.MyApplication;

import www.moyacs.com.myapplication.R;

/**
 * @Author: Akame
 * @Date: 2019/1/9
 * @Description:
 */
public class AnimatorUtil {
    public static void startAnimatorRGB(View view, int resultColor) {
        ObjectAnimator colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", MyApplication.instance.getResources().getColor(R.color.white), resultColor);
        colorAnim.setDuration(500); // 动画时间为2s
        colorAnim.setEvaluator(new ArgbEvaluator()); // 设置估值器
        //监听动画执行完毕
        /*colorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                colorAnim = null;
            }
        });*/
        colorAnim.setRepeatCount(1);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE); // 设置变化反转效果，即第一次动画执行完后再次执行时背景色时从后面的颜色值往前面的变化
        colorAnim.start();
    }
}
