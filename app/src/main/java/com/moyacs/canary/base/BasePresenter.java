package com.moyacs.canary.base;

/**
 * 作者：luoshen on 2018/3/5 0005 15:17
 * 邮箱：rsf411613593@gmail.com
 * 说明：P 层 基类
 */

public interface BasePresenter {

    /**
     * 解除所有订阅，防止内存泄漏
     */
    void unsubscribe();
}
