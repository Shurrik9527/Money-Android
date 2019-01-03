package com.moyacs.canary.base;

/**
 * 作者：luoshen on 2018/3/5 0005 15:17
 * 邮箱：rsf411613593@gmail.com
 * 说明：view 层 基类
 */

public interface BaseView {

    /**
     * 解除所有订阅，防止内存泄漏
     */
    void unsubscribe();
    /**
     * 显示加载框
     */
    void showLoadingDailog();

    /**
     * 取消加载框
     */
    void dismissLoadingDialog();


}
